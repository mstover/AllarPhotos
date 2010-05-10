package com.allarphoto.client.util;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.LogModel;
import com.allarphoto.beans.LogItem;

@CoinjemaObject
public class LogUtil {
	LogModel logger;

	Logger log;

	public LogUtil() {
	}

	public void logAction(String[] params) {
		try {
			LogItem item = new LogItem();
			for (int x = 0; x + 1 < params.length; x += 2) {
				item.setValue(params[x], params[x + 1]);
			}
			logger.addLogItem(item);
		} catch (Exception e) {
			log.debug("Couldn't log to database");
		}
	}

	public void logItem(LogItem item) {
		try {
			logger.addLogItem(item);
		} catch (Exception e) {
			log.debug("Couldn't log to database");
		}
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setLogger(LogModel logger) {
		this.logger = logger;
	}
}