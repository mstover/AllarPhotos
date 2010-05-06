package com.lazerinc.ajaxclient.client.components.irwin;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.components.PopupSupportRequest;

public class IrwinPopupSupportRequest extends PopupSupportRequest {

	public IrwinPopupSupportRequest() {
		// TODO Auto-generated constructor stub
	}

	protected void addTimeframeInfo(VerticalPanel vp) {
		vp.add(AjaxSystem.getLabel("Regular business hours: 8:00AM - 5:00PM EST"));
	}

}
