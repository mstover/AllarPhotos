package com.allarphoto.ajaxclient.client.components.irwin;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.components.EnterKeyListener;
import com.allarphoto.ajaxclient.client.components.SearchTree;

public class IrwinSearchTree extends SearchTree {

	public IrwinSearchTree(SwappablePanel swap) {
		super(swap);
		// TODO Auto-generated constructor stub
	}

	public IrwinSearchTree(SwappablePanel swap, String f, String[] is) {
		super(swap, f, is);
		// TODO Auto-generated constructor stub
	}

	protected VerticalPanel createFilterPanel() {
		sinceDate = new ListBox();
		keywordSearch = new TextBox();
		sinceModDate = new ListBox();
		exactSearch = new CheckBox();
		exactSearch.setText("Exact");
		VerticalPanel fp = new VerticalPanel();
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
		setComponentTitles();
		return fp;
	}

	protected void doSearch(TreeItem item) {
		((IrwinComponentFactory) Services.getServices().factory).jumpTo(item
				.getText());
		super.doSearch(item);
	}

}
