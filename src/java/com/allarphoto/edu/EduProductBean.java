package com.allarphoto.edu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.utils.Right;

public class EduProductBean extends ProductBean {
	private static final long serialVersionUID = 1;

	public EduProductBean() {
	}

	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD, perms))
			types.add(new String[] { "jpg", "Download Low-Res" });
		if (getProduct().getProductFamily().getProductExpirationTester()
				.hasPermission(getProduct(), Right.DOWNLOAD_ORIG, perms))
			types.add(new String[] { "originals", "Download Hi-Res" });
		return types;
	}

	@Override
	public boolean isOrderable(SecurityModel perms) {
		return false;
	}

}
