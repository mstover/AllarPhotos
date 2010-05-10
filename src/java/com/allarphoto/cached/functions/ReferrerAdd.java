package com.allarphoto.cached.functions;

import com.allarphoto.beans.Referrer;

public class ReferrerAdd extends AbstractCacheFunction<Referrer> implements
		CacheAdder<Referrer> {

	@Override
	protected boolean isValidObj(Referrer i) {
		return i != null && (i.getId() > 0 || i.getName() != null);
	}

	@Override
	protected Class getRelevantType() {
		return Referrer.class;
	}

	@Override
	protected Object[] getSearchPath(Referrer i) {
		return new Object[] { "name", i.getName() };
	}

	public void add(Referrer obj) {
		obj.setId(getId("referrers"));

	}

}
