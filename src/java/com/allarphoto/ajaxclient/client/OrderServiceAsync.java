package com.allarphoto.ajaxclient.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxOrder;

public interface OrderServiceAsync {

	public void getOrderVerificationPackages(AjaxCart cart, AsyncCallback acb);

	public void executeOrder(List orders, AjaxAddress aa, AsyncCallback acb);

	public void getDownloadDir(AsyncCallback acb);

	public void getOrders(String status, String fromDate, AsyncCallback acb);

	public void getLibraryOrders(String library, String status,
			String fromDate, AsyncCallback acb);

	public void getUserOrders(String username, String fromDate,
			AsyncCallback acb);

	public void getOrder(String orderNo, AsyncCallback acb);

	public void approveOrder(AjaxOrder o, AsyncCallback acb);

	public void confirmOrder(AjaxOrder o, AsyncCallback acb);

	public void rejectOrder(AjaxOrder o, AsyncCallback acb);

	public void fulfillOrder(AjaxOrder o, AsyncCallback acb);

	public void cancelOrder(AjaxOrder o, AsyncCallback acb);

	public void saveShoppingCart(AjaxCart cart, AsyncCallback acb);

	public void sendLinkEmail(String emailAddress, String message,
			List families, String[] links, AsyncCallback acb);

	public void getShoppingCart(AsyncCallback acb);
}
