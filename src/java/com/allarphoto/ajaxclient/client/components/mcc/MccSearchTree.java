package com.allarphoto.ajaxclient.client.components.mcc;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.components.EnterKeyListener;
import com.allarphoto.ajaxclient.client.components.SearchTree;

public class MccSearchTree extends SearchTree {

	public MccSearchTree(SwappablePanel swap) {
		super(swap);
	}

	public MccSearchTree(SwappablePanel swap, String f, String[] is) {
		super(swap, f, is);
	}

	protected void init() {
		add(AjaxSystem.getLabel("Search..."));
		VerticalPanel hp = createFilterPanel();
		add(hp);
		add(createTree());
		setSpacing(15);
	}

	protected VerticalPanel createFilterPanel() {
		advancedOpen = true;
		VerticalPanel filterPanel = new VerticalPanel();
		sinceDate = new ListBox();
		keywordSearch = new TextBox();
		sinceModDate = new ListBox();
		exactSearch = new CheckBox();
		updateFilterPanel(filterPanel);
		setComponentTitles();
		return filterPanel;
	}

	protected void updateFilterPanel(final VerticalPanel fp) {
		fp.clear();
		fp.addStyleName("search-filters");
		sinceDate.clear();
		keywordSearch.setText("");
		exactSearch.setText("Exact");
		sinceModDate.clear();
		if (advancedOpen) {
			VerticalPanel vp = new VerticalPanel();
			vp.add(AjaxSystem.getLabel("Keyword Filter"));
			keywordSearch.addKeyboardListener(new EnterKeyListener() {

				protected void onEnterPress(Widget source) {
					doSearch(searchTree.getSelectedItem());
				}

			});
			sinceDate.addItem("Images Added Since:");
			sinceDate.addItem("Yesterday");
			sinceDate.addItem("Last Week");
			sinceDate.addItem("Last Month");
			sinceDate.addItem("Last 6 Months");
			sinceDate.addChangeListener(new ChangeListener() {

				public void onChange(Widget sender) {
					doSearch(searchTree.getSelectedItem());
				}

			});
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(keywordSearch);
			hp.add(exactSearch);
			vp.add(hp);
			fp.add(vp);
			fp.setSpacing(5);
			fp.add(sinceDate);
			exactSearch.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					doSearch(searchTree.getSelectedItem());
				}

			});

			sinceModDate.addItem("Images Modified Since:");
			sinceModDate.addItem("Yesterday");
			sinceModDate.addItem("Last Week");
			sinceModDate.addItem("Last Month");
			sinceModDate.addItem("Last 6 Months");
			sinceModDate.addChangeListener(new ChangeListener() {

				public void onChange(Widget sender) {
					doSearch(searchTree.getSelectedItem());
				}

			});
			fp.add(sinceModDate);
			sinceDate.setWidth("175px");
			sinceModDate.setWidth("175px");
		} 
	}

}
