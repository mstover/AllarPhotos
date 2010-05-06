/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDynamic;

import com.lazerinc.application.Product;
import com.lazerinc.beans.OrderItem;

public class BaliOrderModel extends LazerwebOrderModel {

	private static Logger errLog = BaliOrderModel.getLogger();

	@CoinjemaDynamic(alias = "log4j")
	private static Logger getLogger() {
		return null;
	}

	public BaliOrderModel() {
	}

	@Override
	protected void addProductLevelDetails(OrderResponse response,
			Product product, OrderItem logItem) {
		// do nothing for Bali
	}

	@Override
	protected Order createOrderObject(Merchant merchant) {
		return new BaliOrder(merchant, user);
	}
}
