package com.lazerinc.servlet.actionhandler.user;

import static com.lazerinc.servlet.ActionConstants.ACTION_VALIDATE_USER_INFO;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.FatalException;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

/**
 * Allows the information about the current user to be modified.
 * 
 * @author Administrator
 * @action action_modify_user
 * @requestParam none There are no specific request parameters, but any
 *               parameters available whose names match properties of the User
 *               object will be set in the user and saved to the database.
 * @bean UserBean user The users information is updated.
 */
public class ValidateUserInfo extends ActionHandlerBase {

	/**
	 * Modifies the current user from request parameters.
	 */
	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException {
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
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_VALIDATE_USER_INFO;
	}
}
