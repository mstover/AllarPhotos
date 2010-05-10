package com.allarphoto.hbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.utils.Right;

public class HbiDefProductBean extends ProductBean {

	public HbiDefProductBean() {
	}

	@Override
	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms)) {
			types.add(new String[] { "jpg", "Download JPG" });
			String family = getProduct().getProductFamilyName();
			if ((family.equals("hb_hos") || family.equals("hb_leggs") || family.equals("hb_champ") || family.equals("hb_sales") )
					&& (getValue("Image Type").equals("Packaging")
							|| getValue("Image Type").equals("Cad Images")
							|| getValue("Image Type").equals("Logos")
							|| getValue("Image Type").equals("Logo") || getValue(
							"Image Type").equals("Photography")))
				types.add(new String[] { "png", "Download PNG (PowerPoint)" });
			if (family.equals("hb_logos"))
				types.add(new String[] { "eps", "Download EPS" });
			if ((getProduct().getProductFamily().getProductExpirationTester()
					.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms)
					&&	!((family.equals("hb_hos") || family.equals("hb_leggs") || family.equals("hb_champ") || family.equals("hb_sales") ) 
						&& (getValue("Image Type").equals("POS") 
							|| getValue("Image Type").equals("Photography") 
							|| getValue("Image Type").equals("Other"))
						)) ||
					(perms.isSuperAdmin())
			)
				types.add(new String[] { "originals", "Download Original (hi-res)" });
		}

		return types;
	}

	@Override
	public boolean isOrderable(SecurityModel perms) {
		return false;
	}

}
