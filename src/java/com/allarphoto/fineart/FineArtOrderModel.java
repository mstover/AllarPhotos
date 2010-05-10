package com.allarphoto.fineart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.util.Tuple;

import strategiclibrary.service.template.TemplateService;
import strategiclibrary.util.Converter;

import com.allarphoto.application.CartObject;
import com.allarphoto.application.Product;
import com.allarphoto.ecommerce.BaliOrder;
import com.allarphoto.ecommerce.CostReport;
import com.allarphoto.ecommerce.LazerwebOrderModel;
import com.allarphoto.ecommerce.Merchant;
import com.allarphoto.ecommerce.Order;
import com.allarphoto.ecommerce.OrderResponse;
import com.allarphoto.server.ProductService;
import com.allarphoto.utils.Functions;
import com.allarphoto.utils.Right;

public class FineArtOrderModel extends LazerwebOrderModel {

	ProductService productService;

	TemplateService templateService;

	Collection<Product> productsForDownload;

	public FineArtOrderModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void dealWithDownload(List downloads, OrderResponse response,
			Map global) {
		Iterator items = downloads.iterator();
		productsForDownload = new ArrayList();
		while (items.hasNext()) {
			productsForDownload.add(((CartObject) items.next()).getProduct());
		}
		super.dealWithDownload(downloads, response, global);
	}

	@Override
	protected void dealWithZip(List filesToZip, int fileIncrement, Map global,
			OrderResponse response) {
		String filename = controller.getConfigValue("download_dir");
		GregorianCalendar cal = new GregorianCalendar();
		String downloadFile;
		String rand = ugd.createRandomPassword().substring(0, 3);
		downloadFile = user.getUsername() + "_" + (cal.get(Calendar.MONTH) + 1)
				+ cal.get(Calendar.DATE) + "_" + rand + ".zip";
		log.debug("Files to be zipped: " + filesToZip);

		Tuple<Collection<String>, Collection<Map<String, Collection<String>>>> export = productService
				.exportKeywords(productsForDownload);
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("export", export.second);
		values.put("headers", export.first);
		values.put("primaryDelim", "\t");
		values.put("secondaryDelim", "|");
		try {
			File fileExport = new File(filename, downloadFile + ".xls");
			FileWriter fw = new FileWriter(fileExport);
			templateService.mergeTemplate("fineart_download_keywords.csv",
					values, fw);
			fw.close();
			filesToZip.add(fileExport);
		} catch (IOException e) {
			log
					.error("Failed to save download keyword export with download zip");
		}
		Functions.zip((File[]) filesToZip.toArray(new File[0]), filename,
				downloadFile);
		// }
		log.info("Family = " + response.getFamilies().keySet());
		response.addInfo("download_file_" + rand + fileIncrement, downloadFile);
	}
	
	@Override/***************************************************************************
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
		CostReport ret = new FineArtCostReport();
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
					ret.setCost(product, (float) (tempbytes * .15));
					mbytes += tempbytes;
				}
			}
		}
		int cd = (int) mbytes / 500 + 1;
		cd *= 9;
		ret.setShipping(product.getProductFamily(), (float) cd);
		return ret;
	}

	@Override
	protected Order createOrderObject(Merchant merchant) {
		return new FineArtOrder(merchant, user);
	}

	@CoinjemaDependency(type = "productService", method = "productService")
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@CoinjemaDependency(type = "velocityService")
	public void setTemplateService(TemplateService ts) {
		templateService = ts;
	}

}
