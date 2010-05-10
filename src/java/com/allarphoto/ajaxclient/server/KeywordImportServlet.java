package com.allarphoto.ajaxclient.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.WebRequestHandlerContainer;

import com.allarphoto.servlet.AbstractContextServlet;

public class KeywordImportServlet extends AbstractContextServlet {

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		HandlerData info = getThreadLocalHandlerData();
		PrintWriter out = arg1.getWriter();
		try {
			getHandler().executeAction("keyword_file_import", info);
		} catch (Exception e) {
			getLog().error("Failed to upload keyword import", e);
		} finally {
			writeMessages(out, info);
			out.close();
		}

	}

	@CoinjemaDynamic(type = "webRequestService")
	private WebRequestHandlerContainer getHandler() {
		return null;
	}

}
