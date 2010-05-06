package com.lazerinc.ajaxclient.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lazerinc.ajaxclient.client.beans.AjaxAddress;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.AjaxOrder;
import com.lazerinc.ajaxclient.client.beans.AjaxOrderResponse;

public interface OrderService extends RemoteService {

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.OrderVerificationPackage>
	 * @param cart
	 * @return
	 */
	public List getOrderVerificationPackages(AjaxCart cart);

	/**
	 * 
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxOrderResponse>
	 * @gwt.typeArgs orders
	 *               <com.lazerinc.ajaxclient.client.beans.OrderVerificationPackage>
	 * @param orders
	 * @param aa
	 * @return
	 */
	public AjaxOrderResponse executeOrder(List orders, AjaxAddress aa);

	public String getDownloadDir();

	/**
	 * @gwt.typeArgs families <java.lang.String>
	 * @param emailAddress
	 * @param message
	 * @param families
	 * @param links
	 * @return
	 */
	public boolean sendLinkEmail(String emailAddress, String message,
			List families, String[] links);

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxOrder>
	 * @return
	 */
	public List getOrders(String status, String fromDate);

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxOrder>
	 * @return
	 */
	public List getLibraryOrders(String library, String status, String fromDate);

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxOrder>
	 * @return
	 */
	public List getUserOrders(String username, String fromDate);

	public AjaxOrder getOrder(String orderNo);

	public AjaxOrder approveOrder(AjaxOrder o);

	public AjaxOrder confirmOrder(AjaxOrder o);

	public AjaxOrder rejectOrder(AjaxOrder o);

	public AjaxOrder fulfillOrder(AjaxOrder o);

	public AjaxOrder cancelOrder(AjaxOrder o);

	public boolean saveShoppingCart(AjaxCart cart);

	public AjaxCart getShoppingCart();
}
