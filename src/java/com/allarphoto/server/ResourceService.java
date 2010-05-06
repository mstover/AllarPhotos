package com.lazerinc.server;

import java.util.Collection;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;

import com.lazerinc.utils.Resource;

@CoinjemaObject
public class ResourceService {

	CacheService resourceCache;

	@CoinjemaDependency(method = "resourceCache")
	public void setProductCache(CacheService cs) {
		resourceCache = cs;
	}

	public Resource getResource(String n, int t) {
		Resource rs = resourceCache.getCache(Resource.class).getCachedObject(
				new Object[] { "name", n, "type", t });
		return rs;
	}

	public Collection<Resource> getResources(int t) {
		return resourceCache.getCache(Resource.class).getCachedObjects("type",
				t);
	}

	public Resource getResource(int id) {
		return resourceCache.getCache(Resource.class).getCachedObject("id", id);
	}

	public Collection<Resource> getResources() {
		return resourceCache.getCache(Resource.class).getCachedList();
	}

	public void update(Resource res) {
		resourceCache.getCache(Resource.class).updateItem(res);
	}

}
