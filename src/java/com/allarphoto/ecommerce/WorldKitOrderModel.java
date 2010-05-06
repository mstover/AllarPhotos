package com.lazerinc.ecommerce;

public class WorldKitOrderModel extends LazerwebOrderModel {

	public WorldKitOrderModel() {
		super();
	}

	@Override
	protected Order createOrderObject(Merchant merchant) {
		this.log.info("Creating world kit order object");
		return new WorldKitOrder(merchant, user);
	}

}
