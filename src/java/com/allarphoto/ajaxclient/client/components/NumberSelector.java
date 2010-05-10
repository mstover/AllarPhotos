package com.allarphoto.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;

public class NumberSelector extends VerticalPanel implements ClickListener {

	protected Grid cells;

	protected int numCells = 10;

	protected int start = 0, end = 100;

	protected ClickListener listener;

	protected List buttons = new ArrayList();

	protected int startingValue;

	protected int[] series;

	public NumberSelector() {
		this(10, 0, 100, null, 10);
	}

	public NumberSelector(int[] series, ClickListener cl, int startingValue) {
		super();
		this.startingValue = startingValue;
		this.series = series;
		listener = cl;
		start = series[0];
		end = series[series.length - 1];
		numCells = series.length;
		init();
	}

	public NumberSelector(int numCells, int start, int end, ClickListener cl,
			int startingValue) {
		super();
		this.startingValue = startingValue;
		listener = cl;
		this.numCells = numCells;
		this.start = start;
		this.end = end;
		cells = new Grid(1, numCells);
		cells.setCellPadding(0);
		cells.setCellSpacing(0);
		calcSeries();
		init();
	}

	protected void calcSeries() {
		series = new int[numCells];
		float incr = (float) (end - (start * 2))
				/ (float) ((numCells - 1) * (numCells - 1));
		for (int i = 0; i < numCells; i++) {
			if (i == 0)
				series[i] = start;
			else if (i == numCells - 1)
				series[i] = end;
			else
				series[i] = (int) ((i * i * incr) + (start * 2));
		}
	}

	protected void init() {
		cells = new Grid(1, numCells);
		cells.setCellPadding(0);
		cells.setCellSpacing(0);
		Label l = AjaxSystem.getLabel("# To Display", "title");
		l.setTitle("Select the number of search results to display on screen");
		add(l);
		add(cells);
		cells.setWidth("100%");
		cells.setCellPadding(0);
		cells.setCellSpacing(0);
		for (int i = 0; i < series.length; i++) {
			Button b = createButton(series[i]);
			if (series[i] == startingValue)
				b.addStyleName("clicked");
			cells.setWidget(0, i, b);
			cells.getCellFormatter().addStyleName(0, i,
					String.valueOf((char) (i + 'a')));
			b.setWidth("100%");
			b.addClickListener(listener);
			b.addClickListener(this);
			b.setTitle("Show " + b.getText() + " Images");
		}
	}

	protected Button createButton(int value) {
		Button b = new Button(String.valueOf(value));
		buttons.add(b);
		return b;
	}

	public void onClick(Widget sender) {
		Iterator iter = buttons.iterator();
		while (iter.hasNext()) {
			((Widget) iter.next()).removeStyleName("clicked");
		}
		sender.addStyleName("clicked");

	}
}
