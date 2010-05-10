package com.allarphoto.ajaxclient.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.ajaxclient.client.AddressInfo;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.beans.Address;
import com.allarphoto.beans.City;
import com.allarphoto.beans.Company;
import com.allarphoto.beans.Country;
import com.allarphoto.beans.Industry;
import com.allarphoto.beans.OrderItem;
import com.allarphoto.beans.State;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.CommerceUser;

public class AddressInfoImpl extends AbstractGwtServlet implements AddressInfo {
	private static final long serialVersionUID = 1;

	ObjectMappingService objectMapping;

	CacheService cache;

	public AddressInfoImpl() {
	}

	public AjaxAddress[] getMyAddresses() {
		try {
			HandlerData info = getThreadLocalHandlerData();
			UserBean ub = (UserBean) info.getBean("user");
			CommerceUser user = ub.getUser();
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("category", "user");
			values.put("value", user.getUsername());
			Collection<OrderItem> items = objectMapping.getObjects(
					"getOrderItems.sql", values);
			Set<Integer> addressIds = new HashSet<Integer>();
			for (OrderItem item : items) {
				String address = item.getValue("shipping_address");
				if (address != null) {
					try {
						Integer id = Integer.parseInt(address);
						if (id > 0)
							addressIds.add(id);
					} catch (NumberFormatException e) {
					}
				}
			}
			getLog().info("Address ids found = " + addressIds);
			ArrayList<AjaxAddress> aa = new ArrayList<AjaxAddress>();
			for (int id : addressIds) {
				Address a = cache.getCache(Address.class).getCachedObject("id",
						id);
				if (a != null) {
					AjaxAddress addr = createAjaxAddress(a);
					aa.add(addr);
				}
			}
			AjaxAddress userAddr = createAjaxAddress(user.getShippingAddress());
			if (!aa.contains(userAddr))
				aa.add(0, userAddr);
			return aa.toArray(new AjaxAddress[aa.size()]);
		} catch (Exception e) {
			getLog().warn("Failed to load addresses", e);
			return new AjaxAddress[0];
		}
	}

	public String[] getCountries() {
		try {
			Collection<Country> countries = cache.getCache(Country.class)
					.getCachedList();
			String[] ar = new String[countries.size()];
			int count = 0;
			for (Country s : countries) {
				ar[count++] = s.getName();
			}
			return ar;
		} catch (Exception e) {
			getLog().warn("Failed to get country list", e);
			return new String[0];
		}
	}

	public String[] getStates() {
		try {
			Collection<State> states = cache.getCache(State.class)
					.getCachedList();
			String[] ar = new String[states.size()];
			int count = 0;
			for (State s : states) {
				ar[count++] = s.getName();
			}
			return ar;
		} catch (Exception e) {
			getLog().warn("Failed to get state list", e);
			return new String[0];
		}
	}

	public boolean updateAddress(AjaxAddress a) {
		try {
			Address existing = cache.getCache(Address.class).getCachedObject(
					"id", a.getId());
			existing.setAddress1(a.getAddress1());
			existing.setAddress2(a.getAddress2());
			existing.setCity(new City(a.getCity()));
			existing.setState(new State(a.getState()));
			existing.setZip(a.getZip());
			existing.setCountry(new Country(a.getCountry()));
			existing.setPhone(a.getPhone());
			existing.setAttn(a.getAttn());
			existing.setId(a.getId());
			existing.setInUse(a.isInUse());
			getLog().info("company = " + a.getCompany());
			getLog().info("industry = " + a.getCompany().getIndustry());
			Company c = new Company(a.getCompany().getName());
			c.setIndustry(new Industry(a.getCompany().getIndustry()));
			existing.setCompany(c);
			cache.getCache(Address.class).updateItem(existing);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@CoinjemaDependency(type = "cacheService")
	public void setCache(CacheService c) {
		cache = c;
	}

	@CoinjemaDependency(type = "objectMappingService")
	public void setObjectMapper(ObjectMappingService oms) {
		objectMapping = oms;
	}

}
