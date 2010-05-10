package com.allarphoto.ajaxclient.client.components;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.FormField;
import com.allarphoto.ajaxclient.client.beans.OrderVerificationPackage;
import com.allarphoto.ajaxclient.client.beans.Request;

public class VerifyScreen extends VerticalPanel {
	protected AjaxCart cart;

	protected TabPanel tabbed;

	RadioButton accept, refuse;

	CheckoutPanel checkOut;

	protected List packages;

	public VerifyScreen(AjaxCart c, CheckoutPanel checkout) {
		checkOut = checkout;
		cart = c;
		init();
	}

	protected void init() {
		tabbed = new TabPanel();
		Services.getServices().orderService.getOrderVerificationPackages(cart,
				new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						new PopupWarning(
								"Failed to get order verification package info "
										+ arg0);

					}

					public void onSuccess(Object results) {
						packages = (List) results;
						createDisplay();
						if (isDone())
							checkOut.clickNext();
					}

				});
		add(AjaxSystem.getTitle("Checkout"));
		add(tabbed);
		setSpacing(20);

	}

	protected void addLicenseInfo(DockPanel p, String license) {
		HTML licenseText = new HTML(license);
		licenseText.addStyleName("license-text");
		p.add(licenseText, DockPanel.NORTH);
	}

	protected void addVerifyFormElements(DockPanel p, List fields) {
		Grid g = new Grid(fields.size(), 2);
		Iterator iter = fields.iterator();
		int count = 0;
		while (iter.hasNext()) {
			FormField ff = (FormField) iter.next();
			g.setText(count, 0, ff.getName());
			if (ff.isRequired())
				g.getCellFormatter().addStyleName(count, 0, "required");
			g.setWidget(count, 1, ff.createField());
			count++;
		}
		p.add(g, DockPanel.CENTER);
	}

	protected void addControlButtons(DockPanel p) {
		HorizontalPanel hp = new HorizontalPanel();
		accept = new RadioButton("Accept Usage Rights", "license");
		refuse = new RadioButton("Refuse Usage Rights", "license");
		hp.add(accept);
		hp.add(refuse);
		hp.setSpacing(20);
		p.add(hp, DockPanel.SOUTH);
	}

	public boolean hasNextTab() {
		int c = tabbed.getTabBar().getSelectedTab();
		if (c < tabbed.getTabBar().getTabCount() - 1)
			return true;
		else
			return false;
	}

	public boolean isCurrentTabDone() {
		return ((OrderVerificationPackage) packages.get(tabbed.getTabBar()
				.getSelectedTab())).isComplete();
	}

	public void nextTab() {
		tabbed.selectTab(tabbed.getTabBar().getSelectedTab() + 1);
	}

	public String getTabWarning() {
		return ((OrderVerificationPackage) packages.get(tabbed.getTabBar()
				.getSelectedTab())).getWarning();
	}

	public String getWarning() {
		Iterator iter = packages.iterator();
		int count = 0;
		while (iter.hasNext()) {
			OrderVerificationPackage p = (OrderVerificationPackage) iter.next();
			if (!p.isComplete()) {
				tabbed.selectTab(count);
				return p.getWarning();
			}
		}
		return "";
	}

	public boolean isDone() {
		Iterator iter = packages.iterator();
		while (iter.hasNext()) {
			if (!((OrderVerificationPackage) iter.next()).isComplete())
				return false;
		}
		return true;
	}

	protected Widget getOrderDisplay(List requests) {
		Grid vp = new Grid(requests.size(), 3);
		Iterator iter = requests.iterator();
		vp.addStyleName("order-display-table");
		int count = 0;
		while (iter.hasNext()) {
			Request r = (Request) iter.next();
			vp.setWidget(count, 0, OrderDisplay.createProductImage(r
					.getProduct()));
			vp.setWidget(count, 1, OrderDisplay.createUsageRightsPanel(r
					.getProduct()));
			vp.setWidget(count, 2, AjaxSystem.getText(r.toString()));
			count++;
		}
		return new ScrollPanel(vp);
	}

	protected void createDisplay() {
		Iterator iter = packages.iterator();
		while (iter.hasNext()) {
			OrderVerificationPackage ovp = (OrderVerificationPackage) iter
					.next();
			VerticalPanel newTab = new VerticalPanel();
			DockPanel dp = new DockPanel();
			addLicenseInfo(dp, ovp.getLicenseText());
			addVerifyFormElements(dp, ovp.getFields());
			if (ovp.getLicenseText() != null
					&& ovp.getLicenseText().length() > 0)
				addControlButtons(dp);
			newTab.add(dp);
			Widget w = getOrderDisplay(ovp.getRequests());
			newTab.add(w);
			w.setHeight(AjaxSystem
					.getHeightToWindowBottom(w.getAbsoluteTop() + 25));
			newTab.setSpacing(10);
			tabbed.add(newTab, ovp.getFamily());
		}
		tabbed.selectTab(0);
	}

}
