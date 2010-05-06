package com.lazerinc.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class PageSelector extends HorizontalPanel {

	ClickListener listener;

	List labels;

	public PageSelector(int totalImages, int currentOffset, int pageSize,
			ClickListener listener) {
		super();
		this.listener = listener;
		labels = new ArrayList();
		init(totalImages, currentOffset, pageSize);
	}

	protected void init(int totalImages, int currentOffset, int pageSize) {
		currentOffset = currentOffset + pageSize - 1;
		addStyleName("page-selector");
		add(new Label("Pages"));
		int numPages = totalImages / pageSize
				+ Math.min(1, totalImages % pageSize);
		int currPage = currentOffset / pageSize
				+ Math.min(1, currentOffset % pageSize);
		int startingPage = 1;
		if (numPages > 5 && currPage > 3) {
			startingPage = currPage - 2;
			add(new Label("..."));
		}
		if ((currPage == numPages || currPage == numPages - 1) && numPages > 5)
			startingPage = numPages - 4;
		int endingPage = numPages;
		if (numPages > 5 && currPage < numPages - 2) {
			endingPage = currPage + 2;
		}
		if (currPage == 1 || currPage == 2)
			endingPage = Math.min(5, numPages);
		for (int i = startingPage; i <= endingPage; i++) {
			Label l = new Label(Integer.toString(i));
			labels.add(l);
			if (currPage == i)
				l.addStyleName("selected-page");
			add(l);
			l.addClickListener(listener);
		}
		if (numPages > 5 && currPage < numPages - 2) {
			add(new Label("..."));
		}
	}

	public void unselectAll() {
		Iterator iter = labels.iterator();
		while (iter.hasNext()) {
			((Label) iter.next()).removeStyleName("selected-page");
		}
	}

	public void update(int totalImages, int currentOffset, int pageSize) {
		clear();
		init(totalImages, currentOffset, pageSize);
	}

}
