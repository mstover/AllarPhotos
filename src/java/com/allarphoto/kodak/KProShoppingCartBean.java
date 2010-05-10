package com.allarphoto.kodak;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.Controller;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.Cart;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.utils.Right;

@CoinjemaObject
public class KProShoppingCartBean extends ShoppingCartBean {
	Controller controller;

	Logger log;

	public String getTagLine(String pre, Cart cartObj, String post,
			SecurityModel perms) {
		if (!cartObj.getProduct().getProductFamily()
				.getProductExpirationTester().hasPermission(cartObj.prod,
						Right.ORDER, perms))
			return pre + "To order or download this file, please<br>"
					+ "<A HREF='requestexpired.jsp?request_product_family="
					+ cartObj.prod.getProductFamilyName()
					+ "&request_product_id=" + cartObj.prod.getId() + "'>"
					+ "request renegotiation of usage rights.</A><br>" + post;
		else
			return "";
	}

	@CoinjemaDependency(type = "appController")
	public void setController(Controller c) {
		controller = c;
	}

	public Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}
}
