package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.lazerinc.dbtools.QueryItem;
import com.lazerinc.ecommerce.ProductSet;

/*******************************************************************************
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @created January 2, 2002
 * @version 1.0
 ******************************************************************************/

public class ProductSetBean implements Serializable {
	private static final long serialVersionUID = 1;

	int pageSize = 1;

	int displaySize = 0;

	int displaySet = 0;

	int historyIndex = -1;

	Comparator productSorter;

	List<Set<QueryItem>> queryList = new ArrayList<Set<QueryItem>>();

	List<ProductSet> searchList = new ArrayList<ProductSet>();

	List<String> searchHistory = new ArrayList<String>();

	public void clear() {
		queryList.clear();
		searchList.clear();
		searchHistory.clear();
		historyIndex = -1;
	}

	public void clear(int index) {
		System.out.println("clearing product set bean from index: " + index
				+ " search list size = " + searchHistory.size());
		if (index >= 0 && index < queryList.size()) {
			queryList = queryList.subList(0, index + 1);
			searchList = searchList.subList(0, index + 1);
			searchHistory = searchHistory.subList(0, index + 1);
			System.out.println("search list size = " + searchHistory.size());
			historyIndex = index;
		}
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Set<QueryItem> getQuerySet(int index) {
		if (index > 0 && index < queryList.size())
			return queryList.get(index);
		else
			return Collections.EMPTY_SET;
	}

	public void setQuerySet(Set<QueryItem> set, int index) {
		if (index >= 0 && index < queryList.size())
			queryList.set(index, set);
		else if (index >= queryList.size())
			queryList.add(set);
	}

	public void setProductSet(ProductSet set, int index) {
		set.setProductSorter(productSorter);
		if (index >= searchList.size())
			searchList.add(set);
		else
			searchList.set(index, set);
	}

	public void setSearchHistory(String values, int index) {
		if (index >= getSearchHistory().size())
			getSearchHistory().add(values);
		else
			getSearchHistory().set(index, values);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setDisplaySize(int size) {
		this.displaySize = size;
	}

	public int getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySet(int setNum) {
		this.displaySet = setNum;
	}

	public String getSearchCategoryValue(String category) {
		if (queryList == null)
			return null;
		for (Set<QueryItem> querySet : queryList) {
			for (QueryItem item : querySet) {
				if (item != null && item.getCategory().equals(category)) {
					return item.getValue();
				}
			}
		}
		return null;
	}

	public int getDisplaySet() {
		return displaySet;
	}

	public int getNumberPages() {
		int size = size();
		int pageSize = getPageSize();
		return (int) (size / pageSize) + ((size % pageSize > 0) ? 1 : 0);
	}

	public List getDisplayProducts() {
		ProductSet products = getCurrentProductSet();
		List productList = products.getProductList();
		if (getDisplaySet() * getPageSize() <= productList.size()
				&& (getDisplaySet() + 1) * getPageSize() <= productList.size()) {
			productList = productList.subList(getDisplaySet() * getPageSize(),
					(getDisplaySet() + 1) * getPageSize());
		} else if (getDisplaySet() * getPageSize() > productList.size()) {
			productList.clear();
		} else {
			productList = productList.subList(getDisplaySet() * getPageSize(),
					productList.size());
		}
		return productList;
	}

	public String getProductFamily() {
		try {
			int index = getHistoryIndex();
			ProductSet set = getProductSet(index);
			while (set.totalSize() == 0) {
				index--;
				set = getProductSet(index);
			}
			return set.getProductFamilies().iterator().next();
		} catch (RuntimeException e) {
			return "";
		}
	}

	public int getDisplayProductsSize() {
		return getDisplayProducts().size();
	}

	public int getSize() {
		return size();
	}

	/***************************************************************************
	 * Gets the CurrentProductSet attribute of the ProductSetBean object
	 * 
	 * @return The CurrentProductSet value
	 **************************************************************************/
	public ProductSet getCurrentProductSet() {
		if (getHistoryIndex() >= 0
				&& getHistoryIndex() < getSearchList().size()) {
			return (ProductSet) getSearchList().get(getHistoryIndex());
		}
		return null;
	}

	/***************************************************************************
	 * Gets the CurrentQuerySet attribute of the ProductSetBean object
	 * 
	 * @return The CurrentQuerySet value
	 **************************************************************************/
	public Set getCurrentQuerySet() {
		if (getHistoryIndex() >= 0 && getHistoryIndex() < getQueryList().size()) {
			return (Set) getQueryList().get(getHistoryIndex());
		}
		return null;
	}

	/***************************************************************************
	 * Gets the HistoryIndex attribute of the ProductSetBean object
	 * 
	 * @return The HistoryIndex value
	 **************************************************************************/
	public int getHistoryIndex() {
		return historyIndex;
	}

	/***************************************************************************
	 * Sets the HistoryIndex attribute of the ProductSetBean object
	 * 
	 * @param index
	 *            The new HistoryIndex value
	 **************************************************************************/
	public void setHistoryIndex(int index) {
		historyIndex = index;
	}

	public ProductSet getFirstProductSet() {
		if (getHistoryIndex() >= 0 && getSearchList() != null
				&& getSearchList().size() > 0) {
			return (ProductSet) getSearchList().get(0);
		}
		return null;
	}

	public ProductSet getProductSet(int index) {
		if (index < getSearchList().size() && index >= 0)
			return getSearchList().get(index);
		else if (index < 0)
			return getSearchList().get(0);
		return null;
	}

	/***************************************************************************
	 * Adds a feature to the NewProductSet attribute of the ProductSetBean
	 * object
	 * 
	 * @param set
	 *            The feature to be added to the NewProductSet attribute
	 **************************************************************************/
	public void addNewProductSet(ProductSet set) {
		set.setProductSorter(productSorter);
		int index = getHistoryIndex();
		if (index + 1 >= getSearchList().size()) {
			getSearchList().add(set);
		} else {
			getSearchList().set(index + 1, set);
		}
	}

	public void addNewQuerySet(Set<QueryItem> set) {
		int index = getHistoryIndex();
		if (index + 1 >= getQueryList().size()) {
			getQueryList().add(set);
		} else {
			getQueryList().set(index + 1, set);
		}
	}

	public void addNewSearchHistory(String currentSearch) {
		int index = getHistoryIndex();
		if (index + 1 >= getSearchHistory().size()) {
			getSearchHistory().add(currentSearch);
		} else {
			getSearchHistory().set(index + 1, currentSearch);
		}
	}

	public int size() {
		if (getCurrentProductSet() == null)
			return 0;
		return getCurrentProductSet().totalSize();
	}

	public void incrHistoryIndex() {
		setHistoryIndex(getHistoryIndex() + 1);
	}

	public List<Set<QueryItem>> getQueryList() {
		return queryList;
	}

	private List<ProductSet> getSearchList() {
		return searchList;
	}

	public List<String> getSearchHistory() {
		return searchHistory;
	}

	public void setProductSorter(Comparator productSorter) {
		this.productSorter = productSorter;
	}

}
