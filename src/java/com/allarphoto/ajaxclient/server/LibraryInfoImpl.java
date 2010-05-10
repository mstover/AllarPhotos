package com.allarphoto.ajaxclient.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;
import strategiclibrary.util.Files;

import com.allarphoto.ajaxclient.client.LibraryInfo;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.AjaxProductField;
import com.allarphoto.ajaxclient.client.beans.DownloadStats;
import com.allarphoto.ajaxclient.client.beans.DownloadUserStat;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.DownloadItem;
import com.allarphoto.beans.LogItem;
import com.allarphoto.beans.User;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.beans.ProductSetBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.util.SearchUtil;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.dbtools.QueryItem;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.ecommerce.ProductSet;
import com.allarphoto.ecommerce.virtual.VirtualProductFamily;
import com.allarphoto.lazerweb.utils.ProductDateSort;
import com.allarphoto.lazerweb.utils.ProductFieldSort;
import com.allarphoto.lazerweb.utils.ProductNameSort;
import com.allarphoto.server.ProductService;
import com.allarphoto.utils.DatabaseLogger;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

@CoinjemaObject
public class LibraryInfoImpl extends AbstractGwtServlet implements LibraryInfo {

	private static final long serialVersionUID = 1;

	ProductService pService;

	SearchUtil searchUtil = new SearchUtil();

	String uploadDir = "";

	String altUploadDir = "";

	CacheService cache;

	public LibraryInfoImpl() {
		super();
	}

	public String getLibraryName() {
		HandlerData info = getThreadLocalHandlerData();
		try {
			return getProductFamily(info).getDescriptiveName();
		} catch (NullPointerException e) {
			return "No Current Library!";
		}
	}

	public boolean refreshProductFamily(String family) {
		ProductFamily library = dbutil.getProductFamily(family);
		if (currentPerms().getPermission(library.getTableName(),
				Resource.DATATABLE, Right.ADMIN)) {
			library.refresh();
			return true;
		}
		return false;
	}

	public boolean addField(String family, String fieldName) {
		try {
			dbutil.getProductFamily(family).createField(
					ProductField.createField(family, fieldName,
							ProductField.CATEGORY, 0, 0), currentPerms());
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to create product field");
			return false;
		}
	}

	public List getPendingUploads() {
		File[] uploadFiles = new File(altUploadDir)
				.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						if (name.endsWith(".zip"))
							return true;
						else
							return false;
					}
				});
		List uploadPackages = new ArrayList();
		int count = 0;
		for (File pack : uploadFiles) {
			try {
				Properties props = getPropertiesFromZip(pack);
				if (props != null && currentPerms().getPermission(props.getProperty("family"),Resource.DATATABLE, Right.ADMIN))
					uploadPackages.add(new HashMap(props));
			} catch (IOException e) {
				// uploadPackages.add(new HashMap());
			}
		}
		return uploadPackages;
	}

	public Collection getCategoryValues(String f, String fieldName) {
		ProductFamily family = dbutil.getProductFamily(f);
		ProductSetBean psb = new ProductSetBean();
		searchUtil.getInitialSet(f, psb, currentPerms());
		return new ArrayList(searchUtil.getAvailableValues(psb
				.getFirstProductSet(), null, null, fieldName, currentPerms()));

	}

	public String getLibrarySize(String family) {
		return Converter.formatNumber(dbutil.getProductFamily(family)
				.filesize() / 1024 / 1024, "#,###.#")
				+ " MB";
	}

	private void transferZipEntries(ZipInputStream inZip,
			ZipOutputStream outZip, Properties imageMeta) throws IOException {
		try {
			ZipEntry entry = inZip.getNextEntry();
			while (entry != null) {
				if (!entry.getName().equals("imagedata.properties")) {
					writeStreamToZip(entry.getName(), outZip, inZip);
					entry = inZip.getNextEntry();
				} else {
					writeStreamToZip(entry.getName(), outZip, imageMeta);
					entry = inZip.getNextEntry();
				}
			}
		} finally {
			if (inZip != null)
				inZip.close();
		}
	}

	public boolean moveAsset(int productId, String familyName, String newDir,
			String category) {
		try {
			ProductFamily family = dbutil.getProductFamily(familyName);
			LogItem productMove = family.moveAsset(productId, newDir, category,
					currentPerms());
			productMove.setValue("user", ((UserBean) this
					.getThreadLocalHandlerData().getUserBean("user"))
					.getUsername());
			dbLogger.addLogItem(productMove);

			return true;
		} catch (Exception e) {
			getLog().warn("Failed to move asset", e);
			return false;
		}
	}

	public boolean deleteField(String fieldName, String familyName) {
		try {
			ProductFamily family = dbutil.getProductFamily(familyName);
		family.deleteField(fieldName);
		return true;
		}catch(Exception e) { return false; }
	}

	private void writeStreamToZip(String filename, ZipOutputStream out,
			Properties in) throws IOException {
		out.putNextEntry(new ZipEntry(filename));
		in.store(out, "");
		out.closeEntry();
	}

	private void writeStreamToZip(String filename, ZipOutputStream out,
			InputStream in) throws IOException {
		out.putNextEntry(new ZipEntry(filename));
		Files.copy(in, out);
		out.closeEntry();
	}

	public synchronized boolean approveUploadPackage(Map uploadParams) {
		try {
			Properties newProps = new Properties();
			for (Object k : uploadParams.keySet()) {
				String key = (String) k;
				if (!key.equals("Package Name"))
					newProps.setProperty(key, (String) uploadParams.get(key));
			}
			ZipInputStream inZip = new ZipInputStream(new FileInputStream(
					new File(altUploadDir, (String) uploadParams
							.get("Package Name"))));
			int count = 0;
			File copyTo = new File(uploadDir, (String) uploadParams
					.get("Package Name"));
			while (copyTo.exists())
				copyTo = new File(uploadDir, (count++)
						+ (String) uploadParams.get("Package Name"));
			ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
					copyTo));
			try {
				transferZipEntries(inZip, outZip, newProps);
			} finally {
				outZip.close();
				inZip.close();
			}
			new File(altUploadDir, (String) uploadParams.get("Package Name"))
					.delete();
			return true;
		} catch (IOException e) {
			getLog().warn("Failed to approve upload package", e);
			return false;
		}
	}

	Properties getPropertiesFromZip(File f) throws IOException {
		ZipFile zipin = new ZipFile(f);
		Enumeration<? extends ZipEntry> entries = zipin.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().equals("imagedata.properties")) {
				Properties props = new Properties();
				props.load(zipin.getInputStream(entry));
				props.setProperty("Package Name", f.getName());
				return props;
			}
		}
		return null;
	}

	public boolean updateField(String name, String fam, String typ, int dor,
			int so) {
		try {
			AjaxProductField field = new AjaxProductField(name, fam, typ, so,
					dor);
			getLog().info(
					"update field: " + field.getFamily() + "/"
							+ field.getName());
			HandlerData info = getThreadLocalHandlerData();
			ProductField f = dbutil.getProductFamily(field.getFamily())
					.getField(field.getName());
			int type = field.getTypeOf();
			ProductField update = ProductField.createField(field.getFamily(),
					field.getName(), type, field.getDisplayOrder(), field
							.getSearchOrder());
			update.setFieldID(f.getFieldID());
			dbutil.getProductFamily(field.getFamily()).updateField(update,
					((UserBean) info.getBean("user")).getPermissions());
		} catch (Exception e) {
			e.printStackTrace();
			getLog().warn(e);
		}
		return true;
	}

	public AjaxProductFamily getProductFamily(String family) {
		ProductFamily pf = dbutil.getProductFamily(family);
		return createAjaxProductFamily(pf);
	}

	public AjaxProductFamily[] getLibraries(String right) {
		try {
			getLog().info("Getting libraries");
			Collection<VirtualProductFamily> vfs = cache.getCache(VirtualProductFamily.class).getCachedList();
			getLog().info("virtual libraries = " + vfs);
			Collection<ProductFamily> families = dbutil.getProductFamilies();
			if (right == null) {
				AjaxProductFamily[] productFamilies = new AjaxProductFamily[families
						.size()];
				int count = 0;
				for (ProductFamily famObj : families) {
					productFamilies[count++] = createAjaxProductFamily(famObj);
				}
				getLog().info("Got libraries");
				return productFamilies;
			} else {
				SecurityModel perms = currentPerms();
				Collection<AjaxProductFamily> librariesCanAdmin = new LinkedList<AjaxProductFamily>();
				for (ProductFamily famObj : families) {
					if (perms.getPermission(famObj.getTableName(),
							Resource.DATATABLE, right))
						librariesCanAdmin.add(createAjaxProductFamily(famObj));
				}
				getLog().info("Got libraries");
				return (AjaxProductFamily[]) librariesCanAdmin
						.toArray(new AjaxProductFamily[librariesCanAdmin.size()]);

			}
		} catch (Throwable e) {
			getLog().error("getLibraries caused error", e);
			return new AjaxProductFamily[0];
		}
	}

	private ProductSet getProductsFound(HandlerData info) {
		return ((ProductSetBean) info.getBean("productsFound"))
				.getCurrentProductSet();
	}

	private ProductFamily getProductFamily(HandlerData info) {
		return dbutil.getProductFamily(getProductsFound(info)
				.getProductFamilies().iterator().next());
	}

	public String[] getUploadableFolders(String[] previous,
			String familyDescriptiveName) {

		try {
			if (familyDescriptiveName == null) {
				Collection<ProductFamily> families = dbutil
						.getProductFamilies();
				SecurityModel perms = currentPerms();
				Collection<String> librariesCanAdmin = new LinkedList<String>();
				for (ProductFamily famObj : families) {
					if (perms.getPermission(famObj.getTableName(),
							Resource.DATATABLE, Right.UPLOAD))
						librariesCanAdmin.add(famObj.getDescriptiveName());
				}
				return (String[]) librariesCanAdmin
						.toArray(new String[librariesCanAdmin.size()]);
			} else {
				ProductFamily family = dbutil
						.getProductFamilyFromDescription(familyDescriptiveName);
				ProductSetBean setBean = new ProductSetBean();
				searchUtil.getInitialSet(family.getTableName(), setBean,
						currentPerms());
				for (int i = 0; i < previous.length; i++) {
					ProductField field = getFieldName(i, family);
					Set<QueryItem> criteria = new HashSet<QueryItem>();
					QueryItem crit = new QueryItem();
					crit.setCategory(field.getName());
					crit.addValue(previous[i]);
					criteria.add(crit);
					getLog().debug("Searching with " + crit);
					searchUtil.search(setBean, criteria, currentPerms(), false);

				}
				ProductField nextField = getFieldName(previous.length, family);
				Set<String> folders = searchUtil.getAvailableValues(setBean
						.getCurrentProductSet(), null, null, nextField
						.getName(), currentPerms());
				folders.remove("N/A");
				if (nextField.getType() == ProductField.PROTECTED)
					removeProtectedValues(folders, nextField, currentPerms(),Right.UPLOAD);
				return (String[]) folders.toArray(new String[folders.size()]);
			}
		} catch (Exception e) {
			getLog().warn("Problem getting folders", e);
			return new String[0];
		}
	}

	public String[] getSearchFolders(String[][] previous,
			String familyDescriptiveName) {

		try {
			if (familyDescriptiveName == null) {
				Collection<ProductFamily> families = dbutil
						.getProductFamilies();
				SecurityModel perms = currentPerms();
				Collection<String> librariesCanAdmin = new LinkedList<String>();
				for (ProductFamily famObj : families) {
					if (perms.getPermission(famObj.getTableName(),
							Resource.DATATABLE, Right.READ))
						librariesCanAdmin.add(famObj.getDescriptiveName());
				}
				LinkedList<String> libraries = new LinkedList<String>(
						librariesCanAdmin);
				libraries.addFirst("library");
				return libraries.toArray(new String[libraries.size()]);
			} else {
				ProductFamily family = dbutil
						.getProductFamilyFromDescription(familyDescriptiveName);
				getLog().info("Family searched = " + familyDescriptiveName);
				ProductSetBean setBean = createProductSet(previous, family);
				ProductField nextField = (previous.length > 0 && previous[0].length > 0) ? getNextField(
						previous[0], family)
						: getNextField("library", family);
				LinkedList<String> folders = new LinkedList<String>(
						searchUtil.getAvailableValues(setBean
								.getCurrentProductSet(), null, null, nextField
								.getName(), currentPerms()));
				folders.remove("N/A");
				getLog().info("Folders = " + folders);
				while (folders.size() <= 1 && nextField != null
						&& setBean.getCurrentProductSet().totalSize() > 0) {
					nextField = getNextField(nextField.getName(), family);
					if (nextField != null) {
						folders = new LinkedList<String>(searchUtil
								.getAvailableValues(setBean
										.getCurrentProductSet(), null, null,
										nextField.getName(), currentPerms()));
						folders.remove("N/A");
					}
				}
				getLog().info("Folders = " + folders);
				if (nextField != null
						&& nextField.getType() == ProductField.PROTECTED)
					removeProtectedValues(folders, nextField, currentPerms(),Right.READ);

				if (nextField != null)
					folders.addFirst(nextField.getName());
				getLog().info("Folders = " + folders);
				return (String[]) folders.toArray(new String[folders.size()]);
			}
		} catch (Exception e) {
			getLog().warn("Problem getting folders", e);
			return new String[0];
		}
	}

	private ProductField getNextField(String[] fieldList, ProductFamily family) {
		String lastField = fieldList[fieldList.length - 1];
		try {
			if (lastField.equals("keyword-search"))
				lastField = fieldList[fieldList.length - 2];
		} catch (RuntimeException e) {
			lastField = "library";
		}
		return getNextField(lastField, family);
	}

	private ProductField getNextField(String lastField, ProductFamily family) {
		List<ProductField> fields = SearchFieldFilter.resortFields(family
				.getFields());
		if (lastField.equals("library"))
			return fields.get(0);
		boolean match = false;
		for (ProductField f : fields) {
			if (match)
				return f;
			if (f.getName().equals(lastField))
				match = true;
		}
		return null;
	}

	public AjaxProduct[] getProducts(String[][] categoryChoices,
			String familyDescriptiveName, int chunkSize, int offset) {
		try {
			ProductFamily family = dbutil
					.getProductFamilyFromDescription(familyDescriptiveName);
			ProductSetBean setBean = createProductSet(categoryChoices, family);
			getLog().info(
					"Chunksize = " + chunkSize + " offset = " + offset
							+ " product list size = "
							+ setBean.getCurrentProductSet().totalSize());
			AjaxProduct[] products = new AjaxProduct[((chunkSize > 0 && chunkSize < (setBean
					.getCurrentProductSet().totalSize() - offset)) ? chunkSize
					: setBean.getCurrentProductSet().totalSize() - offset)];
			getLog().info("returned products size = " + products.length);
			int count = 0;
			int index = 0;
			ProductBean bean = family.getProductBeanClass().newInstance();
			for (Product p : setBean.getCurrentProductSet().getProductList()) {
				if (count >= products.length)
					break;
				if (index >= offset)
					products[count++] = createAjaxProduct(p, bean);
				index++;
			}
			return products;
		} catch (Exception e) {
			getLog().warn("Failed to get products", e);
			return new AjaxProduct[0];
		}
	}

	public int getProductCount(String[][] categoryChoices,
			String familyDescription) {

		ProductFamily family = dbutil
				.getProductFamilyFromDescription(familyDescription);
		ProductSetBean setBean = createProductSet(categoryChoices, family);
		return setBean.getCurrentProductSet().totalSize();
	}

	public boolean setProductSorter(String sorterName) {
		if ("date".equals(sorterName))
			getThreadLocalHandlerData().setUserBean("productSorter",
					new ProductDateSort());
		else if ("name".equals(sorterName))
			getThreadLocalHandlerData().setUserBean("productSorter",
					new ProductNameSort());
		else if (sorterName != null && sorterName.length() > 0)
			getThreadLocalHandlerData().setUserBean("productSorter",
					new ProductFieldSort(sorterName));
		else
			getThreadLocalHandlerData().removeUserBean("productSorter");
		return true;
	}

	private ProductSetBean createProductSet(String[][] categoryChoices,
			ProductFamily family) {
		ProductSetBean setBean = new ProductSetBean();
		if (getThreadLocalHandlerData().getUserBean("productSorter") != null) {
			setBean.setProductSorter((Comparator) getThreadLocalHandlerData()
					.getUserBean("productSorter"));
		}
		searchUtil
				.getInitialSet(family.getTableName(), setBean, currentPerms());
		if (categoryChoices.length > 0) {
			for (int i = 0; i < categoryChoices[0].length; i++) {
				if (categoryChoices[0][i].equals("library"))
					continue;
				Set<QueryItem> criteria = new HashSet<QueryItem>();
				if (categoryChoices[0][i].equals("keyword-search")) {
					String searchCat = "Date Posted";
					if (categoryChoices[1][i].startsWith("mod:")) {
						categoryChoices[1][i] = categoryChoices[1][i]
								.substring(4);
						searchCat = "Date Modified";
					}
					Calendar dateSearch = Converter.getCalendar(
							categoryChoices[1][i], null);
					if (dateSearch == null || (!isReasonableDate(dateSearch))) {
						if (categoryChoices[1][i].indexOf(":") > -1) {
							String cat = categoryChoices[1][i].substring(0,
									categoryChoices[1][i].indexOf(":"));

							ProductField field = family.getField(cat);
							QueryItem crit = new QueryItem();
							crit.setCategory(field.getName());
							crit.addValue(categoryChoices[1][i].substring(cat
									.length() + 1));
							criteria.add(crit);
							crit.setCompareType(DBConnect.LIKE);
							getLog().debug("Searching with " + crit);
							searchUtil.search(setBean, criteria,
									currentPerms(), false);
						} else
							searchUtil.simpleSearch(setBean,
									categoryChoices[1][i], currentPerms());
					} else {
						criteria.add(searchUtil.getQueryItem(Converter.formatCalendar(dateSearch, "MM/dd/yyyy"),
								new String[] { categoryChoices[1][i] }));
						searchUtil.search(setBean, criteria, currentPerms(),
								false);
					}
				} else if (categoryChoices[0][i].equals("since-date")) {
					QueryItem crit = new QueryItem();
					crit.setCategory("Date Posted");
					crit.addValue(getDate(categoryChoices[1][i]));
					crit.setCompareType(3);
					crit.setSearchType(1);
					criteria.add(crit);
					getLog().debug("Searching with " + crit);
					searchUtil.search(setBean, criteria, currentPerms(), false);
				} else if (categoryChoices[0][i].equals("since-mod-date")) {
					QueryItem crit = new QueryItem();
					crit.setCategory("Date Modified");
					crit.addValue(getDate(categoryChoices[1][i]));
					crit.setCompareType(3);
					crit.setSearchType(1);
					criteria.add(crit);
					getLog().debug("Searching with " + crit);
					searchUtil.search(setBean, criteria, currentPerms(), false);
				} else {
					ProductField field = family.getField(categoryChoices[0][i]);
					QueryItem crit = new QueryItem();
					crit.setCategory(field.getName());
					crit.addValue(categoryChoices[1][i]);
					criteria.add(crit);
					getLog().debug("Searching with " + crit);
					searchUtil.search(setBean, criteria, currentPerms(), false);
				}
			}
		}
		getThreadLocalHandlerData().setUserBean("productsFound", setBean);
		return setBean;
	}

	private boolean isReasonableDate(Calendar dateSearch) {
		return dateSearch.get(Calendar.YEAR) < 2100 && dateSearch.get(Calendar.YEAR) > 1900 &&
		dateSearch.get(Calendar.DATE) > 0 && dateSearch.get(Calendar.DATE) < 32 &&
		dateSearch.get(Calendar.MONTH) >= 0 && dateSearch.get(Calendar.MONTH) < 13;
	}

	private String getDate(String text) {
		Calendar d = new GregorianCalendar();
		if (text.equalsIgnoreCase("Yesterday")) {
			d.add(Calendar.DATE, -1);
			return Converter.formatCalendar(d, "yyyy-MM-dd");
		} else if (text.equalsIgnoreCase("Last Week")) {
			d.add(Calendar.DATE, -7);
			return Converter.formatCalendar(d, "yyyy-MM-dd");
		} else if (text.equalsIgnoreCase("Last Month")) {
			d.add(Calendar.MONTH, -1);
			return Converter.formatCalendar(d, "yyyy-MM-dd");
		} else if (text.equalsIgnoreCase("Last 6 Months")) {
			d.add(Calendar.MONTH, -6);
			return Converter.formatCalendar(d, "yyyy-MM-dd");
		}
		return text;
	}

	public List<String> getDisplayableFields(String familyName) {
		try {
			ProductFamily family = dbutil.getProductFamily(familyName);
			ProductBean bean = family.getProductBeanClass().newInstance();
			bean.setProduct(family.getProducts(currentPerms()).iterator()
					.next());
			return bean.getDisplayFields();
		} catch (Exception e) {
			getLog().warn("Problem getting displayable fields", e);
		}
		return Collections.EMPTY_LIST;
	}

	public List<AjaxProductField> getEditableFields(String familyName) {
		try {
			List<AjaxProductField> editable = new ArrayList<AjaxProductField>();
			ProductFamily family = dbutil.getProductFamily(familyName);
			ProductBean bean = family.getProductBeanClass().newInstance();
			bean.setProduct(family.getProducts(currentPerms()).iterator()
					.next());
			SecurityModel sec = currentPerms();
			boolean isSu = sec.getPermission("all",Resource.DATABASE,Right.ADMIN);
			for (ProductField f : family.getFields()) {
				if (bean.isFieldEditable(f)) {
					editable.add(new AjaxProductField(f.getName(), f
							.getFamily(), f.getType(), f.getSearchOrder(), f
							.getDisplayOrder()));
				} else if(isSu && f.getDisplayOrder() >= 2000) {
					editable.add(new AjaxProductField(f.getName(), f
							.getFamily(), f.getType(), f.getSearchOrder(), f
							.getDisplayOrder()));
				} 
			}
			return editable;
		} catch (Exception e) {
			getLog().warn("Problem getting displayable fields", e);
		}
		return Collections.EMPTY_LIST;
	}

	private void removeProtectedValues(Collection folders,
			ProductField protectedField, SecurityModel perms,Right rt) {
		Iterator iter = folders.iterator();
		while (iter.hasNext()) {
			Object value = iter.next();
			if (value == null)
				iter.remove();
			if (!perms.getPermission(protectedField.getFamily() + "."
					+ protectedField.getName() + "." + value.toString(),
					Resource.PROTECTED_FIELD, rt))
				iter.remove();
		}
	}

	private ProductField getFieldName(int level, ProductFamily family) {
		List<ProductField> fields = UploadFieldFilter.resortFields(family
				.getFields());
		return fields.get(level);

	}

	public ProductService getPService() {
		return pService;
	}

	@CoinjemaDependency(type = "productService")
	public void setPService(ProductService service) {
		pService = service;
	}

	private DownloadStats getDownloadStats(ProductFamily family, Calendar from,
			Calendar to) {
		try {
			if (!currentPerms().getPermission(family.getTableName(),
					Resource.DATATABLE, Right.ADMIN))
				return null;
			Collection<DownloadItem> items = dbLogger.getDownloadRequests(
					family, from, to);
			items.addAll(dbLogger.getUploadRequests(family, from, to));
			DownloadStats stats = new DownloadStats();
			stats.setFrom(Converter.formatCalendar(from, "MM/dd/yyyy"));
			stats.setTo(Converter.formatCalendar(to, "MM/dd/yyyy"));
			stats.setTotal(Converter.formatNumber(calcSize(items), "#,###.0")
					+ " MB");
			stats.setUserMap(calcUserUsage(items));
			return stats;
		} catch (Exception e) {
			getLog().warn("Failed to get download stats", e);
			return null;
		}
	}

	private DownloadUserStat[] calcUserUsage(Collection<DownloadItem> items) {
		Map<String, Long> userMap = new HashMap<String, Long>();
		for (DownloadItem item : items) {
			User u = item.getUser();
			if (u != null) {
				addUserTotal(userMap, item, u);
			} else {
				u = new CommerceUser();
				u.setUsername("Unknown User");
				u.setLastName("Unknown");
				u.setFirstName("User");
				addUserTotal(userMap, item, u);
			}
		}
		DownloadUserStat[] retVals = new DownloadUserStat[userMap.size()];
		int count = 0;
		for (Map.Entry<String, Long> e : userMap.entrySet()) {
			retVals[count++] = new DownloadUserStat(e.getKey(), Converter
					.formatNumber((double) (e.getValue() / 1024F), "#,###.0")
					+ " KB", e.getValue());
		}
		Arrays.sort(retVals);
		return retVals;
	}

	private void addUserTotal(Map<String, Long> userMap, DownloadItem item,
			User u) {
		String fullName = u.getFullName();
		if (userMap.containsKey(fullName)) {
			long size = userMap.get(fullName);
			size += item.getSize();
			userMap.put(fullName, size);
		} else {
			userMap.put(fullName, item.getSize());
		}
	}

	private float calcSize(Collection<DownloadItem> items) {
		double size = 0;
		for (DownloadItem item : items) {
			size += item.getSize();
		}
		return (float) (size / 1024D / 1024D);
	}

	public boolean updateProduct(int productId, String familyName, Map values) {
		try {
			assert (currentPerms().getPermission(familyName,
					Resource.DATATABLE, Right.ADMIN));
			ProductFamily family = dbutil.getProductFamily(familyName);
			Product prod = family.getProduct(productId, currentPerms());
			for (Object key : values.keySet()) {
				if (key.equals("FILENAME")) {
					if (!prod.getName().equals(values.get(key)))
						family.renameAsset(prod.getId(), (String) values
								.get(key), currentPerms());
				} else {
					prod.removeValue((String) key);
					for (String val : splitValues((String) values.get(key))) {
						prod.setValue((String) key, val);
					}
				}
			}
			family.updateProduct(prod);
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to update product", e);
			return false;
		}
	}

	protected String[] splitValues(String s) {
		if (s == null)
			return new String[0];
		else
			return s.split("\\|");
	}

	public DownloadStats getDownloadStats(String family, String fromDate,
			String toDate) {
		return getDownloadStats(dbutil.getProductFamily(family), Converter
				.getCalendar(fromDate), Converter.getCalendar(toDate));
	}

	public DownloadStats getDownloadStats(String family, String fromDate) {
		return getDownloadStats(dbutil.getProductFamily(family), Converter
				.getCalendar(fromDate), new GregorianCalendar());
	}

	public DownloadStats getDownloadStats(String family) {
		return getDownloadStats(dbutil.getProductFamily(family),
				getYearStart(), new GregorianCalendar());
	}

	public boolean setFamilyRemotedManaged(String family, boolean rm) {
		ProductFamily f = dbutil.getProductFamily(family);
		f.setRemoteManaged(rm);
		cache.getCache(ProductFamily.class).updateItem(f);
		return true;
	}

	public Calendar getYearStart() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		return c;
	}

	protected DatabaseLogger dbLogger;

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setDbLogger(DatabaseLogger dl) {
		dbLogger = dl;
	}

	@CoinjemaDependency(alias = "uploadDir", hasDefault = true)
	public void setUploadDir(String ud) {
		uploadDir = ud;
	}

	@CoinjemaDependency(alias = "altUploadDir", hasDefault = true)
	public void setAltUploadDir(String aud) {
		altUploadDir = aud;
	}

	String pathPrefix;

	@CoinjemaDependency(alias = "physicalAsset.path")
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	@CoinjemaDependency(type = "cacheService")
	public void setCache(CacheService c) {
		cache = c;
	}

}
