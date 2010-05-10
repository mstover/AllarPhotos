package com.allarphoto.ajaxclient.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.context.ContextFactory;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import strategiclibrary.util.Converter;
import strategiclibrary.util.Files;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.ajaxclient.client.beans.AjaxCompany;
import com.allarphoto.ajaxclient.client.beans.AjaxGroup;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.AjaxProductField;
import com.allarphoto.ajaxclient.client.beans.AjaxResource;
import com.allarphoto.ajaxclient.client.beans.AjaxRights;
import com.allarphoto.ajaxclient.client.beans.AjaxUser;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.Address;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.security.NoPermissions;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

@CoinjemaObject
public abstract class AbstractGwtServlet extends RemoteServiceServlet {
	Logger log;

	protected DatabaseUtilities dbutil;

	private ThreadLocal<HandlerData> handlerData = new ThreadLocal<HandlerData>() {

		@Override
		protected HandlerData initialValue() {
			return null;
		}

	};

	public AbstractGwtServlet() {
		super();
	}

	protected HandlerData getThreadLocalHandlerData() {
		return handlerData.get();
	}

	protected SecurityModel currentPerms() {
		try {
			return ((UserBean) getThreadLocalHandlerData().getBean("user"))
					.getPermissions();
		} catch (Exception e) {
			return new NoPermissions();
		}
	}

	protected AjaxProductFamily createAjaxProductFamily(ProductFamily pf) {
		return new AjaxProductFamily(pf.getTableName(), pf.getDescription(), pf
				.getDescriptiveName(), createAjaxProductFields(pf.getFields()),
				pf.isRemoteManaged());
	}

	protected CommerceUser currentUser() {
		return ((UserBean) getThreadLocalHandlerData().getBean("user"))
				.getUser();
	}

	protected AjaxProductField[] createAjaxProductFields(
			Collection<ProductField> fs) {
		AjaxProductField[] fields = new AjaxProductField[fs.size()];
		int count = 0;
		for (ProductField f : fs) {
			fields[count++] = new AjaxProductField(f.getName(), f.getFamily(),
					f.getType(), f.getSearchOrder(), f.getDisplayOrder());
		}
		return fields;
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			getLog().info("Calling for GWT service");
			HttpSession session = request.getSession(true);
			ServletContext sContext = session.getServletContext();
			if (session.getAttribute("coinjemaContext") != null) {
				ContextFactory.pushContext((CoinjemaContext) session
						.getAttribute("coinjemaContext"));
			} else
				ContextFactory.pushContext(new CoinjemaContext("lazerweb"));
			HandlerData info = new ServletHandlerData(request, response,
					session, sContext);
			handlerData.set(info);
			getLog().info(info.getRequestMessage());
			super.service(request, response);
			getLog().info("Did the deed");
		} catch (ServletException e) {
			getLog().error("service call caused error", e);
			throw e;
		} catch (IOException e) {
			getLog().error("service call caused error", e);
			throw e;
		} catch (Throwable e) {
			getLog().error("service call caused error", e);
		} finally {
			ContextFactory.popContext();
		}
	}

	protected Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	protected AjaxAddress createAjaxAddress(Address a) {
		if (a == null)
			return null;
		AjaxAddress addr = new AjaxAddress();
		addr.setAddress1(a.getAddress1());
		addr.setAddress2(a.getAddress2());
		addr.setCity(a.getCity().getName());
		addr.setState(a.getState().getName());
		addr.setZip(a.getZip());
		addr.setCountry(a.getCountry().getName());
		addr.setPhone(a.getPhone());
		addr.setAttn(a.getAttn());
		addr.setId(a.getId());
		addr.setInUse(a.isInUse());
		getLog().info("company = " + a.getCompany());
		getLog().info("industry = " + a.getCompany().getIndustry());
		addr.setCompany(new AjaxCompany(a.getCompany().getName(), a
				.getCompany().getIndustry().getName()));
		return addr;
	}

	protected AjaxGroup createAjaxGroup(UserGroup g) {
		AjaxGroup group = new AjaxGroup(g.getName(), g.getDescription());
		for (Resource r : g.getAvailableResourceList()) {
			AjaxResource ar = new AjaxResource(r.getName(), r.getType());
			Rights rights = g.getPermissions(r);
			AjaxRights arights = new AjaxRights(rights.getRight(Right.ADMIN),
					rights.getRight(Right.READ), rights.getRight(Right.ORDER),
					rights.getRight(Right.DOWNLOAD), rights
							.getRight(Right.DOWNLOAD_ORIG), rights
							.getRight(Right.UPLOAD));
			group.addRight(ar, arights);
		}
		return group;
	}

	protected AjaxUser createAjaxUser(CommerceUser u) {
		AjaxUser user = new AjaxUser();
		user.setAddressLine1(u.getAddressLine1());
		user.setAddressLine2(u.getAddressLine2());
		user.setBillAddress1(u.getBillAddress1());
		user.setBillAddress2(u.getBillAddress2());
		user.setBillCity(u.getBillCity().getName());
		user.setBillCountry(u.getBillCountry().getName());
		user.setBillPhone(u.getBillPhone());
		user.setBillState(u.getBillState().getName());
		user.setBillZip(u.getBillZip());
		user.setCity(u.getCity().getName());
		user.setCompany(u.getCompany().getName());
		user.setEmailAddress(u.getEmailAddress());
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setMiddleInitial(u.getMiddleInitial());
		user.setPassword("N/A");
		user.setPhone(u.getPhone());
		user.setReferrer(u.getReferrer().getName());
		user.setShipAddress1(u.getShipAddress1());
		user.setShipAddress2(u.getShipAddress2());
		user.setShipCity(u.getShipCity().getName());
		user.setShipCountry(u.getShipCountry().getName());
		user.setShipPhone(u.getShipPhone());
		user.setShipState(u.getShipState().getName());
		user.setShipZip(u.getShipZip());
		user.setState(u.getState().getName());
		user.setUsername(u.getUsername());
		user.setZip(u.getZip());
		user.setGroups(getGroupList(u.getGroups()));
		user.setExpiration(Converter.formatCalendar(u.getExpDate(),
				"MM/dd/yyyy"));
		return user;
	}

	protected String[] getGroupList(Collection<UserGroup> groups) {
		String[] gs = new String[groups.size()];
		int count = 0;
		for (UserGroup g : groups)
			gs[count++] = g.getName();
		return gs;
	}

	protected AjaxProduct createAjaxProduct(Product p, ProductBean bean) {
		AjaxProduct product = new AjaxProduct();
		product.setId(p.getId());
		product.setName(p.getName());
		product.setExt(Files.getExtension(p.getPrimary()));
		product.setFamilyName(p.getProductFamilyName());
		product.setHeight(Converter.getInt(p.getValue("Height"), 400));
		product.setPath(p.getPathName());
		product.setWidth(Converter.getInt(p.getValue("Width"), 400));
		product.setDateCataloged(Converter.formatCalendar(p.getDateCataloged(),
				"MM-dd-yyyy"));
		product.setDateModified(Converter.formatCalendar(p.getDateModified(),
				"MM-dd-yyyy"));
		product.setDateCreated(Converter.formatCalendar(p.getDateCreated(),
		"MM-dd-yyyy"));
		bean.setProduct(p);
		for (ProductField field : p.getProductFamily().getFields()) {
			product.setValue(field.getName(), bean.getFieldValuesString(field,
					"|").toString());
			product.setRawValues(field.getName(), getStringCollection(bean.getProduct().getValues(
					field.getName())));
		}
		product.setDownloadableTypes(bean.getDownloadableTypes(currentPerms()));
		product.setExpired(bean.isExpired());
		product.setOrderable(bean.isOrderable(currentPerms()));
		return product;
	}

	@CoinjemaDependency(type = "dbutil")
	public void setDatabaseUtilities(DatabaseUtilities db) {
		dbutil = db;
	}
	
	protected Collection<String> getStringCollection(Collection vals)
	{
		Collection<String> newCol = new ArrayList<String>();
		if(vals == null) return null;
		for(Object v : vals)
		{
			if(v != null)
				newCol.add(v.toString());
		}
		return newCol;
	}

}
