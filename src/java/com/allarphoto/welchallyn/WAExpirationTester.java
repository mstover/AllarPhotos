package com.lazerinc.welchallyn;

import java.util.Calendar;
import java.util.GregorianCalendar;

import strategiclibrary.util.Converter;

import com.lazerinc.application.Product;
import com.lazerinc.application.impl.DefaultExpirationTester;

public class WAExpirationTester extends DefaultExpirationTester {

	public WAExpirationTester() {
	}

	public boolean isExpired(Product p) {
		if (p == null)
			return false;
		Calendar date = new GregorianCalendar();
		Calendar releaseDate = Converter.getCalendar(
				p.getValue("Release Date"), null);
		Calendar expiresOn = Converter.getCalendar(p
				.getValue("Usage Expiration"), null);
		if (releaseDate != null && releaseDate.after(date))
			return true;
		date.add(Calendar.MONTH, 3);
		return expiresOn != null && date.after(expiresOn);
	}

}
