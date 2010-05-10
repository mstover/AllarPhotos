package com.allarphoto.ajaxclient.client.components.basic;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.ThumbnailPanel;

public class ThumbnailDragger implements DragPlugIn {

	Image draggedImage;

	public boolean isInstanceof(Widget w) {
		return (w instanceof ThumbnailPanel);
	}

	public void onDrop(Widget w, Element e, int x, int y) {
		draggedImage.setVisible(false);
		draggedImage.removeFromParent();
		draggedImage = null;

	}

	public void onMove(Widget sender, Element e, int x, int y) {
		if (draggedImage == null) {
			draggedImage = new Image(((ThumbnailPanel) sender).getImageUrl());
			draggedImage.setWidth("36px");
			draggedImage.setHeight("36px");
			//ExtDOM.setStyleAttribute(draggedImage.getElement(), "position",
			//		"absolute");
			Services.getServices().mainPanel.getTopParent().add(draggedImage);
			draggedImage.setVisible(true);
		}

		//ExtDOM.setStyleAttribute(draggedImage.getElement(), "left", String
		//		.valueOf(x + sender.getAbsoluteLeft()));
		//ExtDOM.setStyleAttribute(draggedImage.getElement(), "top", String
		//		.valueOf(y + sender.getAbsoluteTop()));

	}

}
