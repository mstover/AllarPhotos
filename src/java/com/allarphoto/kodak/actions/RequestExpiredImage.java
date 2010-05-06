package com.lazerinc.kodak.actions;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.servlet.actionhandler.message.LazerwebFeedback;

public class RequestExpiredImage extends LazerwebFeedback {

	@Override
	public String getName() {
		return "request_expired_image";
	}

	@Override
	public void performAction(HandlerData actionInfo)
			throws InformationalException, ActionException {
		StringWriter msg = new StringWriter();
		Map values = new HashMap();
		values.put("request", actionInfo);
		values.put("config", getController().getProperties());
		values.put("product", dbUtil.getProductFamily(
				actionInfo.getParameter("request_product_family")).getProduct(
				actionInfo.getParameter("request_product_id"),
				getCurrentUserPerms(actionInfo)));
		getTemplateService().mergeTemplate("request_expired_image.vtl", values,
				msg);
		getNotifier().sendMessage(getExpiredRequestRecipients(),
				"Request for Expired Image Usage", "text/html", msg.toString());
		addMessage("Your request for the expired image has been sent", values
				.get("product"), actionInfo);
	}

	@CoinjemaDynamic(method = "expired_request_recipients")
	public String[] getExpiredRequestRecipients() {
		return new String[] { "tomc@lazerinc.com", "kens@lazerinc.com",
				"cynthia.rohnke@kodak.com" };
	}

}
