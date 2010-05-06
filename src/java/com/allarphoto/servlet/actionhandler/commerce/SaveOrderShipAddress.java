package com.lazerinc.servlet.actionhandler.commerce;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.beans.Address;
import com.lazerinc.cached.functions.AddressAdd;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.WebBean;

public class SaveOrderShipAddress extends ActionHandlerBase {

	ObjectMappingService mapper;

	AddressAdd addressAdder = new AddressAdd();

	@CoinjemaDependency(type = "objectMappingService")
	public void setMapper(ObjectMappingService m) {
		mapper = m;
	}

	public SaveOrderShipAddress() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		Address addr = WebBean.setValues(new Address(), actionInfo);
		getLog().debug("saving shipping address: " + addr);
		addr = addressAdder.addOrGet(addr);
		actionInfo.setUserBean("orderShipAddress", addr);
	}

	public String getName() {
		return "save_order_ship_address";
	}

}
