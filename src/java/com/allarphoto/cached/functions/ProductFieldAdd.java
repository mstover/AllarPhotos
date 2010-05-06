package com.lazerinc.cached.functions;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import strategiclibrary.service.cache.Cache;
import strategiclibrary.service.cache.CacheService;

import com.lazerinc.application.Product;
import com.lazerinc.category.ProductField;
import com.lazerinc.category.ProtectedField;
import com.lazerinc.utils.Resource;

public class ProductFieldAdd extends AbstractCacheFunction<ProductField> {

	ResourceAdd resAdder = new ResourceAdd();

	@Override
	protected Class<ProductField> getRelevantType() {
		return ProductField.class;
	}

	@Override
	protected Object[] getSearchPath(ProductField obj) {
		return new Object[] { "family", obj.getFamily(), "name", obj.getName() };
	}

	@Override
	protected boolean isValidObj(ProductField obj) {
		return dbUtil.getProductFamily(obj.getFamily()) != null
				&& obj.getName() != null && obj.getName().length() > 0;
	}

	public void add(ProductField obj) {
		if (!isValidObj(obj))
			throw new RuntimeException("InvalidField");
		obj.setId(getId(obj.getFamily() + "fields"));
		if (obj instanceof ProtectedField) {
			addAllResources(obj);
		}

	}

	@Override
	protected Cache<ProductField> getCache(CacheService cacheService,
			ProductField i) {
		return cacheService.getCache(i.getFamily() + "fields",
				getRelevantType());
	}

	public void addAllResources(ProductField obj) {
		Set<String> values = new HashSet<String>();
		for (Product prod : dbUtil.getProductFamily(obj.getFamily())
				.getProducts(dbUtil.getAdmin())) {
			values.add((String) prod.getValue(obj.getName()));
		}
		for (String value : values) {
			if (value != null && value.length() > 0 && !value.equals(DEFAULT))
				resAdder
						.addOrGet(new Resource(obj.getFamily() + "."
								+ obj.getName() + "." + value,
								Resource.PROTECTED_FIELD));
		}
	}

	private void removeAllResources(ProductField obj) {
		Collection<Resource> r = new LinkedList<Resource>(cache.getCache(
				Resource.class).getCachedObjects("type",
				Resource.PROTECTED_FIELD));
		for (Resource res : r) {
			getLog().info("Looking at resource " + res.getName());
			if (res.getName().startsWith(
					obj.getFamily() + "." + obj.getName() + "."))
				cache.getCache(Resource.class).deleteObject(res);
		}
	}

	public void update(ProductField obj) {
		if (obj.getType() == ProductField.PROTECTED) {
			addAllResources(obj);
			dbUtil.getProductFamily(obj.getFamily()).clearFieldCache();
		} else
			removeAllResources(obj);
	}

	@Override
	public ProductField addOrGet(ProductField i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			getLog().debug("looking for Resource : " + i);
			ProductField tradein = getCache(cache, i).getCachedObject(
					getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

	@Override
	public void delete(ProductField obj) {
		removeAllResources(obj);
	}

}
