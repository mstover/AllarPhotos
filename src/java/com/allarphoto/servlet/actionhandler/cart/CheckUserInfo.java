package com.allarphoto.servlet.actionhandler.cart;

import static com.allarphoto.servlet.ActionConstants.ACTION_CHECK_USER_INFO;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

/**
 * This action handler simply verifies that the current user has all the data
 * necessary to complete an order. If not, an exception is generated and the jsp
 * page can specify an error page.
 * 
 * @author Administrator
 * @action action_check_user_info
 */
public class CheckUserInfo extends ActionHandlerBase {

	/**
	 * Verify that the user has a complete set of info necessary to order items.
	 */
	public void performAction(HandlerData actionInfo) throws ActionException {
		UserBean userBean = getUserBean(actionInfo);
		CommerceUser user = userBean.getUser();
		if (user.getBillAddress1() == null
				|| user.getBillAddress1().equals("")
				|| user.getBillAddress1().equals("N/A")
				|| user.getBillCity() == null
				|| user.getBillCity().equals("")
				|| user.getBillCity().equals("N/A")
				|| ((user.getBillState() == null
						|| user.getBillState().equals("") || user
						.getBillState().equals("N/A")) && (user
						.getBillCountry().equals("United States") || user
						.getBillCountry().equals("CANADA")))
				|| user.getBillZip() == null || user.getBillZip().equals("")
				|| user.getBillZip().equals("N/A")
				|| user.getFirstName() == null
				|| user.getFirstName().equals("")
				|| user.getFirstName().equals("N/A")
				|| user.getLastName() == null || user.getLastName().equals("")
				|| user.getLastName().equals("N/A")
				|| user.getEmailAddress() == null
				|| user.getEmailAddress().indexOf("@") == -1
				|| user.getPhone() == null || user.getPhone().equals("")
				|| user.getPhone().equals("N/A")
		/*
		 * || user.getShipAddress1() == null ||
		 * user.getShipAddress1().equals("") ||
		 * user.getShipAddress1().equals("N/A") || user.getShipCity() == null ||
		 * user.getShipCity().equals("") || user.getShipCity().equals("N/A") ||
		 * ((user.getShipState() == null || user.getShipState().equals("") ||
		 * user.getShipState().equals("N/A")) &&
		 * (user.getShipCountry().equals("United States") ||
		 * user.getShipCountry().equals("CANADA"))) || user.getShipZip() == null ||
		 * user.getShipZip().equals("") || user.getShipZip().equals("N/A") ||
		 * user.getShipCountry() == null || user.getShipCountry().equals("") ||
		 * user.getShipCountry().equals("N/A")
		 */
		) {
			throw new InformationalException(
					LazerwebException.INCOMPLETE_USER_INFO);
		}
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_CHECK_USER_INFO;
	}
}
