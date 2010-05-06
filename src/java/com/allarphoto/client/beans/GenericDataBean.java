package com.lazerinc.client.beans;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;

import com.lazerinc.beans.City;
import com.lazerinc.beans.Country;
import com.lazerinc.beans.Industry;
import com.lazerinc.beans.Referrer;
import com.lazerinc.beans.State;

/**
 * @author Administrator To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates.
 */
@CoinjemaObject
public class GenericDataBean implements Serializable {
	private static final long serialVersionUID = 1;

	private transient CacheService cache;

	/**
	 * @see com.lazerinc.client.beans.ResponseBean#clear()
	 */
	public void clear() {
	}

	@CoinjemaDependency(type = "cacheService", method = "cacheService")
	public void setDataSource(CacheService c) {
		this.cache = c;
	}

	public Collection<State> getStates() {
		return cache.getCache(State.class).getCachedList();
	}

	public Collection<Country> getCountries() {
		return cache.getCache(Country.class).getCachedList();
	}

	public Collection<City> getCities() {
		return cache.getCache(City.class).getCachedList();
	}

	public Collection<Industry> getIndustries() {
		return cache.getCache(Industry.class).getCachedList();
	}

	public Collection<Referrer> getReferrers() {
		return cache.getCache(Referrer.class).getCachedList();
	}

	private void readObject(ObjectInputStream in) {
		try {
			in.defaultReadObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
