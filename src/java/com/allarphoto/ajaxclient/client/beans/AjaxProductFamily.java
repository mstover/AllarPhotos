package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;

public class AjaxProductFamily implements IsSerializable {

	transient Services services;

	String familyName;

	String description;

	String descriptiveName;

	AjaxProductField[] fields;

	boolean remoteManaged;

	public AjaxProductFamily() {

	}

	public AjaxProductFamily(String n, String d, String dn,
			AjaxProductField[] f, boolean rm) {
		familyName = n;
		description = d;
		descriptiveName = dn;
		fields = f;
		remoteManaged = rm;
	}

	public void addField(AjaxProductField newField) {
		AjaxProductField[] newFields = new AjaxProductField[fields.length + 1];
		AjaxSystem.arraycopy(fields, 0, newFields, 1, fields.length);
		newFields[0] = newField;
		fields = newFields;
	}
	
	public void deleteField(String fieldName)
	{
		AjaxProductField[] newFields = new AjaxProductField[fields.length - 1];
		int count = 0;
		for(int i = 0;i < fields.length;i++)
		{
			if(!fields[i].getName().equals(fieldName))
				newFields[count++] = fields[i];
		}
		fields = newFields;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptiveName() {
		return descriptiveName;
	}

	public void setDescriptiveName(String descriptiveName) {
		this.descriptiveName = descriptiveName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public AjaxProductField[] getFields() {
		return fields;
	}

	public void setFields(AjaxProductField[] fields) {
		this.fields = fields;
	}

	public AjaxProductField getField(String fieldName) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(fieldName))
				return fields[i];
		}
		return null;
	}

	public boolean isRemoteManaged() {
		return remoteManaged;
	}

	public void setRemoteManaged(boolean remoteManaged) {
		this.remoteManaged = remoteManaged;
	}

}
