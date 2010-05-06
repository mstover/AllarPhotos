package com.lazerinc.servlet.actionhandler;

import org.apache.velocity.tools.generic.MathTool;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.beans.HtmlHelper;
import strategiclibrary.util.ConverterInstance;

import com.lazerinc.client.beans.DateBean;
import com.lazerinc.client.beans.GenericDataBean;
import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.utils.Counter;

public class Setup extends ActionHandlerBase {

	public Setup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		// getController().setProperty("baseDir",((ServletContext)((ServletHandlerData)actionInfo).getServletContext()).getRealPath(""));
		actionInfo.setAppBean("DB_EQ", DBConnect.EQ);
		actionInfo.setAppBean("DB_IS", DBConnect.IS);
		actionInfo.setAppBean("DB_GTEQ", DBConnect.GTEQ);
		actionInfo.setAppBean("math", new MathTool());
		actionInfo.setAppBean("format", new ConverterInstance());
		actionInfo.setAppBean("ugd", getUgd());
		actionInfo.setAppBean("divCounter", new Counter());
		actionInfo.setAppBean("dateTool", new DateBean());
		actionInfo.setAppBean("htmlHelp", new HtmlHelper());
		actionInfo.setAppBean("dbUtil", dbUtil);
		GenericDataBean dataBean = new GenericDataBean();
		actionInfo.setAppBean("data", dataBean);
	}

	public String getName() {
		return "init";
	}

}
