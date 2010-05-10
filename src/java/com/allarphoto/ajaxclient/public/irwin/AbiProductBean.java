package com.allarphoto.abi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.utils.Right;

public class AbiProductBean extends ProductBean {

	public boolean isFieldDisplayable(ProductField field) {
		return field.getDisplayOrder() > 0;
	}

	public boolean isFieldEditable(ProductField field) {
		if (field.getDisplayOrder() > 1499 && field.getDisplayOrder() < 1600)
			return true;
		else
			return false;
	}

	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		String imageType = (String) getValue("Image Type");
		String region = (String) getValue("Region");
		if ("Templates".equals(imageType) || "Logos".equals(imageType)) {
			if (getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
				types.add(new String[] { "originals", "Download Template" });
		} else {
			if (getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD, perms))
				types
						.add(new String[] { "jpg",
								"Download Low Res (Powerpoint)" });
		}
		if ("Canada".equals(region)) {
			if (getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
				types.add(new String[] { "originals",
						"Download Medum Res (Flyer)" });
		}
		if (("US".equals(region) && "Sell Sheets".equals(imageType))
				|| ("US Private Label".equals(region) && ("Packaging"
						.equals(imageType) || "Mechanicals".equals(imageType)))) {
			if (getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD, perms))
				types.add(new String[] { "pdf", "Download PDF File" });
		}
		return types;
	}

}
