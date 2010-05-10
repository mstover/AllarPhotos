/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.allarphoto.application.Product;

/*******************************************************************************
 * This class holds the information regarding the success or failure of an order
 * for all product families and products ordered. Information for the viewer, as
 * well as internal information is encapsulated here. This class should be used
 * to hold locations of downloadable files, as well as file descriptions,
 * estimated delivery times, etc.
 * 
 * @title: OrderResponse
 * @version: 1.0
 * @copyright: Copyright (c) 1999
 * @author: Michael Stover
 * @company: Lazer inc.
 ******************************************************************************/
public class OrderResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public OrderResponse() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setSuccessful(boolean newSuccessful) {
		successful = newSuccessful;
	}

	public void setSuccessful(ProductFamily fam, boolean newSuccessful) {
		FamilyReport famReport = getFamilyReport(fam);
		famReport.successful = newSuccessful;
	}

	public void setSuccessful(Product prod, boolean newSuccessful) {
		ProductReport prodReport = getProductReport(prod);
		prodReport.successful = newSuccessful;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public boolean isSuccessful(ProductFamily fam) {
		FamilyReport famReport = getFamilyReport(fam);
		return famReport.successful;
	}

	public boolean isSuccessful(Product prod) {
		ProductReport prodReport = getProductReport(prod);
		return prodReport.successful;
	}

	public void setMessage(String newMessage) {
		message = newMessage;
	}

	public void setMessage(ProductFamily fam, String newMessage) {
		FamilyReport famReport = getFamilyReport(fam);
		famReport.message = newMessage;
	}

	public void setMessage(Product prod, String newMessage) {
		ProductReport prodReport = getProductReport(prod);
		prodReport.message = newMessage;
	}

	public String getMessage() {
		return message;
	}

	public String getMessage(ProductFamily fam) {
		FamilyReport famReport = getFamilyReport(fam);
		return famReport.message;
	}

	public String getMessage(Product prod) {
		ProductReport prodReport = getProductReport(prod);
		return prodReport.message;
	}

	public void addInfo(String key, String newInfo) {
		info.put(key, newInfo);
	}

	public void addInfo(ProductFamily fam, String key, String newInfo) {
		FamilyReport famReport = getFamilyReport(fam);
		famReport.info.put(key, newInfo);
	}

	public void addInfo(Product prod, String key, String newInfo) {
		ProductReport prodReport = getProductReport(prod);
		if (key != null)
			prodReport.info.put(key, newInfo);
	}

	public String getInfo(String key) {
		String ret = "";
		ret = (String) info.get(key);
		if (ret == null)
			ret = "";
		return ret;
	}

	public String getInfo(ProductFamily fam, String key) {
		String ret = "";
		FamilyReport famReport = getFamilyReport(fam);
		if (famReport != null)
			ret = (String) famReport.info.get(key);
		if (ret == null)
			ret = "";
		return ret;
	}

	public String getInfo(Product prod, String key) {
		String ret = "";
		ProductReport prodReport = getProductReport(prod);
		if (prodReport != null)
			ret = (String) prodReport.info.get(key);
		if (ret == null)
			ret = "";
		return ret;
	}

	public void setVendorContactInfo(ProductFamily fam, String contact) {
		FamilyReport report = getFamilyReport(fam);
		report.vendorContactInfo = contact;
	}

	public String getVendorContactInfo(ProductFamily fam) {
		FamilyReport report = getFamilyReport(fam);
		return report.vendorContactInfo;
	}

	public Iterator productIterator() {
		return products.keySet().iterator();
	}

	public Map<Product, ProductReport> getProducts() {
		return products;
	}

	public Map<ProductFamily, FamilyReport> getFamilies() {
		return families;
	}

	public Iterator<ProductFamily> familyIterator() {
		return families.keySet().iterator();
	}

	public Iterator getInfoIterator() {
		return info.keySet().iterator();
	}

	public Map<String, String> getInformation() {
		return info;
	}

	public Iterator getInfoIterator(ProductFamily fam) {
		return ((FamilyReport) families.get(fam)).info.keySet().iterator();
	}

	public Iterator getInfoIterator(Product p) {
		return ((ProductReport) products.get(p)).info.keySet().iterator();
	}

	public Map getProductReportMap(Product p) {
		return getProductReport(p).info;
	}

	private FamilyReport getFamilyReport(ProductFamily fam) {
		FamilyReport report;
		if ((report = (FamilyReport) families.get(fam)) == null) {
			report = new FamilyReport();
			families.put(fam, report);
		}
		return report;
	}

	public Map getFamilyReportMap(ProductFamily fam) {
		return getFamilyReport(fam).info;
	}

	private ProductReport getProductReport(Product prod) {
		ProductReport report;
		if ((report = (ProductReport) products.get(prod)) == null) {
			report = new ProductReport();
			products.put(prod, report);
		}
		return report;
	}

	public void add(OrderResponse response) {
		String ls = System.getProperty("line.separator");
		products.putAll(response.products);
		families.putAll(response.families);
		if (!response.isSuccessful())
			successful = false;
		if (message != null && response.getMessage() != null)
			message = message + ls + response.getMessage();
		else if (response.getMessage() != null)
			message = response.getMessage();
		info.putAll(response.info);
		setOrderNo(response.getOrderNo());
	}

	private class ProductReport {

		boolean successful;

		String message;

		Map info = new HashMap();
	}

	private class FamilyReport {

		boolean successful;

		String message;

		Map info = new HashMap();

		String vendorContactInfo;
	}

	public String toString() {
		StringBuffer retBuf = new StringBuffer("OrderResponse: ");
		Iterator it = this.familyIterator();
		retBuf.append(this.getMessage() + " , ");
		retBuf.append("Families: ");
		Object temp;
		while (it.hasNext()) {
			temp = (ProductFamily) it.next();
			retBuf.append(((ProductFamily) temp).getTableName() + " ");
			retBuf.append(this.getMessage((ProductFamily) temp));
			retBuf.append(", ");
		}
		retBuf.append("Products: ");
		it = this.productIterator();
		while (it.hasNext()) {
			temp = (Product) it.next();
			retBuf.append(((Product) temp).getPrimary() + " ");
			retBuf.append(this.getMessage((Product) temp));
			retBuf.append(", ");
		}
		retBuf.append("Info: ");
		it = this.getInfoIterator();
		while (it.hasNext()) {
			temp = it.next();
			if (temp instanceof String)
				retBuf.append((String) temp + " ");
			retBuf.append(", ");
		}
		// TODO: Add string info for messages and information
		return retBuf.toString();
	}

	java.util.SortedMap<Product, ProductReport> products = new TreeMap<Product, ProductReport>();

	java.util.Map<ProductFamily, FamilyReport> families = new HashMap<ProductFamily, FamilyReport>();

	private boolean successful;

	String message;

	Map<String, String> info = new HashMap<String, String>();

	List<String> orderNos = new ArrayList<String>();

	public String getOrderNo() {
		if (orderNos.size() > 0)
			return orderNos.get(0);
		else
			return null;
	}

	public List<String> getOrderNos() {
		return orderNos;
	}

	public void setOrderNo(String orderNo) {
		if (orderNo != null)
			orderNos.add(orderNo);
	}

}
