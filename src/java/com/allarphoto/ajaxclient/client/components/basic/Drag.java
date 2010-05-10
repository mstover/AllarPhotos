package com.allarphoto.ajaxclient.client.components.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class Drag extends MouseListenerAdapter {

	final static public Drag dragger = new Drag();

	static {
		dragger.addPlugin(new SVGDragPlugIn());
	}

	Element grabbed;

	List plugins = new ArrayList();

	DragPlugIn current;

	boolean on = false;

	public void addPlugin(DragPlugIn dp) {
		plugins.add(dp);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		if (on && grabbed == null) {
			grabbed = sender.getElement();
			Iterator iter = plugins.iterator();
			while (iter.hasNext()) {
				DragPlugIn plug = (DragPlugIn) iter.next();
				if (plug.isInstanceof(sender))
					current = plug;
			}
			if (current != null) {
				// Window.alert("Current dragger is " + current);
				DOM.setCapture(grabbed);
			} else
				grabbed = null;
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (on && grabbed != null) {
			try {
				current.onMove(sender, grabbed, x, y);
			} catch (Exception e) {
				DOM.releaseCapture(grabbed);
				try {
					current.onDrop(sender, grabbed, x, y);
				} catch (Exception err) {
				}
				grabbed = null;
				current = null;
			}
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		if (on && grabbed != null) {
			DOM.releaseCapture(grabbed);
			current.onDrop(sender, grabbed, x, y);
			grabbed = null;
			current = null;
		}

	}

	public Drag() {
	}

}
