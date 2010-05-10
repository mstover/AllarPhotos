package com.allarphoto.client.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.category.ProductField;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ExpiredImage;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.server.ProductService;

@CoinjemaObject
public class ProductFileParser {
	static Set<String> primaryNames = new HashSet<String>();
	static {
		primaryNames.add("filename");
		primaryNames.add("file name");
		primaryNames.add("primary");
		primaryNames.add("product name");
		primaryNames.add("productname");
	}

	static Set<String> pathNames = new HashSet<String>();
	static {
		pathNames.add("pathname");
		pathNames.add("path name");
	}

	ProductService pService;

	ProductFamily family;

	String familyName;

	String delimiter;

	SecurityModel permissions;

	DatabaseUtilities dbUtil;

	Collection<String> badColumns = new HashSet<String>();

	Collection<String> badProducts = new HashSet<String>();

	Logger log;

	public ProductFileParser(String productFamilyName, String delimiter,
			SecurityModel permissions) {
		this.delimiter = delimiter;
		familyName = productFamilyName;
		this.permissions = permissions;
	}

	public Collection<Product> parseFileForProducts(String[] lines) {
		Collection<Product> products = new LinkedList<Product>();
		String[] headers = lines[0].split(delimiter);
		int filenameColumn = findPrimaryCol(headers);
		int pathColumn = findPathCol(headers);
		for (int x = 1; x < lines.length; x++) {
			products.add(parseProduct(headers, lines[x].split(delimiter),
					filenameColumn, pathColumn));
		}
		return products;
	}

	private Product parseProduct(String[] headers, String[] line,
			int filenameColumn, int pathColumn) {
		Product p = null;
		if (pathColumn == -1) {
			if (log.isDebugEnabled())
				log.debug("Looking for file named "
						+ stripExtension(stripValue(line[filenameColumn])));
			p = family.getProduct(
					stripExtension(stripValue(line[filenameColumn])), null,
					permissions);
			if (p == null || p instanceof ExpiredImage) {
				badProducts.add(stripValue(line[filenameColumn]));
				return null;
			}
		} else {
			if (log.isDebugEnabled())
				log.debug("Looking for file named "
						+ stripExtension(stripValue(line[filenameColumn])));
			p = family.getProduct(
					stripExtension(stripValue(line[filenameColumn])),
					stripValue(line[pathColumn]), permissions);
			if (p == null || p instanceof ExpiredImage) {
				badProducts.add(stripValue(line[filenameColumn]));
				return null;
			}
		}
		if (log.isDebugEnabled())
			log.debug("Found product " + p.getPrimary() + " " + p.getId());
		for (int x = 0; x < headers.length; x++) {
			if (!primaryNames.contains(stripValue(headers[x]).toLowerCase())
					&& !pathNames
							.contains(stripValue(headers[x]).toLowerCase())) {
				ProductField field = family.getField(stripValue(headers[x]));
				if (field != null && x < line.length) {
					if (log.isDebugEnabled()) {
						log.debug("Updating field " + field.getName()
								+ " with value " + stripValue(line[x]));
						log.debug("Current value: "
								+ p.getValues(field.getName()));
					}
					p.removeValue(field.getName());
					for (String val : splitValues(stripValue(line[x]))) {
						if (field.getType() == ProductField.NUMERICAL) {
							p.setValue(field.getName(), Converter.getDouble(val
									.trim()));
						} else
							p.setValue(field.getName(), val.trim());
					}
					if (log.isDebugEnabled())
						log.debug("After updating value: "
								+ p.getValues(field.getName()));
				} else
					badColumns.add(stripValue(headers[x]));
			}
		}
		return p;
	}

	protected String[] splitValues(String v) {
		if (v == null)
			return new String[0];
		return v.split("\\|");
	}

	public Collection<String> getBadColumns() {
		return badColumns;
	}

	public Collection<String> getBadProducts() {
		return badProducts;
	}

	private int findPrimaryCol(String[] headers) {
		int count = 0;
		for (String head : headers) {
			head = stripValue(head);
			if (log.isDebugEnabled())
				log.debug("Looking at header value: " + head + " compared to "
						+ primaryNames);
			if (primaryNames.contains(head.toLowerCase()))
				return count;
			else
				count++;
		}
		throw new RuntimeException("Invalid meta data: no primary column");
	}

	private int findPathCol(String[] headers) {
		int count = 0;
		for (String head : headers) {
			if (pathNames.contains(stripValue(head).toLowerCase()))
				return count;
			else
				count++;
		}
		return -1;
	}

	private String stripValue(String value) {
		if (value.startsWith("\"") && value.endsWith("\"")
				&& value.length() > 2) {
			return value.substring(1, value.length() - 1);
		} else if (value.startsWith("\"") && value.endsWith("\""))
			return "";
		return value.trim();
	}

	private String stripExtension(String value) {
		int index = value.lastIndexOf(".");
		if (index > -1)
			return value.substring(0, index);
		else
			return value;
	}

	@CoinjemaDependency(type = "productService")
	public void setPService(ProductService service) {
		pService = service;
	}

	@CoinjemaDependency(type = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
		family = dbUtil.getProductFamily(familyName);
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}
}
