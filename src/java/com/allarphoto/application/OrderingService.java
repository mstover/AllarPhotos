package com.lazerinc.application;

import java.util.Collection;
import java.util.Map;

import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.CostReport;
import com.lazerinc.ecommerce.CreditCard;
import com.lazerinc.ecommerce.OrderResponse;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public interface OrderingService extends SecureComponent {

	/***************************************************************************
	 * Calculates the estimated cost for this entire order. This assumes the
	 * collection is filled with CartObject objects.
	 * 
	 * @param products
	 *            Collection of CartObject's
	 * @param instructions
	 *            Set of global instructions for order.
	 * @return cost of all products in a CostReport object.
	 **************************************************************************/
	public CostReport getCost(Collection<CartObject> products,
			Map<String, Object> instructions);

	/***************************************************************************
	 * Executes the order. If the merchant requires a manually processed order,
	 * then the order is simply emailed and logged for future retrieval and
	 * modification.
	 * 
	 * @param command
	 *            The command to be executed - eg "order","download", etc.
	 * @param instructions
	 *            Set of global instructions for order.
	 * @return An OrderResponse object to communicate the results.
	 **************************************************************************/
	public OrderResponse execute(Collection<CartObject> products,
			Map<String, Object> instructions);

	/***************************************************************************
	 * Sets the credit card to be used to process this request.
	 * 
	 * @param cc
	 *            CreditCard object.
	 **************************************************************************/
	public void setCreditCard(CreditCard cc);

	/***************************************************************************
	 * Sets the user doing the order.
	 * 
	 * @param user
	 *            CommerceUser object.
	 **************************************************************************/
	public void setUser(CommerceUser user);
}