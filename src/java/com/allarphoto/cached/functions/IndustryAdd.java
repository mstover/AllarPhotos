package com.lazerinc.cached.functions;

import com.lazerinc.beans.Industry;

public class IndustryAdd extends AbstractCacheFunction<Industry> implements
		CacheAdder<Industry> {

	public void add(Industry ind) {
		ind.setId(getId("industries"));
	}

	@Override
	protected Class<Industry> getRelevantType() {
		return Industry.class;
	}

	protected Object[] getSearchPath(Industry i) {
		return new Object[] { "name", i.getName() };
	}

	protected boolean isValidObj(Industry i) {
		return i != null && (i.getId() > 0 || i.getName() != null);
	}

	@Override
	public Industry addOrGet(Industry i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			getLog().debug("looking for industry : " + i);
			Industry tradein = getCache(cache, i).getCachedObject(
					getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

}
