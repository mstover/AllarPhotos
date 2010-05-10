package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class MoveToActiveIcon extends BaseIcon {

	public MoveToActiveIcon() {
		addStyleName("button-icon");
		init();
	}

	public MoveToActiveIcon(AjaxProduct p) {
		super(p);
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().libraryInfoService.moveAsset(product
						.getId(), product.getFamilyName(), getNewDir(),
						getCategory(), new AsyncCallback() {

							public void onFailure(Throwable arg0) {
								new PopupWarning("Failed to move asset");

							}

							public void onSuccess(Object arg0) {
								new PopupWarning("The asset will be moved to "
										+ getNewDir() + " shortly");

							}

						});
			}

		};
	}

	protected String getNewDir() {
		return "Active";
	}

	protected String getCategory() {
		return "Archive";
	}

	public String getIconUrl() {
		return "move_active.gif";
	}

	public String getToolTip() {
		return "Move Image to Active";
	}

}
