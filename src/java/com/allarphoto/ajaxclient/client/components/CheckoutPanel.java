package com.allarphoto.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxOrderResponse;
import com.allarphoto.ajaxclient.client.beans.Request;

public class CheckoutPanel extends HorizontalPanel {
	AjaxCart cart;

	Widget screen;

	AjaxAddress aa;

	VerifyScreen verify;

	int stage = 0;

	Button next;

	public CheckoutPanel(AjaxCart c) {
		super();
		cart = c;
		init();
	}

	private void init() {

		stage = 0;
		screen = Services.getServices().factory.createComponent("VerifyScreen",
				new Object[] { cart, this });
		verify = (VerifyScreen) screen;
		add(screen);
		setSpacing(25);
		next = getNextButton();
		add(next);
	}

	public void clickNext() {
		next.click();
	}

	private Button getNextButton() {
		final CheckoutPanel t = this;
		Button next = new Button("Next", new ClickListener() {

			public void onClick(Widget arg0) {
				if (stage == 0) {
					VerifyScreen vs = (VerifyScreen) screen;
					if (vs.hasNextTab()) {
						if (vs.isCurrentTabDone())
							vs.nextTab();
						else
							new PopupWarning(vs.getTabWarning());
					} else if (vs.isDone()) {
						clear();
						if (containsOrder(cart)) {
							screen = new UserInfoCheckoutScreen(cart, t);
							add(screen);
							add(getNextButton());
							stage = 1;
						} else {
							clear();
							executeOrder();
							stage = 2;
						}
					} else
						new PopupWarning(vs.getWarning());
				} else if (stage == 1) {
					clear();
					aa = ((UserInfoCheckoutScreen) screen).getAddress();
					if(aa.isValid()) {
					executeOrder();
					stage = 2;
					}
					else  {
						new PopupWarning(aa.getInvalidMessage());
						screen = new UserInfoCheckoutScreen(cart, t);
						add(screen);
						add(getNextButton());
						stage = 1;
					}

				}

			}

		});
		next.addStyleName("top-buffer");
		return next;
	}

	private void executeOrder() {
		BusyPopup.waitFor("Submitting Order");
		Services.getServices().orderService.executeOrder(verify.packages, aa,
				new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						BusyPopup.done("Submitting Order");
					}

					public void onSuccess(Object arg0) {
						VerticalPanel vp = new VerticalPanel();
						screen = vp;
						AjaxOrderResponse order = (AjaxOrderResponse) arg0;
						add(screen);
						if (order.getOrderNos().size() > 0) {
							Iterator iter = order.getOrderNos().iterator();
							while (iter.hasNext()) {
								HorizontalPanel hp = new HorizontalPanel();
								hp.add(AjaxSystem.getLabel("Order No:"));
								String no = (String) iter.next();
								Label orderno = AjaxSystem.getText(no);
								orderno.addClickListener(jumpToOrder(no));
								orderno.addStyleName("linked");
								hp.add(orderno);
								vp.add(hp);
							}
						}
						Iterator iter = order.getInfo().keySet().iterator();
						List downloadFiles = new ArrayList();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							if (!key.equals("zip_file_count")
									&& !key.equals("Total Cost")) {
								HorizontalPanel hp = new HorizontalPanel();
								if (key.startsWith("download_file_")) {
									Label l = AjaxSystem.getLabel(
											"Download File:", "download-link");
									hp.add(l);
									hp.setCellVerticalAlignment(l,
											HasVerticalAlignment.ALIGN_BOTTOM);
									DownloadLink link = new DownloadLink(
											(String) order.getInfo().get(key));
									hp.add(link);
									hp.setCellVerticalAlignment(link,
											HasVerticalAlignment.ALIGN_BOTTOM);
									downloadFiles.add(link);
								} else {
									hp.add(AjaxSystem.getLabel(key + ":"));
									hp.add(AjaxSystem.getText((String) order
											.getInfo().get(key)));
								}
								vp.add(hp);
							}
						}
						createEmailPanel(downloadFiles, vp, order);
						vp.setSpacing(20);
						BusyPopup.done("Submitting Order");
						cart.clear();
						stage = 0;
					}

				});
	}

	protected void createEmailPanel(final List downloadFiles, Panel p,
			final AjaxOrderResponse order) {
		if (downloadFiles.size() > 0) {
			final TextBox emailAddress = new TextBox();
			VerticalPanel vp = new VerticalPanel();
			vp.setSpacing(5);
			vp
					.add(AjaxSystem
							.getLabel("You may send the above download links to an email address"));
			vp.add(AjaxSystem.getLabel("Email Address:"));
			vp.add(emailAddress);
			vp.add(AjaxSystem.getLabel("Message:"));
			final TextArea message = new TextArea();
			vp.add(message);
			p.add(vp);
			p.add(new Button("Send Email", new ClickListener() {

				public void onClick(Widget sender) {
					if (emailAddress.getText().length() > 0) {
						Services.getServices().orderService.sendLinkEmail(
								emailAddress.getText(), message.getText(),
								order.getFamilies(),
								getLinkList(downloadFiles),
								new AsyncCallback() {

									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(Object result) {
										Window
												.alert("Email successfully sent!");

									}

								});
					} else
						Window
								.alert("You must specify a valid email address to send the link to "
										+ order.getInfo().toString());
				}
			}));
		}
	}

	protected String[] getLinkList(List downloadFiles) {
		Iterator iter = downloadFiles.iterator();
		String[] links = new String[downloadFiles.size()];
		int x = 0;
		while (iter.hasNext()) {
			DownloadLink link = (DownloadLink) iter.next();
			links[x++] = link.getLinkUrl();
		}
		return links;

	}

	private ClickListener jumpToOrder(final String orderno) {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				screen = Services.getServices().factory.createComponent("OrderDisplay",new Object[]{orderno});
				clear();
				add(screen);
			}

		};
	}

	private boolean containsOrder(AjaxCart cart) {
		Iterator iter = cart.getRequests().iterator();
		while (iter.hasNext()) {
			Request r = (Request) iter.next();
			if (!r.isDownload())
				return true;
		}
		return false;
	}

	public void refresh() {
		clear();
		init();
	}

	public void workflowInsert(Widget insert) {
		clear();
		add(insert);
	}

	public void endWorkflowinsert() {
		clear();
		add(screen);
		setSpacing(25);
		add(getNextButton());
	}

}
