package com.allarphoto.kodak;

import java.util.Calendar;
import java.util.GregorianCalendar;

import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.impl.DefaultExpirationTester;

public class KodakExpirationTester extends DefaultExpirationTester {

	public boolean isExpired(Product p) {
		if (p == null)
			return false;
		Calendar date = new GregorianCalendar();
		date.add(Calendar.MONTH, 3);
		Calendar expiresOn = Converter.getCalendar(p
				.getValue("Usage Expiration"), null);
		return p.getValue("Usage Expiration") != null
				&& ((expiresOn == null
						&& !p.getValue("Usage Expiration").equals(
								"Not Applicable") && !p.getValue(
						"Usage Expiration").equals("Unlimited")) || (expiresOn != null && date
						.after(expiresOn)));
	}

	public KodakExpirationTester() {
		super();
	}

	@Override
	public boolean willExpire(Product p) {
		if (p == null)
			return false;
		Calendar expiresOn = Converter.getCalendar(p
				.getValue("Usage Expiration"), null);
		return p.getValue("Usage Expiration") == null
				|| expiresOn != null
				|| (expiresOn == null
						&& !p.getValue("Usage Expiration").equals(
								"Not Applicable") && !p.getValue(
						"Usage Expiration").equals("Unlimited"));
	}
}
