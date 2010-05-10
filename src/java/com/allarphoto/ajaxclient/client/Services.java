package com.allarphoto.ajaxclient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxPermissions;
import com.allarphoto.ajaxclient.client.beans.AjaxUser;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.basic.Drag;
import com.allarphoto.ajaxclient.client.components.basic.ThumbnailDragger;

public class Services implements CartListener {
	public UserInformationAsync userInfoService;

	public LibraryInfoAsync libraryInfoService;

	public AddressInfoAsync addressInfo;

	public UploadServiceAsync uploadService;

	public EventServiceAsync eventService;

	public OrderServiceAsync orderService;

	public CustomServiceAsync customService;

	public AjaxUser user;

	public AjaxPermissions perms;

	public LazerMenu menu;

	public MainPanel mainPanel;

	public ComponentFactory factory;

	public String browser;

	public AjaxCart cart;

	public static FocusImpl focuser = (FocusImpl) GWT.create(FocusImpl.class);

	private static Services serviceList;

	private Services() {
		userInfoService = (UserInformationAsync) GWT
				.create(UserInformation.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) userInfoService;
		endpoint.setServiceEntryPoint("/lazerweb/userinfo");

		libraryInfoService = (LibraryInfoAsync) GWT.create(LibraryInfo.class);
		endpoint = (ServiceDefTarget) libraryInfoService;
		endpoint.setServiceEntryPoint("/lazerweb/libinfo");

		addressInfo = (AddressInfoAsync) GWT.create(AddressInfo.class);
		endpoint = (ServiceDefTarget) addressInfo;
		endpoint.setServiceEntryPoint("/lazerweb/addressinfo");

		uploadService = (UploadServiceAsync) GWT.create(UploadService.class);
		endpoint = (ServiceDefTarget) uploadService;
		endpoint.setServiceEntryPoint("/lazerweb/uploadService");

		eventService = (EventServiceAsync) GWT.create(EventService.class);
		endpoint = (ServiceDefTarget) eventService;
		endpoint.setServiceEntryPoint("/lazerweb/eventService");

		orderService = (OrderServiceAsync) GWT.create(OrderService.class);
		endpoint = (ServiceDefTarget) orderService;
		endpoint.setServiceEntryPoint("/lazerweb/orderService");

		customService = (CustomServiceAsync) GWT.create(CustomService.class);
		endpoint = (ServiceDefTarget) customService;
		endpoint.setServiceEntryPoint("/lazerweb/customService");
		Drag.dragger.addPlugin(new ThumbnailDragger());
		cart = new AjaxCart();
		cart.addListener(this);
	}

	static public Services getServices() {
		if (serviceList == null)
			serviceList = new Services();
		return serviceList;
	}

	public void added(Request p) {
		if (cart.getRequests().size() == 4)
			mainPanel.redraw();

	}

	public void cartCleared() {
		mainPanel.redraw();

	}

	public void redraw() {

	}

	public void removed(Request p) {

	}

	public void getShoppingCart() {
		orderService.getShoppingCart(new AsyncCallback() {

			public void onFailure(Throwable arg0) {

			}

			public void onSuccess(Object result) {
				cart.transfer((AjaxCart) result);

			}

		});
	}

	public void getUserPerms() {
		userInfoService.getPermissions(new AsyncCallback() {

			public void onFailure(Throwable arg0) {

			}

			public void onSuccess(Object result) {
				perms = (AjaxPermissions) result;

			}

		});
	}
}
