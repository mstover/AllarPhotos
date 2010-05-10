// Title: ProductFamily
// Version:
// Copyright: Copyright (c) 1998
// Author: Michael Stover
// Company: Lazer Inc.
// Description: ProductFamily

package com.allarphoto.ecommerce;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Functor;

import strategiclibrary.service.cache.Cache;
import strategiclibrary.service.cache.CacheRegistration;
import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Files;
import strategiclibrary.util.TimeConstants;

import com.allarphoto.ajaxclient.client.beans.OrderVerificationPackage;
import com.allarphoto.application.ExpirationTester;
import com.allarphoto.application.NullSecurity;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.application.impl.DefaultExpirationTester;
import com.allarphoto.application.impl.DefaultProductComparator;
import com.allarphoto.beans.LogItem;
import com.allarphoto.beans.Path;
import com.allarphoto.cached.DatabaseObject;
import com.allarphoto.cached.functions.ProductAdd;
import com.allarphoto.cached.functions.ProductFieldAdd;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.server.ProductService;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

@CoinjemaObject
public class ProductFamily implements Serializable, Comparator, DatabaseObject {
	private static final long serialVersionUID = 1;

	private static ExpirationTester defaultTester = new DefaultExpirationTester();

	private static ProductFieldAdd fieldAdder = new ProductFieldAdd();

	private static ProductAdd productAdder = new ProductAdd();

	Logger log;

	protected DatabaseUtilities dbUtil;

	boolean dirty = false;

	boolean remoteManaged = false;

	private String assetPath;

	public ProductFamily() {
	}

	public ProductFamily(String n) {
		tableName = n;
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setId(int newId) {
		id = newId;
	}

	public int getId() {
		return id;
	}

	public void refresh() {
		getCache().clear();
		getFieldCache().clear();
	}

	public void setTableName(String newTableName) {
		tableName = newTableName;
	}

	public void createField(ProductField field, SecurityModel perms) {
		if (!perms.getPermission(getTableName(), Resource.DATATABLE,
				Right.ADMIN))
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
		else
			getFieldCache().addItem(field);
	}

	public void updateField(ProductField f, SecurityModel perms) {
		if (!perms.getPermission(getTableName(), Resource.DATATABLE,
				Right.ADMIN))
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
		else {
			log.info("updating field " + f.getName());
			getFieldCache().updateItem(f);
		}
	}
	
	public void deleteField(String fieldName)
	{
		ProductField f = getField(fieldName);
		getFieldCache().deleteObject(f);
	}

	public String getContextName() {
		return getCustomerName() + "/" + getTableName();
	}

	@CoinjemaDynamic(alias = "uploadProperties", contextMethod = "getContextName")
	public String[][] getRequiredAndOptionalUploadProperties() {
		return new String[0][0];
	}

	@CoinjemaDynamic(type = "productBeanClass", contextMethod = "getContextName")
	public Class<ProductBean> getProductBeanClass() {
		return ProductBean.class;
	}

	public ProductBean getProductBean(Product p) {
		try {
			ProductBean pb = getProductBeanClass().newInstance();
			pb.setProduct(p);
			return pb;
		} catch (Exception e) {
			log.fatal("Failed to create product bean for family "
					+ getTableName(), e);
			return null;
		}
	}

	public long filesize() {
		String path = getProducts(dbUtil.getAdmin()).iterator().next()
				.getPathName();
		if (path.indexOf("/") > -1)
			path = path.substring(0, path.indexOf("/"));
		return Files.getSize(new File(assetPath, path));
	}

	public String getTableName() {
		return tableName;
	}

	public void clearFieldCache() {
		getFieldCache().clear();
	}

	public void setDescriptiveName(String newDescriptiveName) {
		descriptiveName = newDescriptiveName;
	}

	public String getDescriptiveName() {
		return descriptiveName;
	}

	public Merchant getMerchant() {
		return dbUtil.getMerchant(this);
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setProductType(int newProductType) {
		productType = newProductType;
	}

	public int getProductType() {
		return productType;
	}

	public void setPriceKeys(String[] newPriceKeys) {
		priceKeys = newPriceKeys;
	}

	public String[] getPriceKeys() {
		return priceKeys;
	}

	public List<ProductField> getFields() {
		return new ArrayList(getFieldCache().getCachedList());
	}

	public ProductField getField(String name) {
		if (name.equals("expired_category")) {
			return dbUtil.expiredImage;
		} else if (name.equals("Date Posted"))
			return dbUtil.dateField;
		else if (name.equals("Date Modified"))
			return dbUtil.modDateField;
		else
			return getFieldCache().getCachedObject("name", name);
	}

	private Cache<ProductField> getFieldCache() {
		initFieldRegistration();
		return familyCache.getCache(getFieldCacheName(), ProductField.class);
	}

	private synchronized void initFieldRegistration() {
		if (!familyCache.isRegistered(getFieldCacheName())) {
			familyCache.registerCache(createFieldRegistration());
		}
	}

	private synchronized void initRegistration() {
		if (!familyCache.isRegistered(getTableName())) {
			familyCache.registerCache(createRegistration());
		}
	}

	@CoinjemaDynamic(type = "productService")
	private ProductService getProductService() {
		return null;
	}

	private CacheRegistration createFieldRegistration() {
		CacheRegistration fieldReg = new CacheRegistration();
		fieldReg.setCacheName(getFieldCacheName());
		fieldReg.setObjectType(ProductField.class);
		fieldReg.setRetrievalSql("getField.sql");
		Map defValues = new HashMap();
		defValues.put("family", getTableName());
		fieldReg.setDefaultSearchValues(defValues);
		fieldReg.setInsertSql("addField.sql");
		fieldReg.setUpdateSql("updateField.sql");
		fieldReg.setDeleteSql("deleteProductField.sql");
		fieldReg.setAddFunctor(new Functor(fieldAdder, "add"));
		fieldReg.setUpdateFunctor(new Functor(fieldAdder, "update"));
		fieldReg.setDeleteFunctor(new Functor(fieldAdder, "delete"));
		Map<String, Functor> catFuncs = new HashMap<String, Functor>();
		catFuncs.put("name", new Functor("getName"));
		catFuncs.put("id", new Functor("getFieldID"));
		fieldReg.setCategoryFunctors(catFuncs);
		fieldReg.setPrimaryPath(new Object[] { "id", null });
		fieldReg.setPrimaryFunctors(Arrays.asList(new Functor[] { new Functor(
				"getFieldID") }));
		fieldReg.setCacheTime(TimeConstants.HOUR * 6);
		return fieldReg;
	}

	private CacheRegistration createRegistration() {
		CacheRegistration fieldReg = new CacheRegistration();
		fieldReg.setCacheName(getTableName());
		fieldReg.setObjectType(CommerceProduct.class);
		fieldReg.setRetrievalSql("findAllProducts.sql");
		fieldReg.setDeleteSql("deleteProduct.sql");
		Map defValues = new HashMap();
		defValues.put("table", getTableName());
		defValues.put("family", getTableName());
		fieldReg.setDefaultSearchValues(defValues);
		fieldReg.setAddFunctor(new Functor(productAdder, "add"));
		fieldReg.setUpdateFunctor(new Functor(productAdder, "update"));
		Map<Object, Functor> catFuncs = new HashMap<Object, Functor>();
		catFuncs.put("Primary", new Functor("getPrimary"));
		catFuncs.put("path", new Functor("getPath"));
		catFuncs.put("id", new Functor("getId"));
		catFuncs.put("name", new Functor("getName"));
		catFuncs.put(new String[] { "path", "name" }, null);
		fieldReg.setCategoryFunctors(catFuncs);
		fieldReg.setPrimaryPath(new Object[] { "id", null });
		fieldReg.setPrimaryFunctors(Arrays.asList(new Functor[] { new Functor(
				"getId") }));
		fieldReg.setAdHocRetrieval(new Functor(getProductService(),
				"findProducts"));
		fieldReg.setProcessFunctors(Arrays.asList(new Functor[] { new Functor(
				"setProductFamily")
				.preChain(new Functor("getProductFamilyName").postChain(dbUtil,
						"getProductFamily")) }));
		fieldReg.setCacheTime(TimeConstants.HOUR * 6);
		return fieldReg;
	}

	public boolean isRemoteManaged() {
		return remoteManaged;
	}

	public void setRemoteManaged(boolean rm) {
		remoteManaged = rm;
	}

	public boolean exists(Path p) {
		return getCache().getCachedObjects("path", p.getName()).size() > 0;
	}

	public String getPathRoot() {
		Product p = getCache().getCachedList().iterator().next();
		return p.getPath().getRoot();
	}

	private String fieldCacheName = null;

	private synchronized String getFieldCacheName() {
		if (fieldCacheName == null)
			fieldCacheName = getTableName() + "fields";
		return fieldCacheName;
	}

	/***************************************************************************
	 * Get the names of all fields that are "descriptions".
	 * 
	 * @return Array of descriptions.
	 **************************************************************************/
	public String[] getDescriptionFields() {
		if (descriptionFields == null) {
			int count = -1;
			ArrayList list = new ArrayList();
			for (ProductField field : getFields())
				if (field.getType() == ProductField.DESCRIPTION)
					list.add(field.getName());
			descriptionFields = (String[]) list.toArray(descriptionFields);
		}
		return descriptionFields;
	}

	/***************************************************************************
	 * Returns all the searchable fields sorted by searchorder.
	 * 
	 * @return Array of ProductField objects.
	 **************************************************************************/
	public ProductField[] getSearchFields() {
		boolean notHit = true;
		ArrayList list = new ArrayList();
		for (ProductField field : getFields())
			if (field.getSearchOrder() > 0)
				list.add(field);
		Collections.sort(list, this);
		return (ProductField[]) list.toArray(new ProductField[0]);
	}

	/***************************************************************************
	 * Method to satisfy Comparator interface requirement.
	 * 
	 * @param o1
	 *            First object to be compared.
	 * @param o2
	 *            Second object to be compared.
	 * @return returns -1 if o1<o2, 0 if o1=o2, and 1 if o1>o2.
	 **************************************************************************/
	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof ProductField))
			return -1;
		else if (!(o2 instanceof ProductField))
			return 1;
		int v1 = ((ProductField) o1).getSearchOrder();
		int v2 = ((ProductField) o2).getSearchOrder();
		if (v1 < v2)
			return -1;
		else if (v1 == v2) {
			String s1 = ((ProductField) o1).getName();
			String s2 = ((ProductField) o2).getName();
			return s1.compareTo(s2);
		} else
			return 1;
	}

	public void setPrimaryLabel(String newPrimaryLabel) {
		primaryLabel = newPrimaryLabel;
	}

	public String getPrimaryLabel() {
		return primaryLabel;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProductFamily other = (ProductFamily) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	public int getCurrentSearchIndex() {
		return currentSearchIndex;
	}

	public void setOrderModelClass(String newOrderModelClass) {
		orderModelClass = newOrderModelClass;
	}

	public String getOrderModelClass() {
		return orderModelClass;
	}

	private int id;

	private String tableName;

	private String customerName;

	private String descriptiveName;

	private String description;

	private String[] priceKeys;

	private String[] descriptionFields = null;

	private int productType;

	private String primaryLabel;

	private int currentSearchIndex;

	private String orderModelClass;

	private CacheService familyCache;

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

	@CoinjemaDependency(type = "cacheService")
	public void setFamilyCache(CacheService familyCache) {
		this.familyCache = familyCache;
	}

	/***************************************************************************
	 * Adds a product to the this.database.
	 * 
	 * @param p
	 *            Product object.
	 **************************************************************************/
	public void addProduct(Product p) {
		getCache().addItem((CommerceProduct) p);
	}

	@CoinjemaDynamic(type = "productExpirationTester", contextMethod = "getContextName")
	public ExpirationTester getProductExpirationTester() {
		return null;
	}

	@CoinjemaDynamic(type = "productSorter", contextMethod = "getContextName")
	public Comparator getProductComparator() {
		return new DefaultProductComparator();
	}

	private Comparator sorter;

	public synchronized Comparator getProductSorter() {
		if (sorter == null)
			sorter = getProductComparator();
		return sorter;
	}

	@CoinjemaDynamic(type = "downloadVerificationPackage", contextMethod = "getContextName")
	public OrderVerificationPackage getFamilyDownloadVerificationPackage() {
		return null;
	}

	@CoinjemaDynamic(type = "orderVerificationPackage", contextMethod = "getContextName")
	public OrderVerificationPackage getFamilyOrderVerificationPackage() {
		return null;
	}

	private Collection<Product> cullCollection(SecurityModel perms,
			Collection<? extends Product> products) {
		Collection<Product> newList = new LinkedList<Product>();
		try {
			ExpirationTester expirationTester = getProductExpirationTester();
			if (products == null)
				return newList;
			log.debug("Culling product list (before): " + products.size());
			for (Product p : products) {
				if (p != null) {
					if (expirationTester.hasExpiredPermission(p, Right.READ,
							perms)) {
						newList.add(p);
					}
				}
			}
			log.debug("Culling product list (before): " + newList.size());
			return newList;
		} catch (NullPointerException e) {
			log.error("Failed to get expiration tester for product family "
					+ getTableName() + ", customer= " + getContextName(), e);
			return newList;
		}
	}

	private Collection<? extends Product> getExpiredImages(
			Collection<? extends Product> products) {
		List<Product> ps = new LinkedList<Product>();
		ExpirationTester expirationTester = getProductExpirationTester();
		for (Product p : products) {
			if (expirationTester.isExpired(p))
				ps.add(p);
		}
		return ps;
	}

	/***************************************************************************
	 * Gets the Product attribute of the ProductService object
	 * 
	 * @param familyName
	 *            Description of Parameter
	 * @param productId
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @param security
	 *            Description of Parameter
	 * @return The Product value
	 **************************************************************************/
	public Product getProduct(Object productId, SecurityModel security) {
		if (!(productId instanceof Integer) && productId != null)
			productId = Integer.parseInt(productId.toString());
		Product p = getCache().getCachedObject("id", productId);
		if (p == null) {
			p = new ExpiredImage();
			p.setProductFamily(this);
		} else if (!getProductExpirationTester().hasPermission(p, "read",
				security)) {
			p = new ExpiredImage();
			p.setProductFamily(this);
		}
		return p;
	}

	/***************************************************************************
	 * Get an array of Products from a single product family.
	 * 
	 * @param productTable
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @param security
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 **************************************************************************/
	public Collection<? extends Product> getProducts(SecurityModel security) {
		return getCache().getCachedList();
	}

	/***************************************************************************
	 * Gets all available values from a category value field for a given list of
	 * products.
	 * 
	 * @param fieldName
	 *            Name of category.
	 * @param origList
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Array of Strings of available values.
	 **************************************************************************/
	public SortedSet<Object> getAvailableCategoryValues(Set<Product> origList,
			String fieldName, SecurityModel security) {
		SortedSet<Object> available = new TreeSet<Object>();
		for (Product p : origList) {
			Collection vals = p.getValues(fieldName);
			if (vals != null)
				available.addAll(vals);
			else
				available.add(dbUtil.DEFAULT);
		}
		return available;
	}

	/***************************************************************************
	 * Gets all available values from a primary value field for a given list of
	 * products.
	 * 
	 * @param origList
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return Array of Strings of available values.
	 **************************************************************************/
	public SortedSet getAvailablePrimaryValues(Set<Product> origList,
			SecurityModel security) {
		SortedSet available = new TreeSet();
		for (Product p : origList) {
			available.add(p.getPrimary());
		}
		return available;
	}

	/***************************************************************************
	 * Gets the ProductsByDate attribute of the ProductService object
	 * 
	 * @param productTable
	 *            Description of Parameter
	 * @param date
	 *            Description of Parameter
	 * @param security
	 *            Description of Parameter
	 * @param controller
	 *            Description of Parameter
	 * @return The ProductsByDate value
	 **************************************************************************/
	public Product[] getProductsByDate(GregorianCalendar date,
			SecurityModel security) {
		return (Product[]) getCache().getCachedObjects("dateCataloged", date)
				.toArray(new Product[0]);
	}

	private Cache<Product> getCache() {
		initRegistration();
		return familyCache.getCache(getTableName(), Product.class);
	}

	public Product getProduct(String name, String path, SecurityModel security) {
		Product p = path != null ? getCache().getCachedObject("path", path,
				"name", name) : getCache().getCachedObject("name", name);
		if (p == null) {
			p = new ExpiredImage();
			p.setProductFamily(this);
		} else if (!getProductExpirationTester().hasPermission(p, "read",
				security)) {
			p = new ExpiredImage();
			p.setProductFamily(this);
		}
		return p;
	}

	public void deleteProduct(Product p, SecurityModel permissions) {
		if (getProductExpirationTester().hasPermission(p, "admin", permissions)) {
			getCache().deleteObject((CommerceProduct) p);
		}
	}

	public void updateProduct(Product product) {
		getCache().updateItem((CommerceProduct) product);
	}

	public ProductSet getExpiredImages(SecurityModel security) {
		ProductSet retVal = new ProductSet();
		if (security.getPermission(getTableName() + ".expired",
				Resource.EXPIRED_ITEMS, Right.READ)) {
			Collection<? extends Product> images = getExpiredImages(getCache()
					.getCachedList());
			retVal.addAll(getTableName(), images);
		} else
			throw new RuntimeException(
					LazerwebException.PRODUCT_EXPIRED_EXCEPTION);
		log.debug("Number expired products found in cache: "
				+ retVal.totalSize());
		return retVal;
	}

	public ProductSet getUnexpiredImages(SecurityModel security) {
		ProductSet retVal = new ProductSet();
		Collection<? extends Product> images = cullCollection(
				new NullSecurity(), getCache().getCachedList());
		retVal.addAll(getTableName(), images);
		return retVal;
	}

	public ProductSet findProductsWithPrimary(String searchTerm,
			int compareType, int searchType, SecurityModel security) {
		ProductSet retVal = new ProductSet();
		retVal.addAll(getTableName(), cullCollection(security, getCache()
				.getCachedObjects("Primary", searchTerm)));
		return retVal;
	}

	public ProductSet findProductsWithNumerical(String fieldName,
			Number searchTerm, int compareType, int searchType,
			Object[] pIDList, SecurityModel security) {
		if (pIDList.length == 0) {
			return new ProductSet();
		}
		ProductSet retVal = new ProductSet();
		retVal.addAll(getTableName(), cullCollection(security, getCache()
				.getCachedObjects(fieldName, searchTerm)));
		retVal.and(getProductsWithIds(pIDList));
		return retVal;
	}

	public ProductSet findProductsWithNumerical(String fieldName,
			Number searchTerm, int compareType, int searchType,
			SecurityModel security) {
		ProductSet retVal = new ProductSet();
		retVal.addAll(getTableName(), cullCollection(security, getCache()
				.getCachedObjects(fieldName, searchTerm)));
		return retVal;
	}

	public ProductSet findProductsWithDescription(String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {
		ProductSet retVal = new ProductSet();
		retVal.addAll(getTableName(), cullCollection(security, getCache()
				.getCachedObjects(isExact(searchTerm) ? "EXACT_SEARCH" : "NULL", stripQuotes(searchTerm))));
		return retVal;
	}
	
	protected boolean isExact(String x)
	{
		if(x.startsWith("\"") && x.endsWith("\"")) return true;
		else return false;
	}
	
	protected String stripQuotes(String x)
	{
		if(x.startsWith("\"")) x = x.substring(1);
		if(x.endsWith("\"")) x = x.substring(0,x.length()-1);
		return x;
	}

	public ProductSet findProductsWithDate(String fieldName, String searchTerm,
			int compareType, int searchType, SecurityModel security) {
		ProductSet retVal = new ProductSet();
		if (compareType == DBConnect.EQ)
			retVal.addAll(getTableName(), cullCollection(security, getCache()
					.getCachedObjects("on" + fieldName, searchTerm)));
		else
			retVal.addAll(getTableName(), cullCollection(security, getCache()
					.getCachedObjects(fieldName, searchTerm)));
		return retVal;
	}

	private ProductSet getProductsWithIds(Object[] pIDList) {
		ProductSet idMatches = new ProductSet();
		for (Object id : pIDList) {
			Product p = null;
			if (id instanceof Product)
				p = getCache().getCachedObject("id", ((Product) id).getId());
			else
				p = getCache().getCachedObject("id", id);
			if (p == null) {
				continue;
			}
			idMatches.add(getTableName(), p);
		}
		return idMatches;
	}

	public ProductSet findProductsWithCategory(String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {
		log.debug("searching cache with field " + fieldName + " and value "
				+ searchTerm);
		ProductSet retVal = new ProductSet();
		if (fieldName == null)
			return retVal;
		if (compareType == DBConnect.LIKE)
			fieldName = fieldName + "%%";
		if (searchTerm.equals(DatabaseUtilities.DEFAULT)) {
			retVal.addAll(getTableName(),
					cullCollection(security, productsWithoutValue(fieldName,
							getCache().getCachedList())));
		} else
			retVal.addAll(getTableName(), cullCollection(security, getCache()
					.getCachedObjects(fieldName, searchTerm)));
		log.debug("Number products found in cache: " + retVal.totalSize());
		return retVal;
	}

	private Collection<Product> productsWithoutValue(String field,
			Collection<? extends Product> products) {
		List<Product> thoseWithout = new LinkedList<Product>();
		for (Product p : products) {
			if (p.getValue(field) == null
					|| p.getValue(field) == DatabaseUtilities.DEFAULT) {
				thoseWithout.add(p);
			}
		}
		return thoseWithout;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@CoinjemaDependency(alias = "physicalAsset.path")
	public void setAssetPath(String c) {
		assetPath = c;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LogItem moveAsset(int productId, String newDir, String category,
			SecurityModel perms) {
		assert (perms.getPermission(getTableName(), Resource.DATATABLE,
				Right.ADMIN));
		Product p = getProduct(productId, perms);
		String currentValue = (String) p.getValue(category);
		String path = p.getPathName();
		String newPath = path.replaceFirst("/" + Pattern.quote(currentValue)
				+ "/", "/" + newDir + "/");
		p.setPath(new Path(newPath));
		p.resetValue(category, newDir);
		File actualAsset = new File(new File(assetPath, path), "_originals/"
				+ p.getPrimary());
		File newLocation = new File(new File(assetPath, newPath), "_originals/"
				+ p.getPrimary());
		newLocation.getParentFile().mkdirs();
		log.info("Moving file from " + actualAsset.getAbsolutePath() + " to "
				+ newLocation.getAbsolutePath());
		actualAsset.renameTo(newLocation);
		updateProduct(p);
		moveDerivatives(p.getName(), new File(assetPath, newPath), new File(
				assetPath, path));
		LogItem productMove = new LogItem();
		productMove.setValue("family", getDescriptiveName());
		productMove.setValue("product", p.getPrimary());
		productMove.setValue("from", currentValue);
		productMove.setValue("to", newDir);
		productMove.setValue("action", "move");
		return productMove;
	}

	public void moveDerivatives(final String name, File newDir, File basePath) {
		for (File dir : basePath.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String filename) {
				if (dir.isDirectory() && filename.startsWith("_")
						&& !filename.equals("_originals"))
					return true;
				else
					return false;
			}
		})) {
			final File subDir = dir;
			for (File deriv : dir.listFiles(new FilenameFilter() {

				public boolean accept(File f, String filename) {
					if (filename.startsWith(name + ".")
							&& -1 == filename.indexOf(".", name.length() + 1)) {
						return true;
					} else
						return false;
				}

			})) {
				File derivDir = new File(newDir, subDir.getName());
				derivDir.mkdirs();
				deriv.renameTo(new File(derivDir, deriv.getName()));
			}
		}
	}

	public LogItem renameAsset(int productId, String newName,
			SecurityModel perms) {
		assert (perms.getPermission(getTableName(), Resource.DATATABLE,
				Right.ADMIN));
		Product p = getProduct(productId, perms);
		String currentValue = p.getName();
		String newPrimary = newName + "." + Files.getExtension(p.getPrimary());
		String path = p.getPathName();
		File actualAsset = new File(new File(assetPath, path), "_originals/"
				+ p.getPrimary());
		File newLocation = new File(new File(assetPath, path), "_originals/"
				+ newPrimary);
		if(actualAsset.equals(newLocation)) return null;
		log.info("Moving file from " + actualAsset.getAbsolutePath() + " to "
				+ newLocation.getAbsolutePath());
		actualAsset.renameTo(newLocation);
		for (Object category : new HashSet(p.getValueNames())) {
			if (category.toString().startsWith("_")
					&& p.getValue(category.toString()) != null) {
				p.resetValue(category.toString(), p.getValue(
						category.toString()).toString().replaceAll(
						Pattern.quote("/" + p.getName() + "."),
						"/" + newName + "."));
			}
		}
		p.setPrimary(newPrimary);
		updateProduct(p);
		renameDerivatives(currentValue, newName, new File(assetPath, path));
		LogItem productMove = new LogItem();
		productMove.setValue("family", getDescriptiveName());
		productMove.setValue("product", p.getPrimary());
		productMove.setValue("from", currentValue);
		productMove.setValue("to", newName);
		productMove.setValue("action", "move");
		refresh();
		return productMove;
	}

	private void renameDerivatives(final String oldName, final String newName,
			File basePath) {
		for (File dir : basePath.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (dir.isDirectory() && name.startsWith("_")
						&& !name.equals("_originals"))
					return true;
				else
					return false;
			}
		})) {
			try {
				for (File deriv : dir.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String filename) {
						if (filename.startsWith(oldName + ".")
								&& -1 == filename.indexOf(".",
										oldName.length() + 1)) {
							return true;
						} else
							return false;
					}

				})) {
					deriv.renameTo(new File(deriv.getParentFile(), newName
							+ "." + Files.getExtension(deriv)));
				}
			} catch (Exception e) {
			}
		}
	}
}