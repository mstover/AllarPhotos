package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;

public class Legend extends PopupPanel{
	VerticalPanel vp;

	public Legend() {
		super();
		vp = new VerticalPanel();
		init();
	}
	
	public void init()
	{
		vp.add(createLegend("order_product.gif","Order an original file on CD or special delivery"));
		vp.add(createLegend("jpg_download.gif","Request download of a JPG version of the image"));
		vp.add(createLegend("originals_download.gif","Request download of the original (possibly high-res) file"));
		if(Services.getServices().perms.isLibraryAdmin())
		{
			vp.add(createLegend("move_obsolete.gif","Move the asset to the obsolete section of the library"));
			vp.add(createLegend("move_offline.gif","Remove the asset from the active library"));
			vp.add(createLegend("move_active.gif","Move the asset to the active section of the library"));
			vp.add(createLegend("edit.gif","Edit the meta-data for an asset"));
		}
		vp.add(createLegend("cancel.gif","Cancel a download or order request or close a pop-up window"));
		vp.add(createLegend("checkout.gif","Execute order requests currently in your shopping cart"));
		vp.add(createLegend("cart-clear.gif","Cancel all requests currently in shopping cart"));
		vp.add(createLegend("cart-save.gif","Save your current shopping cart - it will be reloaded next time you login"));
		add(vp);
		vp.setSpacing(5);
		setPopupPosition(Window.getClientWidth() / 3,
				Window.getClientHeight() / 6);
		addStyleName("legend-popup");
	}
	
	protected  HorizontalPanel createLegend(String icon,String explanation)
	{
		HorizontalPanel hp = new HorizontalPanel();
		Image img = new Image(Services.getServices().factory.getIconFolder() + "/"
				+ icon);
		
		hp.add(img);
		hp.add(AjaxSystem.getText(explanation));
		hp.setSpacing(5);
		return hp;
	}
	
	protected Widget getCloseButton()
	{
		Image img = new Image(Services.getServices().factory.getIconFolder() + "/close.gif");
		img.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				hide();
				
			}
			
		});
		return img;
	}

}
