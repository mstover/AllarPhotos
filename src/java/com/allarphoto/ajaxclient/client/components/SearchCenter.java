package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Redrawable;
import com.allarphoto.ajaxclient.client.SwappablePanel;

public class SearchCenter extends VerticalPanel implements SwappablePanel,
		Redrawable {

	Widget swap;

	public SearchCenter(Widget s) {
		super();
		swap = s;
		init();
	}

	private void init() {
		add(new Toolbar(this));
		add(swap);
	}

	public void swapin(Widget w) {
		if (swap == null) {
			add(w);
		}
		if (swap == w)
			return;
		else {
			remove(swap);
			add(w);
		}
		swap = w;
	}

	public void redraw() {
		if (swap instanceof Redrawable) {
			((Redrawable) swap).redraw();
		}
	}

}
