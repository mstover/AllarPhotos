package com.lazerinc.ajaxclient.client.uploader;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.PopupWarning;

public class Uploader extends HorizontalPanel {

	Services services = Services.getServices();

	TreeControls treeControl;

	FormPanel uploadForm;

	public Uploader(TreeControls treeControl) {
		super();
		this.treeControl = treeControl;
		init();
	}

	public void init() {
		VerticalPanel fieldPanel = new VerticalPanel();
		setStyleName("center");
		fieldPanel.add(new Label("Choose File for Upload"));
		uploadForm = new FormPanel("upload-frame");
		uploadForm.setAction("sendit.upload");
		uploadForm.setEncoding("multipart/form-data");
		uploadForm.setMethod("POST");
		FileUpload uploadField = new FileUpload();
		uploadField.setName("file-to-upload");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(uploadField);
		hp.add(new Button("Upload File", new ClickListener() {

			public void onClick(Widget sender) {
				treeControl.updateCategories();
				BusyPopup.waitFor("Sending upload data");
				uploadForm.submit();
				Timer t = new Timer() {

					public void run() {
						Services.getServices().uploadService
								.isUploadDone(new AsyncCallback() {

									public void onFailure(Throwable arg0) {
										new PopupWarning(
												"Warning, upload probably failed");
										BusyPopup.done("Sending upload data");
									}

									public void onSuccess(Object val) {
										if (((Boolean) val).booleanValue()) {
											cancel();
											Services.getServices().uploadService
													.getMetaCategories(new AsyncCallback() {

														public void onFailure(
																Throwable arg0) {
															new PopupWarning(
																	"Warning, upload probably failed");
															BusyPopup
																	.done("Sending upload data");
														}

														public void onSuccess(
																Object result) {
															if (result != null) {
																new UploadMetaDataScreen(
																		(String[][]) result);
															}
															BusyPopup
																	.done("Sending upload data");
														}

													});
										}

									}

								});
					}

				};
				t.scheduleRepeating(2000);
			}

		}));
		hp
				.add(new HTML(
						"<input id='hidden-categories' name='categories' type='hidden' value=''>"));
		hp
				.add(new HTML(
						"<input id='hidden-has-new-cat' name='has_new_cat' type='hidden' value='false'>"));
		uploadForm.add(hp);
		fieldPanel.add(uploadForm);
		fieldPanel.add(new Label(
				"Select an individual image to upload to a category chosen in the tree on the left, "
						+ " or select a zipfile of images to upload."));
		fieldPanel.add(new Label(
				"Filename must not contain the following illegal characters: /\\\":;'`!&*"));
		add(fieldPanel);
		add(new HTML(
				"<iframe name='upload-frame' style='height:"+Services.getServices().mainPanel.getCenterHeight()+"px;width:"+
				(Services.getServices().mainPanel.getCenterWidth()*.6) + "px;border:0'></iframe>"));
	}

}
