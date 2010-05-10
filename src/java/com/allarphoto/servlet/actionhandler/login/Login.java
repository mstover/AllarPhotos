package com.allarphoto.servlet.actionhandler.login;

import static com.allarphoto.servlet.ActionConstants.ACTION_LOGIN;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PASSWORD;
import static com.allarphoto.servlet.RequestConstants.REQUEST_USERNAME;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.allarphoto.client.beans.GenericDataBean;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.util.LogUtil;
import com.allarphoto.client.util.ShoppingCartUtil;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.servlet.actionhandler.cart.CartHandlerBase;

/**
 * Logs the user in to the DAM system.
 * 
 * @author Administrator
 * @action action_login
 * @requestParam request_username The user's username
 * @requestParam request_password The user's password
 * @bean UserBean user The user's information is stored in the UserBean and is
 *       available for the duration of the session.
 * @bean ShoppingCartBean cart The user's shopping cart is created and
 *       initialized at this time.
 * @bean GenericDataBean data This bean is also created and initialized. It
 *       makes available generic information about the system, states,
 *       countries, etc.
 */
public class Login extends CartHandlerBase {
	public LogUtil logUtil = new LogUtil();

	/**
	 * @see java.lang.Object#Object()
	 */
	public Login() {
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#requiresLogin()
	 */
	public boolean requiresLogin() {
		return false;
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws ActionException {
		CommerceUser user = getUgd().checkUser(
				actionInfo.getParameter(REQUEST_USERNAME),
				actionInfo.getParameter(REQUEST_PASSWORD));
		getLog().info("Logging in user: " + user.getUsername());
		getLog().info(
				"controller = " + getController() + " and product table = "
						+ getController().getConfigValue("product_tables"));
		loginActivities(actionInfo, user);
		if (actionInfo.hasParam("remember-me")) {
			actionInfo.removeUserBean("ignoreCookie");
			String[] host = ((String) actionInfo.getBean("host")).split(":");
			getLog().info(
					"Adding password cookie " + user.getEncryptedPassword());

			try {
				actionInfo.addPermCookie("username", URLEncoder.encode(user
						.getUsername(), "utf-8"), host[0],
						((ServletHandlerData) actionInfo).getRequest()
								.getContextPath(), false);
				actionInfo.addPermCookie("password", URLEncoder.encode(user
						.getEncryptedPassword(), "utf-8"), host[0],
						((ServletHandlerData) actionInfo).getRequest()
								.getContextPath(), false);
			} catch (UnsupportedEncodingException e) {
				getLog().warn("Unsupported encoding - utf-8");
			}
		}
		actionInfo.setUserBean("login_redirection_url", null);
	}

	protected void loginActivities(HandlerData actionInfo, CommerceUser user)
			throws ActionException {
		UserBean userBean = getUserBean(actionInfo);
		if (userBean == null) {
			userBean = new UserBean();
			actionInfo.setUserBean("user", userBean);
		}
		userBean.setUser(user);
		userBean.setPermissions(getUgd().getSecurity(user));
		ShoppingCartBean shoppingCart = cartUtil.getCart(actionInfo);
		cartUtil.initCart(shoppingCart, user);
		initializeDataBean(actionInfo);
		logUtil.logAction(new String[] { "action", "login", "user",
				user.getUsername() });
		getLog().info("Login successful for: " + user.getUsername());
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_LOGIN;
	}

	public void initializeDataBean(HandlerData actionInfo) {
	}
}