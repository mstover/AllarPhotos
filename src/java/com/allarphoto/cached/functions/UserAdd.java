package com.lazerinc.cached.functions;

import org.coinjema.context.CoinjemaDependency;

import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.ecommerce.UserGroup;

public class UserAdd extends AbstractCacheFunction<CommerceUser> implements
		CacheAdder<CommerceUser> {
	CompanyAdd companyAdder = new CompanyAdd();

	CityAdd cityAdder = new CityAdd();

	StateAdd stateAdder = new StateAdd();

	CountryAdd countryAdder = new CountryAdd();

	ReferrerAdd referrerAdder = new ReferrerAdd();

	DatabaseUtilities dbUtil;

	@Override
	public CommerceUser addOrGet(CommerceUser i) {
		if (!isValidObj(i))
			throw new RuntimeException(LazerwebException.INCOMPLETE_USER_INFO);
		if (i.getId() > 0)
			return i;
		else {
			CommerceUser tradein = getCache(cache, i).getCachedObject(
					getSearchPath(i));
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

	@Override
	protected boolean isValidObj(CommerceUser obj) {
		return obj != null
				&& (obj.getId() > 0 || (obj.getUsername() != null
						&& obj.getUsername().length() > 0
						&& obj.getEmailAddress() != null
						&& obj.getEmailAddress().length() > 0
						&& obj.getLastName() != null
						&& obj.getLastName().length() > 0
						&& obj.getFirstName() != null && obj.getFirstName()
						.length() > 0));
	}

	@Override
	protected Class<CommerceUser> getRelevantType() {
		return CommerceUser.class;
	}

	@Override
	protected Object[] getSearchPath(CommerceUser obj) {
		return new Object[] { "username", obj.getUsername() };
	}

	public void add(CommerceUser obj) {
		obj.setUserID(getId("users"));
		subObjects(obj);
		this.cache.clear(UserGroup.class);
	}

	private void subObjects(CommerceUser obj) {
		obj.setShipCity(cityAdder.addOrGet(obj.getShipCity()));
		obj.setBillCity(cityAdder.addOrGet(obj.getBillCity()));
		obj.setCity(cityAdder.addOrGet(obj.getBillCity()));
		obj.setShipState(stateAdder.addOrGet(obj.getShipState()));
		obj.setBillState(stateAdder.addOrGet(obj.getBillState()));
		obj.setState(stateAdder.addOrGet(obj.getBillState()));
		obj.setBillCountry(countryAdder.addOrGet(obj.getBillCountry()));
		obj.setShipCountry(countryAdder.addOrGet(obj.getShipCountry()));
		obj.setCompany(companyAdder.addOrGet(obj.getCompany()));
		obj.setReferrer(referrerAdder.addOrGet(obj.getReferrer()));
	}

	public void update(CommerceUser obj) {
		subObjects(obj);
		dbUtil.saveUserProperties(obj);
		this.cache.clear(UserGroup.class);
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

}
