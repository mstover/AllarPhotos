package com.allarphoto.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ProductFamily;

public class OrderItem extends LogItem {

	private static final long serialVersionUID = 1;

	Map<String, String> productValue = new HashMap<String, String>();

	Product product;

	public Product getProduct(SecurityModel security) {
		if (product == null) {
			ProductFamily family = getCache().getCache(ProductFamily.class)
					.getCachedObject("descriptiveName", getValue("family"));
			product = family.getProduct(Converter.getInt(
					getValue("product_id"), -1), security);
		}
		return product;
	}

	public OrderItem setProduct(Product product) {
		this.product = product;
		return this;
	}

	public String getOrderNo() {
		return getValue("orderNo");
	}

	public OrderItem setOrderNo(String orderNo) {
		setValue("orderNo", orderNo);
		return this;
	}

	public OrderItem setProductValue(String key, String val) {
		productValue.put(key, val);
		return this;
	}

	public String getProductValue(String key) {
		return productValue.get(key);
	}

	public Set<Map.Entry<String, String>> getProductValues() {
		return productValue.entrySet();
	}

	@Override
	public OrderItem setValue(String name, String value) {
		return (OrderItem) super.setValue(name, value);
	}

	@Override
	public OrderItem setSortBy(String[] newSortBy) {
		return (OrderItem) super.setSortBy(newSortBy);
	}

	@CoinjemaDynamic(type = "dbutil")
	private DatabaseUtilities getDbUtil() {
		return null;
	}

	@CoinjemaDynamic(type = "cacheService")
	protected CacheService getCache() {
		return null;
	}

}
