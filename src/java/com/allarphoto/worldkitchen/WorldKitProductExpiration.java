package com.allarphoto.worldkitchen;

import java.util.Calendar;
import java.util.GregorianCalendar;

import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.application.impl.DefaultExpirationTester;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class WorldKitProductExpiration extends DefaultExpirationTester {

	public boolean isExpired(Product p) {
		if (p == null)
			return false;
		Calendar date = new GregorianCalendar();
		Calendar expiresOn = Converter.getCalendar(p.getValue("Expiration"),
				null);
		return expiresOn != null && date.after(expiresOn);
	}

	public boolean hasPermission(Product p, Right right, SecurityModel perms) {
		boolean hasRight = perms.getPermission(p.getProductFamilyName(),
				Resource.DATATABLE, right);
		if (hasRight && isExpired(p))
			hasRight = perms.getPermission(p.getProductFamilyName(),
					Resource.DATATABLE, Right.ADMIN);
		return hasRight;
	}

	public boolean hasExpiredPermission(Product p, Right right,
			SecurityModel perms) {
		boolean hasRight = true;
		if (hasRight && isExpired(p))
			hasRight = perms.getPermission(p.getProductFamilyName(),
					Resource.DATATABLE, Right.ADMIN);
		return hasRight;
	}

}
