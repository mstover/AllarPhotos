package com.lazerinc.ajaxclient.server;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.Converter;

import com.lazerinc.ajaxclient.client.EventService;
import com.lazerinc.ajaxclient.client.beans.AjaxLogItem;
import com.lazerinc.beans.LogItem;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.utils.DatabaseLogger;

public class EventServiceImpl extends AbstractGwtServlet implements
		EventService {

	DatabaseLogger logger;

	ObjectMappingService mapper;

	public String[] getActions() {
		Collection<String> actions = (Collection<String>) mapper.getObjects(
				"getLogActions.sql", new HashMap());
		getLog().info("Actions = " + actions);
		return actions.toArray(new String[0]);
	}

	public AjaxLogItem[] getEvents(String action, String category,
			String value, String sinceDate, String toDate) {
		try {
			if (category == null || category.length() == 0)
				category = null;
			if (value == null || value.length() == 0) {
				value = null;
				category = null;
			}
			Collection<LogItem> items = logger.getLogItems(action, category,
					value, Converter.getCalendar(sinceDate, null), Converter
							.getCalendar(toDate, null));
			AjaxLogItem[] ajaxItems = new AjaxLogItem[items.size()];
			Collections.sort((List) items);
			int count = 0;
			for (LogItem item : items)
				ajaxItems[count++] = createAjaxLogItem(item);
			return ajaxItems;
		} catch (Exception e) {
			getLog().warn("Problem getting events ", e);
			return new AjaxLogItem[0];
		}
	}

	public String[] getCategories(String action, String sinceDate, String toDate) {
		try {
			Collection<LogItem> items = logger.getLogItems(action, null, null,
					Converter.getCalendar(sinceDate, null), Converter
							.getCalendar(toDate, null));
			Set<String> categories = new HashSet<String>();
			for (LogItem item : items) {
				categories.addAll(item.getItem().keySet());
			}
			getThreadLocalHandlerData().setUserBean("event_log_items",
					(Serializable) items);
			categories.remove("action");
			categories.remove(null);
			String[] cats = categories.toArray(new String[categories.size()]);
			Arrays.sort(cats);
			return cats;
		} catch (Exception e) {
			getThreadLocalHandlerData().removeUserBean("event_log_items");
			getLog().warn("Problem getting events ", e);
			return new String[0];
		}
	}

	public String[] getValues(String action, String category, String sinceDate,
			String toDate) {
		try {
			Collection<LogItem> items = (Collection<LogItem>) getThreadLocalHandlerData()
					.getUserBean("event_log_items");
			if (items == null)
				items = logger.getLogItems(action, null, null, Converter
						.getCalendar(sinceDate, null), Converter.getCalendar(
						toDate, null));
			Set<String> values = new HashSet<String>();
			for (LogItem item : items) {
				values.add(item.getValue(category));
			}
			values.remove(null);
			String[] val = values.toArray(new String[values.size()]);
			Arrays.sort(val);
			return val;
		} catch (Exception e) {
			getLog().warn("Problem getting events ", e);
			return new String[0];
		}
	}

	private AjaxLogItem createAjaxLogItem(LogItem item) {
		AjaxLogItem ajaxItem = new AjaxLogItem();
		ajaxItem.setDate(Converter.formatCalendar(item.getDateTime(),
				"M/d/yy (HH:mm:ss)"));
		ajaxItem.setValue("user", item.getValue("user"));
		for (String key : item.getItem().keySet())
			ajaxItem.setValue(key, item.getValue(key).toString());
		return ajaxItem;
	}

	@CoinjemaDependency(type = "objectMappingService")
	public void setMapper(ObjectMappingService mapper) {
		this.mapper = mapper;
	}

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setDatabaseLogger(DatabaseLogger lm) {
		logger = lm;
	}

}
