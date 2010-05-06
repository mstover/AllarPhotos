package com.lazerinc.ecommerce.virtual;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.lazerinc.application.Product;
import com.lazerinc.beans.Path;
import com.lazerinc.ecommerce.CommerceProduct;
import com.lazerinc.ecommerce.ProductFamily;

public class VirtualProduct extends CommerceProduct {
	
	Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public VirtualProduct() {
	}
	
	public VirtualProduct(Product p)
	{
		product = p;
	}

	public int compareTo(Object o) {
		return product.compareTo(o);
	}

	public boolean equals(Object p) {
		return product.equals(p);
	}

	public Calendar getDateCataloged() {
		return product.getDateCataloged();
	}

	public Calendar getDateCreated() {
		return product.getDateCreated();
	}

	public Calendar getDateModified() {
		return product.getDateModified();
	}

	public String getDisplayName() {
		return product.getDisplayName();
	}

	public int getId() {
		return product.getId();
	}

	public int getInventory() {
		return product.getInventory();
	}

	public String getName() {
		return product.getName();
	}

	public Path getPath() {
		return product.getPath();
	}

	public String getPathName() {
		return product.getPathName();
	}

	public Float getPrice(String key) {
		return product.getPrice(key);
	}

	public Float getPriceBreak(String priceKey, int breakPoint) {
		return product.getPriceBreak(priceKey, breakPoint);
	}

	public Float getPriceBreak(String priceKey, String breakPoint) {
		return product.getPriceBreak(priceKey, breakPoint);
	}

	public Map getPriceBreaks() {
		return product.getPriceBreaks();
	}

	public Integer[] getPriceBreaks(String priceKey) {
		return product.getPriceBreaks(priceKey);
	}

	public String getPrimary() {
		return product.getPrimary();
	}

	public ProductFamily getProductFamily() {
		return product.getProductFamily();
	}

	public String getProductFamilyName() {
		return product.getProductFamilyName();
	}

	public Object getValue(String valueName) {
		return product.getValue(valueName);
	}

	public Set getValueNames() {
		return product.getValueNames();
	}

	public Collection getValues(String name) {
		return product.getValues(name);
	}

	public int hashCode() {
		return product.hashCode();
	}

	public void removeValue(String name) {
		product.removeValue(name);
	}

	public void resetValue(String name, Object v) {
		product.resetValue(name, v);
	}

	public void setDateCataloged(Calendar newDateCataloged) {
		product.setDateCataloged(newDateCataloged);
	}

	public void setDateCataloged(Date newDateCataloged) {
		product.setDateCataloged(newDateCataloged);
	}

	public void setDateCreated(Calendar newDateCreated) {
		product.setDateCreated(newDateCreated);
	}

	public void setDateModified(Calendar newDateModified) {
		product.setDateModified(newDateModified);
	}

	public void setId(int newId) {
		product.setId(newId);
	}

	public void setInventory(int newInventory) {
		product.setInventory(newInventory);
	}

	public void setPath(Path p) {
		product.setPath(p);
	}

	public void setPrice(String key, float pr) {
		product.setPrice(key, pr);
	}

	public void setPriceBreak(String priceKey, int breakPoint, float price) {
		product.setPriceBreak(priceKey, breakPoint, price);
	}

	public void setPriceBreak(String priceKey, String breakPoint, String price) {
		product.setPriceBreak(priceKey, breakPoint, price);
	}

	public void setPriceBreaks(Map newPriceBreaks) {
		product.setPriceBreaks(newPriceBreaks);
	}

	public void setPrimary(String newPrimary) {
		product.setPrimary(newPrimary);
	}

	public void setProductFamily(ProductFamily newProductFamily) {
		product.setProductFamily(newProductFamily);
	}

	public void setProductFamilyName(String family) {
		product.setProductFamilyName(family);
	}

	public void setValue(String name, Object v) {
		product.setValue(name, v);
	}
	
}
