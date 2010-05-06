package com.lazerinc.ajaxclient.client.components.fineart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.utils.Right;

public class FineArtProductBean extends ProductBean {

	public FineArtProductBean() {
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
