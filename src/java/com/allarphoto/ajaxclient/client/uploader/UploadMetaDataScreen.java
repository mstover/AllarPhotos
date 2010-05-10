package com.allarphoto.ajaxclient.client.uploader;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class UploadMetaDataScreen extends PopupPanel {
	FlexTable table;

	String[][] categories;

	String[] primaryNames;

	boolean failure = false;

	TextBox[][] fields;

	public UploadMetaDataScreen() {
	}

	public UploadMetaDataScreen(String[][] cats) {
		super(false);
		this.categories = cats;
		table = new FlexTable();
		init();
		Services.getServices().uploadService
				.getPrimaryNames(new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						failure = true;
						new PopupWarning("failure");
					}

					public void onSuccess(Object result) {
						primaryNames = (String[]) result;
						fields = new TextBox[primaryNames.length][categories[0].length
								+ categories[1].length];
						int row = 1;
						int col = 0;
						for (int i = 0; i < primaryNames.length; i++) {
							col = 0;
							table.setText(row, col, primaryNames[i]);
							for (int j = 0; j < categories[0].length
									+ categories[1].length; j++) {
								col++;
								fields[i][j] = new TextBox();
								fields[i][j].addStyleName("spreadsheet");
								table.setWidget(row, col, fields[i][j]);
							}
							row++;
						}
						table.getFlexCellFormatter().setColSpan(row, 0,
								categories[0].length + categories[1].length);
					}

				});
		addStyleName("popup-info");
		setPopupPosition(Window.getClientWidth() / 4, (int) (Window
				.getClientHeight() / 2.5));
		setVisible(true);
		show();
		final int maxHeight = Integer
				.parseInt(AjaxSystem.getHeightToWindowBottom((int) (Window
						.getClientHeight() / 2.5)) + 15);
		Timer t = new Timer() {

			public void run() {
				if (getOffsetHeight() <= 10)
					schedule(500);
				else if (getOffsetHeight() > maxHeight)
					setHeight(AjaxSystem.getHeightToWindowBottom(maxHeight));
			}

		};
		t.schedule(500);
	}

	private String[][] getMetaData() {
		String[][] meta = new String[primaryNames.length][categories[0].length
				+ categories[1].length + 1];
		for (int i = 0; i < primaryNames.length; i++) {
			meta[i][0] = primaryNames[i];
			for (int j = 0; j < categories[0].length; j++) {
				String value = fields[i][j].getText();
				if (value == null || value.length() < 1) {
					new PopupWarning("Required field " + categories[0][j]
							+ " must have value for image " + primaryNames[i]);
					return null;
				} else
					meta[i][j + 1] = value;
			}
			for (int j = 0; j < categories[1].length; j++) {
				meta[i][j + categories[0].length + 1] = fields[i][j
						+ categories[0].length].getText();
			}
		}
		return meta;
	}

	private void init() {
		table.setBorderWidth(1);
		table.setCellPadding(0);
		table.setCellSpacing(0);
		int row = 0;
		int col = 1;
		table.setWidget(row, 0, new Button("Done", new ClickListener() {

			public void onClick(Widget arg0) {
				String[][] meta = getMetaData();
				if (meta != null) {
					BusyPopup.waitFor("Uploading Meta Data");
					Services.getServices().uploadService.uploadMeta(meta,
							new AsyncCallback() {

								public void onFailure(Throwable arg0) {
									new PopupWarning(
											"Failed to upload meta data");
									BusyPopup.done("Uploading Meta Data");
									hide();
								}

								public void onSuccess(Object result) {
									if (!((Boolean) result).booleanValue()) {
										new PopupWarning(
												"Failed to upload meta data");
									}
									BusyPopup.done("Uploading Meta Data");
									hide();
								}

							});
				}
			}

		}));
		for (int i = 0; i < categories[0].length; i++) {
			table.setText(row, col, categories[0][i]);
			table.getCellFormatter().addStyleName(row, col, "header");
			table.getCellFormatter().addStyleName(row, col, "required");
			col++;
		}
		for (int i = 0; i < categories[1].length; i++) {
			table.setText(row, col, categories[1][i]);
			table.getCellFormatter().addStyleName(row, col, "header");
			col++;
		}
		ScrollPanel sp = new ScrollPanel(table);
		sp.addStyleName("spreadsheet");
		add(sp);

	}

}
