package com.allarphoto.cached.functions;

import com.allarphoto.beans.Company;

public class CompanyAdd extends AbstractCacheFunction<Company> implements
		CacheAdder<Company> {
	@Override
	public Company addOrGet(Company i) {
		if (!isValidObj(i))
			return getCache(cache, i).getCachedObject(getDefaultPath());
		if (i.getId() > 0)
			return i;
		else {
			subobjects(i);
			Company tradein = getCache(cache, i).getCachedObject(
					getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

	IndustryAdd industryAdder = new IndustryAdd();

	public void add(Company c) {
		subobjects(c);
		c.setId(getId("companies"));
	}

	private void subobjects(Company c) {
		c.setIndustry(industryAdder.addOrGet(c.getIndustry()));
	}

	@Override
	protected Class<Company> getRelevantType() {
		return Company.class;
	}

	@Override
	protected Object[] getSearchPath(Company obj) {
		return new Object[] { "name", obj.getName() };
	}

	@Override
	protected boolean isValidObj(Company obj) {
		return obj != null && (obj.getId() > 0 || obj.getName() != null);
	}

}
