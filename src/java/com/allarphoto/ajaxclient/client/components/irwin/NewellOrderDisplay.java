package com.lazerinc.ajaxclient.client.components.irwin;

import com.lazerinc.ajaxclient.client.UpdateListener;
import com.lazerinc.ajaxclient.client.beans.AjaxOrder;
import com.lazerinc.ajaxclient.client.components.OrderDisplay;

public class NewellOrderDisplay extends OrderDisplay {

	public NewellOrderDisplay(String orderNo) {
		super(orderNo);
	}

	public NewellOrderDisplay(AjaxOrder o) {
		super(o);
	}

	public NewellOrderDisplay(AjaxOrder o, UpdateListener ul) {
		super(o, ul);
	}

	protected boolean filterInfo(String key) {
		return !key.equals("family") && !key.equals("Order.class") 
		&& !key.equals("action");
	}

}
