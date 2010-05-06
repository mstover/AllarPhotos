package com.lazerinc.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxProduct implements IsSerializable {

	String name;

	String ext;

	String familyName;

	/**
	 * @gwt.typeArgs <java.lang.String>
	 */
	List downloadableTypes = new ArrayList();

	String path;

	boolean expired, orderable;

	String dateModified, dateCataloged,dateCreated;

	private int height, width;

	private int id = -1;

	/**
	 * 
	 * @gwt.typeArgs <java.lang.String,java.lang.String>
	 */
	private java.util.Map values = new HashMap();

	/**
	 * @gwt.typeArgs <java.lang.String,java.util.HashSet<java.lang.String>>
	 */
	private java.util.Map rawValues = new HashMap();

	public AjaxProduct() {
	}

	public void setValue(String key, String value) {
		values.put(key, value);
	}

	public String getValue(String key) {
		return (String) values.get(key);
	}

	public String getExt() {
		return ext;
	}

	public void addDownloadableType(String type) {
		downloadableTypes.add(type);
	}

	public List getDownloadableTypes() {
		return downloadableTypes;
	}

	public void setDownloadableTypes(Collection types) {
		downloadableTypes.addAll(types);
	}

	public void setRawValues(String key, Collection vals) {
		if (vals != null)
			rawValues.put(key, new HashSet(vals));
	}

	public Set getRawValues(String key) {
		Set s = (Set) rawValues.get(key);
		if (s == null)
			return new HashSet();
		else
			return s;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((familyName == null) ? 0 : familyName.hashCode());
		result = PRIME * result + id;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final AjaxProduct other = (AjaxProduct) obj;
		if (familyName == null) {
			if (other.familyName != null)
				return false;
		} else if (!familyName.equals(other.familyName))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	public String getDateCataloged() {
		return dateCataloged;
	}

	public void setDateCataloged(String dateCataloged) {
		this.dateCataloged = dateCataloged;
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

}
