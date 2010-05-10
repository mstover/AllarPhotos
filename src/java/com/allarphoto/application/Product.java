/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.application;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

import com.allarphoto.beans.Path;
import com.allarphoto.ecommerce.ProductFamily;

/*******************************************************************************
 * This interface describes a class that can represent any kind of product. In
 * addition to implementing the below methods, the Product object should
 * override the default equals() and hashCode() methods to ensure proper sorting
 * in Sorted Collections. Also, so that the real object can be retrieved just by
 * sending in a Product object with the Primary and family TableName set.
 ******************************************************************************/
public interface Product extends Comparable, Serializable {

	public int getId();

	public void setPrimary(String newPrimary);

	public String getPrimary();

	public Path getPath();

	public void setPath(Path p);

	public String getPathName();

	/**
	 * @deprecated use getName()
	 * @return
	 */
	public String getDisplayName();

	public String getName();

	/***************************************************************************
	 * Sets the price hashtable for this product.
	 * 
	 * @param newPrice
	 *            price hashtable to be set.
	 *            ********************************************************************
	 *            public void setPrice(java.util.Hashtable newPrice);
	 */

	/***************************************************************************
	 * Sets a particular price for the product given a key and a price float
	 * value.
	 * 
	 * @param key
	 *            Key to use to store the price under.
	 * @param pr
	 *            Price to be stored.
	 **************************************************************************/
	public void setPrice(String key, float pr);

	/***************************************************************************
	 * Gets the price hashtable for this product.
	 * 
	 * @return hashtable of keys-prices for this product.
	 *         *******************************************************************
	 *         public java.util.Hashtable getPrice();
	 */

	/***************************************************************************
	 * Gets a particular price out of the price hash given the price key
	 * 
	 * @param key
	 *            Key to use to find a particular price.
	 * @return float price value.
	 **************************************************************************/
	public Float getPrice(String key);

	public void setInventory(int newInventory);

	public int getInventory();

	/***************************************************************************
	 * *******************************************************************
	 * public void setValue(java.util.Map newValue);
	 */

	/***************************************************************************
	 * ****************************************************************** public
	 * java.util.Map getValue();
	 */

	/***************************************************************************
	 * @param valueName
	 *            Name of value to retrieve
	 * @return Value.
	 **************************************************************************/
	public Object getValue(String valueName);

	public Collection getValues(String name);

	/***************************************************************************
	 * Sets a value for the product.
	 * 
	 * @param name
	 *            Name of value to set.
	 * @param v
	 *            Value to set.
	 **************************************************************************/
	public void setValue(String name, Object v);

	/**
	 * Sets a value for the product, erasing previous values.
	 * 
	 * @param name
	 * @param v
	 */
	public void resetValue(String name, Object v);

	/***************************************************************************
	 * Removes a value for the product.
	 * 
	 * @param name
	 *            Name of value to remove.
	 * @param v
	 *            Value to set.
	 **************************************************************************/
	public void removeValue(String name);

	/***************************************************************************
	 * Gets a list of value names for this product in their proper order.
	 * 
	 * @return Array list of value names.
	 **************************************************************************/
	public Set getValueNames();

	public Calendar getDateCataloged();

	public Calendar getDateModified();

	public void setDateCataloged(Calendar newDateCataloged);

	public void setDateModified(Calendar newDateModified);
	
	public void setDateCreated(Calendar newDateCreated);
	
	public Calendar getDateCreated();

	public void setDateCataloged(java.util.Date newDateCataloged);

	public void setId(int newId);

	public void setProductFamily(
			com.allarphoto.ecommerce.ProductFamily newProductFamily);

	public void setProductFamilyName(String family);

	public com.allarphoto.ecommerce.ProductFamily getProductFamily();

	public String getProductFamilyName();

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
	public void setPriceBreak(String priceKey, String breakPoint, String price);

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
	public void setPriceBreak(String priceKey, int breakPoint, float price);

	/***************************************************************************
	 * Gets all the price break points for a given price key as a Map object.
	 * 
	 * @param priceKey
	 *            Price Key to look up price breaks for.
	 * @return Map object mapping breakpoints to prices/item.
	 **************************************************************************/
	public java.util.Map getPriceBreaks();

	/***************************************************************************
	 * Gets all the price break points for a given price key as a sorted Integer
	 * Array.
	 * 
	 * @param priceKey
	 *            Price Key to look up price breaks for.
	 * @return Map object mapping breakpoints to prices/item.
	 **************************************************************************/
	public Integer[] getPriceBreaks(String priceKey);

	/***************************************************************************
	 * Gets a price break point.
	 * 
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @return price at breakpoint per item.
	 **************************************************************************/
	public Float getPriceBreak(String priceKey, int breakPoint);

	/***************************************************************************
	 * Gets a price break point.
	 * 
	 * @param breakPoint
	 *            Number of items at which price break is made.
	 * @return price at breakpoint per item.
	 **************************************************************************/
	public Float getPriceBreak(String priceKey, String breakPoint);

	public void setPriceBreaks(java.util.Map newPriceBreaks);

	public boolean equals(Object p);

	public int hashCode();

}
