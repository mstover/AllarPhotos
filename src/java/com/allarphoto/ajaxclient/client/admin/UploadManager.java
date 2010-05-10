package com.allarphoto.ajaxclient.client.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class UploadManager extends DockPanel {

	List packages;

	Panel controlPanel;

	public UploadManager() {
		// TODO Auto-generated constructor stub
	}

	public UploadManager(List packages) {
		this.packages = packages;
		init();
	}

	private void init() {
		Tree packageTree = new Tree();
		for (int i = 0; i < packages.size(); i++) {
			TreeItem item = new TreeItem((String) ((Map) packages.get(i))
					.get("Package Name"));
			item.setUserObject(packages.get(i));
			packageTree.addItem(item);
		}
		packageTree.addTreeListener(new TreeAction());
		ScrollPanel scroller = new ScrollPanel();
		VerticalPanel vp = new VerticalPanel();
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		vp.add(AjaxSystem.getTitle("Uploaded Packages"));
		vp.add(packageTree);
		scroller.add(vp);
		scroller.setHeight(((int) (Window.getClientHeight() * .85)) + "px");
		scroller.setWidth("100%");
		add(scroller, DockPanel.WEST);
		controlPanel = new VerticalPanel();
		add(controlPanel, DockPanel.CENTER);
		setSpacing(20);
		setWidth("100%");
		setHeight("100%");
	}

	class TreeAction implements TreeListener {
		Label[] labels;

		TextBox[] values;

		public TreeAction() {

		}

		public void onTreeItemSelected(final TreeItem item) {
			Map props = (Map) item.getUserObject();
			controlPanel.clear();
			controlPanel.add(AjaxSystem.getTitle("Upload Parameters"));
			labels = new Label[props.size()];
			values = new TextBox[props.size()];
			Iterator iter = props.keySet().iterator();
			int count = 0;
			while (iter.hasNext()) {
				HorizontalPanel hp = new HorizontalPanel();
				String key = (String) iter.next();
				labels[count] = AjaxSystem.getLabel(key);
				labels[count].setWidth("150px");
				hp.add(labels[count]);
				values[count] = new TextBox();
				values[count].setText((String) props.get(key));
				hp.add(values[count++]);
				controlPanel.add(hp);
			}
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new Button("Approve Package", new ClickListener() {

				public void onClick(Widget sender) {
					Map uploadParams = new HashMap();
					for (int i = 0; i < labels.length; i++) {
						uploadParams.put(labels[i].getText(), values[i]
								.getText());
					}
					BusyPopup.waitFor("Approving Package");
					Services.getServices().libraryInfoService
							.approveUploadPackage(uploadParams,
									new AsyncCallback() {

										public void onFailure(Throwable caught) {
											BusyPopup.done("Approving Package");
											new PopupWarning(caught.toString());
										}

										public void onSuccess(Object result) {
											if (!((Boolean) result)
													.booleanValue()) {
												new PopupWarning(
														"Failed to approve upload package");
											} else
												item.getTree().removeItem(item);
											controlPanel.clear();
											BusyPopup.done("Approving Package");
										}

									});

				}

			}));
			controlPanel.add(hp);

		}

		public void onTreeItemStateChanged(TreeItem item) {
			// TODO Auto-generated method stub

		}

	}

}
