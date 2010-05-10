package com.allarphoto.client.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MetadataBean implements Serializable {
	private static final long serialVersionUID = 1;

	Map<String, Collection<String>> metadata = new HashMap<String, Collection<String>>();

	Collection<String> fields = new LinkedList<String>();

	Collection<String> products;

	private static final String DEFAULT = "N/A";

	public MetadataBean() {
	}

	public MetadataBean(List fields, Collection<String> products) {
		initMetadata(fields, products);
	}

	public void initMetadata(List fields, Collection<String> products) {
		setFields(fields);
		setProducts(products);
	}

	public void setMetadata(Map<String, Collection<String>> metadata) {
		this.metadata = metadata;
	}

	public Map<String, Collection<String>> getMetadata() {
		return metadata;
	}

	public void addProduct(String product, Map pData) {
		Map<String, Collection<String>> mData = getMetadata();
		Iterator it = getFields().iterator();
		String val, field;
		List<String> temp = new ArrayList<String>();
		while (it.hasNext()) {
			field = (String) it.next();
			val = (String) pData.get(field);
			if (null == val)
				val = DEFAULT;
			temp.add(val);
		}
		mData.put(product, temp);
		setMetadata(mData);
	}

	public void setFields(Collection<String> fields) {
		this.fields = fields;
	}

	public Collection<String> getFields() {
		return fields;
	}

	public void addField(String field) {
		fields.add(field);
	}

	public void addProductDatum(String product, String field, String datum) {
	}

	public String getProductDatum(String product, String field) {
		return "";
	}

	public Collection<String> getProductData(String product) {
		return getMetadata().get(product);
	}

	public void clear() {
		fields.clear();
		metadata.clear();
	}

	public Collection<String> getProducts() {
		return products;
	}

	public void setProducts(Collection<String> products) {
		this.products = products;
	}

}