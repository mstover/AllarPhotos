package com.lazerinc.ajaxclient.client.components.basic;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class SVGDragPlugIn implements DragPlugIn {

	public boolean isInstanceof(Widget w) {
		return false;//(w instanceof SVGWidget);
	}

	public void onDrop(Widget w, Element e, int x, int y) {
	}

	public void onMove(Widget sender, Element grabbed, int x, int y) {
		//ExtDOM.setAttributeNS(grabbed, "x", x + "px");
		//ExtDOM.setAttributeNS(grabbed, "y", y + "px");

	}

}
