package com.lazerinc.servlet.actionhandler;

import java.util.Map;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.servlet.ActionConstants;
import com.lazerinc.servlet.RequestConstants;

public interface ActionHandler extends ActionConstants, RequestConstants {

	public void performAction(HandlerData actionInfo) throws LazerwebException;

	public String getActionName();

	public String getErrorPage(Map requestParameters);

	public boolean requiresLogin();
}