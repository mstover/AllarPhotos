package com.allarphoto.ajaxclient.client.uploader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.BusyPopup;

public class TreeControls {

	Services services = Services.getServices();

	Tree tree;

	String currentLibrary = null;

	FolderListener listen;

	public Widget getLibraryTree() {
		tree = new Tree();
		listen = new FolderListener();
		tree.addTreeListener(listen);
		tree.addFocusListener(listen);
		tree.addStyleName("folder-tree");
		BusyPopup.waitFor("Loading Library Tree");
		services.libraryInfoService.getUploadableFolders(new String[0], null,
				new TreeCallback(tree, 0));
		return tree;
	}

	public String getCurrentValues() {
		TreeItem selected = tree.getSelectedItem();
		StringBuffer values = new StringBuffer();
		TreeItem parent = selected;
		if (parent != null) {
			while (parent.getParentItem() != null) {
				if (parent.getHTML().startsWith(
						"<img src=\""
								+ Services.getServices().factory
										.getIconFolder() + "/"
								+ "folder_txt.gif\"><input ")) {
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

	public void updateCategories() {
		Element cats = DOM.getElementById("hidden-categories");
		DOM.setAttribute(cats, "value", getCurrentValues());
	}

	private String[] getCurrentValuesArray() {
		TreeItem selected = tree.getSelectedItem();
		List values = new ArrayList();
		TreeItem parent = selected;
		if (parent != null) {
			while (parent.getParentItem() != null) {
				values.add(parent.getText().trim());
				parent = parent.getParentItem();
			}
		}
		Collections.reverse(values);
		String[] vals = new String[values.size()];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = (String) values.get(i);
		}
		return vals;
	}

	class FolderListener implements TreeListener, FocusListener {
		TreeItem lastItem;

		public void onFocus(Widget sender) {

			final String html = ((Tree) sender).getSelectedItem().getHTML();
			if (html.startsWith("<img src=\""
					+ Services.getServices().factory.getIconFolder() + "/"
					+ "folder_txt.gif\"><input ")) {
				DeferredCommand.add(null);
				DeferredCommand.add(new Command() {

					public void execute() {
						Services.focuser.focus(DOM.getElementById(AjaxSystem
								.parseId(html)));
					}

				});
			}
		}

		public void onLostFocus(Widget sender) {
			updateCategories();
		}

		private int getDepth(TreeItem item) {
			int count = 1;
			TreeItem parent = item.getParentItem();
			while (parent != null) {
				count++;
				parent = parent.getParentItem();
			}
			return count;
		}

		private String getTopName(TreeItem item) {
			TreeItem child = item;
			TreeItem parent = child.getParentItem();
			while (parent != null) {
				child = parent;
				parent = child.getParentItem();
			}
			if (currentLibrary == null
					|| !currentLibrary.equals(child.getText())) {
				currentLibrary = child.getText();
			}
			return child.getText().trim();
		}

		public void onTreeItemSelected(TreeItem item) {
			replaceInputBox(item);
			lastItem = item;
			String text = item.getText();
			final String html = item.getHTML();
			if (text.trim().equals("Add New Folder")
					|| (html.trim().startsWith(
							"<img src=\""
									+ Services.getServices().factory
											.getIconFolder() + "/"
									+ "folder_txt.gif\">") && text.length() > 0)) {
				String value;
				if (text.trim().equals("Add New Folder"))
					value = "";
				else
					value = text;
				final String id = AjaxSystem.uniqueId();
				item.setHTML("<img src='"
						+ Services.getServices().factory.getIconFolder() + "/"
						+ "folder_txt.gif'><input type='text' id='" + id
						+ "' value='" + value + "'>");
				Element cats = DOM.getElementById("hidden-has-new-cat");
				DOM.setAttribute(cats, "value", "true");
				DeferredCommand.add(null);
				DeferredCommand.add(new Command() {

					public void execute() {
						Services.focuser.focus(DOM.getElementById(id));
					}

				});
			} else if (html.startsWith("<img src=\""
					+ Services.getServices().factory.getIconFolder() + "/"
					+ "folder_txt.gif\"><input ")) {
				if (item.getChildCount() == 0) {
					item.addItem(new TreeItem("<img src='"
							+ Services.getServices().factory.getIconFolder()
							+ "/" + "folder_grey.gif'>Add New Folder"));
				}
				Element cats = DOM.getElementById("hidden-has-new-cat");
				DOM.setAttribute(cats, "value", "true");
				DeferredCommand.add(null);
				DeferredCommand.add(new Command() {

					public void execute() {
						Services.focuser.focus(DOM.getElementById(AjaxSystem
								.parseId(html)));
					}

				});
			} else {
				BusyPopup.waitFor("Loading Folders");
				int depth = getDepth(item);
				if (item.getChildCount() == 0) {
					String familyName = getTopName(item);
					services.libraryInfoService.getUploadableFolders(
							getCurrentValuesArray(), familyName,
							new TreeCallback(item, depth));
				} else
					BusyPopup.done("Loading Folders");
				Element cats = DOM.getElementById("hidden-has-new-cat");
				DOM.setAttribute(cats, "value", "false");
			}
		}

		private TreeItem replaceInputBox(TreeItem item) {
			if (lastItem != null
					&& (item == null || lastItem != item)
					&& lastItem.getHTML().startsWith(
							"<img src=\""
									+ Services.getServices().factory
											.getIconFolder() + "/"
									+ "folder_txt.gif\"><input ")) {
				String id = AjaxSystem.parseId(lastItem.getHTML());
				String value = DOM
						.getAttribute(DOM.getElementById(id), "value");
				if (value != null && value.length() > 0)
					lastItem.setHTML("<img src='"
							+ Services.getServices().factory.getIconFolder()
							+ "/" + "folder_txt.gif'> " + value);
				else
					lastItem.setHTML("<img src='"
							+ Services.getServices().factory.getIconFolder()
							+ "/" + "folder_grey.gif'> Add New Folder");
			}
			return lastItem;
		}

		public void onTreeItemStateChanged(TreeItem item) {
			// TODO Auto-generated method stub

		}

	}

	void addItems(String[] values, TreeItem parent) {
		if (parent == null)
			return;
		for (int i = 0; i < values.length; i++) {
			parent.addItem("<img src='"
					+ Services.getServices().factory.getIconFolder() + "/"
					+ "folderIcon.gif'> " + values[i]);
		}
	}

	void addItems(String[] values, Tree parent) {
		if (parent == null)
			return;
		for (int i = 0; i < values.length; i++) {
			parent.addItem("<img src='"
					+ Services.getServices().factory.getIconFolder() + "/"
					+ "folderIcon.gif'> " + values[i]);
		}
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
			}
			if (item != null) {
				if (item.getParentItem() != null)
					item.addItem(new TreeItem("<img src='"
							+ Services.getServices().factory.getIconFolder()
							+ "/" + "folder_grey.gif'> Add New Folder"));
				addItems(folders, item);
				item.setState(true);
				BusyPopup.done("Loading Folders");
			}
		}
	}
}
