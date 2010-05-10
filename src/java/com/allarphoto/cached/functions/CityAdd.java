package com.allarphoto.cached.functions;

import com.allarphoto.beans.City;

public class CityAdd extends AbstractCacheFunction<City> implements
		CacheAdder<City> {

	@Override
	protected boolean isValidObj(City i) {
		return i != null && (i.getId() > 0 || i.getName() != null);
	}

	@Override
	protected Class<City> getRelevantType() {
		return City.class;
	}

	@Override
	protected Object[] getSearchPath(City i) {
		return new Object[] { "name", i.getName() };
	}

	public void add(City obj) {
		obj.setId(getId("cities"));
	}

	@Override
	public City addOrGet(City i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			City tradein = getCache(cache, i).getCachedObject(getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

}
