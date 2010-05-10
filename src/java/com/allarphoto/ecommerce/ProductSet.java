// Title: LazerWeb
// Version:
// Copyright: Copyright (c) 1998
// Author: Michael Stover
// Company: Lazer Inc.
// Description: Your description

package com.allarphoto.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.Product;

/*******************************************************************************
 * This class keeps a listing of products by their product numbers or sku. One
 * or ther other - not both (or else the list will contain duplicates). It
 * tracks multiple lists of products - one for each product family.
 ******************************************************************************/
@CoinjemaObject
public class ProductSet implements Serializable {
	private static final long serialVersionUID = 1;

	HashMap<String, Set<Product>> productList;

	HashMap sortedLists;

	Comparator productSorter;

	DatabaseUtilities dbutil;

	public ProductSet() {
		productList = new HashMap<String, Set<Product>>();
		sortedLists = new HashMap();
	}

	public String toString() {
		StringBuffer page = new StringBuffer("");
		page.append("{");
		for (String pf : productList.keySet()) {
			page.append(pf + "=");
			for (Product p : productList.get(pf)) {
				page.append(", " + p);
			}
			page.append(System.getProperty("line.separator"));
		}
		page.append("}");
		return page.toString();
	}

	/***************************************************************************
	 * Add a product to the ProductSet
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param product
	 *            Product ID #.
	 *            ***********************************************************
	 *            public void add(String pf,int product) {
	 *            add(pf,Integer.toString(product)); }
	 */

	/***************************************************************************
	 * Removes a product from the ProductSet
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param product
	 *            Product ID #.
	 *            ***********************************************************
	 *            public void remove(String pf,int product) {
	 *            remove(pf,Integer.toString(product));
	 *            sortedLists.put(pf,null); }
	 */

	/***************************************************************************
	 * Adds a family to the ProductSet object.
	 * 
	 * @param pf
	 *            Product Family to be added.
	 **************************************************************************/
	public void add(String pf) {
		if (!productList.containsKey(pf)) {
			Set set = new HashSet();
			productList.put(pf, set);
		}
	}

	/***************************************************************************
	 * Removes a family from the ProductSet object.
	 * 
	 * @param pf
	 *            Product Family to be removed.
	 **************************************************************************/
	public void remove(String pf) {
		productList.remove(pf);
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Add a product to the ProductSet
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param product
	 *            Product sku.
	 **************************************************************************/
	public void add(String pf, Product product) {
		Set set;
		if (productList.containsKey(pf))
			set = (Set) productList.get(pf);
		else {
			set = new HashSet();
			productList.put(pf, set);
		}
		set.add(product);
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Removes a product from the ProductSet
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param product
	 *            Product sku.
	 **************************************************************************/
	public void remove(String pf, Product product) {
		Set set;
		if (productList.containsKey(pf)) {
			set = (Set) productList.get(pf);
			set.remove(product);
		}
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Adds an array of products to the ProductSet.
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param products
	 *            Array of ID's of product, or sku's.
	 **************************************************************************/
	public void addAll(String pf, Product[] products) {
		Set set;
		if (productList.containsKey(pf))
			set = (Set) productList.get(pf);
		else {
			set = new HashSet();
			productList.put(pf, set);
		}
		int count = -1;
		while (++count < products.length)
			set.add(products[count]);
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Adds another ProductSet object to this one.
	 * 
	 * @param pSet
	 *            ProductSet object to be added.
	 **************************************************************************/
	public void addAll(ProductSet pSet) {
		if (pSet == null)
			return;
		int count = -1;
		for (String fam : pSet.getProductFamilies())
			addAll(fam, pSet.getProductSet(fam));
	}

	/***************************************************************************
	 * Adds a Set of products to the ProductSet object.
	 * 
	 * @param pf
	 *            Product Family.
	 * @param set
	 *            Set of products to be added.
	 **************************************************************************/
	public void addAll(String pf, Collection set) {
		Set thisSet = (Set) productList.get(pf);
		Set newSet = new HashSet();
		if (thisSet != null)
			newSet.addAll(thisSet);
		newSet.addAll(set);
		productList.put(pf, newSet);
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Takes the AND of this ProductSet and the given array of products and
	 * family name.
	 * 
	 * @param pf
	 *            Product family.
	 * @param products
	 *            Array of products.
	 **************************************************************************/
	public void and(String pf, Product[] products) {
		Set set = (Set) productList.get(pf);
		Set newSet = new HashSet();
		if (set != null) {
			int count = -1;
			while (++count < products.length) {
				if (set.contains(products[count]))
					newSet.add(products[count]);
			}
			productList.put(pf, newSet);
		}
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Takes the AND of this ProductSet and the given set of products and family
	 * name.
	 * 
	 * @param pf
	 *            Product family.
	 * @param products
	 *            Set of products.
	 **************************************************************************/
	public void and(String pf, Set products) {
		Set set = (Set) productList.get(pf);
		Set newSet = new HashSet();
		if (set != null) {
			Iterator it = set.iterator();
			Object temp;
			while (it.hasNext()) {
				temp = it.next();
				if (products.contains(temp))
					newSet.add(temp);
			}
			productList.put(pf, newSet);
		}
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * ANDs together this product set with the given ProductSet.
	 * 
	 * @param s
	 *            ProductSet to be ANDed with this one.
	 **************************************************************************/
	public void and(ProductSet s) {
		Set set;
		Set hold = new HashSet();
		Collection<String> temp = new LinkedList<String>(s.getProductFamilies());
		for (String fam : temp) {
			hold.add(fam);
			set = s.getProductSet(fam);
			if (set != null && set.size() > 0)
				and(fam, set);
			else
				productList.remove(fam);
		}
		temp = new LinkedList<String>(getProductFamilies());
		for (String fam : temp) {
			if (!hold.contains(fam))
				productList.remove(fam);
			sortedLists.put(fam, null);
		}
	}

	/**
	 * ANDs or Xor's together this product set with the given ProductSet. The
	 * behavior is essentially: if this.and(s).size() == 0, then union the two
	 * sets. Otherwise it is an intersection.
	 * 
	 * @param s
	 */
	public void andXor(ProductSet s) {
		for (String pt : s.productList.keySet()) {
			if (s.productList.get(pt) == null
					|| s.productList.get(pt).size() == 0)
				continue;
			Set<Product> thisSet = productList.get(pt);
			if (thisSet != null && thisSet.size() > 0) {
				thisSet = new HashSet<Product>(thisSet);
				if (!thisSet.removeAll(s.productList.get(pt))) {
					addAll(pt, s.productList.get(pt));
				} else {
					productList.get(pt).removeAll(thisSet);
					sortedLists.put(pt, null);
				}
			} else {
				setProductSet(pt, s.productList.get(pt));
			}
		}
	}

	/***************************************************************************
	 * Performs an AND NOT opertaion on this product set with the given product
	 * set.
	 * 
	 * @param s
	 *            ProductSet to be AND NOTed with this one.
	 **************************************************************************/
	public void andNot(ProductSet s) {
		Set set1, set2;
		Object temp;
		Collection<String> tempList = new LinkedList<String>(s
				.getProductFamilies());
		for (String fam : tempList) {
			set1 = getProductSet(fam);
			set2 = s.getProductSet(fam);
			Iterator it = set1.iterator();
			while (it.hasNext()) {
				temp = it.next();
				if (set2.contains(temp))
					set1.remove(temp);
			}
			setProductSet(fam, set1);
			sortedLists.put(fam, null);
		}

	}

	/***************************************************************************
	 * Gets the set of products for a particular product family.
	 * 
	 * @param pf
	 *            Product Family.
	 * @return Set of Products within that product family.
	 **************************************************************************/
	public Set<Product> getProductSet(String pf) {
		SortedSet ret = (SortedSet) sortedLists.get(pf);
		if (ret == null) {
			if (productSorter != null)
				ret = new TreeSet(productSorter);
			else
				ret = new TreeSet(dbutil.getProductFamily(pf)
						.getProductSorter());
			Set tempS = (Set) productList.get(pf);
			if (tempS != null) {
				ret.addAll(tempS);
				sortedLists.put(pf, ret);
			}
		}
		return ret;
	}

	/***************************************************************************
	 * Sets the set of products for a particular product family.
	 * 
	 * @param pf
	 *            Product Family.
	 * @param set
	 *            Set object to set to.
	 * @return Set of Products within that product family.
	 **************************************************************************/
	public void setProductSet(String pf, Set set) {
		productList.put(pf, set);
		sortedLists.put(pf, null);
	}

	/***************************************************************************
	 * Add a product to the ProductSet
	 * 
	 * @param pf
	 *            Product Family name or identifier.
	 * @param sku
	 *            Product sku.
	 **************************************************************************/

	/***************************************************************************
	 * Get a list of Product Families in the ProductSet object.
	 * 
	 * @return Array list of product Families.
	 **************************************************************************/
	public Collection<String> getProductFamilies() {
		return productList.keySet();
	}

	/***************************************************************************
	 * Gets a list of the products for a particular Product Family.
	 * 
	 * @param pf
	 *            Product Family.
	 * @return Array list of productID's or skus (String[]).
	 **************************************************************************/
	public Product[] getProductList(String pf) {
		SortedSet ret = (SortedSet) sortedLists.get(pf);
		if (productList.get(pf) == null)
			return null;
		if (ret == null) {
			if (productSorter != null)
				ret = new TreeSet(productSorter);
			else
				ret = new TreeSet(dbutil.getProductFamily(pf)
						.getProductSorter());
			ret.addAll((Set) productList.get(pf));
			sortedLists.put(pf, ret);
		}
		return (Product[]) ret.toArray(new Product[0]);
	}

	public LinkedList<Product> getProductList() {
		LinkedList<Product> products = new LinkedList<Product>();
		for (String family : getProductFamilies()) {
			Product[] pArray = getProductList(family);
			for (int x = 0; x < pArray.length; x++) {
				products.add(pArray[x]);
			}
		}
		return products;
	}

	/***************************************************************************
	 * Get the number of products for a given product family.
	 * 
	 * @param pf
	 *            Product Family.
	 * @return Number of products in the family.
	 **************************************************************************/
	public int size(String pf) {
		int retVal;
		Set set = (Set) productList.get(pf);
		if (set == null)
			retVal = 0;
		else
			retVal = set.size();
		return retVal;
	}

	/***************************************************************************
	 * Get the total number of products in this ProductSet.
	 * 
	 * @return Number of products in the ProductSet.
	 **************************************************************************/
	public int totalSize() {
		int total = 0;
		for (String fam : getProductFamilies())
			total += size(fam);
		return total;
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	@CoinjemaDependency(alias = "dbutil")
	public void setDatabaseUtility(DatabaseUtilities db) {
		dbutil = db;
	}

	public void setProductSorter(Comparator productSorter) {
		this.productSorter = productSorter;
		if (productSorter != null) {
			sortedLists.clear();
		}
	}

}
