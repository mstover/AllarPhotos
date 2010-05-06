package com.lazerinc.irwin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.utils.Right;

public class IrwinProductBean extends ProductBean {

	public IrwinProductBean() {
	}

	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms))
			types.add(new String[] { "jpg", "Download low-res" });
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
			types.add(new String[] { "originals", "Download High-res" });
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms)
				&& this.getValue("Brand").equals("Mechanicals") )
			types.add(new String[] { "zip", "Download Mech Archive" });
		return types;
	}

	public boolean isOrderable(SecurityModel perms) {
		return false;
	}

}
