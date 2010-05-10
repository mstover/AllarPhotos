package com.allarphoto.client.util;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.ecommerce.UserProperties;
import com.allarphoto.server.UserService;
import com.allarphoto.utils.Functions;

/*******************************************************************************
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @created January 2, 2002
 * @version 1.0
 ******************************************************************************/
@CoinjemaObject
public class ShoppingCartUtil {
	DatabaseUtilities dbutil;

	Logger log;

	/***************************************************************************
	 * Constructor for the ShoppingCartUtil object
	 **************************************************************************/
	public ShoppingCartUtil() {
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLogger(Logger l) {
		log = l;
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param cartBean
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @param user
	 *            Description of Parameter
	 **************************************************************************/
	public void initCart(ShoppingCartBean cartBean, CommerceUser user) {
		UserService uService = getUgd();
		String savedCart = user.getProperty(UserProperties.SHOPPING_CART);
		StringBuffer replaceCart = new StringBuffer();
		boolean replaceFlag = false;
		String[] productEntries = Functions.split(savedCart, "|");
		for (int x = 0; x < productEntries.length; x++) {
			String[] entryInfo = Functions.split(productEntries[x], "~");
			if (entryInfo.length >= 2) {
				replaceFlag = replaceFlag
						|| processEntry(entryInfo, cartBean, replaceCart,
								productEntries[x], uService.getSecurity(user));
			}
		}
		if (replaceFlag) {
			user.setProperty(UserProperties.SHOPPING_CART, replaceCart
					.toString());
			uService.saveUserProperties(user);
		}
	}

	private boolean processEntry(String[] details, ShoppingCartBean cartBean,
			StringBuffer replaceCart, String wholeEntry,
			SecurityModel permissions) {
		boolean replaceFlag = false;
		ProductFamily fam = dbutil.getProductFamily(details[0]);
		Product p = fam.getProduct(details[1], permissions);
		if (null != p.getPrimary()) {
			cartBean.add(p);
			if (details.length > 2) {
				for (int i = 2; i < details.length; i++) {
					log.info("details[" + i + "] = " + details[i]);
					String instr = details[i];
					if (instr != null) {
						if ("order".equals(instr))
							cartBean.addInstruction(p, "order", "");
						else if (instr.startsWith("download:"))
							cartBean.addInstruction(p, "download", instr
									.substring("download:".length()));
					}
				}
			}
			if (replaceCart.length() > 0) {
				replaceCart.append("|" + wholeEntry);
			} else {
				replaceCart.append(wholeEntry);
			}
		} else {
			replaceFlag = true;
		}
		return replaceFlag;
	}

	public ShoppingCartBean getCart(HandlerData info) throws ActionException {
		ShoppingCartBean bean = (ShoppingCartBean) info.getUserBean("cart");
		Class<? extends ShoppingCartBean> expectedClass = getShoppingCartClass();
		if (bean == null || !bean.getClass().equals(expectedClass)) {
			info.removeUserBean("cart");
			return info.getUserBean("cart", expectedClass);
		}
		return bean;
	}

	@CoinjemaDynamic(method = "shoppingCartClass")
	protected Class<? extends ShoppingCartBean> getShoppingCartClass() {
		System.out.println("Getting default shopping cart");
		return ShoppingCartBean.class;
	}

	@CoinjemaDynamic(type = "userService", method = "userService")
	protected UserService getUgd() {
		return null;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDatabaseUtilities(DatabaseUtilities db) {
		this.dbutil = db;
	}
}
