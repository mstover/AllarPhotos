package com.allarphoto.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.context.ContextFactory;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.ActionMessage;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import strategiclibrary.service.webaction.WebRequestHandler;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.security.NoPermissions;

@CoinjemaObject(type = "servlet")
public abstract class AbstractContextServlet extends HttpServlet {
	private static final long serialVersionUID = 1;

	WebRequestHandler serviceHandler;

	Logger log;

	private ThreadLocal<HandlerData> handlerData = new ThreadLocal<HandlerData>() {

		@Override
		protected HandlerData initialValue() {
			return null;
		}

	};

	protected SecurityModel currentPerms() {
		try {
			return ((UserBean) getThreadLocalHandlerData().getBean("user"))
					.getPermissions();
		} catch (Exception e) {
			return new NoPermissions();
		}
	}

	protected void writeMessages(PrintWriter out, HandlerData info) {
		Collection messages = (Collection) info.getUserBean("messages");
		if (messages != null) {
			for (Object o : messages) {
				ActionMessage am = (ActionMessage) o;
				out.write(am.getMsg());
				out.write("\n");
			}
		}
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		ServletContext sContext = session.getServletContext();
		if (session.getAttribute("coinjemaContext") != null) {
			ContextFactory.pushContext((CoinjemaContext) session
					.getAttribute("coinjemaContext"));
		} else
			ContextFactory.pushContext(new CoinjemaContext("lazerweb"));
		try {
			HandlerData info = new ServletHandlerData(request, response,
					session, sContext);
			getLog().debug("Setting handler data: " + info);
			handlerData.set(info);
			serviceHandler.performAction(info);
			super.service(request, response);
		} catch (ActionException e) {
			throw new ServletException();
		} finally {
			ContextFactory.popContext();
		}
	}

	protected HandlerData getThreadLocalHandlerData() {
		return handlerData.get();
	}

	protected Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	@CoinjemaDependency(type = "webRequestService", method = "webRequestService")
	public void setWebRequestHandler(WebRequestHandler h) {
		this.serviceHandler = h;
	}

}
