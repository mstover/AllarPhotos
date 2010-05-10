package com.allarphoto.cached.functions;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.DataBase;
import strategiclibrary.service.cache.Cache;
import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.sql.ObjectMappingService;

import com.allarphoto.cached.DatabaseObject;
import com.allarphoto.ecommerce.DatabaseUtilities;

@CoinjemaObject(type = "cacheFunction")
public abstract class AbstractCacheFunction<T extends DatabaseObject>
		implements CacheAdder<T> {
	protected static final String DEFAULT = "N/A";

	protected CacheService cache;

	protected ObjectMappingService mapper;

	protected DataBase db;

	protected DatabaseUtilities dbUtil;

	private transient Logger log;

	@CoinjemaDependency(type = "cacheService", method = "cacheService")
	public void setCache(CacheService c) {
		cache = c;
	}

	@CoinjemaDependency(type = "objectMappingService", method = "objectMappingService")
	public void setMapper(ObjectMappingService m) {
		mapper = m;
	}

	protected DataBase getDb() {
		return db;
	}

	@CoinjemaDependency(type = "database", method = "database")
	public void setDb(DataBase db) {
		this.db = db;
	}

	protected int getId(String tablename) {
		Map values = new HashMap();
		values.put("table", tablename);
		return ((Number) mapper.getObjects("getNewId.sql", values).iterator()
				.next()).intValue();
	}

	protected Cache<T> getCache(CacheService cacheService, T i) {
		return cacheService.getCache(getRelevantType());
	}

	public T addOrGet(T i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			T tradein = getCache(cache, i).getCachedObject(getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

	public void update(T obj) {

	}

	public void add(T obj) {

	}

	public void delete(T obj) {

	}

	protected abstract boolean isValidObj(T obj);

	protected abstract Class<T> getRelevantType();

	protected Object[] getDefaultPath() {
		return new Object[] { "name", DEFAULT };
	}

	abstract protected Object[] getSearchPath(T obj);

	protected Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	@CoinjemaDependency(type = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

}
