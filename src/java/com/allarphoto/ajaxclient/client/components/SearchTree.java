package com.lazerinc.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.components.basic.Drag;

public class SearchTree extends VerticalPanel {

	SearchResultsViewer viewer;

	boolean preset = false;

	SwappablePanel swapPanel;

	protected Tree searchTree;

	protected TextBox keywordSearch;
	
	protected CheckBox exactSearch;

	protected ListBox sinceDate, sinceModDate;

	Panel filterPanel;

	protected String[] initialSelection;

	protected int initialIndex;

	protected boolean advancedOpen;

	public SearchTree(SwappablePanel swap) {
		swapPanel = swap;
		advancedOpen = false;
		init();
	}

	public SearchTree(SwappablePanel swap, String f, String[] is) {
		initialSelection = is;
		swapPanel = swap;
		currentLibrary = f;
		preset = true;
		advancedOpen = false;
		initialIndex = 0;
		init();
	}

	Services services = Services.getServices();

	String currentLibrary = null;

	FolderListener listen;

	protected void init() {
		filterPanel = createFilterPanel();
		add(filterPanel);
		add(createTree());
		setSpacing(15);
	}

	protected Tree createTree() {
		searchTree = new Tree();
		searchTree.setImageBase(Services.getServices().factory.getIconFolder()
				+ "/");
		searchTree.addStyleName("folder-tree");
		searchTree.addMouseListener(Drag.dragger);
		listen = new FolderListener();
		searchTree.addTreeListener(listen);
		BusyPopup.waitFor("Loading Library Tree");
		services.libraryInfoService.getSearchFolders(new String[0][0],
				currentLibrary, new TreeCallback(searchTree, 0));
		add(AjaxSystem.getLabel(getFolderTreeLabel()));
		return searchTree;
	}
	
	protected String getFolderTreeLabel() {
		return "Folders";
	}

	protected VerticalPanel createFilterPanel() {
		VerticalPanel filterPanel = new VerticalPanel();
		sinceDate = new ListBox();
		keywordSearch = new TextBox();
		sinceModDate = new ListBox();
		exactSearch = new CheckBox();
		updateFilterPanel(filterPanel);
		setComponentTitles();
		return filterPanel;
	}

	protected void setComponentTitles() {
		keywordSearch
				.setTitle("Enter a keyword to further filter search results");
		sinceDate
				.setTitle("Enter time frame to filter results based on when images were first cataloged");
		sinceModDate
				.setTitle("Enter time frame to filter results based on when images were last modified");
	}

	protected String getFiltersLabel() {
		return "Keyword Filter";
	}
	
	protected void updateFilterPanel(final VerticalPanel fp) {
		fp.clear();
		fp.addStyleName("search-filters");
		sinceDate.clear();
		HorizontalPanel keywordPanel = new HorizontalPanel();
		keywordPanel.add(keywordSearch);
		keywordPanel.add(exactSearch);
		exactSearch.setText("Exact");
		keywordSearch.setText("");
		sinceModDate.clear();
		if (advancedOpen) {
			VerticalPanel vp = new VerticalPanel();
			vp.add(AjaxSystem.getLabel(getFiltersLabel()));
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
			vp.add(keywordPanel);
			fp.add(vp);
			fp.setSpacing(5);
			fp.add(sinceDate);

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
			exactSearch.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					doSearch(searchTree.getSelectedItem());
				}

			});
			fp.add(sinceModDate);
			sinceDate.setWidth("175px");
			sinceModDate.setWidth("175px");
			Label adv = AjaxSystem.getLabel("Close Advanced Search", "linked");
			adv.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					advancedOpen = !advancedOpen;
					updateFilterPanel(fp);
				}
			});
			fp.add(adv);
		} else {
			Label adv = AjaxSystem
					.getLabel("Advanced Filter Options", "linked");
			adv.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					advancedOpen = !advancedOpen;
					updateFilterPanel(fp);
				}
			});
			fp.add(adv);
		}
	}

	public AjaxProduct[] getCurrentProductList() {
		return viewer.products;
	}

	public String getCurrentValues() {
		TreeItem selected = searchTree.getSelectedItem();
		StringBuffer values = new StringBuffer();
		TreeItem parent = selected;
		if (parent != null) {
			while (parent.getParentItem() != null) {
				if (parent.getHTML()
						.startsWith(getFolderIconHtml() + "<input ")) {
					String id = AjaxSystem.parseId(parent.getHTML());
					String value = DOM.getAttribute(DOM.getElementById(id),
							"value");
					values.insert(0, "," + value.trim());
				} else
					values.insert(0, "," + parent.getText().trim());
				parent = parent.getParentItem();
			}
			values.insert(0, parent.getText().trim());
		}
		return values.toString();
	}

	private String[][] getCurrentValuesArray() {
		TreeItem selected = searchTree.getSelectedItem();
		List values = new ArrayList();
		List fields = new ArrayList();
		TreeItem parent = selected;
		if (parent != null) {
			while (parent.getParentItem() != null) {
				values.add(parent.getText().trim());
				fields.add(((String) parent.getUserObject()).trim());
				parent = parent.getParentItem();
			}
			if (preset) {
				values.add(parent.getText().trim());
				fields.add(((String) parent.getUserObject()).trim());
			}
		}
		if (sinceDate.getSelectedIndex() >= 0
				&& !sinceDate.getItemText(sinceDate.getSelectedIndex()).equals(
						"Images Added Since:")) {
			values.add(sinceDate.getItemText(sinceDate.getSelectedIndex()));
			fields.add("since-date");
		}
		if (sinceModDate.getSelectedIndex() >= 0
				&& !sinceModDate.getItemText(sinceModDate.getSelectedIndex())
						.equals("Images Modified Since:")) {
			values.add(sinceModDate
					.getItemText(sinceModDate.getSelectedIndex()));
			fields.add("since-mod-date");
		}
		Collections.reverse(values);
		Collections.reverse(fields);
		if (keywordSearch.getText().trim().length() > 0) {
			if(exactSearch.isChecked()) values.add("\""+keywordSearch.getText().trim()+"\"");
			else {
				/* 2008.10.29 NEED TO CHECK HERE SUCH THAT THE keySearch
				 * is not interpreted by the DB as a DateSearch
				 * if it is not intended to be.
				 * EXAMPLE: IRWIN SKU of 2076709 is being seen by the DB
				 * as a date search of 10/07/2081
				*/
				String keyParse = keywordSearch.getText().trim();
				if(keyParse.length() > 6 && keyParse.startsWith("20")) {
					keyParse = keyParse.substring(0, 2) + " " + keyParse.substring(2, keyParse.length());
				}
				values.add(keyParse);
			}
			fields.add("keyword-search");
		}
		String[] vals = new String[values.size()];
		String[] cats = new String[fields.size()];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = (String) values.get(i);
			cats[i] = (String) fields.get(i);
		}
		String[][] valuesAndCategories = new String[2][cats.length];
		valuesAndCategories[0] = cats;
		valuesAndCategories[1] = vals;
		return valuesAndCategories;
	}

	private int getDepth(TreeItem item) {
		int count = 1;
		if (item == null)
			return 0;
		TreeItem parent = item.getParentItem();
		while (parent != null) {
			count++;
			parent = parent.getParentItem();
		}
		return count;
	}

	class FolderListener implements TreeListener {
		TreeItem lastItem;

		public void onTreeItemSelected(TreeItem item) {
			doSearch(item);
		}

		public void onTreeItemStateChanged(TreeItem item) {
			// TODO Auto-generated method stub

		}

	}

	private String getTopName(TreeItem item) {
		TreeItem child = item;
		TreeItem parent = child.getParentItem();
		while (parent != null) {
			child = parent;
			parent = child.getParentItem();
		}
		if (currentLibrary == null || !currentLibrary.equals(child.getText())) {
			currentLibrary = child.getText();
		}
		return child.getText().trim();
	}

	private void setViewer(String familyName) {
		if (viewer == null) {
			viewer = (SearchResultsViewer) Services.getServices().factory
					.createComponent("SearchResultsViewer", new Object[] {
							getCurrentValuesArray(), familyName });
			swapPanel.swapin(viewer);
		} else {
			viewer.resetImages(getCurrentValuesArray(), familyName);
			swapPanel.swapin(viewer);
			Services.getServices().cart.redraw();
		}
	}

	protected void addItems(String[] values, TreeItem parent) {
		if (parent == null)
			return;
		parent.removeItems();
		for (int i = 1; i < values.length; i++) {
			if (values[i] == null || values[i].length() == 0)
				values[i] = "N/A";
			TreeItem item = new TreeItem(getFolderIconHtml() + values[i]);
			item.setTitle(values[0] + " = " + values[i]);
			item.setUserObject(values[0]);
			parent.addItem(item);
		}
	}

	protected void addItems(String[] values, Tree parent) {
		if (parent == null)
			return;
		for (int i = 1; i < values.length; i++) {
			if (values[i] == null || values[i].length() == 0)
				values[i] = "N/A";
			TreeItem item = new TreeItem(getFolderIconHtml() + values[i]);
			item.setTitle(values[0] + " = " + values[i]);
			item.setUserObject(values[0]);
			parent.addItem(item);
		}
	}

	protected String getFolderIconHtml() {
		return "<img style='vertical-align:middle;' src='"
				+ Services.getServices().factory.getIconFolder() + "/"
				+ "folderIcon.gif'> ";
	}

	protected void doSearch(TreeItem item) {
		BusyPopup.waitFor("Loading Folders");
		int depth = getDepth(item);
		String familyName = (currentLibrary != null ? currentLibrary
				: getTopName(item));
		setViewer(familyName);
		if (depth > 0)
			services.libraryInfoService.getSearchFolders(
					getCurrentValuesArray(), familyName, new TreeCallback(item,
							depth));
		else
			BusyPopup.done("Loading Folders");
	}

	protected TreeItem findSelectionFromTree(Tree t, String selectionPath) {
		for (int i = 0; i < t.getItemCount(); i++) {
			if (selectionPath.equals(t.getItem(i).getText().trim())) {
				return t.getItem(i);
			}
		}
		return null;

	}

	protected TreeItem findSelectionFromItem(TreeItem ti, String selection) {
		for (int i = 0; i < ti.getChildCount(); i++) {
			if (selection.equals(ti.getChild(i).getText().trim()))
				return ti.getChild(i);
		}
		return null;
	}

	class TreeCallback implements AsyncCallback {
		Tree tree;

		TreeItem item;

		int level;

		public TreeCallback(Tree t, int l) {
			tree = t;
			level = l;
		}

		public TreeCallback(TreeItem t, int l) {
			item = t;
			level = l;
		}

		public void onFailure(Throwable caught) {
			tree.addItem(caught.toString());
			BusyPopup.done("Loading Library Tree");
			BusyPopup.done("Loading Folders");
		}

		public void onSuccess(Object result) {
			String[] folders = (String[]) result;
			if (tree != null) {
				addItems(folders, tree);
				BusyPopup.done("Loading Library Tree");
				if (tree.getItemCount() > 0) {
					if (initialSelection == null)
						tree.setSelectedItem(tree.getItem(0));
					else {
						TreeItem item = findSelectionFromTree(tree,
								initialSelection[initialIndex++]);
						while(item == null && initialSelection.length > initialIndex)
							item = findSelectionFromTree(tree,
									initialSelection[initialIndex++]);
						tree.setSelectedItem(item);
					}
				}
			}
			if (item != null) {
				addItems(folders, item);
				BusyPopup.done("Loading Folders");
				DeferredCommand.addPause();
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						TreeItem p = item.getParentItem();
						TreeItem viewed = item;
						if (p != null) {
							int index = p.getChildIndex(viewed);
							if (index > 0)
								viewed = p.getChild(index - 1);
						}
						DOM.scrollIntoView(viewed.getElement());
						item.setState(true);
						if (initialSelection != null
								&& initialIndex < initialSelection.length) {
							TreeItem item2 = findSelectionFromItem(
									item, initialSelection[initialIndex++]);
								while(item2 == null && initialSelection.length > initialIndex)
									item2 = findSelectionFromItem(
											item, initialSelection[initialIndex++]);
							searchTree.setSelectedItem(item2);
						}
					}
				});
			}
		}
	}

}
