package com.lazerinc.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrderVerificationPackage implements IsSerializable {

	String licenseText;

	String family;

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.FormField>
	 */
	List fields = new ArrayList();

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.Request>
	 */
	List requests = new ArrayList();

	public OrderVerificationPackage() {

	}

	public OrderVerificationPackage(String lt) {
		licenseText = lt;
	}

	public OrderVerificationPackage(String lt, String f) {
		this(lt);
		family = f;
	}

	public boolean isComplete() {
		Iterator iter = fields.iterator();
		while (iter.hasNext()) {
			FormField ff = (FormField) iter.next();
			if (ff.isRequired()
					&& (ff.getText() == null || ff.getText().length() == 0)) {
				return false;
			}
		}
		return true;
	}

	public String getWarning() {
		StringBuffer buf = new StringBuffer(
				"You must fill out the following fields: <br>");
		Iterator iter = fields.iterator();
		while (iter.hasNext()) {
			FormField ff = (FormField) iter.next();
			if (ff.isRequired()
					&& (ff.getText() == null || ff.getText().length() == 0)) {
				buf.append(ff.getName()).append("<br>");
			}
		}
		return buf.toString();
	}

	public void addRequest(Request r) {
		requests.add(r);
	}

	public void addRequests(Collection reqs) {
		requests.addAll(reqs);
	}

	public List getRequests() {
		return requests;
	}

	public OrderVerificationPackage getCopy() {
		OrderVerificationPackage copy = new OrderVerificationPackage(
				licenseText, family);
		copy.fields.addAll(fields);
		copy.requests.addAll(requests);
		return copy;
	}

	public void addField(FormField ff) {
		fields.add(ff);
	}

	public List getFields() {
		return fields;
	}

	public OrderVerificationPackage merge(OrderVerificationPackage pack) {
		if (!family.equals(pack.family))
			family = family + " - " + pack.family;
		requests.addAll(pack.requests);
		return this;
	}

	public boolean isMergeable(OrderVerificationPackage pack) {
		if (pack.licenseText.equals(licenseText) && isSameFields(pack.fields))
			return true;
		else
			return false;
	}

	private boolean isSameFields(List ff) {
		if (fields.size() == ff.size()) {
			Iterator iter = fields.iterator();
			while (iter.hasNext()) {
				if (!ff.contains(iter.next()))
					return false;
			}
			return true;
		}
		return false;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getLicenseText() {
		return licenseText;
	}

	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}

}
