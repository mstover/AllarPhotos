package com.allarphoto.ajaxclient.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Command;

public class CommandFactory {

	Map commandMap = new HashMap();

	public Command get(String name) {
		return (Command) commandMap.get(name);
	}

	public CommandFactory add(String name, Command cmd) {
		commandMap.put(name, cmd);
		return this;
	}

	public String toString() {
		return commandMap.toString();
	}

}
