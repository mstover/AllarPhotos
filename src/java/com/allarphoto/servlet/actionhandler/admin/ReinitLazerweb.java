package com.allarphoto.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class ReinitLazerweb extends ActionHandlerBase {
	CacheService cache;

	public ReinitLazerweb() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException {
		SecurityModel permissions = this.getCurrentUserPerms(actionInfo);
		if (permissions.getPermission("all", Resource.DATABASE, Right.ADMIN)) {
			database.clearCache();
			cache.clearAll();
		} else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);

	}

	@CoinjemaDependency(type = "cacheService")
	public void setCache(CacheService cache) {
		this.cache = cache;
	}

	public String getName() {
		return "reinit";
	}

}