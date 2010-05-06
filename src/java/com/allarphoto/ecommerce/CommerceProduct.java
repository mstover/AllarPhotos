/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import strategiclibrary.util.Files;

import com.lazerinc.application.Product;
import com.lazerinc.beans.Path;
import com.lazerinc.cached.DatabaseObject;

public class CommerceProduct implements Product, Serializable, DatabaseObject {
	private static final long serialVersionUID = 1;

	String family;

	Path path;

	public Path getPath() {
		return path;
	}

	public String getPathName() {
		return path.getName();
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public CommerceProduct() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public int getId() {
		return id;
	}

	public void setPrimary(String newPrimary) {
		primary = newPrimary;
	}

	public String getPrimary() {
		return primary;
	}

	String displayName;

	public synchronized String getDisplayName() {
		return getName();
	}

	public synchronized String getName() {
		if (displayName == null) {
			displayName = Files.hackOffExtension(getPrimary());
		}
		return displayName;
	}

	/***************************************************************************
	 * Sets the price hashtable for this product.
	 * 
	 * @param newPrice
	 *            price hashtable to be set.
	 **************************************************************************/
	public void setPrice(java.util.Hashtable newPrice) {
		price = newPrice;
	}

	/***************************************************************************
	 * Sets a particular price for the product given a key and a price float
	 * value.
	 * 
	 * @param key
	 *            Key to use to store the price under.
	 * @param pr
	 *            Price to be stored.
	 **************************************************************************/
	public void setPrice(String key, float pr) {
		price.put(key, new Float(pr));
	}

	/***************************************************************************
	 * Gets the price hashtable for this product.
	 * 
	 * @return hashtable of keys-prices for this product.
	 **************************************************************************/
	public java.util.Hashtable getPrice() {
		return price;
	}

	/***************************************************************************
	 * Gets a particular price out of the price hash given the price key
	 * 
	 * @param key
	 *            Key to use to find a particular price.
	 * @return float price value.
	 **************************************************************************/
	public Float getPrice(String key) {
		Float pr = (Float) price.get(key);
		return pr;
	}

	public void setInventory(int newInventory) {
		inventory = newInventory;
	}

	public int getInventory() {
		return inventory;
	}

	public void setValue(MultiMap newValue) {
		value = newValue;
	}

	public MultiMap getValue() {
		return value;
	}

	/***************************************************************************
	 * @param valueName
	 *            Name of value to retrieve
	 * @return Value.
	 **************************************************************************/
	public Object getValue(String valueName) {
		Collection<Object> values = (Collection<Object>) value.get(valueName);
		if (values != null && values.size() > 0)
			return values.iterator().next();
		else
			return null;
	}

	/***************************************************************************
	 * Sets a value for the product.
	 * 
	 * @param name
	 *            Name of value to set.
	 * @param v
	 *            Value to set.
	 **************************************************************************/
	public void setValue(String name, Object v) {
		value.put(name, v);
	}

	public void resetValue(String name, Object v) {
		value.remove(name);
		value.put(name, v);

	}

	/***************************************************************************
	 * Removes a value for the product.
	 * 
	 * @param name
	 *            Name of value to remove.
	 * @param v
	 *            Value to set.
	 **************************************************************************/
	public void removeValue(String name) {
		value.remove(name);
	}

	/***************************************************************************
	 * Gets a list of value names for this product in their proper order.
	 * 
	 * @return Array list of value names.
	 **************************************************************************/
	public Set getValueNames() {
		return value.keySet();
	}

	public Collection getValues(String name) {
		return (Collection) value.get(name);
	}

	public Calendar getDateCataloged() {
		return dateCataloged;
	}

	public void setDateCataloged(Calendar newDateCataloged) {
		dateCataloged = newDateCataloged;
	}

	public void setDateCataloged(java.util.Date newDateCataloged) {
		dateCataloged = new GregorianCalendar();
		dateCataloged.setTime(newDateCataloged);
	}

	public void setId(int newId) {
		id = newId;
	}

	public void setProductFamily(
			com.lazerinc.ecommerce.ProductFamily newProductFamily) {
		productFamily = newProductFamily;
	}

	public void setProductFamilyName(String family) {
		this.family = family;
	}

	public com.lazerinc.ecommerce.ProductFamily getProductFamily() {
		return productFamily;
	}

	public String getProductFamilyName() {
		return productFamily != null ? productFamily.getTableName() : family;
	}

	/***************************************************************************
	 * Sets a price break point.
	 * 
	 * @param priceKey
	 *            Price key for which price break point applies.
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @param price
	 *            Price of each item beyond price break point.
	 **************************************************************************/
	public void setPriceBreak(String priceKey, String breakPoint, String price) {
		Integer bp = new Integer(breakPoint);
		Float p = new Float(price);
		Map map = (Map) priceBreaks.get(priceKey);
		if (map == null)
			map = new HashMap();
		map.put(bp, p);
		priceBreaks.put(priceKey, map);
	}

	/***************************************************************************
	 * Sets a price break point.
	 * 
	 * @param priceKey
	 *            Price key for which price break point applies.
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @param price
	 *            Price of each item beyond price break point.
	 **************************************************************************/
	public void setPriceBreak(String priceKey, int breakPoint, float price) {
		Integer bp = new Integer(breakPoint);
		Float p = new Float(price);
		Map map = (Map) priceBreaks.get(priceKey);
		if (map == null)
			map = new HashMap();
		map.put(bp, p);
		priceBreaks.put(priceKey, map);
	}

	/***************************************************************************
	 * Gets all the price break points for a given price key as a sorted Integer
	 * Array.
	 * 
	 * @param priceKey
	 *            Price Key to look up price breaks for.
	 * @return Map object mapping breakpoints to prices/item.
	 **************************************************************************/
	public Integer[] getPriceBreaks(String priceKey) {
		Integer[] ret = new Integer[0];
		Map map = (Map) priceBreaks.get(priceKey);
		if (map != null) {
			ret = (Integer[]) map.keySet().toArray(new Integer[0]);
			Arrays.sort(ret);
		}
		return ret;
	}

	/***************************************************************************
	 * Gets a price break point.
	 * 
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @return price at breakpoint per item.
	 **************************************************************************/
	public Float getPriceBreak(String priceKey, int breakPoint) {
		return (Float) ((Map) priceBreaks.get(priceKey)).get(new Integer(
				breakPoint));
	}

	/***************************************************************************
	 * Gets a price break point.
	 * 
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @return price at breakpoint per item.
	 **************************************************************************/
	public Float getPriceBreak(String priceKey, String breakPoint) {
		return (Float) ((Map) priceBreaks.get(priceKey)).get(new Integer(
				breakPoint));
	}

	/***************************************************************************
	 * Gets the total price of the items given a quantity. Assumes the public
	 * price is to be used. This method probably shouldn't be used - the
	 * business model class should calculate the price. This is here for
	 * reference purposes.
	 * 
	 * @param num
	 *            Number of items to be bought.
	 * @param priceKey
	 *            Price key to use to find initial price.
	 * @return Total price of that many items given the price breaks.
	 **************************************************************************/
	public float getPriceTotal(int num, String priceKey) {
		Integer[] breaks = (Integer[]) priceBreaks.keySet().toArray(
				new Integer[1]);
		int count = 0;
		float total = 0;
		Float pr = getPrice(priceKey);
		if (pr != null) {
			do {
				if (breaks.length == 0)
					total += num * pr.floatValue();
				else if (count == 0 && breaks.length > 0)
					total += breaks[count].intValue() * pr.floatValue();
				else if (pr.floatValue() < ((Number) priceBreaks
						.get(breaks[count])).floatValue())
					total += (breaks[count].intValue() - breaks[count - 1]
							.intValue())
							* pr.floatValue();
				else
					total += (breaks[count].intValue() - breaks[count - 1]
							.intValue())
							* ((Number) priceBreaks.get(breaks[count]))
									.floatValue();
			} while (++count < breaks.length && num > breaks[count].intValue());
		}
		return total;
	}

	public void setPriceBreaks(java.util.Map newPriceBreaks) {
		priceBreaks = newPriceBreaks;
	}

	public java.util.Map getPriceBreaks() {
		return priceBreaks;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		if (getId() > 0)
			result = PRIME * result + getId();
		else {
			result = PRIME * result + ((path == null) ? 0 : path.hashCode());
			result = PRIME * result
					+ ((primary == null) ? 0 : primary.hashCode());
		}
		result = PRIME * result
				+ ((productFamily == null) ? 0 : productFamily.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CommerceProduct other = (CommerceProduct) obj;
		if (getId() > 0 && other.getId() > 0) {
			if (getId() != other.getId())
				return false;
		} else {
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			if (primary == null) {
				if (other.primary != null)
					return false;
			} else if (!primary.equals(other.primary))
				return false;
		}
		if (productFamily == null) {
			if (other.productFamily != null)
				return false;
		} else if (!productFamily.equals(other.productFamily))
			return false;
		return true;
	}

	public String toString() {
		return getId() + "";
	}

	public int compareTo(Object o2) {
		Product p2;
		int ret = 0;

		p2 = (Product) o2;
		if (getProductFamily() != null && p2.getProductFamily() != null)
			ret = getProductFamily().getTableName().compareTo(
					p2.getProductFamily().getTableName());
		if (ret == 0)
			ret = getPrimary().compareTo(p2.getPrimary());
		if (ret == 0 && getPath() != null)
			ret = getPath().compareTo(p2.getPath());
		return ret;
	}

	private String primary;

	private java.util.Hashtable price = new Hashtable();

	private int id;

	private Calendar dateCataloged, dateModified,dateCreated;

	private com.lazerinc.ecommerce.ProductFamily productFamily;

	private MultiMap value = MultiValueMap.decorate(new HashMap(),
			new Factory() {
				public Object create() {
					return new HashSet();
				}
			});

	public final static int PHYSICAL = 0;

	public final static int ELECTRONIC = 1;

	private int inventory;

	private java.util.Map priceBreaks = new HashMap();

	public Calendar getDateCreated() {
		if(dateCreated == null) return getDateCataloged();
		return dateCreated;
	}

	public void setDateCreated(Calendar newDateCreated) {
		dateCreated = newDateCreated;
		
	}

	public Calendar getDateModified() {
		return dateModified;
	}

	public void setDateModified(Calendar dateModified) {
		this.dateModified = dateModified;
	}
}