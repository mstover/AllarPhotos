package com.allarphoto.beans;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

import com.allarphoto.ecommerce.CommerceUser;

public class UploadItem extends DownloadItem {

	public void setUser(User user) {
		setValue("user", user.getUsername());
		if (user.getCompany() != null)
			setValue("Company", user.getCompany().getName());
	}

	public UploadItem() {
		setValue("action", "upload");
	}

}
