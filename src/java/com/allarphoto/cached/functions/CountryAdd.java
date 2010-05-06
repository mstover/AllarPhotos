package com.lazerinc.cached.functions;

import com.lazerinc.beans.Country;

public class CountryAdd extends AbstractCacheFunction<Country> implements
		CacheAdder<Country> {

	@Override
	protected boolean isValidObj(Country i) {
		return i != null
				&& (i.getId() > 0 || i.getName() != null || i.getCode() != null);
	}

	@Override
	protected Class<Country> getRelevantType() {
		return Country.class;
	}

	@Override
	protected Object[] getSearchPath(Country obj) {
		return null;
	}

	public void add(Country obj) {
		obj.setId(getId("states"));

	}

	@Override
	public Country addOrGet(Country i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			Country tradein = getCache(cache, i).getCachedObject(
					new Object[] { "name", i.getName() });
			if (tradein == null) {
				tradein = getCache(cache, i).getCachedObject(
						new Object[] { "code", i.getCode() });
			}
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

}
