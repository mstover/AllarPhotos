package com.lazerinc.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Tuple;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.category.ProductField;
import com.lazerinc.client.beans.ProductBean;
import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.ecommerce.CommerceProduct;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.ecommerce.Merchant;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

/*******************************************************************************
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @created January 3, 2002
 * @version 1.0
 ******************************************************************************/
@CoinjemaObject(type = "productService")
public class ProductService {

	private Logger log;

	private DatabaseUtilities dbUtil;

	private ObjectMappingService objectMapper;

	private CacheService productCache;

	DBConnect database;

	/***************************************************************************
	 * = "number_of_products_shown".
	 **************************************************************************/
	public static String MAX_PRODUCTS_SHOWN = "number_of_products_shown";

	/***************************************************************************
	 * = "product_tables".
	 **************************************************************************/

	// Stored Procedures
	final static String catValuesSP = "_cat_values";

	final static String productValuesSP = "_prod_values";

	// Default database string value
	final static String DEFAULT = "N/A";

	final static String GROUP_IMAGE = "Group Image";

	final static String MULTIPLE_TABLES = "multiple tables";

	private static Map commonCategories = new HashMap();

	/***************************************************************************
	 * Constructor for the ProductService object
	 **************************************************************************/
	public ProductService() {
	}

	@CoinjemaDependency(method = "productCache")
	public void setProductCache(CacheService cs) {
		productCache = cs;
	}

	public void refreshCaches() {
		productCache.clear(CommerceProduct.class);
	}

	@CoinjemaDependency(method = "productMapper")
	public void setProductMapper(ObjectMappingService oms) {
		objectMapper = oms;
	}

	public int getProductTableID(String familyName) {
		return dbUtil.getProductTableID(familyName);
	}

	public Merchant getMerchant(String merchant, SecurityModel security) {
		Merchant merch = null;
		if (security.getPermission(merchant, Resource.MERCHANT, Right.ADMIN))
			merch = dbUtil.getMerchant(merchant);
		return merch;
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param set
	 *            Description of Parameter
	 * @param pt
	 *            Description of Parameter
	 * @param f
	 *            Description of Parameter
	 * @param searchValue
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	public boolean checkIfCommon(Set<Product> set, String pt, ProductField f,
			String searchValue, SecurityModel security) {
		for (Product p : set) {
			if (!searchValue.equals(p.getValue(f.getName())))
				return false;
		}
		return true;
	}

	public Collection getAllProducts() {
		return new LinkedList();
	}

	public Tuple<Collection<String>, Collection<Map<String, Collection<String>>>> exportKeywords(
			Collection<Product> products) {
		Collection<Map<String, Collection<String>>> export = new ArrayList<Map<String, Collection<String>>>();
		Set<String> headers = new HashSet<String>();
		headers.add("filename");

		for (Product p : products) {
			ProductBean pBean = p.getProductFamily().getProductBean(p);
			Map<String, Collection<String>> values = new TreeMap<String, Collection<String>>();
			values.put("filename", Arrays
					.asList(new String[] { p.getPrimary() }));
			values.put("pathname", Arrays
					.asList(new String[] { p.getPathName() }));
			for (Object key : pBean.getDisplayFields()) {
				if (!key.toString().startsWith("_")) {
					headers.add(key.toString());
					values.put(key.toString(), (Collection<String>) p
							.getValues(key.toString()));
				}
			}
			for (Object key : p.getValueNames()) {
				if (!key.toString().startsWith("_")
						&& !headers.contains(key.toString())) {
					values.put(key.toString(), (Collection<String>) p
							.getValues(key.toString()));
					headers.add(key.toString());
				}
			}
			export.add(values);
		}
		headers.add("pathname");
		for (String header : headers) {
			for (Map<String, Collection<String>> data : export) {
				if (!data.containsKey(header))
					data.put(header, Arrays.asList(new String[] { "" }));
			}
		}
		return new Tuple<Collection<String>, Collection<Map<String, Collection<String>>>>(
				headers, export);
	}

	public Collection<Product> findProducts(Map<String, Object> searchValue) {
		Map<String, Object> values = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : searchValue.entrySet()) {
			if ("family".equals(entry.getKey())
					|| "table".equals(entry.getKey()))
				values.put("table", entry.getValue());
			else if ("id".equals(entry.getKey()))
				values.put("id", entry.getValue());
			else if ("Primary".equals(entry.getKey()))
				values.put("primaryValue", entry.getValue());
			else if ("name".equals(entry.getKey()))
				values.put("name", entry.getValue());
			else if ("path".equals(entry.getKey()))
				values.put("path", entry.getValue());
			else if (entry.getValue() instanceof Number) {
				values.put("category", entry.getKey());
				values.put("numberValue", entry.getValue());
			} else if (("dateCataloged".equals(entry.getKey()) || "Date Posted"
					.equals(entry.getKey()))
					&& entry.getValue() instanceof Calendar)
				values.put("dateValue", entry.getValue());
			else if ("dateCataloged".equals(entry.getKey())
					|| "Date Posted".equals(entry.getKey()))
				values.put("dateValueString", entry.getValue());
			else if (("ondateCataloged".equals(entry.getKey()) || "onDate Posted"
					.equals(entry.getKey()))
					&& entry.getValue() instanceof Calendar)
				values.put("onDateValue", entry.getValue());
			else if ("ondateCataloged".equals(entry.getKey())
					|| "onDate Posted".equals(entry.getKey()))
				values.put("onDateValueString", entry.getValue());
			else if (("dateModified".equals(entry.getKey()) || "Date Modified"
					.equals(entry.getKey()))
					&& entry.getValue() instanceof Calendar)
				values.put("modDateValue", entry.getValue());
			else if ("dateModified".equals(entry.getKey())
					|| "Date Modified".equals(entry.getKey()))
				values.put("modDateValueString", entry.getValue());
			else if (("ondateModified".equals(entry.getKey()) || "onDate Modified"
					.equals(entry.getKey()))
					&& entry.getValue() instanceof Calendar)
				values.put("onModDateValue", entry.getValue());
			else if ("ondateModified".equals(entry.getKey())
					|| "onDate Modified".equals(entry.getKey()))
				values.put("onModDateValueString", entry.getValue());
			else if ("NULL".equals(entry.getKey()) && entry.getValue() != null) {
				values.put("searchTerm", entry.getValue());
			} 
			else if ("EXACT_SEARCH".equals(entry.getKey()) && entry.getValue() != null) {
				values.put("exactSearchTerm", entry.getValue());
			} else if (entry.getKey().endsWith("%%")) {
				values.put("category", entry.getKey().substring(0,
						entry.getKey().length() - 2));
				values.put("value", entry.getValue());
				values.put("like_search", "true");
			} else {
				values.put("category", entry.getKey());
				values.put("value", entry.getValue());
			}
		}
		log.debug("Searching for products with values: " + values);
		return (Collection<Product>) objectMapper.getObjects(
				"findProducts.sql", values);
	}

	public Collection<ProductFamily> getProductFamilies() {
		return dbUtil.getProductFamilies();
	}

	@CoinjemaDependency(alias = "log4j")
	public void setlogger(Logger l) {
		log = l;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect database) {
		this.database = database;
	}
}
