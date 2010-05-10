package com.allarphoto.cached.functions;

import com.allarphoto.beans.State;

public class StateAdd extends AbstractCacheFunction<State> implements
		CacheAdder<State> {

	@Override
	protected boolean isValidObj(State i) {
		return i != null
				&& (i.getId() > 0 || i.getName() != null || i.getCode() != null);
	}

	@Override
	protected Class<State> getRelevantType() {
		return State.class;
	}

	@Override
	protected Object[] getSearchPath(State obj) {
		return null;
	}

	public void add(State obj) {
		obj.setId(getId("states"));

	}

	@Override
	public State addOrGet(State i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			State tradein = getCache(cache, i).getCachedObject(
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
