package com.lazerinc.ajaxclient.client.admin.library;

import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;
import com.lazerinc.ajaxclient.client.beans.AjaxProductField;
import com.lazerinc.ajaxclient.client.beans.DownloadStats;
import com.lazerinc.ajaxclient.client.beans.DownloadUserStat;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.DatePicker;
import com.lazerinc.ajaxclient.client.components.EnterKeyListener;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.icons.DeleteFieldIcon;

public class LibraryTreeActions implements TreeListener {

	Widget iframe;

	Label refreshResponse = new Label("");

	public LibraryTreeActions() {
	}

	public void onTreeItemSelected(final TreeItem item) {
		refreshResponse.setText("");
		if (item.getText().equals("View Download Stats")) {
			AjaxProductFamily family = (AjaxProductFamily) item.getParentItem()
					.getUserObject();
			createDownloadViewer(family, null, null);
		} else {
			AjaxProductFamily family = (AjaxProductFamily) item.getUserObject();
			createFamilyAdminPanel(family);
		}
	}

	private ClickListener createRmListener(final AjaxProductFamily family) {
		return new ClickListener() {

			public void onClick(Widget sender) {
				RadioButton w = (RadioButton) sender;
				if (w.getText().equals("Yes")) {
					makeFamilyRemoteManaged(family.getFamilyName(), true);
					family.setRemoteManaged(true);
				} else {
					makeFamilyRemoteManaged(family.getFamilyName(), false);
					family.setRemoteManaged(false);
				}
			}

		};
	}

	private void makeFamilyRemoteManaged(String family, boolean rm) {
		Services.getServices().libraryInfoService.setFamilyRemotedManaged(
				family, rm, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						// TODO Auto-generated method stub

					}

				});
	}

	private void createFamilyAdminPanel(AjaxProductFamily family) {
		Label familyName = new Label(family.getDescriptiveName());
		familyName.setStyleName("title");
		Label desc = new Label(family.getDescription());
		Label table = new Label(family.getFamilyName());
		VerticalPanel main = new VerticalPanel();
		main.add(familyName);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Description:"));
		hp.add(desc);
		main.add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Remote Managed:"));
		RadioButton yesRm = new RadioButton("rm", "Yes");
		ClickListener rmListener = createRmListener(family);
		yesRm.addClickListener(rmListener);
		RadioButton noRm = new RadioButton("rm", "No");
		noRm.addClickListener(rmListener);
		if (family.isRemoteManaged())
			yesRm.setChecked(true);
		else
			noRm.setChecked(true);
		hp.add(yesRm);
		hp.add(noRm);
		main.add(hp);
		final HorizontalPanel fhp = new HorizontalPanel();
		fhp.add(AjaxSystem.getLabel("Library Size:"));
		Services.getServices().libraryInfoService.getLibrarySize(family
				.getFamilyName(), new AsyncCallback() {

			public void onFailure(Throwable caught) {
				fhp.add(AjaxSystem.getText("Failed to Calculate"));

			}

			public void onSuccess(Object result) {
				fhp.add(AjaxSystem.getText((String) result));
			}

		});
		main.add(fhp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Table Name:"));
		hp.add(table);
		main.add(hp);
		main.add(getRefreshPanel(family));
		main.add(getKeywordUploadPanel(family));
		main.add(getFieldAddPanel(family));
		main.add(getFieldPanel(family));
		Services.getServices().mainPanel.setCenter(main);
	}

	private Widget getFieldAddPanel(final AjaxProductFamily family) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Add New Category:"));
		TextBox fieldBox = new TextBox();
		fieldBox.addKeyboardListener(new EnterKeyListener() {

			protected void onEnterPress(final Widget source) {
				if (family.getField(((TextBox) source).getText()) != null) {
					new PopupWarning("A field with that name already exists");
				} else {
					BusyPopup.waitFor("Adding New Field");
					Services.getServices().libraryInfoService.addField(family
							.getFamilyName(), ((TextBox) source).getText(),
							new AsyncCallback() {

								public void onFailure(Throwable caught) {
									new PopupWarning(caught.toString());
									BusyPopup.done("Adding New Field");
								}

								public void onSuccess(Object result) {
									if (((Boolean) result).booleanValue()) {
										family.addField(new AjaxProductField(
												((TextBox) source).getText(),
												family.getFamilyName(),
												"Category", 0, 0));
										createFamilyAdminPanel(family);
									} else {
										new PopupWarning(
												"You don't have permission to add a field to this library");
									}
									BusyPopup.done("Adding New Field");
								}
							});
				}
			}

		});
		hp.add(fieldBox);
		return hp;
	}

	private void createDownloadViewer(final AjaxProductFamily family,
			String fromDate, String toDate) {
		BusyPopup.waitFor("Loading Download Stats");
		AsyncCallback async = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				new PopupWarning(caught.getMessage());
				BusyPopup.done("Loading Download Stats");
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
			 */
			public void onSuccess(Object result) {
				DownloadStats stats = (DownloadStats) result;
				FlexTable statsTable = new FlexTable();
				statsTable.setCellSpacing(0);
				VerticalPanel main = new VerticalPanel();
				main.add(AjaxSystem.getTitle(family.getDescriptiveName()
						+ " Download Statistics"));
				main.add(statsTable);
				final DatePicker newFromDate = new DatePicker(stats.fromDate());
				final DatePicker newToDate = new DatePicker(stats.toDate());
				newToDate.setText(stats.toDate());
				int row = 0;
				statsTable.setWidget(row, 0, AjaxSystem.getLabel("Get:"));
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(AjaxSystem.getText("From:"));
				hp.add(newFromDate);
				hp.add(AjaxSystem.getText("To:"));
				hp.add(newToDate);
				hp.add(new Button("Go", new ClickListener() {

					public void onClick(Widget sender) {
						createDownloadViewer(family, newFromDate.getText(),
								newToDate.getText());
					}

				}));
				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
				statsTable.setWidget(row, 1, hp);
				statsTable.getCellFormatter().setAlignment(row++, 1,
						HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_BOTTOM);
				statsTable.setWidget(row, 0, AjaxSystem
						.getLabel("Currently Displaying:"));
				hp = new HorizontalPanel();
				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
				hp.add(AjaxSystem.getLabel("From:"));
				hp.add(AjaxSystem.getText(stats.fromDate()));
				hp.add(AjaxSystem.getLabel("To:"));
				hp.add(AjaxSystem.getText(stats.toDate()));
				statsTable.setWidget(row, 1, hp);
				statsTable.getRowFormatter().setVerticalAlign(row,
						HasVerticalAlignment.ALIGN_BOTTOM);
				statsTable.getCellFormatter().setAlignment(row++, 1,
						HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_BOTTOM);
				statsTable.setWidget(row, 0, AjaxSystem
						.getLabel("Total Downloads:"));
				statsTable.setWidget(row, 1, AjaxSystem.getText(stats
						.getTotal()));
				statsTable.getCellFormatter().setAlignment(row++, 1,
						HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_BOTTOM);
				statsTable.setWidget(row, 0, AjaxSystem
						.getLabel("User Details:"));
				statsTable.getFlexCellFormatter().setColSpan(row, 0, 2);
				statsTable.getFlexCellFormatter().setAlignment(row++, 0,
						HasHorizontalAlignment.ALIGN_CENTER,
						HasVerticalAlignment.ALIGN_BOTTOM);
				for (int i = 0; i < stats.getUsers().length; i++) {
					DownloadUserStat user = stats.getUsers()[i];
					statsTable.setText(row, 0, user.getName());
					statsTable.setText(row, 1, user.getSizeString());
					statsTable.getCellFormatter().addStyleName(row, 0, "label");
					statsTable.getCellFormatter().setAlignment(row, 0,
							HasHorizontalAlignment.ALIGN_LEFT,
							HasVerticalAlignment.ALIGN_BOTTOM);
					statsTable.getRowFormatter().addStyleName(row,
							(i % 2 == 0 ? "even" : "odd"));
					statsTable.getCellFormatter().setAlignment(row++, 1,
							HasHorizontalAlignment.ALIGN_RIGHT,
							HasVerticalAlignment.ALIGN_BOTTOM);
				}
				Services.getServices().mainPanel.setEast(null);
				Services.getServices().mainPanel.setCenter(main);
				BusyPopup.done("Loading Download Stats");
			}

		};
		if (fromDate == null && toDate == null)
			Services.getServices().libraryInfoService.getDownloadStats(family
					.getFamilyName(), async);
		else
			Services.getServices().libraryInfoService.getDownloadStats(family
					.getFamilyName(), fromDate, toDate, async);
	}

	private Panel getKeywordUploadPanel(AjaxProductFamily family) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Import Keyword Metadata:"));
		VerticalPanel vp = new VerticalPanel();
		FormPanel form = new FormPanel("import-frame");
		form.setAction("keywordImport");
		form.setEncoding("multipart/form-data");
		form.setMethod("POST");
		FileUpload uploadFile = new FileUpload();
		uploadFile.setName("keyword_file");
		vp.add(uploadFile);
		form.add(vp);
		vp
				.add(new HTML(
						"<select name='charSet' size='1'><option value='' selected>Default(iso-8859-1)</option><option value='UNICODE'>Unicode</option><option value='utf-8'>UTF-8</option></select>"));
		vp.add(new HTML("<input type='submit' value='Upload File'>"));
		vp.add(new HTML("<input name='product_family' type='hidden' value='"
				+ family.getFamilyName() + "'>"));
		hp.add(form);
		iframe = new HTML(
				"<iframe height='600' width='100%' scrolling='auto' name='import-frame' style='border:0px;'></iframe>");
		iframe.setWidth("100%");
		iframe.setHeight("100%");
		Services.getServices().mainPanel.setEast(iframe);
		return hp;
	}

	private HorizontalPanel getRefreshPanel(AjaxProductFamily family) {
		HorizontalPanel hp;
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Refresh Library Cache:"));
		Button refreshButton = createRefreshButton(family.getFamilyName(), hp);
		hp.add(refreshButton);
		hp.add(refreshResponse);
		return hp;
	}

	private Button createRefreshButton(final String family, final Panel p) {
		return new Button("Refresh", new ClickListener() {

			public void onClick(Widget sender) {
				Services.getServices().libraryInfoService.refreshProductFamily(
						family, new AsyncCallback() {

							public void onFailure(Throwable caught) {
								refreshResponse
										.setText("Failed to refresh library");

							}

							public void onSuccess(Object result) {
								if (((Boolean) result).booleanValue())
									refreshResponse
											.setText("Library successfully refreshed");
								else
									refreshResponse
											.setText("Failed to refresh library");
							}
						});

			}

		});
	}

	private Panel getFieldPanel(AjaxProductFamily family) {
		HTMLTable table = new FlexTable();
		AjaxProductField[] fields = family.getFields();
		table.setCellPadding(15);
		table.setCellSpacing(0);
		Label fieldNameLabel = new Label("Category Name");
		FieldTableSort sorter = new FieldTableSort(family, table);
		fieldNameLabel.addClickListener(sorter);
		Label deleteColumn = new Label("Delete");
		Label typeLabel = new Label("Type");
		typeLabel.addClickListener(sorter);
		Label displayOrderLabel = new Label("Display Order");
		displayOrderLabel.addClickListener(sorter);
		Label searchOrderLabel = new Label("Search Order");
		searchOrderLabel.addClickListener(sorter);
		table.setWidget(0,0,deleteColumn);
		table.setWidget(0, 1, fieldNameLabel);
		table.setWidget(0, 2, typeLabel);
		table.setWidget(0, 3, displayOrderLabel);
		table.setWidget(0, 4, searchOrderLabel);
		table.getCellFormatter().addStyleName(0, 0, "header");
		table.getCellFormatter().addStyleName(0, 1, "header");
		table.getCellFormatter().addStyleName(0, 2, "header");
		table.getCellFormatter().addStyleName(0, 3, "header");
		table.getCellFormatter().addStyleName(0, 4, "header");
		setTableRowData(table, fields,family);
		table.addTableListener(new LibraryFieldListener(family));
		table.setWidth("100%");
		ScrollPanel tableScroll = new ScrollPanel(table);
		tableScroll.setWidth("100%");
		tableScroll.setHeight("100%");
		return tableScroll;
	}

	private void setTableRowData(HTMLTable table, AjaxProductField[] fields,AjaxProductFamily family) {
		for (int i = 0; i < fields.length; i++) {
			DeleteFieldIcon icon = new DeleteFieldIcon(fields[i].getName(),family,table,i+1);
			table.setWidget(i+1,0,icon);
			table.setText(i + 1, 1, fields[i].getName());
			if (i % 2 == 0)
				table.getRowFormatter().addStyleName(i + 1, "odd");
			else
				table.getRowFormatter().addStyleName(i + 1, "even");
			table.getCellFormatter().addStyleName(i + 1, 1, "label");
			table.setText(i + 1, 2, fields[i].getType());
			table
					.setText(i + 1, 3, String.valueOf(fields[i]
							.getDisplayOrder()));
			table.getCellFormatter().setAlignment(i + 1, 3,
					HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_TOP);
			table.setText(i + 1, 4, String.valueOf(fields[i].getSearchOrder()));
			table.getCellFormatter().setAlignment(i + 1, 4,
					HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_TOP);
		}
	}

	public void onTreeItemStateChanged(TreeItem item) {

	}

	public class FieldTableSort implements ClickListener, Comparator {
		AjaxProductFamily family;

		HTMLTable table;

		String l;

		boolean reverse;

		public FieldTableSort(AjaxProductFamily f, HTMLTable t) {
			family = f;
			table = t;
			reverse = false;
		}

		public void onClick(Widget sender) {
			BusyPopup.waitFor("Sorting...");
			String text = ((Label) sender).getText();
			if (text.equals(l))
				reverse = !reverse;
			else {
				reverse = false;
				l = text;
			}
			AjaxProductField[] fields = family.getFields();
			Arrays.sort(fields, this);
			setTableRowData(table, fields,family);
			BusyPopup.done("Sorting...");
		}

		public int compare(Object o1, Object o2) {
			AjaxProductField field1 = (AjaxProductField) o1;
			AjaxProductField field2 = (AjaxProductField) o2;
			if (l.equals("Category Name")) {
				return reverse ? field2.getName().compareTo(field1.getName())
						: field1.getName().compareTo(field2.getName());
			}
			if (l.equals("Type")) {
				return reverse ? field2.getType().compareTo(field1.getType())
						: field1.getType().compareTo(field2.getType());
			}
			if (l.equals("Display Order")) {
				return reverse ? field2.getDisplayOrder()
						- field1.getDisplayOrder() : field1.getDisplayOrder()
						- field2.getDisplayOrder();
			}
			if (l.equals("Search Order")) {
				return reverse ? field2.getSearchOrder()
						- field1.getSearchOrder() : field1.getSearchOrder()
						- field2.getSearchOrder();
			}
			return 0;
		}
	}

}
