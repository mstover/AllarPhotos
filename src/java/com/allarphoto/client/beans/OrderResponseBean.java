package com.allarphoto.client.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.allarphoto.application.Product;
import com.allarphoto.ecommerce.OrderResponse;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.utils.Functions;

/**
 * @author Administrator To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates.
 */
public class OrderResponseBean implements Serializable {
	private static final long serialVersionUID = 1;

	OrderResponse response;

	/**
	 * @see com.allarphoto.client.beans.ResponseBean#clear()
	 */
	public void clear() {
		response = null;
	}

	public void setResponse(OrderResponse response) {
		this.response = response;
	}

	public OrderResponse getResponse() {
		return response;
	}

	public String getOrderNo() {
		return response.getOrderNo();
	}

	public boolean isOrder() {
		return getOrderNo() != null && getOrderNo().length() > 0;
	}

	public boolean isDownloadOrder() {
		return getDownloadFiles().size() > 0;
	}

	public Map getDownloadFiles() {
		Map downloadFiles = new HashMap();
		Iterator it = response.getInfoIterator();
		while (it.hasNext()) {
			String temp = (String) it.next();
			if (temp.startsWith("download_file")) {
				downloadFiles.put(temp, response.getInfo(temp));
			}
		}
		return downloadFiles;
	}

	public Map getOrderInformation() {
		Map orderInfo = new HashMap();
		Iterator it = response.getInfoIterator();
		while (it.hasNext()) {
			String temp = (String) it.next();
			if (!temp.equals("zip_file_count") && !temp.equals("MAC")
					&& !temp.equals("WIN") && !temp.startsWith("download_file")) {
				orderInfo.put(temp, Functions.stripString(Functions
						.stripString(response.getInfo(temp), "]"), "["));
			}
		}
		return orderInfo;
	}

	public String getMessage(ProductFamily fam) {
		return response.getMessage(fam);
	}

	public String getMessage() {
		if (response == null)
			return "Order Failed";
		return response.getMessage();
	}

	public String getMessage(Product p) {
		return response.getMessage(p);
	}

	public Map getProducts() {
		Map products = new HashMap(response.getProducts());
		Iterator it = products.keySet().iterator();
		Product temp;
		while (it.hasNext()) {
			temp = (Product) it.next();
			products.put(temp, getProductReport(temp));
		}
		return products;
	}

	public Map getFamilies() {
		Map families = new HashMap(response.getFamilies());
		Iterator it = families.keySet().iterator();
		ProductFamily temp;
		while (it.hasNext()) {
			temp = (ProductFamily) it.next();
			families.put(temp, getFamilyReport(temp));
		}
		return families;
	}

	public Map getFamilyReport(ProductFamily fam) {
		return response.getFamilyReportMap(fam);
	}

	public Map getProductReport(Product p) {
		return response.getProductReportMap(p);
	}

	public Map getInformation() {
		return response.getInformation();
	}

	/**
	 * @see java.util.Map#containsKey(Object)
	 */
	public boolean containsKey(Object key) {
		return getInformation().containsKey(key);
	}

	/**
	 * @see java.util.Map#containsValue(Object)
	 */
	public boolean containsValue(Object value) {
		return getInformation().containsValue(value);
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		return getInformation().entrySet();
	}

	/**
	 * @see java.util.Map#get(Object)
	 */
	public Object get(Object key) {
		return getInformation().get(key);
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return getInformation().isEmpty();
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		return getInformation().keySet();
	}

	/**
	 * @see java.util.Map#put(Object, Object)
	 */
	public Object put(Object arg0, Object arg1) {
		return null;
	}

	/**
	 * @see java.util.Map#putAll(Map)
	 */
	public void putAll(Map arg0) {
	}

	/**
	 * @see java.util.Map#remove(Object)
	 */
	public Object remove(Object arg0) {
		return null;
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size() {
		return getInformation().size();
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		return getInformation().values();
	}

	public String getString() {
		return response.toString();
	}
}
