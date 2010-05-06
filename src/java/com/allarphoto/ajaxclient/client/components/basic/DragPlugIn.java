package com.lazerinc.ajaxclient.client.components.basic;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public interface DragPlugIn {

	public void onMove(Widget sender, Element e, int x, int y);

	public boolean isInstanceof(Widget w);

	public void onDrop(Widget w, Element e, int x, int y);

}
