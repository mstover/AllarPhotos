package com.lazerinc.cached.functions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lazerinc.beans.Address;
import com.lazerinc.client.exceptions.LazerwebException;

public class AddressAdd extends AbstractCacheFunction<Address> implements
		CacheAdder<Address> {
	CityAdd cityAdder = new CityAdd();

	CompanyAdd companyAdder = new CompanyAdd();

	CountryAdd countryAdder = new CountryAdd();

	StateAdd stateAdder = new StateAdd();

	@Override
	protected boolean isValidObj(Address i) {
		return i != null
				&& (i.getId() > 0 || (i.getAddress1() != null && i.getCity() != null));
	}

	@Override
	protected Class<Address> getRelevantType() {
		return Address.class;
	}

	@Override
	protected Object[] getSearchPath(Address obj) {
		return null;
	}

	public void update(Address obj) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("address", obj);
		this.mapper.doUpdate("updateAddress.sql", values);
	}

	@Override
	public Address addOrGet(Address i) {
		if (!isValidObj(i))
			throw new RuntimeException(LazerwebException.INCOMPLETE_INFO);
		if (i.getId() > 0)
			return i;
		else {
			subObjects(i);
			Address tradein = null;
			Collection<Address> cached = getCache(cache, i).getCachedObjects(
					"address1", i.getAddress1());
			for (Address c : cached) {
				getLog().debug("evaluating to address " + c);
				if (i != null && i.equals(c)) {
					tradein = c;
					break;
				}
			}
			if (tradein == null) {
				getCache(cache, i).addItem(i);
				return i;
			}
			return tradein;
		}
	}

	public void add(Address addr) {
		subObjects(addr);
		addr.setId(getId("address"));
	}

	private void subObjects(Address addr) {
		addr.setCity(cityAdder.addOrGet(addr.getCity()));
		addr.setCompany(companyAdder.addOrGet(addr.getCompany()));
		addr.setState(stateAdder.addOrGet(addr.getState()));
		addr.setCountry(countryAdder.addOrGet(addr.getCountry()));
	}

}
