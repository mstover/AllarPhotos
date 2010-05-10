package com.allarphoto.servlet.actionhandler.login;

import static com.allarphoto.servlet.ActionConstants.ACTION_PASSWORD_REQUEST;
import static com.allarphoto.servlet.RequestConstants.REQUEST_EMAIL;

import java.util.Collection;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.utils.Functions;

/**
 * This will cause the password for the requested username be sent to the user's
 * email account.
 * 
 * @author Administrator
 * @action action_password_request
 * @requestParam request_username The username of the user requesting the
 *               password
 * @requestParam request_email The email address of the user who lost their
 *               password. The password will only be sent if the email address
 *               given matches the email address already on record for the user.
 * @bean StringBean passwordMessage A message to indicate whether the password
 *       was sent through email, or, if not, why it was not sent.
 * @bean StringBean passwordSuccess A message to indicate whether the retrieval
 *       of the lost password was successful.
 */
@CoinjemaObject(type = "action")
public class PasswordRequest extends Login {
	NotificationService notifier;

	/**
	 * @see java.lang.Object#Object()
	 */
	public PasswordRequest() {
	}

	@CoinjemaDependency(method = "emailService")
	public void setNotifier(NotificationService s) {
		notifier = s;
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#requiresLogin()
	 */
	public boolean requiresLogin() {
		return false;
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws ActionException {
		String username = actionInfo.getParameter("username");
		CommerceUser user = getUgd().getUser(username);
		boolean sent = false;
		if (user == null) {
			user = getUgd().getUserByEmail(
					actionInfo.getParameter(REQUEST_EMAIL));
		} 
		if(user != null) sendPassword(actionInfo, user);
		else throw new LazerwebException(LazerwebException.NO_SUCH_USER);
	}

	private boolean sendPassword(HandlerData actionInfo, CommerceUser u) throws ActionException, InformationalException,
			LazerwebException {
		if (!u.getPassword().equals(dbUtil.DEFAULT)) {
			if (emailInfo(u)) {
					addMessage(
							"An email containing your password has been sent.",
							null, actionInfo);
				return true;
			} else {
				throw new InformationalException(
						LazerwebException.EMAIL_FAILURE);
			}
		} else {
			throw new LazerwebException(LazerwebException.INCOMPLETE_USER_INFO);
		}
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_PASSWORD_REQUEST;
	}

	protected boolean emailInfo(CommerceUser user) {
		boolean retVal = false;
		String nl = System.getProperty("line.separator");
		StringBuffer message = new StringBuffer();
		message.append("This message was sent by the Online Library" + nl + nl);

		String username = user.getUsername();
		message.append("Your username is " + username + nl);

		String password = user.getPassword();
		message.append("Your password is " + password);
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					"webmaster@lazerinc.com", "Reply from Online Library",
					"text/plain", message.toString());
			retVal = true;
		} catch (Exception e) {
			retVal = false;
			getLog().error("Failed to send email", e);
		}
		return retVal;
	}

}