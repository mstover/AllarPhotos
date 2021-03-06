package com.allarphoto.ajaxclient.client.components.pavilion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.utils.Right;

public class PavilionProductBean extends ProductBean {

	public PavilionProductBean() {
	}

	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms))
			types.add(new String[] { "jpg", "Download Low-Res" });
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
			types.add(new String[] { "originals", "Download High-Res" });
		return types;
	}

	public boolean isOrderable(SecurityModel perms) {
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.ORDER, perms))
			return true;
		else
			return false;
	}

}
