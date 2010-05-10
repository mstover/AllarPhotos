package com.allarphoto.servlet.actionhandler.user;

import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.coinjema.context.ContextOriented;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import static com.allarphoto.servlet.ActionConstants.ACTION_VALIDATE_LOGIN;
import static com.allarphoto.servlet.ActionConstants.ACTION_LOGOUT;

import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.servlet.actionhandler.login.Login;

public class AutoAction extends Login {

	public AutoAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		actionInfo
				.setUserBean("configuration", getController().getProperties());
		actionInfo.setRequestBean("params", actionInfo);
		if (actionInfo.getUserBean("browser") == null) {
			HttpServletRequest request = ((ServletHandlerData) actionInfo)
					.getRequest();
			String userAgent = request.getHeader("User-Agent");
			if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
				actionInfo.setUserBean("browser", "IE");
			} else if (userAgent != null && userAgent.indexOf("Gecko") > -1) {
				actionInfo.setUserBean("browser", "Mozilla");
			} else {
				actionInfo.setUserBean("browser", "Other");
			}
			getLog().info("User agent = " + userAgent);
		}
		actionInfo.setRequestBean("contextDir", getContextDir(actionInfo));
		actionInfo.setRequestBean("uri", ((ServletHandlerData) actionInfo)
				.getRequest().getRequestURI());
		try {
			URL url = new URL(((ServletHandlerData) actionInfo).getRequest()
					.getRequestURL().toString());
			actionInfo.setUserBean("host", url.getHost()
					+ (url.getPort() == -1 ? "" : (":" + url.getPort())));
		} catch (Exception e) {
			getLog().info("Bad url");
		}
		actionInfo.setRequestBean("refererURL",
				((ServletHandlerData) actionInfo).getRequest().getHeader(
						"referer"));
		actionInfo.setUserBean("coinjemaContext",
				((ContextOriented) actionInfo).getCoinjemaContext());
		if (actionInfo.getUserBean("user") == null
				&& actionInfo.containsAction(ACTION_VALIDATE_LOGIN)) {
			try {
				String username = actionInfo.getCookie("username");
				getLog().info("retrieved username = " + username);
				if (username != null
						&& actionInfo.getBean("ignoreCookie") == null) {
					username = URLDecoder.decode(username, "utf-8");
					String encPassword = actionInfo.getCookie("password");
					if (encPassword != null) {
						encPassword = URLDecoder.decode(encPassword, "utf-8");
						getLog().info("retrieved password = " + encPassword);
						try {
							CommerceUser user = getUgd()
									.checkUserWithEncPasswd(username,
											encPassword);
							getLog().info(
									"Logging in user: " + user.getUsername());
							getLog().info(
									"controller = "
											+ getController()
											+ " and product table = "
											+ getController().getConfigValue(
													"product_tables"));
							loginActivities(actionInfo, user);
						} catch (ActionException e) {
							actionInfo.setUserBean("ignoreCookie", "yup");
							throw e;
						}
					}
				}
			} catch (Exception e) {
				getLog().warn("Failed to auto login", e);
			}
		}
	}

	private String getContextDir(HandlerData info) {
		HttpServletRequest request = ((ServletHandlerData) info).getRequest();
		String path = request.getContextPath() + request.getServletPath();
		if (path.startsWith("/"))
			path = path.substring(1);
		int index = path.lastIndexOf("/");
		if (index > -1)
			return path.substring(0, path.lastIndexOf("/"));
		else
			return "";
	}

	public String getName() {
		return "autoAction";
	}

}
