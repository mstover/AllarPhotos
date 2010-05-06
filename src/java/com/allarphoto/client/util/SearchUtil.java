package com.lazerinc.client.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.coinjema.collections.HashTree;
import org.coinjema.collections.SortedHashTree;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.lazerinc.application.Controller;
import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.category.ProductField;
import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.client.beans.SearchCategoryBean;
import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.dbtools.QueryItem;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.ecommerce.ProductSet;
import com.lazerinc.server.ProductService;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;
import com.lazerinc.utils.Rights;

/*******************************************************************************
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @created January 2, 2002
 * @version 1.0
 ******************************************************************************/

@CoinjemaObject
public class SearchUtil {

	private Logger log;

	private Controller controller;

	private DBConnect database;

	private DatabaseUtilities dbUtil;

	private ProductService productService;

	@CoinjemaDependency(alias = "log4j")
	public void setLogger(Logger l) {
		log = l;
	}

	@CoinjemaDependency(type = "appController", method = "appController")
	public void setController(Controller c) {
		controller = c;
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect db) {
		database = db;
	}

	@CoinjemaDependency(type = "productService", method = "productService")
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbutil) {
		this.dbUtil = dbutil;
	}

	/** = "number_of_products_shown". */
	public static String MAX_PRODUCTS_SHOWN = "number_of_products_shown";

	/***************************************************************************
	 * Constructor for the SearchUtil object
	 **************************************************************************/
	public SearchUtil() {
	}

	/***************************************************************************
	 * Method to deal with the top search categories action. This method is used
	 * to find the next level of categories to search. All responses are placed
	 * into the given session object. This method is dependent on other methods
	 * reducing the ProductSet that this method will use.
	 * 
	 * @param controller
	 *            Description of Parameter
	 * @param products
	 *            Description of Parameter
	 * @return sorted Map of all currently searchable fields and their values.
	 *         It is organized as follows: <BR>
	 *         <B>ProductField</B> , values (String[])array.
	 **************************************************************************/
	public HashTree getSearchableCategories(ProductSet products,
			SecurityModel security) {
		SortedHashTree tempTree;
		SortedHashTree topSearchCategories = new SortedHashTree();
		String rootName = controller.getConfigValue("search_root_name");
		topSearchCategories.add(rootName);
		for (String pts : products.getProductFamilies()) {
			tempTree = (SortedHashTree) topSearchCategories.getTree(rootName);
			createHashTree(tempTree, dbUtil.getProductFamily(pts), products,
					security);
			/*
			 * tempTree.add(pts[x]); if(pts.length == 1) { tempTree =
			 * tempTree.get(ptsts[x]); createHashTree(tempTree,pts[x],products); }
			 */
		}
		return topSearchCategories;
	}

	/***************************************************************************
	 * Returns the current list of Product in the current product set, provided
	 * the number of products is less than the stated maximum in the config file
	 * for the site.
	 * 
	 * @param controller
	 *            Description of Parameter
	 * @param set
	 *            Description of Parameter
	 * @param permissions
	 *            Description of Parameter
	 * @return Array of current Product objects in current search set.
	 **************************************************************************/
	public Product[] getProducts(ProductSet set, SecurityModel permissions) {
		Object[] pIDs;
		Product[] products = new Product[set.totalSize()];
		int productCount = 0;
		for (String fam : set.getProductFamilies()) {
			ProductFamily family = dbUtil.getProductFamily(fam);
			pIDs = set.getProductList(fam);
			for (int count = 0; count < pIDs.length; count++) {
				products[productCount++] = family.getProduct(pIDs[count],
						permissions);
			}
		}
		Arrays.sort(products);
		return products;
	}

	public Product getProduct(String pFamily, String productId,
			SecurityModel permissions) {
		try {
			return dbUtil.getProductFamily(pFamily).getProduct(productId,
					permissions);
		} catch (NullPointerException e) {
			log.warn("Bad family name " + pFamily, e);
			throw e;
		}
	}

	/***************************************************************************
	 * Creates a new SearchCriteria object assuming that the "key" argument
	 * holds information about the category name, the comparison type to be used
	 * in the search, and the search type to be used in the search. The info
	 * should be organized as: <BR>
	 * <category_name>| <compareType>| <searchType>| <[Internal:]and|or>|
	 * <[External:]and|or|andnot>.
	 * 
	 * @param key
	 *            String containing information about the search criteria.
	 * @param vals
	 *            Array of values.
	 * @return new SearchCriteria object.
	 **************************************************************************/
	public QueryItem getQueryItem(String key, String[] vals) {
		QueryItem search = new QueryItem();
		String[] info = Functions.split(key, "|");
		if (info.length >= 1) {
			search.setCategory(info[0]);
			search.setValues(vals);
		}
		if (info.length >= 3) {
			search.setCategory(info[0]);
			search.setCompareType(Integer.parseInt(info[1]));
			search.setSearchType(Integer.parseInt(info[2]));
		}
		if (info.length >= 4) {
			if (info[3].indexOf("and") > -1) {
				search.setAnd(true);
			} else {
				search.setAnd(false);
			}
		}
		if (info.length == 5) {
			if (info[4].indexOf("and") > -1) {
				search.setExternalLogic(QueryItem.AND);
			} else if (info[4].indexOf("or") > -1) {
				search.setExternalLogic(QueryItem.OR);
			} else {
				search.setExternalLogic(QueryItem.ANDNOT);
			}
		}
		return search;
	}

	/***************************************************************************
	 * Method to deal with a simple, library-wide "or" keyword search.
	 * 
	 * @param productBean
	 *            Description of Parameter
	 * @param searchString
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	public ProductSet simpleOrSearch(ProductSetBean productBean,
			String searchString, SecurityModel security) {
		String[] searches = searchString.split("\\s");
		QueryItem query = getQueryItem("NULL|" + DBConnect.EQ + "|"
				+ DBConnect.IS + "|or|and", searches);
		Set<QueryItem> querySet = new HashSet<QueryItem>();
		querySet.add(query);
		ProductSet basis = productBean.getCurrentProductSet();
		ProductSet newSet = new ProductSet();
		for (int x = 0; x < searches.length; x++) {
			newSet
					.addAll((findProducts(basis, null, searches[x],
							DBConnect.LIKE, DBConnect.CONTAINSCASEINSENSITIVE,
							security)));
		}
		productBean.addNewSearchHistory(Functions.unsplit(searches, " OR "));
		productBean.addNewQuerySet(querySet);
		productBean.addNewProductSet(newSet);
		productBean.incrHistoryIndex();
		return newSet;
	}

	/***************************************************************************
	 * Method to deal with a simple, library-wide keyword search.
	 * 
	 * @param productBean
	 *            Description of Parameter
	 * @param searchString
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	public ProductSet simpleSearch(ProductSetBean productBean,
			String searchString, SecurityModel security) {
		String[] searches = null;
		boolean exact = isExact(searchString);
		if(!exact)
		{
			searches = searchString.split("\\s");
		}
		else searches = new String[]{searchString};
		QueryItem query = getQueryItem("NULL|" + DBConnect.EQ + "|"
				+ DBConnect.IS + "|and|and", searches);
		Set<QueryItem> querySet = new HashSet<QueryItem>();
		querySet.add(query);
		ProductSet basis = productBean.getCurrentProductSet();
		ProductSet newSet = new ProductSet();
		newSet.addAll(basis);
		for (int x = 0; x < searches.length; x++) {
			newSet = (findProducts(newSet, null, searches[x], DBConnect.LIKE,
					DBConnect.CONTAINSCASEINSENSITIVE, security));
		}
		productBean.addNewQuerySet(querySet);
		productBean.addNewSearchHistory(Functions.unsplit(searches, " AND "));
		productBean.addNewProductSet(newSet);
		productBean.incrHistoryIndex();
		return newSet;
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param tree
	 *            Description of Parameter
	 * @param fam
	 *            Description of Parameter
	 * @param products
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 **************************************************************************/
	protected void createHashTree(SortedHashTree tree, ProductFamily fam,
			ProductSet products, SecurityModel security) {
		List list;
		boolean multipleFamilies = false;
		if (products.getProductFamilies().size() > 1) {
			multipleFamilies = true;
		}
		int searchLevel = 1;
		ProductField[] categories = fam.getSearchFields();
		for (int count = categories.length - 1; count >= 0
				&& categories[count].getSearchOrder() >= searchLevel; count--) {
			Set values = getAvailableValues(products, categories[count]
					.getName(), security);
			values.remove(DatabaseUtilities.DEFAULT);
			values.remove("Not Applicable");
			if (values.size() > 1) {
				searchLevel = categories[count].getSearchOrder();
			}
			if (values.size() > 1) {
				Iterator it = values.iterator();
				while (it.hasNext()) {
					String temp = (String) it.next();
					log.debug("stepping through values for "
							+ categories[count].getName() + ", val: " + temp);
					if (!multipleFamilies
							&& checkIfCommon(products, categories[count], temp,
									security)) {
						log.debug("adding common element: " + temp
								+ ", for node: " + categories[count].getName());
						tree.add("Common", categories[count].getName());
						((SortedHashTree) tree.getTree("Common")).add(
								categories[count].getName(), temp);
					} else {
						log.debug("adding value: " + temp + ", to "
								+ categories[count].getName());
						tree.add(categories[count].getName(), temp);
					}
				}
			} else if (values.size() == 1
					&& !values.contains(DatabaseUtilities.DEFAULT)) {
				list = new LinkedList();
				list.add("Common");
				list.add(categories[count].getName());
				tree.add(list, values);
			}
			log.debug("are values still in order? cat: "
					+ categories[count].getName() + ", val: "
					+ tree.list(categories[count].getName()));
		}
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param set
	 *            Description of Parameter
	 * @param fieldName
	 *            Description of Parameter
	 * @param searchValue
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	protected boolean checkIfCommon(ProductSet set, ProductField fieldName,
			String searchValue, SecurityModel security) {
		boolean retVal = true;
		if (set == null) {
			set = new ProductSet();
		}
		for (String fam : set.getProductFamilies()) {
			retVal = retVal
					&& productService.checkIfCommon(set.getProductSet(fam),
							fam, fieldName, searchValue, security);
			if (!retVal) {
				break;
			}
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets all available values for a given category (as an array of strings),
	 * using the current product set. If searchField and searchValue are null,
	 * it uses the current product set. If not null, it uses them to create a
	 * narrower, temporary product set.
	 * 
	 * @param searchField
	 *            Name of field on which to search to narrow field of products.
	 * @param searchValue
	 *            Value to search for in searchField to narrow field of
	 *            products.
	 * @param fieldName
	 *            Name of field to get available values for.
	 * @param set
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Array of strings of the values.
	 **************************************************************************/
	public Set getAvailableValues(ProductSet set, String searchField,
			String searchValue, String fieldName, SecurityModel security) {
		if (searchField != null && searchValue != null) {
			set = findProducts(set, searchField, searchValue, DBConnect.EQ,
					DBConnect.IS, security);
		} else if (fieldName == null) {
			return dbUtil.getKeywords(set.getProductFamilies());
		}
		return getAvailableValues(set, fieldName, security);
	}

	public void getInitialSet(String pt, ProductSetBean productBean,
			SecurityModel security) {
		ProductSet set = new ProductSet();
		set.addAll(productBean.getCurrentProductSet());
		if (security.getPermission(pt, Rights.DATATABLE, Right.READ)) {
			ProductFamily productFamily = dbUtil.getProductFamily(pt);
			set.add(pt);
			set.addAll(pt, productFamily.getProducts(security));
			SortedMap<ProductField, List<String>> fields = new TreeMap<ProductField, List<String>>(
					new SearchOrderSort());
			for (Resource res : security.getAvailableResourceList(
					Resource.PROTECTED_FIELD, Right.READ)) {
				if (res.getName().startsWith(pt)) {
					String[] catVal = res.getName().split("\\.");
					ProductField f = productFamily.getField(catVal[1]);
					List<String> values = fields.get(f);
					if (values == null) {
						values = new LinkedList<String>();
						fields.put(f, values);
					}
					values.add(catVal[2]);
				}
			}// have to do the fields in search order, else the results are
			// not correct
			for (ProductField f : fields.keySet()) {
				log.debug("getting products for field " + f.getName());
				ProductSet tempSet = new ProductSet();
				tempSet.add(pt);
				for (String value : fields.get(f)) {
					tempSet.addAll(productFamily.findProductsWithCategory(f							
							.getName(), value, DBConnect.EQ, DBConnect.IS,
							security));
					log.debug("Product list size for value(" + value + ") = "
							+ tempSet.size(pt));
				}
				set.and(pt,tempSet.getProductSet(pt));
				log.debug("Product list current size = "
						+ set.size(pt));
			}
		}
		Set queryList = new HashSet();
		productBean.setHistoryIndex(0);
		productBean.setProductSet(set, 0);
		productBean.setQuerySet(queryList, 0);
		productBean.setSearchHistory("", 0);
	}

	/***************************************************************************
	 * Method to deal with the drill search action. All responses are placed
	 * into the given session object.
	 * 
	 * @param productBean
	 *            Description of Parameter
	 * @param catValues
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	public ProductSet search(ProductSetBean productBean, Set catValues,
			SecurityModel security, boolean toggle) {
		if (catValues.size() == 0)
			return productBean.getCurrentProductSet();
		int historyIndex = combineSearchMaps(productBean, catValues, toggle);
		recreateSearchHistory(productBean, historyIndex);
		ProductSet set = new ProductSet();
		ProductSet hold = productBean.getProductSet(historyIndex - 1);
		set = new ProductSet();
		set.addAll(hold);
		for (int i = historyIndex; i <= productBean.getHistoryIndex(); i++) {
			log
					.debug("query set = " + productBean.getQueryList()
							+ " i = " + i);
			boolean first = true;
			log.warn("Searching with criteria : " + productBean.getQuerySet(i));
			for (QueryItem criteria : productBean.getQuerySet(i)) {
				log.debug("base set size = " + set.totalSize()
						+ " families in set = " + set.getProductFamilies());
				ProductSet temp = processCriteriaSearch(criteria, set, hold,
						security);
				if (first) {
					set = temp;
					first = false;
				} else {
					mergeProductSets(criteria, set, temp);
				}
				log.debug("After search, set size = " + set.totalSize());
			}
			productBean.setProductSet(set, i);
			set = new ProductSet();
			set.addAll(productBean.getProductSet(i));
			hold = productBean.getProductSet(i);
		}
		return productBean.getCurrentProductSet();
	}

	private void recreateSearchHistory(ProductSetBean productBean,
			int historyIndex) {
		for (int i = historyIndex; i <= productBean.getHistoryIndex(); i++) {
			StringBuffer historyValues = new StringBuffer();
			for (QueryItem item : productBean.getQuerySet(i)) {
				if (historyValues.length() == 0)
					historyValues.append(item.getValue());
				else
					historyValues.append(", ").append(item.getValue());
			}
			productBean.setSearchHistory(historyValues.toString(), i);
		}
	}

	private Set getAvailableValues(ProductSet prList, String fieldName,
			SecurityModel security) {
		ProductField f;
		SortedSet retVal = new TreeSet();
		for (String pt : prList.getProductFamilies()) {
			ProductFamily family = dbUtil.getProductFamily(pt);
			f = family.getField(fieldName);
			if (f != null) {
				switch (Math.abs(f.getType())) {
				case ProductField.TAG:
					;
				case ProductField.CATEGORY: {
					retVal.addAll(family.getAvailableCategoryValues(prList
							.getProductSet(pt), fieldName, security));
					break;
				}
				case ProductField.PRIMARY: {
					retVal.addAll(family.getAvailablePrimaryValues(prList
							.getProductSet(pt), security));
					break;
				}
				}
			}
		}
		log.debug("catValues category: " + fieldName + " values: " + retVal);
		return retVal;
	}

	/***************************************************************************
	 * This updates the current category tree after a search and updates the
	 * list of common categories.
	 * 
	 * @param searchSet
	 *            A ProductSet object containing the current search results.
	 * @param categories
	 *            The SearchCategoryBean that gets updated here.
	 * @param controller
	 *            the DatabaseApplicationController
	 * @param security
	 *            the SecurityModel
	 **************************************************************************/

	public void refreshCategories(ProductSet searchSet,
			SearchCategoryBean categories, SecurityModel security) {
		HashTree catTree = getSearchableCategories(searchSet, security);
		log.debug("searchable categories: " + catTree.list());
		String root = (String) catTree.getObject();
		log.debug("root " + root);
		categories.setRootName(root);
		categories.setCommonCategories(catTree.getTree(root).getTree("Common"));
		log.debug("common categories: " + categories.getCommonCategories());
		catTree.getTree(root).remove("Common");
		categories.setSearchCategories(catTree.getTree(root));
		log.debug("search categories "
				+ categories.getSearchCategories().list());
	}

	/***************************************************************************
	 * This method combines an old search map with a new one, determining what
	 * new searches need to be made, whether the search needs to be reset.
	 * 
	 * @param oldSearch
	 *            A Map object summarizing the old search.
	 * @param newSearch
	 *            Description of Parameter
	 * @return The index of the search that is being effected.
	 **************************************************************************/
	private int combineSearchMaps(ProductSetBean productSet,
			Set<QueryItem> newSearch, boolean toggle) {
		int historyIndex = toggle ? insertToggledSearchValues(productSet,
				newSearch) : -1;
		if (newSearch.size() > 0) {
			productSet.addNewQuerySet(newSearch);
			productSet.incrHistoryIndex();
		}
		if (historyIndex == -1)
			historyIndex = productSet.getHistoryIndex();
		return historyIndex;
	}

	private int insertToggledSearchValues(ProductSetBean productSet,
			Set<QueryItem> newSearch) {
		int index = 0;
		boolean fixIndex = false;
		for (Set<QueryItem> criteria : productSet.getQueryList()) {
			boolean match = criteria.size() > 0;
			for (QueryItem item : criteria) {
				if (item != null && containsCategory(newSearch, item)) {
					fixIndex = true;
				}
			}
			if (!fixIndex)
				index++;
		}
		return index;
	}

	private boolean containsCategory(Set<QueryItem> set, QueryItem baseItem) {
		boolean contains = false;
		Set<QueryItem> tempSet = new HashSet<QueryItem>(set);
		for (QueryItem item : tempSet) {
			if (baseItem.getCategory().equals(item.getCategory())) {
				set.remove(item);
				baseItem.setValues(item.getValues());
				baseItem.setAnd(item.isAnd());
				baseItem.setCompareType(item.getCompareType());
				baseItem.setExternalLogic(item.getExternalLogic());
				baseItem.setSearchType(item.getSearchType());
				contains = true;
			}
		}
		return contains;
	}

	private ProductSet processCriteriaSearch(QueryItem criteria,
			ProductSet baseAndSet, ProductSet baseOrSet, SecurityModel security) {
		if (criteria.getCategory().equals("Data-Family")) {
			ProductSet tempProductSet = new ProductSet();
			String library = (String) criteria.getValue();
			tempProductSet.addAll(library, baseAndSet.getProductSet(library));
			return tempProductSet;
		}
		ProductSet resultSet = new ProductSet();
		if (criteria.getValue() == null || criteria.getValue().length() == 0) {
			resultSet.addAll(baseAndSet);
			return resultSet;
		}
		int count = 0;
		Iterator catVals = criteria.getValues().iterator();
		while (catVals.hasNext()) {
			String value = (String) catVals.next();
			if (!criteria.isAnd() || count == 0) {
				if (criteria.getExternalLogic() == QueryItem.AND) {
					resultSet.addAll(findProducts(baseAndSet, criteria
							.getCategory(), value, criteria.getCompareType(),
							criteria.getSearchType(), security));
				} else {
					resultSet.addAll(findProducts(baseOrSet, criteria
							.getCategory(), value, criteria.getCompareType(),
							criteria.getSearchType(), security));
				}
			} else {
				if (criteria.getExternalLogic() == QueryItem.AND) {
					resultSet.and(findProducts(baseAndSet, criteria
							.getCategory(), value, criteria.getCompareType(),
							criteria.getSearchType(), security));
				} else {
					resultSet.and(findProducts(resultSet, criteria
							.getCategory(), value, criteria.getCompareType(),
							criteria.getSearchType(), security));
				}
			}
			count++;
		}
		return resultSet;
	}

	private void mergeProductSets(QueryItem criteria, ProductSet set,
			ProductSet newSet) {
		switch (criteria.getExternalLogic()) {
		case QueryItem.OR:
			set.addAll(newSet);
			break;
		case QueryItem.ANDNOT:
			set.andNot(newSet);
			break;
		case QueryItem.AND:
			set.and(newSet);
			break;
		}
	}

	private ProductSet findProducts(ProductSet set, String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {
		ProductSet retVal = new ProductSet();
		if (set == null) {
			set = new ProductSet();
		}
		Collection<String> pfs = set.getProductFamilies();
		if (pfs.size() == 0) {
			pfs = new LinkedList<String>();
			for (ProductFamily fam : dbUtil.getProductFamilies())
				pfs.add(fam.getTableName());
		}
		for (String fam : pfs) {
			ProductSet products = findProducts(fam, fieldName, searchTerm,
					compareType, searchType, security);
			log.debug("Number products found = " + products.totalSize());
			if (retVal.size(fam) == 0) {
				log.debug("Adding all");
				retVal.addAll(products);
			} else {
				log.debug("Anding all");
				retVal.and(products);
			}
			log.debug("Number products remaining = " + retVal.totalSize());
			retVal.and(fam, set.getProductSet(fam));
		}
		return retVal;
	}
	
	protected boolean isExact(String x)
	{
		if(x.startsWith("\"") && x.endsWith("\"")) return true;
		else return false;
	}

	private ProductSet findProducts(String pt, String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {
		ProductFamily family = dbUtil.getProductFamily(pt);
		ProductSet retVal = new ProductSet();
		log.debug("Field is " + fieldName + " table = " + pt + "(before)");
		if (fieldName == null) {
			retVal.addAll(family.findProductsWithDescription(fieldName,
					searchTerm, compareType, searchType, security));
			return retVal;
		}
		log.debug("Field is " + fieldName + " table = " + pt);
		ProductField f = family.getField(fieldName);
		if (f != null) {
			log.debug("Field = " + f.getName() + "(" + f.getType() + ")");
			switch (Math.abs(f.getType())) {
			case ProductField.TAG:
				;
			case ProductField.CATEGORY: {
				retVal = family.findProductsWithCategory(fieldName, searchTerm,
						compareType, searchType, security);
				break;
			}
			case ProductField.DESCRIPTION: {
				retVal = family.findProductsWithDescription(fieldName,
						searchTerm, compareType, searchType, security);
				break;
			}
			case ProductField.PRIMARY: {
				retVal = family.findProductsWithPrimary(searchTerm,
						compareType, searchType, security);
				break;
			}
			case ProductField.NUMERICAL: {
				retVal = family.findProductsWithNumerical(fieldName, new Float(
						searchTerm), compareType, searchType, security);
				break;
			}
			case ProductField.DATE_FIELD: {
				retVal = family.findProductsWithDate(fieldName, searchTerm,
						compareType, searchType, security);
				break;
			}
			case ProductField.EXPIRED_TYPE:
				if (searchTerm.toUpperCase().indexOf("UNEXPIRED") > -1)
					retVal = family.getUnexpiredImages(security);
				else
					retVal = family.getExpiredImages(security);
				break;
			}
		}
		return retVal;
	}
}
