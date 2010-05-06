package com.lazerinc.ajaxclient.client.components;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.icons.CloseIcon;
import com.lazerinc.ajaxclient.client.components.icons.NextIcon;
import com.lazerinc.ajaxclient.client.components.icons.PreviousIcon;

public class ProductPopup extends PopupPanel implements ClickListener {
	AjaxProduct product;

	AjaxProduct origProduct;

	Request request;

	boolean isCart;

	public ProductPopup(AjaxProduct p) {
		super(true);
		product = p;
		origProduct = p;
	}

	public ProductPopup(AjaxProduct p, boolean cart) {
		this(p);
		isCart = cart;
	}

	public ProductPopup(Request r) {
		this(r.getProduct(), true);
		request = r;
	}

	public void onClick(Widget arg0) {
		AjaxSystem.clearPopup();
		clear();
		product = origProduct;
		init();
		addStyleName("product-popup");
		setPopupPosition((int) (Window.getClientWidth() / 3.9), Window
				.getClientHeight() / 10);
		setVisible(true);
		show();
		AjaxSystem.setPopup(this);
		final int maxHeight = Integer.parseInt(AjaxSystem.getHeightToWindowBottom(Window
				.getClientHeight() / 10 + 20));
		Timer t = new Timer() {

			public void run() {
				if(getOffsetHeight() <= 10) schedule(500);	
				else
					if(getOffsetHeight() > maxHeight)
						setHeight(maxHeight+"px");
			}
			
		};
		t.schedule(500);
		
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
		setProduct(request.getProduct());
	}

	public AjaxProduct getProduct() {
		return product;
	}

	public void setProduct(AjaxProduct product) {
		this.product = product;
	}

	public void init() {
		VerticalPanel vp = new VerticalPanel();
		Widget w = getControls();
		vp.add(w);
		vp.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_LEFT);
		Label l = AjaxSystem.getTitle(product.getName());
		vp.add(l);
		vp.setCellHorizontalAlignment(l, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(createPreviewImage(product));
		add(vp);
	}

	protected Widget getControls() {
		HorizontalPanel hp = new HorizontalPanel();
		Widget prevBut = new PreviousIcon(this, isCart);
		Widget nextBut = new NextIcon(this, isCart);
		Widget closeBut = new CloseIcon(this);
		hp.add(prevBut);
		hp.add(closeBut);
		hp.add(nextBut);
		hp.setSpacing(5);
		return hp;
	}

	private Widget createPreviewImage(AjaxProduct product) {
		final HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel panel = new VerticalPanel();
		ProductDisplayImage img = new ProductDisplayImage(product, 0);
		panel.add(img);
		panel.add(Services.getServices().factory.createComponent(
				"ProductToolbar", new Object[] { product }));
		hp.add(panel);
		initFields(product, hp);
		hp.setSpacing(10);
		return hp;
	}

	protected void initFields(AjaxProduct product, final HorizontalPanel hp) {
		Services.getServices().libraryInfoService.getDisplayableFields(product
				.getFamilyName(), new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(arg0.toString());

			}

			public void onSuccess(Object results) {
				hp.add(getKeywordPanel((List) results));

			}

		});
	}

	protected Panel getKeywordPanel(List displayFields) {
		Grid keyPanel = new Grid(displayFields.size(), 2);
		for (int i = 0; i < displayFields.size(); i++) {
			keyPanel.setWidget(i, 0, AjaxSystem.getLabel(displayFields.get(i)
					+ ":"));
			keyPanel.getCellFormatter().setHorizontalAlignment(i,0, HasHorizontalAlignment.ALIGN_LEFT);
			if ("File Name".equals(displayFields.get(i)))
				keyPanel.setWidget(i, 1, AjaxSystem.getText(product.getName()));
			else if("Creation Date".equals(displayFields.get(i)))
				keyPanel.setWidget(i,1,AjaxSystem.getText(product.getDateCreated()));
			else if("Catalog Date".equals(displayFields.get(i)))
				keyPanel.setWidget(i,1,AjaxSystem.getText(product.getDateCataloged()));
			else if("Modified Date".equals(displayFields.get(i)))
				keyPanel.setWidget(i,1,AjaxSystem.getText(product.getDateModified()));
			else
				keyPanel.setWidget(i, 1, AjaxSystem.getText(product
						.getValue((String) displayFields.get(i))));
			keyPanel.getCellFormatter().setHorizontalAlignment(i,1, HasHorizontalAlignment.ALIGN_LEFT);
		}
		keyPanel.setCellPadding(0);
		keyPanel.setCellSpacing(0);
		return new ScrollPanel(keyPanel);
	}

}
