// Title: Toms Stuff
// Version:
// Copyright: Copyright (c) 1999
// Author: Tom Cousins
// Company: Lazer Inc.
// Description: Tools & Trials
package com.allarphoto.servlet.actionhandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.context.ContextOriented;

import strategiclibrary.service.webaction.AbstractWebAction;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.Controller;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.cached.functions.CityAdd;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.server.ProductService;
import com.allarphoto.server.UserService;

@CoinjemaObject(type = "action")
public abstract class ActionHandlerBase extends AbstractWebAction {
	protected static CityAdd cityAdder = new CityAdd();

	Controller controller;

	protected DatabaseUtilities dbUtil;

	protected DBConnect database;

	Logger log;

	protected static Set emptyStrings = new HashSet();

	protected UserService ugd;

	protected ProductService productService;
	static {
		emptyStrings.add("");
	}

	public Controller getController() {
		return controller;
	}

	public ActionHandlerBase() {
		super();
	}

	@CoinjemaDependency(method = "appController")
	public void setController(Controller controller) {
		System.out.println("Setting controller in context "
				+ ((ContextOriented) this).getCoinjemaContext()
				+ " controller = " + controller);
		this.controller = controller;
	}

	@CoinjemaDynamic(alias = "systemEmailReplyTo")
	protected String getEmailReplyTo() {
		return "webmaster@lazerinc.com";
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect database) {
		this.database = database;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

	protected Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public boolean requiresLogin() {
		return true;
	}

	public SecurityModel getCurrentUserPerms(HandlerData actionInfo) {
		UserBean userBean = getUserBean(actionInfo);
		if (userBean == null)
			return null;
		return userBean.getPermissions();
	}

	protected UserBean getUserBean(HandlerData actionInfo) {
		return (UserBean) actionInfo.getUserBean("user");
	}

	@CoinjemaDependency(type = "userService", method = "userService")
	public void setUserService(UserService ugd) {
		this.ugd = ugd;
	}

	protected UserService getUgd() {
		return ugd;
	}

	protected ProductService getProductService() {
		return productService;
	}

	@CoinjemaDependency(type = "productService", method = "productService")
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}