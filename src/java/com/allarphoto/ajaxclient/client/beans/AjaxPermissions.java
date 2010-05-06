package com.lazerinc.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxPermissions implements IsSerializable {

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxResource,com.lazerinc.ajaxclient.client.beans.AjaxRights>
	 */
	Map perms = new HashMap();

	public void addRight(AjaxResource r, AjaxRights rights) {
		if (rights.hasAnyRight())
			perms.put(r, rights);
	}

	public AjaxResource[] getResources(int type) {
		List resources = new ArrayList();
		Iterator iter = perms.keySet().iterator();
		while (iter.hasNext()) {
			AjaxResource r = (AjaxResource) iter.next();
			if (r.getType() == type)
				resources.add(r);
		}
		AjaxResource[] o = new AjaxResource[resources.size()];
		iter = resources.iterator();
		int count = 0;
		while (iter.hasNext()) {
			o[count++] = (AjaxResource) iter.next();
		}
		Arrays.sort(o);
		return o;
	}
	
	public boolean isLibraryAdmin()
	{
		if(hasPermission(new AjaxResource("all",
				AjaxResource.DATABASE),"admin")) return true;
		AjaxResource[] res = getResources(AjaxResource.DATATABLE);
		if(res == null) return false;
		for(int i = 0;i < res.length;i++)
		{
			if(hasPermission(res[i],"admin")) return true;
		}
		return false;
	}

	public AjaxRights getRights(AjaxResource r) {
		return (AjaxRights) perms.get(r);
	}

	public boolean hasPermission(AjaxResource r, String right) {
		AjaxRights all = (AjaxRights) perms.get(new AjaxResource("all",
				AjaxResource.DATABASE));
		if (all != null && all.isAdmin())
			return true;
		AjaxRights rights = (AjaxRights) perms.get(r);
		if (rights == null)
			return false;
		else {
			if (right.equals("admin"))
				return rights.isAdmin();
			else
				return false;
		}

	}

	public boolean hasAnyRight(AjaxResource res) {
		AjaxRights rights = (AjaxRights) perms.get(res);
		return rights != null && rights.hasAnyRight();
	}

	public void updateRight(String right, AjaxResource res, boolean updateValue) {
		if (getRights(res) == null)
			addRight(res, new AjaxRights());
		if (right.equals("admin"))
			getRights(res).setAdmin(updateValue);
		else if (right.equals("read"))
			getRights(res).setRead(updateValue);
		else if (right.equals("order"))
			getRights(res).setOrder(updateValue);
		else if (right.equals("download"))
			getRights(res).setDownload(updateValue);
		else if (right.equals("download_orig"))
			getRights(res).setDownload_orig(updateValue);
		else if (right.equals("upload"))
			getRights(res).setUpload(updateValue);
		if (!getRights(res).hasAnyRight())
			perms.remove(res);
	}

}
