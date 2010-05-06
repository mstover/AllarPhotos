package com.lazerinc.servlet.actionhandler.message;

import static com.lazerinc.servlet.ActionConstants.ACTION_LAZERWEB_FEEDBACK;
import static com.lazerinc.servlet.RequestConstants.REQUEST_EMAIL;
import static com.lazerinc.servlet.RequestConstants.REQUEST_EMAIL_TO;
import static com.lazerinc.servlet.RequestConstants.REQUEST_FULL_NAME;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.servlet.actionhandler.login.Login;

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
public class LazerwebFeedback extends Login {
	NotificationService notifier;

	private static final String ls = System.getProperty("line.separator");

	/**
	 * @see java.lang.Object#Object()
	 */
	public LazerwebFeedback() {
	}

	/**
	 * @see java.lang.Object#Object()
	 */
	public LazerwebFeedback(CoinjemaContext cc) {
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#requiresLogin()
	 */
	public boolean requiresLogin() {
		return false;
	}

	protected NotificationService getNotifier() {
		return notifier;
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo)
			throws InformationalException, ActionException {
		String fullName = actionInfo.getParameter(REQUEST_FULL_NAME);
		String email = actionInfo.getParameter(REQUEST_EMAIL);
		String[] sendTo = actionInfo.getParameterValues(REQUEST_EMAIL_TO);
		String subject = actionInfo.getParameter("subject",
				"Online Library Feedback");
		StringWriter msg = new StringWriter();
		Map values = new HashMap();
		values.put("request", actionInfo);
		values.put("config", getController().getProperties());
		getTemplateService().mergeTemplate("site_feedback.vtl", values, msg);
		String message = msg.toString();
		boolean emailSuccess = false;
		if (message == null || message.equals("") || email == null
				|| email.equals("") || fullName == null || fullName.equals("")) {
			addMessage(
					"An error occurred in sending your comments.  Please complete all fields below.",
					null, actionInfo);
			throw new InformationalException(LazerwebException.INCOMPLETE_INFO);
		}
		try {
			List<String> recipients = new LinkedList<String>();
			for (String rec : sendTo) {
				String[] multRecs = rec.split(",");
				for (String mrec : multRecs) {
					recipients.add(mrec.trim());
				}
			}
			notifier.sendMessage((String[]) recipients
					.toArray(new String[recipients.size()]), email, subject,
					"text/plain", message);
			emailSuccess = true;
		} catch (Exception e) {
			emailSuccess = false;
		}
		if (emailSuccess) {
			addMessage("Your message has been sent.", null, actionInfo);
		} else {
			addMessage("There was an error sending your confirmation."
					+ "Please check your email address and try again.", null,
					actionInfo);
			throw new InformationalException(LazerwebException.EMAIL_FAILURE);
		}
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_LAZERWEB_FEEDBACK;
	}

	@CoinjemaDependency(method = "emailService")
	public void setEmailService(NotificationService s) {
		notifier = s;
	}

	@CoinjemaDynamic(type = "velocityService", method = "emailTemplateService")
	public TemplateService getTemplateService() {
		return null;
	}

}