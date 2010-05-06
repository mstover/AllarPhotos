package com.lazerinc.ajaxclient.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Tuple;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.util.Converter;

import com.lazerinc.ajaxclient.client.OrderService;
import com.lazerinc.ajaxclient.client.beans.AjaxAddress;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.AjaxOrder;
import com.lazerinc.ajaxclient.client.beans.AjaxOrderItem;
import com.lazerinc.ajaxclient.client.beans.AjaxOrderResponse;
import com.lazerinc.ajaxclient.client.beans.FormField;
import com.lazerinc.ajaxclient.client.beans.OrderVerificationPackage;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.application.CartObject;
import com.lazerinc.application.Controller;
import com.lazerinc.application.OrderingService;
import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.application.ServiceGateway;
import com.lazerinc.beans.Address;
import com.lazerinc.beans.City;
import com.lazerinc.beans.Company;
import com.lazerinc.beans.Country;
import com.lazerinc.beans.OrderItem;
import com.lazerinc.beans.State;
import com.lazerinc.cached.functions.AddressAdd;
import com.lazerinc.client.beans.Cart;
import com.lazerinc.client.beans.DateBean;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.util.ShoppingCartUtil;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.ecommerce.OrderResponse;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.ecommerce.UserProperties;
import com.lazerinc.ecommerce.Order.Status;
import com.lazerinc.server.UserService;
import com.lazerinc.utils.DatabaseLogger;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

@CoinjemaObject
public class OrderServiceImpl extends AbstractGwtServlet implements
		OrderService {
	private static final long serialVersionUID = 1;

	DatabaseLogger dblogger;

	CacheService cache;

	NotificationService emailer;

	UserService ugd;

	protected ShoppingCartUtil cartUtil = new ShoppingCartUtil();

	AddressAdd addressAdder = new AddressAdd();

	public OrderServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public List getOrderVerificationPackages(AjaxCart cart) {
		getLog().info("Getting order packages");
		try {
			List<OrderVerificationPackage> packs = new ArrayList<OrderVerificationPackage>();
			MultiKeyMap groupings = new MultiKeyMap();
			for (Object r : cart.getRequests()) {
				Request req = (Request) r;
				List<Request> group = (List<Request>) groupings.get(req
						.getProduct().getFamilyName(), req.isDownload());
				if (group == null) {
					group = new ArrayList<Request>();
					groupings.put(req.getProduct().getFamilyName(), req
							.isDownload(), group);
				}
				group.add(req);
			}
			for (Object key : groupings.keySet()) {
				MultiKey mKey = (MultiKey) key;
				OrderVerificationPackage pack = getFamilyVerificationPackage(
						dbutil.getProductFamily((String) mKey.getKey(0)),
						(Boolean) mKey.getKey(1)).getCopy();
				pack.addRequests((List<Request>) groupings.get(key));
				mergePacks(packs, pack);
			}
			return packs;
		} catch (Exception e) {
			getLog().warn("Failed to retrieve order packages", e);
			return Collections.EMPTY_LIST;
		}
	}

	@CoinjemaDynamic(alias = "appController")
	protected Controller getAppController() {
		return null;
	}

	public AjaxOrderResponse executeOrder(List orders, AjaxAddress aa) {
		try {
			OrderResponse response = new OrderResponse();
			OrderingService model;
			UserBean userBean = (UserBean) getThreadLocalHandlerData().getBean(
					"user");
			Map<String, Tuple<Collection<Request>, Collection<FormField>>> byFamily = organizeByFamily(orders);
			for (String f : byFamily.keySet()) {
				ProductFamily family = dbutil.getProductFamily(f);
				model = ServiceGateway.getOrderService(family
						.getOrderModelClass());
				model.setUser(userBean.getUser());
				model.setSecurity(currentPerms());
				response.add(model.execute(
						getCartObjects(byFamily.get(f).first), getInstructions(
								byFamily.get(f).second, aa)));
			}
			AjaxOrderResponse aresponse = createAjaxOrderResponse(response);
			for (ProductFamily family : response.getFamilies().keySet())
				aresponse.addFamily(family.getDescriptiveName());
			return aresponse;
		} catch (Exception e) {
			getLog().warn("Order failed", e);
			return new AjaxOrderResponse();
		}
	}

	public List getOrders(String status, String fromDate) {
		try {
			Collection<Order> orders = dblogger.getOrders(status,
					getDate(fromDate), currentPerms());
			filterByPermissions(orders, currentPerms(), dbutil,
					(UserBean) getThreadLocalHandlerData().getBean("user"));
			Collections.sort((List) orders);
			List ajaxOrders = new ArrayList();
			int count = 0;
			for (Order o : orders) {
				ajaxOrders.add(createAjaxOrder(o));
				count++;
				if (count >= 100) {
					AjaxOrder ord = new AjaxOrder();
					ord.setOrderNo("More...");
					ajaxOrders.add(ord);
					break;
				}
			}
			return ajaxOrders;
		} catch (Exception e) {
			getLog().warn("Failed to get orders", e);
			return new ArrayList();
		}
	}

	private Calendar getDate(String fromDate) {
		DateBean db = new DateBean();
		Calendar d = null;
		if ("LAST_WEEK".equals(fromDate)) {
			d = db.lastWeek();
		} else if ("LAST_MONTH".equals(fromDate))
			d = db.lastMonth();
		else if ("LAST_YEAR".equals(fromDate))
			d = db.lastYear();
		else
			d = Converter.getCalendar(fromDate, null);
		return d;
	}

	public boolean saveShoppingCart(AjaxCart acart) {
		try {
			ShoppingCartBean cart = createCart(acart);
			String serialCart = cart.saveCartToString();
			CommerceUser user = ((UserBean) getThreadLocalHandlerData()
					.getUserBean("user")).getUser();
			user.setProperty(UserProperties.SHOPPING_CART, serialCart);
			ugd.updateUser(user, currentPerms(), true);
			return true;
		} catch (Exception e) {
			log.error("Failed to save shopping cart", e);
			return false;
		}
	}

	public AjaxCart getShoppingCart() {
		try {
			ShoppingCartBean cart = cartUtil
					.getCart(getThreadLocalHandlerData());
			CommerceUser user = ((UserBean) getThreadLocalHandlerData()
					.getUserBean("user")).getUser();
			cartUtil.initCart(cart, user);
			return createAjaxCart(cart);
		} catch (Exception e) {
			log.error("Problem retrieving shopping cart", e);
			return null;
		}
	}

	private AjaxCart createAjaxCart(ShoppingCartBean cart) throws Exception {
		AjaxCart acart = new AjaxCart();
		for (Product p : cart.getProducts()) {
			Map<String, Set<String>> instr = cart.getInstructions(p);
			ProductBean bean = p.getProductFamily().getProductBeanClass()
					.newInstance();
			for (String key : instr.keySet()) {
				if (key != null) {
					if (key.equals("order"))
						acart.add(new Request(createAjaxProduct(p, bean), null,
								"Order Original"));
					else if (key.equals("download")) {
						for (String ft : instr.get(key)) {
							acart.add(new Request(createAjaxProduct(p, bean),
									true, ft, null, "Download " + ft));
						}
					}
				}
			}
		}
		return acart;
	}

	private ShoppingCartBean createCart(AjaxCart acart) throws Exception {
		ShoppingCartBean cart = cartUtil.getCart(getThreadLocalHandlerData());
		cart.clear();
		for (Request req : (Collection<Request>) acart.getRequests()) {
			ProductFamily family = dbutil.getProductFamily(req.getProduct()
					.getFamilyName());
			Product p = family.getProduct(req.getProduct().getId(),
					currentPerms());
			cart.add(p);
			cart.addInstruction(p, req.isDownload() ? "download" : "order", req
					.isDownload() ? req.getFiletype() : "");
		}
		return cart;
	}

	public List getLibraryOrders(String library, String status, String fromDate) {
		try {
			ProductFamily family = dbutil
					.getProductFamilyFromDescription(library);
			Collection<Order> orders = dblogger.getOrders(family, status,
					getDate(fromDate), currentPerms());
			filterByPermissions(orders, currentPerms(), dbutil,
					(UserBean) getThreadLocalHandlerData().getBean("user"));
			Collections.sort((List) orders);
			List ajaxOrders = new ArrayList();
			int count = 0;
			for (Order o : orders) {
				ajaxOrders.add(createAjaxOrder(o));
				count++;
				if (count >= 100) {
					AjaxOrder ord = new AjaxOrder();
					ord.setOrderNo("More...");
					ajaxOrders.add(ord);
					break;
				}
			}
			return ajaxOrders;
		} catch (Exception e) {
			getLog().warn("Failed to get orders", e);
			return new ArrayList();
		}
	}

	public List getUserOrders(String username, String fromDate) {
		try {
			Collection<Order> orders = dblogger.getUserOrders(username,
					getDate(fromDate), currentPerms());
			filterByPermissions(orders, currentPerms(), dbutil,
					(UserBean) getThreadLocalHandlerData().getBean("user"));
			Collections.sort((List) orders);
			List ajaxOrders = new ArrayList();
			int count = 0;
			for (Order o : orders) {
				ajaxOrders.add(createAjaxOrder(o));
				count++;
				if (count >= 100) {
					AjaxOrder ord = new AjaxOrder();
					ord.setOrderNo("More...");
					ajaxOrders.add(ord);
					break;
				}
			}
			return ajaxOrders;
		} catch (Exception e) {
			getLog().warn("Failed to get orders", e);
			return new ArrayList();
		}
	}

	public AjaxOrder getOrder(String orderNo) {
		return createAjaxOrder(dblogger.getOrder(orderNo, currentPerms()));
	}

	public AjaxOrder createAjaxOrder(Order o) {
		AjaxOrder ao = new AjaxOrder();
		ao.setDate(Converter.formatCalendar(o.getDateTime(), "MM/dd/yyyy"));
		ao.setOrderNo(o.getOrderNo());
		ao.setShippingAddress(createAjaxAddress(o.getShippingAddress()));
		ao.setStatus(o.getStatus().toString());
		ao.setUser(createAjaxUser(o.getUser()));
		ProductFamily family = dbutil.getProductFamilyFromDescription(o
				.getValue("family"));
		if (currentPerms().getPermission(family.getTableName(),
				Resource.DATATABLE, Right.ADMIN)) {
			ao.setApprove(true);
		}
		if (currentPerms().getPermission("all", Resource.DATABASE, Right.ADMIN)) {
			ao.setApprove(true);
			ao.setFulfill(true);
			ao.setConfirm(true);
		}
		if (currentUser().equals(o.getUser()))
			ao.setConfirm(true);
		for (OrderItem item : o) {
			ao.addItem(createAjaxOrderItem(item));
		}
		for (String k : o.getInfoMap().keySet()) {
			ao.addValue(k, o.getValue(k));
		}
		return ao;

	}

	public AjaxOrderItem createAjaxOrderItem(OrderItem item) {
		try {
			AjaxOrderItem ai = new AjaxOrderItem();
			Product p = item.getProduct(currentPerms());
			ProductBean bean = p.getProductFamily().getProductBeanClass()
					.newInstance();
			ai.setProduct(createAjaxProduct(p, bean));
			if ("rejected".equals(item.getValue("status")))
				ai.setRejected(true);
			for (String k : item.getItem().keySet()) {
				ai.addValue(k, item.getValue(k));
			}
			return ai;
		} catch (Exception e) {
			getLog().warn("Failed to get order item", e);
			return null;
		}
	}

	protected void filterByPermissions(Collection<Order> orders,
			SecurityModel perms, DatabaseUtilities dbUtil, UserBean userBean) {
		Iterator<Order> iter = orders.iterator();
		while (iter.hasNext()) {
			Order order = iter.next();
			String datatable = dbUtil.getProductFamilyFromDescription(
					order.getValue("family")).getTableName();
			if (!order.getUser().getUsername().equals(userBean.getUsername())
					&& !perms.getPermission(datatable, Resource.DATATABLE,
							Right.ADMIN)) {
				iter.remove();
			}
		}
	}

	private Order getOrder(AjaxOrder o) {
		return dblogger.getOrder(o.getOrderNo(), currentPerms());
	}

	public AjaxOrder approveOrder(AjaxOrder o) {
		Order order = getOrder(o);
		order.setStatus(Status.AWAITING_FULFILLMENT);
		boolean confirmationNeeded = false;
		boolean allRejected = true;
		for (AjaxOrderItem item : (List<AjaxOrderItem>) o.getItems()) {
			if (item.isRejected()) {
				confirmationNeeded = true;
				order.setItemValue(item.getProduct().getId(), "status",
						"rejected", currentPerms());
			} else
				allRejected = false;
		}
		if (confirmationNeeded)
			order.setStatus(Order.Status.AWAITING_CONFIRMATION);
		if (confirmationNeeded && !allRejected) {
			order.confirmPartial();
		} else if (allRejected) {
			order.setStatus(Status.REJECTED);
			order.reject();
		} else
			order.fulfill();
		dblogger.addOrder(order);
		return createAjaxOrder(order);
	}

	public AjaxOrder cancelOrder(AjaxOrder o) {
		Order order = getOrder(o);
		order.setStatus(Status.CANCELLED);
		order.cancel();
		dblogger.addOrder(order);
		return createAjaxOrder(order);
	}

	public AjaxOrder confirmOrder(AjaxOrder o) {
		Order order = getOrder(o);
		order.setStatus(Status.AWAITING_FULFILLMENT);
		order.fulfill();
		dblogger.addOrder(order);
		return createAjaxOrder(order);
	}

	public AjaxOrder fulfillOrder(AjaxOrder o) {
		Order order = getOrder(o);
		order.setStatus(Order.Status.FULFILLED);
		dblogger.addOrder(order);
		return createAjaxOrder(order);
	}

	public AjaxOrder rejectOrder(AjaxOrder o) {
		Order order = getOrder(o);
		order.setStatus(Status.REJECTED);
		order.reject();
		dblogger.addOrder(order);
		return createAjaxOrder(order);
	}

	public String getDownloadDir() {
		return getProperties().getProperty("download_url");
	}

	@CoinjemaDynamic(type = "lazerweb.config")
	private Properties getProperties() {
		return null;
	}

	private Collection<CartObject> getCartObjects(Collection<Request> requests) {
		List<CartObject> carts = new ArrayList<CartObject>();
		for (Request r : requests) {
			ProductFamily family = dbutil.getProductFamily(r.getProduct()
					.getFamilyName());
			Cart c = new Cart(family.getProduct(r.getProduct().getId(),
					currentPerms()), 1);
			if (r.isDownload()) {
				c.addInstruction("download", r.getFiletype());
			} else
				c.addInstruction("order", "");
			carts.add(c);
		}
		return carts;

	}

	private AjaxOrderResponse createAjaxOrderResponse(OrderResponse response) {
		AjaxOrderResponse ajaxResp = new AjaxOrderResponse(response
				.getInformation());
		ajaxResp.setOrderNos(response.getOrderNos());
		return ajaxResp;
	}

	private Map<String, Object> getInstructions(Collection<FormField> fields,
			AjaxAddress aa) {
		Map<String, Object> global = new HashMap<String, Object>();
		if (aa != null) {
			Address a = new Address();
			a.setAddress1(aa.getAddress1());
			a.setAddress2(aa.getAddress2());
			a.setAttn(aa.getAttn());
			a.setCity(new City(aa.getCity()));
			a.setCompany(new Company(aa.getCompany().getName()));
			a.setCountry(new Country(aa.getCountry()));
			a.setPhone(aa.getPhone());
			a.setState(new State(aa.getState()));
			a.setZip(aa.getZip());
			a = addressAdder.addOrGet(a);
			getLog().info("Using address " + a + " from id " + aa.getId());
			global.put("orderShipAddress", a);
		}
		for (FormField ff : fields) {
			global.put(ff.getName(), ff.getValue());
		}
		return global;
	}

	public boolean sendLinkEmail(String emailAddress, String message,
			List families, String[] links) {
		StringBuffer mes = new StringBuffer("The following "
				+ makeString(families)
				+ " files have been made available for you to download:\n\n");
		Controller con = getAppController();
		for (String download : links)
			mes.append(download).append("\n\n");
		mes.append(message);
		emailer.sendMessage(new String[] { emailAddress },
				((UserBean) getThreadLocalHandlerData().getUserBean("user"))
						.getEmailAddress(), "Images Available from "
						+ makeString(families), "text/plain", mes.toString());
		return true;
	}

	private String makeString(List listOfStrings) {
		StringBuffer buf = new StringBuffer();
		if (listOfStrings.size() > 1)
			buf.append("[");
		int len = 0;
		for (Object s : listOfStrings) {
			buf.append(s.toString());
			len++;
			if (len < listOfStrings.size())
				buf.append(", ");
		}
		if (listOfStrings.size() > 1)
			buf.append("]");
		return buf.toString();
	}

	/**
	 * @param orders
	 * @return
	 */
	private Map<String, Tuple<Collection<Request>, Collection<FormField>>> organizeByFamily(
			List orders) {
		Map<String, Tuple<Collection<Request>, Collection<FormField>>> byFamily = new HashMap<String, Tuple<Collection<Request>, Collection<FormField>>>();
		for (OrderVerificationPackage pack : (List<OrderVerificationPackage>) orders) {
			for (Request request : (Collection<Request>) pack.getRequests()) {
				Tuple<Collection<Request>, Collection<FormField>> tup = byFamily
						.get(request.getProduct().getFamilyName());
				if (tup == null) {
					Collection<Request> requests = new HashSet<Request>();
					tup = new Tuple<Collection<Request>, Collection<FormField>>(
							requests, pack.getFields());
					byFamily.put(request.getProduct().getFamilyName(), tup);
				}
				tup.first.add(request);
			}
		}
		return byFamily;
	}

	private void mergePacks(List<OrderVerificationPackage> packs,
			OrderVerificationPackage pack) {
		for (OrderVerificationPackage p : packs) {
			if (p.isMergeable(pack)) {
				p.merge(pack);
				return;
			}
		}
		packs.add(pack);
	}

	private OrderVerificationPackage getFamilyVerificationPackage(
			ProductFamily family, boolean isDownload) {
		if (isDownload)
			return family.getFamilyDownloadVerificationPackage();
		else
			return family.getFamilyOrderVerificationPackage();
	}

	@CoinjemaDependency(type = "cacheService")
	public void setCache(CacheService c) {
		cache = c;
	}

	@CoinjemaDependency(type = "databaseLogger")
	public void setDblogger(DatabaseLogger dblogger) {
		this.dblogger = dblogger;
	}

	@CoinjemaDependency(type = "emailService")
	public void setEmailer(NotificationService emailer) {
		this.emailer = emailer;
	}

	@CoinjemaDependency(type = "userService")
	public void setUserService(UserService u) {
		ugd = u;
	}

}
