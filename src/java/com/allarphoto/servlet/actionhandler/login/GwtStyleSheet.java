package com.allarphoto.servlet.actionhandler.login;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.ContextFactory;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class GwtStyleSheet extends ActionHandlerBase {

	public GwtStyleSheet() {
	}

	public String getName() {
		return "get_gwt_style";
	}

	public void performAction(HandlerData info) throws ActionException {
		String host = (String) info.getBean("host");
		if (host != null && host.indexOf(":") > -1)
			host = host.substring(0, host.indexOf(":"));
		ContextFactory.pushContext(new CoinjemaContext(host));
		try {
			info.setUserBean("gwtStylesheet", getStyleSheetName());
		} finally {
			ContextFactory.popContext();
		}

	}

	@CoinjemaDynamic(alias = "gwtStyleSheet")
	public String getStyleSheetName() {
		return null;
	}

}
