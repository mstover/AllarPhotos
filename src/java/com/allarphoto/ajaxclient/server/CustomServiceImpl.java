package com.allarphoto.ajaxclient.server;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.ContextFactory;

import com.allarphoto.ajaxclient.client.ComponentFactory;
import com.allarphoto.ajaxclient.client.CustomService;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.factory.DefaultComponentFactory;

public class CustomServiceImpl extends AbstractGwtServlet implements
		CustomService {
	private static final long serialVersionUID = 1;

	public CustomServiceImpl() {
	}

	public ComponentFactory getComponentFactory() {
		String host = (String) getThreadLocalHandlerData().getBean("host");
		if (host != null && host.indexOf(":") > -1)
			host = host.substring(0, host.indexOf(":"));
		ContextFactory.pushContext(new CoinjemaContext(host));
		try {
			return getComponentFactoryClass().newInstance();
		} catch (Exception e) {
			getLog().warn(
					"Failed to instantiate factory class: "
							+ getComponentFactoryClass(), e);
			return new DefaultComponentFactory();
		} finally {
			ContextFactory.popContext();
		}
	}

	public AjaxProductFamily[] getLibraryList() {
		String host = (String) getThreadLocalHandlerData().getBean("host");
		if (host != null && host.indexOf(":") > -1)
			host = host.substring(0, host.indexOf(":"));
		ContextFactory.pushContext(new CoinjemaContext(host));
		try {
			String[] libs = getLibraries();
			AjaxProductFamily[] fams = new AjaxProductFamily[libs.length];
			int count = 0;
			for (String lib : libs)
				fams[count++] = createAjaxProductFamily(dbutil
						.getProductFamily(lib));
			return fams;
		} catch (Exception e) {
			getLog().warn(
					"Failed to retrieve library list: "
							+ getComponentFactoryClass(), e);
			return new AjaxProductFamily[0];
		} finally {
			ContextFactory.popContext();
		}
	}

	@CoinjemaDynamic(type = "gwtComponentFactory")
	private Class<ComponentFactory> getComponentFactoryClass() {
		return null;
	}

	@CoinjemaDynamic(alias = "libraryList")
	private String[] getLibraries() {
		return null;
	}

}
