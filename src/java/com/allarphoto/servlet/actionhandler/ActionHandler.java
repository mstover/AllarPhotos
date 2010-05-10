package com.allarphoto.servlet.actionhandler;

import java.util.Map;

import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.servlet.ActionConstants;
import com.allarphoto.servlet.RequestConstants;

public interface ActionHandler extends ActionConstants, RequestConstants {

	public void performAction(HandlerData actionInfo) throws LazerwebException;

	public String getActionName();

	public String getErrorPage(Map requestParameters);

	public boolean requiresLogin();
}