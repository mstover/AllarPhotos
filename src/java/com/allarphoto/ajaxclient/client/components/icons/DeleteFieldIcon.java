package com.lazerinc.ajaxclient.client.components.icons;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.PopupWarning;

public class DeleteFieldIcon extends BaseIcon {
	String fieldName;
	
	AjaxProductFamily family;

	HTMLTable table;

	int row;

	public DeleteFieldIcon() {
		// TODO Auto-generated constructor stub
	}

	public DeleteFieldIcon(String fieldName, AjaxProductFamily family, HTMLTable t,
			int row) {
		super();
		this.family = family;
		this.fieldName = fieldName;
		this.table = t;
		this.row = row;
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget w) {

				if (Window
						.confirm("Are you sure you want to delete the field?")) {
					BusyPopup.waitFor("Delete Field");
					Services.getServices().libraryInfoService.deleteField(
							fieldName, family.getFamilyName(), new AsyncCallback() {

								public void onFailure(Throwable caught) {
									BusyPopup.done("Delete Field");
									new PopupWarning("Failed to delete Field");

								}

								public void onSuccess(Object result) {
									BusyPopup.done("Delete Field");
									table.getRowFormatter().setStylePrimaryName(row,
											"deleted");
									family.deleteField(fieldName);
								}

							});
				}
			}
		};
	}

	public String getIconUrl() {
		return "cancel.gif";
	}

	public String getToolTip() {
		return "Delete category from the Library";
	}

}
