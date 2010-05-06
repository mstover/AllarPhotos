package com.lazerinc.servlet.actionhandler.admin;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.DownloadItem;
import com.lazerinc.beans.User;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.DatabaseLogger;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class GetDownloadRequests extends ActionHandlerBase {
	protected DatabaseLogger dbLogger;

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setDbLogger(DatabaseLogger dl) {
		dbLogger = dl;
	}

	public GetDownloadRequests() {
		super();
	}

	public String getName() {
		return "get_download_requests";
	}

	public void performAction(HandlerData info) throws ActionException {
		ProductFamily family = dbUtil.getProductFamily(info.getParameter(
				"product_family", ""));
		SecurityModel perms = getCurrentUserPerms(info);
		if (!perms.getPermission(family.getTableName(), Resource.DATATABLE,
				Right.ADMIN))
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
		Collection<DownloadItem> items = dbLogger.getDownloadRequests(family,
				Converter.getCalendar(info.getParameter("since_date")),
				Converter.getCalendar(info.getParameter("to_date")));
		info.setRequestBean("downloadRequests", items);
		info.setRequestBean("totalSize", calcSize(items));
		info.setRequestBean("userMap", calcUserUsage(items));

	}

	private Map<User, Long> calcUserUsage(Collection<DownloadItem> items) {
		Map<User, Long> userMap = new TreeMap<User, Long>();
		for (DownloadItem item : items) {
			User u = item.getUser();
			if (u != null) {
				addUserTotal(userMap, item, u);
			} else {
				u = new CommerceUser();
				u.setUsername("Unknown User");
				u.setLastName("Unknown");
				u.setFirstName("User");
				addUserTotal(userMap, item, u);
			}
		}
		return userMap;
	}

	private void addUserTotal(Map<User, Long> userMap, DownloadItem item, User u) {
		if (userMap.containsKey(u)) {
			long size = userMap.get(u);
			size += item.getSize();
			userMap.put(u, size);
		} else {
			userMap.put(u, item.getSize());
		}
	}

	private double calcSize(Collection<DownloadItem> items) {
		double size = 0;
		for (DownloadItem item : items) {
			size += item.getSize();
		}
		return (size / 1024D / 1024D);
	}

}