package com.allarphoto.ajaxclient.client.components.edu;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.components.NumberSelector;

public class EduNumberSelector extends NumberSelector {

	public EduNumberSelector() {
		// TODO Auto-generated constructor stub
	}

	public EduNumberSelector(int[] series, ClickListener cl, int sv) {
		super(series, cl, sv);
		// TODO Auto-generated constructor stub
	}

	public EduNumberSelector(int numCells, int start, int end,
			ClickListener cl, int sv) {
		super(numCells, start, end, cl, sv);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		HorizontalPanel hp = new HorizontalPanel();
		Label l = AjaxSystem.getLabel("# To Display", "title");
		l.setTitle("Select the number of search results to display on screen");
		add(l);
		cells = new Grid(1, numCells);
		cells.addStyleName("cells");
		cells.setCellPadding(0);
		cells.setCellSpacing(0);
		add(cells);
		cells.setWidth("100%");
		cells.setCellPadding(0);
		cells.setCellSpacing(0);
		for (int i = 0; i < series.length; i++) {
			Label b = (Label) createLink(series[i]);
			if (series[i] == startingValue)
				b.addStyleName("clicked");
			cells.setWidget(0, i, b);
			b.setWidth("100%");
			b.addClickListener(listener);
			b.addClickListener(this);
			b.setTitle("Show " + b.getText() + " Images");
		}
		hp.add(cells);
		add(hp);
	}

	protected Label createLink(int value) {
		Label b = new Label(String.valueOf(value));
		buttons.add(b);
		return b;
	}

}
