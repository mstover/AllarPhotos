package com.lazerinc.hbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.utils.Right;

public class IntimatesProductBean extends ProductBean {

	@Override
	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms)) {
			types.add(new String[] { "jpg", "Download JPG" });
			String family = getProduct().getProductFamilyName();
			if (getValue("Image Type") != null) {
				if (getValue("Image Type").equals("Packaging")
						|| getValue("Image Type").equals("Digital Photography"))
					types.add(new String[] { "png", "Download PNG" });
				else if (getValue("Image Type").equals("Logos") /* || 
						getValue("Image Type").equals("Icons") */ ) {
					types.add(new String[] { "eps", "Download EPS" });
					types.add(new String[] { "png", "Download PNG" });
				} else if(getValue("Image Type").equals("Catalogs")) {
					types.add(new String[]{"originals","Download Original"});
				}
			}
			if(perms.isSuperAdmin()) types.add(new String[]{"originals","Download Original"});
		}
		return types;
	}

	@Override
	public boolean isOrderable(SecurityModel perms) {

		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.ORDER, perms)) {
			if (getValue("Image Type") != null
					&& (getValue("Image Type").equals("Logos") || getValue("Image Type").equals("Catalogs")))
				return false;
			else
				return true;
		} else
			return false;
	}
}
