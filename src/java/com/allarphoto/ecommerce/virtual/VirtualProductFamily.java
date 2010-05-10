package com.allarphoto.ecommerce.virtual;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.Converter;

import com.allarphoto.ajaxclient.client.beans.OrderVerificationPackage;
import com.allarphoto.application.ExpirationTester;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.application.impl.DefaultExpirationTester;
import com.allarphoto.beans.LogItem;
import com.allarphoto.beans.Path;
import com.allarphoto.beans.User;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.ecommerce.Merchant;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.ecommerce.ProductSet;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class VirtualProductFamily extends ProductFamily {

	SortedSet<VirtualProduct> products = new TreeSet<VirtualProduct>();

	Set<ProductFamily> families = new HashSet<ProductFamily>();
	
	User owner;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public synchronized void addProduct(Product p) {
		VirtualProduct vp = new VirtualProduct(p);
		if (products.add(vp)) {
			families.add(p.getProductFamily());
			Map<Object, Object> values = new HashMap<Object, Object>();
			values.put("product", vp);
			values.put("virtualFamily", this);
			mapper.doUpdate("addVirtualProduct.sql", values);
		}
	}

	@Override
	public void clearFieldCache() {
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return super.compare(o1, o2);
	}

	@Override
	public void createField(ProductField field, SecurityModel perms) {
	}

	@Override
	public void deleteField(String fieldName) {
	}

	@Override
	public synchronized void deleteProduct(Product p, SecurityModel permissions) {
		if (permissions.getPermission(getTableName(), Resource.VIRT_DATATABLE,
				Right.ADMIN)) {
			VirtualProduct vp = find(p);
			if (vp != null) {
				products.remove(vp);
				Map<Object, Object> values = new HashMap<Object, Object>();
				values.put("product", vp);
				values.put("virtualFamily", this);
				mapper.doUpdate("removeVirtualProduct.sql", values);
			}
		}
	}

	private VirtualProduct find(Product p) {
		VirtualProduct vp = null;
		for (VirtualProduct v : products) {
			if (v.getProduct().equals(p)) {
				vp = v;
				break;
			}
		}
		return vp;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public boolean exists(Path p) {
		return false;
	}

	@Override
	public long filesize() {
		long size = 0;
		for (VirtualProduct vp : products)
			size += ((Number) vp.getValue("File Size")).longValue();
		return size;
	}

	@Override
	public ProductSet findProductsWithCategory(String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {
		ProductSet ps = new ProductSet();
		ps.add(this.getTableName());
		for (VirtualProduct vp : products) {
			Object value = vp.getValue(fieldName);
			if (value != null)
				value = value.toString();
			if (searchTerm.equals(value))
				ps.add(getTableName(), vp);
		}
		return ps;
	}

	@Override
	public ProductSet findProductsWithDate(String fieldName, String searchTerm,
			int compareType, int searchType, SecurityModel security) {
		ProductSet retVal = new ProductSet();
		Calendar searchDate = Converter.getCalendar(searchTerm);
		Calendar endSearch = (Calendar) searchDate.clone();
		Calendar startSearch = (Calendar) searchDate.clone();
		startSearch.add(Calendar.DATE, -1);
		endSearch.add(Calendar.DATE, 1);
		for (VirtualProduct vp : products) {
			Calendar productDate = null;
			if ("dateCataloged".equals(fieldName))
				productDate = vp.getDateCreated();
			else if ("dateModified".equals(fieldName))
				productDate = vp.getDateModified();
			if (compareType == DBConnect.EQ) {
				if (productDate.after(startSearch)
						&& productDate.before(endSearch))
					retVal.add(getTableName(), vp);
			} else if (productDate.after(startSearch))
				retVal.add(getTableName(), vp);
		}
		return retVal;
	}
	
	@Override
	public ProductSet findProductsWithDescription(String fieldName,
			String searchTerm, int compareType, int searchType,
			SecurityModel security) {

		ProductSet ps = new ProductSet();
		ps.add(this.getTableName());
		for (VirtualProduct vp : products) {
			Object value = vp.getValue(fieldName);
			if (value != null)
			{
				value = value.toString();
			if (((String)value).indexOf(searchTerm) > -1)
				ps.add(getTableName(), vp);
			}
		}
		return ps;
	}

	@Override
	public ProductSet findProductsWithNumerical(String fieldName,
			Number searchTerm, int compareType, int searchType,
			Object[] pIDList, SecurityModel security) {
		return new ProductSet();
	}

	@Override
	public ProductSet findProductsWithNumerical(String fieldName,
			Number searchTerm, int compareType, int searchType,
			SecurityModel security) {
		return new ProductSet();
	}

	@Override
	public ProductSet findProductsWithPrimary(String searchTerm,
			int compareType, int searchType, SecurityModel security) {
		ProductSet ps = new ProductSet();
		ps.add(this.getTableName());
		for (VirtualProduct vp : products) {
			if(vp.getPrimary().equals(searchTerm))
				ps.add(getTableName(), vp);
		}
		return ps;
	}



	@Override
	public String getDescription() {
		return super.getTableName();
	}

	@Override
	public String[] getDescriptionFields() {
		return new String[0];
	}

	@Override
	public String getDescriptiveName() {
		return super.getTableName();
	}

	@Override
	public ProductSet getExpiredImages(SecurityModel security) {
		ProductSet ps = new ProductSet();
		for(VirtualProduct vp : products)
		{
			if(vp.getProductFamily().getProductExpirationTester().isExpired(vp))
				ps.add(getTableName(),vp);
		}
		return ps;
	}

	@Override
	public OrderVerificationPackage getFamilyDownloadVerificationPackage() {
		return super.getFamilyDownloadVerificationPackage();
	}

	@Override
	public OrderVerificationPackage getFamilyOrderVerificationPackage() {
		return super.getFamilyOrderVerificationPackage();
	}

	@Override
	public ProductField getField(String name) {
		return null;
	}

	@Override
	public List<ProductField> getFields() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public Merchant getMerchant() {
		// TODO Auto-generated method stub
		return super.getMerchant();
	}

	@Override
	public String getOrderModelClass() {
		// TODO Auto-generated method stub
		return super.getOrderModelClass();
	}

	@Override
	public String getPathRoot() {
		return "/";
	}

	@Override
	public String[] getPriceKeys() {
		// TODO Auto-generated method stub
		return super.getPriceKeys();
	}

	@Override
	public String getPrimaryLabel() {
		// TODO Auto-generated method stub
		return super.getPrimaryLabel();
	}

	@Override
	public Product getProduct(Object productId, SecurityModel security) {
		for(VirtualProduct vp : products)
		{
			if(vp.getId() == Converter.getInt(productId))
				return vp;
		}
		return null;
	}

	@Override
	public Product getProduct(String name, String path, SecurityModel security) {
		for(VirtualProduct vp : products)
		{
			if(vp.getName().equals(name) && vp.getPathName().equals(path))
				return vp;
		}
		return null;
	}

	@Override
	public ProductBean getProductBean(Product p) {
		return p.getProductFamily().getProductBean(p);
	}

	@Override
	public ExpirationTester getProductExpirationTester() {
		return new DefaultExpirationTester();
	}

	@Override
	public synchronized Collection<? extends Product> getProducts(SecurityModel security) {
		if(products.size() == 0)
		{
			Map<Object,Object> values = new HashMap<Object,Object>();
			values.put("virtualFamily",getTableName());
			Collection<Map> virtualProducts = mapper.getObjects("getVirtualProducts.sql",values);
			for(Map virtProd : virtualProducts)
			{
				int id = Converter.getInt(virtProd.get("product_id"));
				String table = (String)virtProd.get("product_table");
				ProductFamily family = dbUtil.getProductFamily(table);
				addProduct(family.getProduct(id, security));
			}
		}
		return products;
	}
	
	@Override
	public Product[] getProductsByDate(GregorianCalendar date,
			SecurityModel security) {
		ProductSet retVal = new ProductSet();
		date.add(Calendar.DATE, -1);
		for (VirtualProduct vp : products) {
			Calendar productDate = null;
			if (productDate.after(date))
				retVal.add(getTableName(), vp);
		}
		return retVal.getProductList(getTableName());
	}

	@Override
	public ProductField[] getSearchFields() {
		return new ProductField[0];
	}

	@Override
	public ProductSet getUnexpiredImages(SecurityModel security) {
		ProductSet ps = new ProductSet();
		for(VirtualProduct vp : products)
		{
			if(!vp.getProductFamily().getProductExpirationTester().isExpired(vp))
				ps.add(getTableName(),vp);
		}
		return ps;
	}

	@Override
	public boolean isRemoteManaged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LogItem moveAsset(int productId, String newDir, String category,
			SecurityModel perms) {
		return null;
	}

	@Override
	public void moveDerivatives(String name, File newDir, File basePath) {
	}

	@Override
	public synchronized void refresh() {
		products.clear();
	}

	@Override
	public LogItem renameAsset(int productId, String newName,
			SecurityModel perms) {
		return null;
	}
	
	@Override
	public void updateField(ProductField f, SecurityModel perms) {
	}

	@Override
	public void updateProduct(Product product) {
		product.getProductFamily().updateProduct(product);
	}

	ObjectMappingService mapper;

	@CoinjemaDependency(method = "objectMapper", type = "objectMappingService", hasDefault = true)
	public void setMapper(ObjectMappingService m) {
		mapper = m;
	}

}
