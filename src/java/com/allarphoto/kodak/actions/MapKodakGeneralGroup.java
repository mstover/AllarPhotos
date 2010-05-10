package com.allarphoto.kodak.actions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class MapKodakGeneralGroup extends ActionHandlerBase {

	public String getName() {
		return "kdk_map_general_group";
	}

	public void performAction(HandlerData info) throws ActionException {
		Set groupSet = new HashSet(this.getUserBean(info).getGroups());
		List<UserGroup> groups = new LinkedList<UserGroup>();
		if (groupSet.contains(getUgd().getGroup("Kodak_DAI_Dept")))
			groups.add(getUgd().getGroup("Kodak_DAI_Users"));
		if (groupSet.contains(getUgd().getGroup("Kodak_Sec_Dept")))
			groups.add(getUgd().getGroup("Kodak_Sec_Users"));
		if (groupSet.contains(getUgd().getGroup("Kodak_Pro_Dept")))
			groups.add(getUgd().getGroup("Kodak_Professional"));
		if (groupSet.contains(getUgd().getGroup("Kodak_SecPro_Dept"))) {
			groups.add(getUgd().getGroup("Kodak_Professional"));
			groups.add(getUgd().getGroup("Kodak_Sec_Users"));
		}
		if (groupSet.contains(getUgd().getGroup("Kodak_ConPro_Dept"))) {
			groups.add(getUgd().getGroup("Kodak_Professional"));
			groups.add(getUgd().getGroup("Kodak_DAI_Users"));
		}
		if (groupSet.contains(getUgd().getGroup("Kodak_Ink_Dept")))
			groups.add(getUgd().getGroup("Kodak_Inkjet"));

		info.setRequestBean("groupList", groups);

	}

}
