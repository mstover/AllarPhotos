package com.lazerinc.servlet.actionhandler.commerce;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class SendDownloadLink extends ActionHandlerBase {

	NotificationService emailer;

	public SendDownloadLink() {
	}

	public String getName() {
		return "send_download_link";
	}

	public void performAction(HandlerData info) throws ActionException {
		String emailAddress = info.getParameter("email_address");
		String message = info.getParameter("message");
		String[] downloadLinks = info.getParameterValues("download_link");
		String library = info.getParameter("library");
		StringBuffer mes = new StringBuffer("The following " + library
				+ " files have been made available for you to download:\n\n");
		for (String download : downloadLinks)
			mes.append(download).append("\n\n");
		mes.append(message);
		emailer.sendMessage(new String[] { emailAddress }, getUserBean(info)
				.getEmailAddress(), "Images Available from " + library,
				"text/plain", mes.toString());
		addMessage("Download Links Successfully emailed to " + emailAddress,
				library, info);
	}

	@CoinjemaDependency(type = "emailService")
	public void setEmailer(NotificationService emailer) {
		this.emailer = emailer;
	}

}
