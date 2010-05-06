package com.lazerinc.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

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