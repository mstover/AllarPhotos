/*
 * DateBean.java Created on June 18, 2003, 12:41 PM
 */

package com.lazerinc.client.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author ksmoker
 */
public class DateBean implements Serializable {
	private static final long serialVersionUID = 1;

	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public void clear() {
	}

	public Calendar now() {
		return new GregorianCalendar();
	}

	public Calendar getDate() {
		return new GregorianCalendar();
	}

	public String getToday() {
		return formatDate(getDate());
	}

	public String getYesterday() {
		GregorianCalendar yest = new GregorianCalendar();
		yest.add(Calendar.DATE, -1);
		return formatDate(yest);
	}

	public String getLastWeek() {
		GregorianCalendar lastWeek = new GregorianCalendar();
		lastWeek.add(Calendar.DATE, -7);
		return formatDate(lastWeek);
	}

	public String getLastMonth() {
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.add(Calendar.MONTH, -1);
		return formatDate(lastMonth);
	}

	public Calendar yesterday() {
		Calendar d = now();
		d.add(Calendar.DAY_OF_YEAR, -1);
		return d;
	}

	public Calendar lastWeek() {
		Calendar d = now();
		d.add(Calendar.DAY_OF_YEAR, -7);
		return d;
	}

	public Calendar lastMonth() {
		Calendar d = now();
		d.add(Calendar.MONTH, -1);
		return d;
	}

	public Calendar prevMonth(int num_months) {
		Calendar d = now();
		d.add(Calendar.MONTH, -1 * num_months);
		return d;
	}

	public Calendar lastYear() {
		Calendar d = now();
		d.add(Calendar.YEAR, -1);
		return d;
	}

	public Calendar prevYear(int num_years) {
		Calendar d = now();
		d.add(Calendar.YEAR, -1 * num_years);
		return d;
	}

	public String formatDate(Calendar date) {
		if (date != null)
			return formatter.format(date.getTime());
		else
			return "";
	}

}
