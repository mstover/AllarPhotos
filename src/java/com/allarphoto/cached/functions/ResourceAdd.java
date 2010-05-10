package com.allarphoto.cached.functions;

import com.allarphoto.beans.Industry;
import com.allarphoto.utils.Resource;

public class ResourceAdd extends AbstractCacheFunction<Resource> implements
		CacheAdder<Resource> {

	@Override
	protected Class<Resource> getRelevantType() {
		return Resource.class;
	}

	@Override
	protected Object[] getSearchPath(Resource obj) {
		return new Object[] { "name", obj.getName(), "type", obj.getType() };
	}

	@Override
	protected boolean isValidObj(Resource obj) {
		return obj != null
				&& (obj.getId() > 0 || (obj.getName() != null && obj.getType() > 0));
	}

	public void add(Resource obj) {
		obj.setId(getId("resources"));
	}

	@Override
	protected Object[] getDefaultPath() {
		return new Object[] { "name", "N/A", "type", 1 };
	}

	@Override
	public Resource addOrGet(Resource i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			getLog().debug("looking for Resource : " + i);
			Resource tradein = getCache(cache, i).getCachedObject(
					getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

}
