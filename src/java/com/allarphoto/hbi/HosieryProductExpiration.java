package com.allarphoto.hbi;

import java.util.Calendar;
import java.util.GregorianCalendar;

import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.impl.DefaultExpirationTester;

public class HosieryProductExpiration extends DefaultExpirationTester {

	public HosieryProductExpiration() {
	}

	public boolean pastExpiredThreshold(Product p) {
		if (this.isExpired(p)) {
			Calendar dateToday = new GregorianCalendar();
			Calendar threshold = Converter.getCalendar(p
					.getValue("Usage Expiration"), null);
			if(threshold != null)
				threshold.add(Calendar.MONTH, -1);
				/* NOTE: Per Melissa Hermanson 3/28/08
				 * Any expired image should be treated as though
				 * the image is past the Expiration threshold in
				 * that the borders aren't visible.
				 * threshold.add(Calendar.MONTH, 3);
				 */
			if (dateToday.after(threshold))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	public boolean isExpired(Product p) {
		if (p == null)
			return false;
		Calendar date = new GregorianCalendar();
		Calendar releaseDate = Converter.getCalendar(
				p.getValue("Release Date"), null);
		Calendar expiresOn = Converter.getCalendar(p
				.getValue("Usage Expiration"), null);
		if(expiresOn != null)
				expiresOn.add(Calendar.MONTH, -1);
		if (releaseDate != null && releaseDate.after(date))
			return true;
		return expiresOn != null && date.after(expiresOn);
	}

	public boolean willExpire(Product p) {
		if (p == null)
			return false;
		if (p.getValue("Usage Expiration") != null
				&& (p.getValue("Usage Expiration").toString().length() > 1)
				&& !(p.getValue("Usage Expiration").equals("Unlimited")) )
			return true;
		else
			return false;
	}

	public boolean hasUsageRights(Product p) {
		if (p == null)
			return false;
		if (p.getValue("Usage Expiration") != null
				&& (p.getValue("Usage Expiration").toString().length() > 1)
				&& !(p.getValue("Usage Expiration").equals("Unlimited")) )
			return true;
		else if (p.getValue("Usage Rights") != null
				&& (p.getValue("Usage Rights").toString().length() > 1)
				&& !(p.getValue("Usage Rights").equals("Unlimited")) )
			return true;
		else
			return false;
	}

}
