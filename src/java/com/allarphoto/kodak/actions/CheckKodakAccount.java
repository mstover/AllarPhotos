package com.allarphoto.kodak.actions;

import java.util.HashSet;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class CheckKodakAccount extends ActionHandlerBase {

	public String getName() {
		return "check_kodak_user";
	}

	public void performAction(HandlerData info) throws ActionException {
		Set groupSet = new HashSet(this.getUserBean(info).getGroups());
		getLog().info("Groups belonged to = " + groupSet);
		getLog().info("compare to = " + getUgd().getGroup("Kodak_DAI_Dept"));
		if (groupSet.contains(getUgd().getGroup("Kodak_DAI_Dept"))
				|| groupSet.contains(getUgd().getGroup("Kodak_Sec_Dept"))
				|| groupSet.contains(getUgd().getGroup("Kodak_Pro_Dept"))
				|| groupSet.contains(getUgd().getGroup("Kodak_SecPro_Dept"))
				|| groupSet.contains(getUgd().getGroup("Kodak_ConPro_Dept"))
				|| groupSet.contains(getUgd().getGroup("Kodak_Ink_Dept"))) {
			throw new LazerwebException("GeneralAccountWarning");
		}
	}

}
