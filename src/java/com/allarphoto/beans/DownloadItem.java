package com.allarphoto.beans;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.ecommerce.ProductSet;

public class DownloadItem extends LogItem {
	private static final long serialVersionUID = 1;

	CommerceUser user;

	Product product;

	public DownloadItem() {
		super();
	}

	public CommerceUser getUser() {
		if (user == null) {
			user = getCache().getCache(CommerceUser.class).getCachedObject(
					"username", getValue("user"));
		}
		return user;
	}

	public Product getProduct(SecurityModel security) {
		if (product == null) {
			CacheService cache = getCache();
			int pid = -1;
			ProductFamily family = cache.getCache(ProductFamily.class)
					.getCachedObject("descriptiveName", getValue("family"));
			if ((pid = Converter.getInt(getValue("product_id"), -1)) != -1) {
				product = family.getProduct(pid, security);
			} else {
				ProductSet set = family.findProductsWithPrimary(
						getValue("file"), 0, 0, security);
				if (set.size(family.getTableName()) == 1)
					product = set.getProductList(family.getTableName())[0];
			}
		}
		return product;
	}

	public long getSize() {
		return (long) Converter.getDouble(getValue("filesize"), 0D);
	}

	@CoinjemaDynamic(type = "cacheService")
	private CacheService getCache() {
		return null;
	}

}
