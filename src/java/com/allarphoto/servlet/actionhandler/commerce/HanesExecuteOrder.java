package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_HANES_EXECUTE_ORDER;

/**
 * After all the instructions have been set in the users shopping cart, this
 * action will cause the execution of the ordering instructions.
 * 
 * @author Administrator
 * @action action_execute_order
 * @requestParam ShipTo Adds information about who to ship the order to.
 * @requestParam SoldTo Adds information about who is being billed for the
 *               order.
 * @requestParam accountName Adds information about which account the order is
 *               for.
 * @requestParam instructions Adds special, custom instructions to the order
 * @requestParam dlOnly True or False indicating whether the order represents
 *               only downloading of files.
 * @bean ShoppingCartBean cart The information built up in the shopping cart is
 *       used to fulfill the order. Additional information may be set in the
 *       order. After execution, the shopping cart is cleared of all products
 *       that participated in the order.
 * @bean OrderResponseBean orderResponse An order response bean is created that
 *       holds all the information regarding the results of executing the order.
 */
public class HanesExecuteOrder extends BaliExecuteOrder {

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_HANES_EXECUTE_ORDER;
	}
}