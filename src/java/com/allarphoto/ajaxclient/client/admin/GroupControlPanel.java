package com.allarphoto.ajaxclient.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxGroup;
import com.allarphoto.ajaxclient.client.beans.AjaxResource;
import com.allarphoto.ajaxclient.client.beans.AjaxRights;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class GroupControlPanel extends TabPanel implements TabListener {

	AjaxGroup group;

	public GroupControlPanel() {
	}

	public GroupControlPanel(String g) {
		BusyPopup.waitFor("Loading Group Permissions");
		Services.getServices().userInfoService.getGroup(g, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				BusyPopup.done("Loading Group Permissions");
				new PopupWarning("Failed to load group permissions");

			}

			public void onSuccess(Object result) {
				group = (AjaxGroup) result;
				BusyPopup.done("Loading Group Permissions");
				init();
				setHeight("100%");
				setWidth("100%");
				selectTab(1);
			}

		});

	}

	private void init() {
		VerticalPanel vp = new VerticalPanel();
		ScrollPanel sp = new ScrollPanel(createPermsTable("Group", true, false,
				false, false, false, false));
		sp.setHeight("500px");
		vp.add(createSelectPanel(AjaxResource.GROUP));
		vp.add(sp);
		add(vp, "Groups");
		vp = new VerticalPanel();
		sp = new ScrollPanel(createPermsTable("Library", true, true, true,
				true, true, true));
		sp.setHeight("500px");
		vp.add(createSelectPanel(AjaxResource.DATATABLE));
		vp.add(sp);
		add(vp, "Libraries");
		vp = new VerticalPanel();
		sp = new ScrollPanel(createPermsTable("Merchant", true, false, false,
				false, false, false));
		sp.setHeight("500px");
		vp.add(createSelectPanel(AjaxResource.MERCHANT));
		vp.add(sp);
		add(vp, "Merchants");
		vp = new VerticalPanel();
		sp = new ScrollPanel(createPermsTable("Expired Field", true, true,
				true, true, true, false));
		sp.setHeight("500px");
		vp.add(createSelectPanel(AjaxResource.EXPIRED_ITEMS));
		vp.add(sp);
		add(vp, "Expired&nbsp;Images", true);
		vp = new VerticalPanel();
		sp = new ScrollPanel(createPermsTable("Protected Field", true, true,
				true, true, true, true));
		sp.setHeight("500px");
		vp.add(createSelectPanel(AjaxResource.PROTECTED_FIELD));
		vp.add(sp);
		add(vp, "Protected&nbsp;Fields", true);
	}

	private FlexTable createPermsTable(String colname, boolean admin,
			boolean read, boolean order, boolean download,
			boolean download_orig, boolean upload) {
		FlexTable table = new FlexTable();
		table.addStyleName("perms-table");
		table.setText(0, 0, colname);
		int count = 1;
		if (admin)
			table.setText(0, count++, "Admin");
		if (read)
			table.setText(0, count++, "Read");
		if (order)
			table.setText(0, count++, "Order");
		if (download)
			table.setText(0, count++, "Download");
		if (download_orig)
			table.setText(0, count++, "Download Originals");
		if (upload)
			table.setText(0, count++, "Upload");
		table.getRowFormatter().addStyleName(0, "label");
		return table;
	}

	private Image createRightsImage(final AjaxResource res, final String right,
			final boolean hasRight,FlexTable t,int row) {
		final Image img = hasRight ? new Image("check.gif")
				: new Image("x.gif");
		img
				.addClickListener(createUpdateRightsClick(res, right, hasRight,
						img,t,row));
		return img;
	}

	private ClickListener createUpdateRightsClick(final AjaxResource res,
			final String right, final boolean hasRight, final Image img,final FlexTable t,final int row) {
		return new ClickListener() {

			public void onClick(Widget sender) {
				final ClickListener clicker = this;
				Services.getServices().userInfoService.updateRight(group
						.getName(), res.getName(), res.getType(), right,
						!hasRight, new AsyncCallback() {

							public void onFailure(Throwable caught) {
								new PopupWarning(caught.toString());

							}

							public void onSuccess(Object result) {
								if (((Boolean) result).booleanValue()) {
									group.updateRight(right, res, !hasRight);
									img.setUrl(!hasRight ? "check.gif"
											: "x.gif");
									img.removeClickListener(clicker);
									img
											.addClickListener(createUpdateRightsClick(
													res, right, !hasRight, img,t,row));
									if (!group.hasAnyRight(res)) {
										t.removeRow(row);
									}
								} else
									new PopupWarning(
											"Failed to update permissions");
							}

						});

			}

		};
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		return super.onBeforeTabSelected(sender, tabIndex);

	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		super.onTabSelected(sender, tabIndex);
		switch (tabIndex) {
		case 0:
			loadGroupRights();
			break;
		case 1:
			loadLibraryRights();
			break;
		case 2:
			loadMerchantRights();
			break;
		case 3:
			loadExpiredImagesRights();
			break;
		case 4:
			loadProtectedFieldRights();
			break;
		}
	}

	private String getDefaultRight(int resType, AjaxRights newRight) {
		switch (resType) {
		case AjaxResource.DATATABLE:
			newRight.setRead(true);
			return "read";
		case AjaxResource.EXPIRED_ITEMS:
			newRight.setRead(true);
			return "read";
		case AjaxResource.GROUP:
			newRight.setRead(true);
			return "admin";
		case AjaxResource.MERCHANT:
			newRight.setRead(true);
			return "admin";
		case AjaxResource.PROTECTED_FIELD:
			newRight.setRead(true);
			return "read";
		default:
			newRight.setRead(true);
			return "admin";
		}
	}

	private Widget createSelectPanel(final int resType) {
		final ListBox resourceList = new ListBox();
		resourceList.addItem("");
		resourceList.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				BusyPopup.waitFor("Adding new resource");
				AjaxRights newRights = new AjaxRights();
				String defRight = getDefaultRight(resType, newRights);
				group.addRight(
						new AjaxResource(resourceList.getItemText(resourceList
								.getSelectedIndex()), resType), newRights);
				Services.getServices().userInfoService.updateRight(group
						.getName(), resourceList.getItemText(resourceList
						.getSelectedIndex()), resType, defRight, true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								BusyPopup.done("Adding new resource");

							}

							public void onSuccess(Object result) {
								selectTab(getTabBar().getSelectedTab());
								BusyPopup.done("Adding new resource");
							}

						});

			}

		});
		Services.getServices().userInfoService.getResourceNames(resType,
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						String[] resources = (String[]) result;
						for (int i = 0; i < resources.length; i++) {
							if (group.getRights(new AjaxResource(resources[i],
									resType)) == null)
								resourceList.addItem(resources[i]);
						}
					}
				});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Select to Add: "));
		hp.add(resourceList);
		return hp;
	}

	private void loadProtectedFieldRights() {
		FlexTable table = (FlexTable) ((ScrollPanel) ((VerticalPanel) getWidget(4))
				.getWidget(1)).getWidget();
		AjaxResource[] res = group.getResources(AjaxResource.PROTECTED_FIELD);
		int rowCount = 1;
		for (int i = 0; i < res.length; i++) {
			table.setWidget(i + rowCount, 0, AjaxSystem.getText(res[i]
					.getName()));
			AjaxRights rights = group.getRights(res[i]);
			int count = 1;
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"admin", rights.isAdmin(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"read", rights.isRead(),table,i + rowCount));
			table.getRowFormatter().setStyleName(i + rowCount,
					(i % 2 == 0 ? "odd" : "even"));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"order", rights.isOrder(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"download", rights.isDownload(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"download_orig", rights.isDownload_orig(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"upload", rights.isUpload(),table,i + rowCount));
		}
	}

	private void loadExpiredImagesRights() {
		FlexTable table = (FlexTable) ((ScrollPanel) ((VerticalPanel) getWidget(3))
				.getWidget(1)).getWidget();
		AjaxResource[] res = group.getResources(AjaxResource.EXPIRED_ITEMS);
		int rowCount = 1;
		for (int i = 0; i < res.length; i++) {
			table.setWidget(i + rowCount, 0, AjaxSystem.getText(res[i]
					.getName()));
			AjaxRights rights = group.getRights(res[i]);
			int count = 1;
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"admin", rights.isAdmin(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"read", rights.isRead(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"order", rights.isOrder(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"download", rights.isDownload(),table,i + rowCount));
			table.setWidget(i + rowCount, count++, createRightsImage(res[i],
					"download_orig", rights.isDownload_orig(),table,i + rowCount));
			table.getRowFormatter().setStyleName(i + rowCount,
					(i % 2 == 0 ? "odd" : "even"));
		}
	}

	private void loadMerchantRights() {
		FlexTable table = (FlexTable) ((ScrollPanel) ((VerticalPanel) getWidget(2))
				.getWidget(1)).getWidget();
		AjaxResource[] res = group.getResources(AjaxResource.MERCHANT);
		int rowCount = 1;
		for (int i = 0; i < res.length; i++) {
			table.setWidget(i + rowCount, 0, AjaxSystem.getText(res[i]
					.getName()));
			AjaxRights rights = group.getRights(res[i]);
			table.setWidget(i + rowCount, 1, createRightsImage(res[i], "admin",
					rights.isAdmin(),table,i + rowCount));
			table.getRowFormatter().setStyleName(i + rowCount,
					(i % 2 == 0 ? "odd" : "even"));
		}
	}

	private void loadGroupRights() {
		FlexTable table = (FlexTable) ((ScrollPanel) ((VerticalPanel) getWidget(0))
				.getWidget(1)).getWidget();
		AjaxResource[] res = group.getResources(AjaxResource.GROUP);
		int rowCount = 1;
		for (int i = 0; i < res.length; i++) {
			table.setWidget(i + rowCount, 0, AjaxSystem.getText(res[i]
					.getName()));
			AjaxRights rights = group.getRights(res[i]);
			table.setWidget(i + rowCount, 1, createRightsImage(res[i], "admin",
					rights.isAdmin(),table,i + rowCount));
			table.getRowFormatter().setStyleName(i + rowCount,
					(i % 2 == 0 ? "odd" : "even"));
		}
	}

	private void loadLibraryRights() {
		FlexTable table = (FlexTable) ((ScrollPanel) ((VerticalPanel) getWidget(1))
				.getWidget(1)).getWidget();
		AjaxResource[] res = group.getResources(AjaxResource.DATATABLE);
		int rowCount = 1;
		for (int i = 0; i < res.length; i++) {
				table.setWidget(i + rowCount, 0, AjaxSystem.getText(res[i]
						.getName()));
				AjaxRights rights = group.getRights(res[i]);
				int count = 1;
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "admin", rights.isAdmin(),table,i + rowCount));
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "read", rights.isRead(),table,i + rowCount));
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "order", rights.isOrder(),table,i + rowCount));
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "download", rights.isDownload(),table,i + rowCount));
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "download_orig", rights.isDownload_orig(),table,i + rowCount));
				table.setWidget(i + rowCount, count++, createRightsImage(
						res[i], "upload", rights.isUpload(),table,i + rowCount));
				table.getRowFormatter().setStyleName(i + rowCount,
						(i % 2 == 0 ? "odd" : "even"));
		}
	}

}
