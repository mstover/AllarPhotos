package com.allarphoto.application;

import com.allarphoto.ecommerce.Merchant;
import com.allarphoto.ecommerce.Order;

public interface Fulfillment extends SecureComponent {

	public boolean notifyFulfillment(Order order, Merchant merchant);

	public boolean notifyMerchant(Order order, Merchant merchant);

	public boolean sendApprovalRequest(Order order, Merchant merchant);

	public boolean sendConfirmationRequest(Order order, Merchant merchant);

	public boolean sendRejectNotification(Order order, Merchant merchant);

	public boolean sendCancellationNotification(Order order, Merchant merchant);

}
