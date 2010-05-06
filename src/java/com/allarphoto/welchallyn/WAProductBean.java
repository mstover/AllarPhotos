package com.lazerinc.welchallyn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.utils.Right;

public class WAProductBean extends ProductBean {

	public WAProductBean() {
	}

	@Override
	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms)) {
			types.add(new String[] { "jpg", "Download JPG" });
			String family = getProduct().getProductFamilyName();
			/*
			if ((getValue("Image Type").equals("Packaging")
							|| getValue("Image Type").equals("Logos")
							|| getValue("Image Type").equals("Logo") 
				)
				types.add(new String[] { "png", "Download PNG (PowerPoint)" });
			*/
			if (getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms)
			)
				types.add(new String[] { "originals", "Download Original" });
		}

		return types;
	}

	@Override
	public boolean isOrderable(SecurityModel perms) {
		return false;
	}

}
