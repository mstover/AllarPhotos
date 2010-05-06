/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.util.Converter;

import com.lazerinc.application.CartObject;
import com.lazerinc.application.Controller;
import com.lazerinc.application.ExpirationTester;
import com.lazerinc.application.LogModel;
import com.lazerinc.application.OrderModel;
import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.Address;
import com.lazerinc.beans.LogItem;
import com.lazerinc.beans.OrderItem;
import com.lazerinc.category.ProductField;
import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.server.ResourceService;
import com.lazerinc.server.UserService;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;
import com.lazerinc.utils.Rights;

@CoinjemaObject
public class LazerwebOrderModel implements OrderModel {
	protected Logger log;

	protected SecurityModel security;

	protected DBConnect database;

	protected LogModel dbLogger;

	protected CommerceUser user;

	protected ResourceService resService;

	protected UserService ugd;

	protected CreditCard cc;

	protected Controller controller;

	protected DatabaseUtilities dbUtil;

	private String pathPrefix;

	public LazerwebOrderModel() {
		Functions.javaLog("Lazerweb Order Model object created."
				+ System.getProperty("line.separator"));
	}

	public void setSecurity(SecurityModel s) {
		security = s;
	}

	@CoinjemaDependency(type = "appController", method = "appController")
	public void setController(Controller c) {
		controller = c;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect db) {
		database = db;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities du) {
		dbUtil = du;
	}

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setDatabaseLogger(LogModel lm) {
		dbLogger = lm;
	}

	protected boolean hasxxxPermission(Product p, Right right) {

		ExpirationTester expire = p.getProductFamily()
				.getProductExpirationTester();
		boolean hasRight = expire.hasPermission(p, right, security);
		if (!security.getPermission(p.getProductFamilyName(),
				Resource.DATATABLE, Right.ADMIN)) {
			for (ProductField field : p.getProductFamily().getFields()) {
				if (field.getType() == ProductField.PROTECTED) {
					Resource res = resService.getResource(p.getProductFamily()
							.getTableName()
							+ "."
							+ field.getName()
							+ "."
							+ p.getValue(field.getName()),
							Resource.PROTECTED_FIELD);
					hasRight = hasRight && security.getPermission(res, right);
				}
			}
		}
		return hasRight;
	}

	/***************************************************************************
	 * Calculates the estimated cost for this entire order. This assumes the
	 * collection is filled with CartObject objects.
	 * 
	 * @param products
	 *            Collection of CartObject's
	 * @param global
	 *            Set of global instructions for order.
	 * @return cost of all products in a CostReport object.
	 **************************************************************************/
	public CostReport getCost(Collection<CartObject> cart,
			Map<String, Object> global) {
		CostReport ret = new CostReport();
		double mbytes = 0, tempbytes = 0;
		Iterator it = cart.iterator();
		CartObject temp;
		String filetypes = controller.getConfigValue("file_types");
		String[] filetypesArray = Functions.split(filetypes, ",");
		Product product = null;
		int numberOrdered = 0, pixPerInch = 1;
		while (it.hasNext()) {
			temp = (CartObject) it.next();
			product = temp.getProduct();
			if (product.getProductFamily().getProductExpirationTester()
					.hasPermission(product, Right.ORDER, security)) {
				if (temp.getInstructions().containsKey("order")) {
					tempbytes = Converter.getDouble(product.getValue("File Size"),0)/1024/1024;
					ret.setCost(product, (float) (tempbytes * .39));
					mbytes += tempbytes;
				}
			}
			if (product.getProductFamily().getProductExpirationTester()
					.hasPermission(product, Right.DOWNLOAD, security)) {
				getLog().debug("Can download " + product);
				if (temp.getInstructions().containsKey("download")) {
					getLog().debug("Do download " + product);
					if (isNormalDownload(temp.getInstructions())) {
						getLog().debug(
								"normal download "
										+ new File(pathPrefix + "/"
												+ product.getPathName()
												+ product.getValue("_jpg"))
												.length());
						ret.addToDownloadSize(new File(pathPrefix + "/"
								+ product.getPathName()
								+ product.getValue("_jpg")).length());
					}
					for (int x = 0; x < filetypesArray.length; x++) {
						if (((Set) temp.getInstructions().get("download"))
								.contains(filetypesArray[x].trim())
								&& product.getValue("_" + filetypesArray[x]) != null
								&& new File(pathPrefix
										+ "/"
										+ product.getPathName()
										+ product.getValue("_"
												+ filetypesArray[x])).exists()) {
							getLog()
									.debug(
											"abnormal download "
													+ new File(
															pathPrefix
																	+ "/"
																	+ product
																			.getPathName()
																	+ product
																			.getValue("_"
																					+ filetypesArray[x]))
															.length());
							ret
									.addToDownloadSize(new File(pathPrefix
											+ "/"
											+ product.getPathName()
											+ product.getValue("_"
													+ filetypesArray[x]))
											.length());
						}
					}
				}
			}
		}
		int cd = (int) mbytes / 500 + 1;
		cd *= 9;
		ret.setShipping(product.getProductFamily(), (float) cd);
		return ret;
	}

	/***************************************************************************
	 * Deal with the ordered assets. Obtain a cost for the order, transmit the
	 * order to the merchant and send a confirmation to the user (on call to
	 * emailOrderToMerchant).
	 * 
	 * @param orders
	 *            A LinkedList of orders.
	 * @param response
	 *            The OrderResponse object to produce the response.
	 * @param costReport
	 *            The costReport for the order.
	 * @param merchant
	 *            Who needs to get the order.
	 * @param global
	 *            A global Map of special instructions generated from user
	 *            input.
	 **************************************************************************/
	protected void dealWithOrders(LinkedList orders, OrderResponse response,
			CostReport costReport, Merchant merchant, Map global) // added
	// 'Map
	// global'
	// for
	// special
	// instructions
	{
		Address shippingAddress = (Address) global.get("orderShipAddress");
		Functions.javaLog("using LazerwebOrderModel dealWithOrders.");
		Iterator it = orders.iterator();
		CartObject temp;
		Product product;
		float cost = 0;
		saveOrderInfoFromglobal(response, global);
		// next if statement added for special instructions
		while (it.hasNext()) {
			temp = (CartObject) it.next();
			product = temp.getProduct();
			if (product.getProductFamily().getProductExpirationTester()
					.hasPermission(product, Right.ORDER, security)) {
				saveProductInstructionsFromCart(response, temp);
				saveProductCostInfo(response, costReport, temp);
				response.addInfo(product.getProductFamily(), "family", product
						.getProductFamily().getDescriptiveName());
			} else
				response.addInfo(product, "error", "No Permissions to order");
		}
		saveTotalOrderCost(response, costReport);
		fulfillOrder(response, costReport, merchant, shippingAddress);
		response.addInfo("zip_file_count", "0"); // added for the case when
		// only an
		// order and not a download. If an order is combined with a down load,
		// then
		// the value of zip_file_count created in dealWithDownload will overide
		// the
		// value created here, since the ...Order is called before ...Download
		// in the
		// execute method.
	}

	protected void saveTotalOrderCost(OrderResponse response,
			CostReport costReport) {
		java.text.NumberFormat format = java.text.NumberFormat
				.getCurrencyInstance();
		response.addInfo("Total Cost", format.format(costReport
				.calculateTotal()));
	}

	protected void saveProductCostInfo(OrderResponse response,
			CostReport costReport, CartObject temp) {
		java.text.NumberFormat format = java.text.NumberFormat
				.getCurrencyInstance();
		response.addInfo(temp.getProduct(), "cost", format.format(costReport
				.getCost(temp.getProduct())));
	}

	protected void saveOrderInfoFromglobal(OrderResponse response, Map global) {
		Iterator instrIt = global.keySet().iterator();
		while (instrIt.hasNext()) {
			Object instrTemp = instrIt.next();
			if (!(global.get(instrTemp) instanceof Address)
					&& global.get(instrTemp) != null)
				response.addInfo(instrTemp.toString(), global.get(instrTemp)
						.toString());
		}
		// global.clear(); //why are we clearing this?
	}

	protected void saveProductInstructionsFromCart(OrderResponse response,
			CartObject temp) {
		String instrKey;
		Set orderInstr;
		Iterator prodInstrIt = temp.getInstructions().keySet().iterator();
		response.addInfo(temp.getProduct(), null, null);
		while (prodInstrIt.hasNext()) {
			instrKey = (String) prodInstrIt.next();
			Functions.javaLog("instruction key = " + instrKey);
			orderInstr = (Set) temp.getInstructions().get(instrKey);
			Functions.javaLog("instruction set = " + orderInstr);
			Iterator orderInstrIt = orderInstr.iterator();
			String specInstr = null;
			String specTemp;
			int counter = 0;
			while (orderInstrIt.hasNext()) {
				if (null != (specTemp = (String) orderInstrIt.next())) {
					if (counter > 0) {
						specInstr = specInstr + ", " + specTemp;
					} else {
						specInstr = specTemp;
					}
				}
				counter++;
			}
			if (null != specInstr && specInstr.length() > 0) {
				response.addInfo(temp.getProduct(), instrKey, specInstr);
			}
		}
	}

	/**
	 * Saves each order with the order number.
	 * 
	 * @param response
	 * @return
	 */
	protected Order recordOrders(OrderResponse response, Merchant merchant,
			CostReport costReport, Address shippingAddress) {
		Order order = createOrderObject(merchant);
		order.setShippingAddress(shippingAddress);
		String[] sortBy = new String[] { "file" };
		Iterator o = response.productIterator();
		while (o.hasNext()) {
			Product product = (Product) o.next();
			OrderItem logItem = order.newItem().setProduct(product).setValue(
					"file", (String) product.getPrimary()).setOrderNo(
					response.getOrderNo()).setSortBy(sortBy);
			Iterator i = response.getInfoIterator();
			while (i.hasNext()) {
				String info = i.next().toString();
				order.setValue(info, response.getInfo(info));
			}
			order.setValue("family", product.getProductFamily()
					.getDescriptiveName());
			addProductLevelDetails(response, product, logItem);
		}
		order.setOrderNo(response.getOrderNo());
		dbLogger.addOrder(order);
		return order;
	}

	protected void addProductLevelDetails(OrderResponse response,
			Product product, OrderItem logItem) {
		Iterator i;
		i = response.getInfoIterator(product);
		while (i.hasNext()) {
			String key = (String) i.next();
			logItem.setProductValue(key, response.getInfo(product, key));
		}
	}

	protected Order createOrderObject(Merchant merchant) {
		return new Order(merchant, user);
	}

	protected void fulfillOrder(OrderResponse response, CostReport costReport,
			Merchant merchant, Address shippingAddress) {
		response.setOrderNo(createOrderNo(user));
		Order order = recordOrders(response, merchant, costReport,
				shippingAddress);
		order.setSecurity(security);
		try {
			order.fulfill();
		} catch (OrderFailedException e) {
			if (e.getMessage().equals("Fulfillment")) {
				response
						.addInfo(
								"errror",
								"EMAIL ORDER FAILED.<br>Please record items ordered and email the webmaster for help.");
			} else if (e.getMessage().equals("Merchant")) {
				response
						.addInfo(
								"errror",
								"EMAIL ORDER FAILED.<br>Please record items ordered and email the webmaster for help.");
			}
		}
	}

	protected String createOrderNo(CommerceUser user) {
		return user.getUserID() + "_" + System.currentTimeMillis()
				+ Converter.formatNumber((float) Math.random(), ".#####");
	}

	/***************************************************************************
	 * Description
	 * 
	 * @param downloads
	 *            A List of requested assets to download.
	 * @param response
	 *            The OrderResponse object to produce the response.
	 * @param global
	 *            A global Map of special instructions generated from user
	 *            input.
	 **************************************************************************/
	protected void dealWithDownload(List downloads, OrderResponse response,
			Map global) {
		Iterator items = downloads.iterator();
		List<File> filesToZip = new LinkedList<File>();
		String fs = "/";
		Product product;
		String filetypes = controller.getConfigValue("file_types");
		Map instructions;
		CartObject item;
		int fileIncrementor = 0; // used in checking zip file size against
		// threshold
		while (items.hasNext()) {
			item = (CartObject) items.next();
			product = item.getProduct();
			if (!product.getProductFamily().getProductExpirationTester()
					.hasPermission(product, Right.DOWNLOAD, security)) {
				response.addInfo(product, "error", "No permission to download");
				continue;
			}
			response.addInfo(product, "download", "download");
			instructions = item.getInstructions();
			if (filetypes == null || filetypes.length() == 0
					|| isNormalDownload(instructions)) {
				File fileToZip = new File(pathPrefix + "/"
						+ product.getPathName() + product.getValue("_jpg"));
				filesToZip.add(fileToZip);
				LogItem logItem = new LogItem();
				logItem.setValue("action", "download");
				logItem.setValue("file", product.getPrimary());
				logItem.setValue("family", product.getProductFamily()
						.getDescriptiveName());
				logItem.setValue("product_id", String.valueOf(product.getId()));
				logItem
						.setValue("filesize", String
								.valueOf(fileToZip.length()));
				logItem.setValue("user", user.getUsername());
				additionalDownloadValues(global, logItem);
				dbLogger.addLogItem(logItem);
			}
			if (filetypes != null && filetypes.length() > 0) {
				String[] filetypesArray = Functions.split(filetypes, ",");
				for (int x = 0; x < filetypesArray.length; x++) {
					log.debug("testing for download of filetype: '"
							+ filetypesArray[x] + "'");
					log.debug("filetypes wanted: "
							+ instructions.get("download"));
					if (((Set) instructions.get("download"))
							.contains(filetypesArray[x].trim())) {
						if (product.getValue("_" + filetypesArray[x]) != null
								&& new File(pathPrefix
										+ "/"
										+ product.getPathName()
										+ product.getValue("_"
												+ filetypesArray[x])).exists()) {
							File fileToZip = new File(pathPrefix + "/"
									+ product.getPathName()
									+ product.getValue("_" + filetypesArray[x]));
							filesToZip.add(fileToZip);
							LogItem logItem = new LogItem();
							logItem.setValue("action", "download");
							logItem.setValue("product_id", String
									.valueOf(product.getId()));
							logItem.setValue("file", product.getName() + "."
									+ filetypesArray[x]);
							logItem.setValue("file type", filetypesArray[x]);
							logItem.setValue("family", product
									.getProductFamily().getDescriptiveName());
							logItem.setValue("filesize", String
									.valueOf(fileToZip.length()));
							logItem.setValue("user", user.getUsername());
							additionalDownloadValues(global, logItem);
							dbLogger.addLogItem(logItem);
						}
					}
				}
			}
		}
		dealWithZip(filesToZip, fileIncrementor, global, response);
		response.addInfo("zip_file_count", "" + fileIncrementor);
	}

	protected boolean isNormalDownload(Map instructions) {
		return ((Set) instructions.get("download")).contains(null)
				|| ((Set) instructions.get("download")).contains("product");
	}

	/**
	 * Allow a subclass to pack custom values into the download response
	 * information map.
	 * 
	 * @param global
	 * @param logItem
	 */
	protected void additionalDownloadValues(Map global, LogItem logItem) {
	}

	/***************************************************************************
	 * Zips the download files in batches sent from dealWithDownload
	 * 
	 * @param filesToZip
	 *            Linked list of files to zip
	 * @param fileIncrement
	 *            Used to label each download file
	 * @param global
	 * @param response
	 **************************************************************************/
	protected void dealWithZip(List filesToZip, int fileIncrement, Map global,
			OrderResponse response) {
		String filename = controller.getConfigValue("download_dir");
		GregorianCalendar cal = new GregorianCalendar();
		String downloadFile;
		/*
		 * We will eventually want to comment out the Mac section on upgrade to
		 * 2000 if(global.containsKey("MAC")) { downloadFile =
		 * user.getUsername()+"_"+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)+
		 * fileIncrement+".sit";
		 * Functions.zipForMac((File[])filesToZip.toArray(new
		 * File[0]),filename+downloadFile,
		 * controller.getConfigValue("dropstuff")); } else { //
		 */
		String rand = ugd.createRandomPassword().substring(0, 3);
		downloadFile = user.getUsername() + "_" + (cal.get(Calendar.MONTH) + 1)
				+ cal.get(Calendar.DATE) + "_" + rand + ".zip";
		log.debug("Files to be zipped: " + filesToZip);
		Functions.zip((File[]) filesToZip.toArray(new File[0]), filename,
				downloadFile);
		// }
		log.info("Family = " + response.getFamilies().keySet());
		response.addInfo("download_file_" + rand + fileIncrement, downloadFile);
	}

	/***************************************************************************
	 * Zips the download files in batches sent from dealWithDownload
	 * 
	 * @param path -
	 *            The path to the file minus the file extension
	 * @param ext -
	 *            The suffix of the file ... should be a proper file extension.
	 * @return - A File or null if the File does not exist.
	 **************************************************************************/
	protected File findFilename(String path, String ext) {
		String test = path + "." + ext;
		File file = new File(test);
		if (file.exists()) {
			return file;
		} else {
			int index = path.lastIndexOf(".");
			if (index > -1) {
				test = path.substring(0, index + 1) + ext;
				file = new File(test);
				if (file.exists())
					return file;
			}
		}
		return null;
	}

	protected void checkIfOrder(OrderResponse orderResponse, LinkedList orders,
			CartObject cartObject) {
		if (cartObject.getInstructions().containsKey("order")
				&& cartObject.getProduct().getProductFamily()
						.getProductExpirationTester().hasPermission(
								cartObject.getProduct(), Right.ORDER, security)) {
			orders.add(cartObject);
		}
	}

	/***************************************************************************
	 * Executes the order. If the merchant requires a manually processed order,
	 * then the order is simply emailed and logged for future retrieval and
	 * modification.
	 * 
	 * @param products
	 *            The products to be executed.
	 * @param global
	 *            Set of global instructions.
	 * @return An OrderResponse object to communicate the results.
	 **************************************************************************/
	public OrderResponse execute(Collection<CartObject> cart,
			Map<String, Object> global) {
		/* First, get a cost report. */
		CostReport costReport = getCost(cart, global);
		OrderResponse orderResponse = new OrderResponse();
		LinkedList orders = new LinkedList(), download = new LinkedList();
		CartObject temp = null;
		Map instructions;
		Iterator it = cart.iterator();
		Merchant merchant = null;
		boolean flag = false;
		while (it.hasNext()) {
			temp = (CartObject) it.next();
			instructions = temp.getInstructions();
			if (!flag) {
				merchant = dbUtil.getMerchant(temp.getProduct()
						.getProductFamily());
				flag = true;
			}
			checkIfOrder(orderResponse, orders, temp);
			checkIfDownload(orderResponse, download, temp);
		}
		/*
		 * The order of the next to 'if' statements, which call dealWithOrders
		 * and dealWithDownload respectively, will effect the value of
		 * zip_file_count. If the sequence in which these two methods is
		 * changed, that may adversly effect the results of the download page.
		 * added "global" to call to dealWithOrders to deal with spec instr
		 */
		if (orders.size() > 0) {
			dealWithOrders(orders, orderResponse, costReport, merchant, global);
		}
		if (download.size() > 0) {
			dealWithDownload(download, orderResponse, global);
		}
		clearCartInstructions(cart);
		return orderResponse;
	}

	protected void clearCartInstructions(Collection<CartObject> cart) {
		for (CartObject obj : cart) {
			obj.getInstructions().clear();
		}
	}

	protected void checkIfDownload(OrderResponse response, LinkedList download,
			CartObject temp) {
		log.debug("Checking if order is a download order: "
				+ temp.getInstructions().get("download"));
		if (temp.getInstructions().containsKey("download")
				&& temp.getProduct().getProductFamily()
						.getProductExpirationTester().hasPermission(
								temp.getProduct(), Right.DOWNLOAD, security)) {
			log.debug("It is a download order");
			download.add(temp);
		}
	}

	/***************************************************************************
	 * Sets the credit card to be used to process this request.
	 * 
	 * @param cc
	 *            CreditCard object.
	 **************************************************************************/
	public void setCreditCard(CreditCard c) {
		cc = c;
	}

	/***************************************************************************
	 * Sets the user doing the order.
	 * 
	 * @param user
	 *            CommerceUser object.
	 **************************************************************************/
	public void setUser(CommerceUser u) {
		user = u;
	}

	public Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	@CoinjemaDependency(alias = "physicalAsset.path")
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	@CoinjemaDependency(type = "userService")
	public void setUgd(UserService ugd) {
		this.ugd = ugd;
	}
}
