package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.Command;

public abstract class ParameterizedCommand implements Command, Cloneable {

	public String param;

	public abstract void execute();

}
