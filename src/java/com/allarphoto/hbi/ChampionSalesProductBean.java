package com.allarphoto.hbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.utils.Right;

public class ChampionSalesProductBean extends HbiDefProductBean {

	public ChampionSalesProductBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms)) {
			types.add(new String[] { "jpg", "Download JPG" });
		}
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
			types.add(new String[] { "originals", "Download Hi-Res" });
		return types;
	}

	@Override
	public boolean isOrderable(SecurityModel perms) {

		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.ORDER, perms)) {
			return true;
		} else
			return false;
	}

}
