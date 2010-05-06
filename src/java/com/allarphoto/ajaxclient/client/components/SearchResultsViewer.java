package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Redrawable;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.components.icons.SimpleIcon;

public class SearchResultsViewer extends DockPanel implements Redrawable {
	protected String family;

	protected Panel controls;

	protected MultiProductPanel center;

	protected AjaxProduct[] products;

	protected ClickListener numberToShowClick;

	protected int offset = 0;

	protected int totalFound = 0;

	protected Label currentlyDisplayed;

	protected String[][] selectedValues;

	protected int numToShow = 5;

	boolean restart = false;

	protected HorizontalPanel prevNextPanel = new HorizontalPanel();

	protected NumberSelector displayNumSelect;

	// CheckBox five, twelve, twenty, fifty, hundred, fiveHundred;

	public SearchResultsViewer() {

	}

	public SearchResultsViewer(String[][] selectedValues, String familyName) {
		family = familyName;
		currentlyDisplayed = AjaxSystem.getText("", "header");
		addStyleName("results-view");
		center = new MultiProductPanel();
		this.selectedValues = selectedValues;
		initControls();
		resetImages(selectedValues, familyName);
	}

	public void redraw() {
		clear();
		initControls();
		updatePrevNextPanel();
		add(center, DockPanel.CENTER);
		center.redraw();
	}

	public void resetImages(String[][] selectedValues, String familyName) {
		family = familyName;
		this.selectedValues = selectedValues;
		BusyPopup.waitFor("Loading Images...");
		offset = 0;
		Services.getServices().libraryInfoService.getProducts(selectedValues,
				familyName, numToShow, offset, getInitCallback());
	}

	protected AsyncCallback getInitCallback() {
		final Widget parent = this;
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				BusyPopup.done("Loading Images...");
				new PopupWarning(arg0.toString());
			}

			public void onSuccess(Object results) {
				if (!center.isAttached()) {
					add(center, DockPanel.CENTER);
				}
				center.clear();
				products = (AjaxProduct[]) results;
				center.add(products);
				updateImageCount();
				BusyPopup.done("Loading Images...");
			}

		};
	}

	protected void updateImageCount() {
		Services.getServices().libraryInfoService.getProductCount(
				selectedValues, family, new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						totalFound = ((Integer) result).intValue();
						updatePrevNextPanel();
						updateCurrentlyDisplayed();
					}

				});
	}

	protected void updatePrevNextPanel() {
		prevNextPanel.clear();
		if (offset > 0) {
			SimpleIcon previous = new SimpleIcon("previous.gif",
					new ClickListener() {

						public void onClick(Widget sender) {
							adjustOffsetShown(Math.max(offset - numToShow, 0));
						}

					}, "Get previous " + numToShow + " images");
			prevNextPanel.add(previous);
			prevNextPanel.setCellHorizontalAlignment(previous,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			SimpleIcon disabledPrevious = new SimpleIcon(
					"previous_disabled.gif", new ClickListener() {
						public void onClick(Widget sender) {
						}
					}, "");
			prevNextPanel.add(disabledPrevious);
			prevNextPanel.setCellHorizontalAlignment(disabledPrevious,
					HasHorizontalAlignment.ALIGN_LEFT);

		}
		prevNextPanel.add(displayNumSelect);
		prevNextPanel.setCellHorizontalAlignment(displayNumSelect,
				HasHorizontalAlignment.ALIGN_CENTER);
		displayNumSelect.addStyleName("number-selector");
		if (offset + numToShow < totalFound) {
			SimpleIcon next = new SimpleIcon("next.gif", new ClickListener() {

				public void onClick(Widget sender) {
					adjustOffsetShown(Math.min(offset + numToShow, totalFound
							- numToShow));
				}

			}, "Get next " + numToShow + " images");
			prevNextPanel.add(next);
			prevNextPanel.setCellHorizontalAlignment(next,
					HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			SimpleIcon disabledNext = new SimpleIcon("next_disabled.gif",
					new ClickListener() {
						public void onClick(Widget sender) {
						}
					}, "");
			prevNextPanel.add(disabledNext);
			prevNextPanel.setCellHorizontalAlignment(disabledNext,
					HasHorizontalAlignment.ALIGN_LEFT);

		}
		prevNextPanel.setWidth(String
				.valueOf((int) (center.getOffsetWidth() * .90))
				+ "px");
	}

	protected AsyncCallback getSecondaryCallback() {
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				BusyPopup.done("Retrieving Images...");
				new PopupWarning(arg0.toString());
			}

			public void onSuccess(Object r) {
				updateImageCount();
				AjaxProduct[] result = (AjaxProduct[]) r;
				AjaxProduct[] newProducts = new AjaxProduct[numToShow];
				if (numToShow == result.length || restart) {
					AjaxSystem.arraycopy(result, 0, newProducts, 0, Math.min(
							result.length, newProducts.length));
					center.clear();
					restart = false;
				} else {
					AjaxSystem.arraycopy(products, 0, newProducts, 0,
							products.length);
					AjaxSystem.arraycopy(result, 0, newProducts,
							products.length, result.length);
				}
				products = newProducts;
				BusyPopup.done("Retrieving Images...");
				center.add(result);
				updateCurrentlyDisplayed();
			}

		};
	}

	protected void initControls() {
		numberToShowClick = new ClickListener() {

			public void onClick(Widget w) {
				if (w instanceof Button) {
					int num = Integer.parseInt(((Button) w).getText());
					adjustNumShown(num);
				} else if (w instanceof Label) {
					int num = Integer.parseInt(((Label) w).getText());
					adjustNumShown(num);
				}
			}

		};/*
			 * five = new CheckBox("5");
			 * five.addClickListener(numberToShowClick); five.setChecked(true);
			 * twelve = new CheckBox("12");
			 * twelve.addClickListener(numberToShowClick); twenty = new
			 * CheckBox("20"); twenty.addClickListener(numberToShowClick); fifty =
			 * new CheckBox("50"); fifty.addClickListener(numberToShowClick);
			 * hundred = new CheckBox("100");
			 * hundred.addClickListener(numberToShowClick); fiveHundred = new
			 * CheckBox("500"); fiveHundred.addClickListener(numberToShowClick);
			 */
		displayNumSelect = (NumberSelector) Services.getServices().factory
				.createComponent("NumberSelector", new Object[] {
						new int[] { 5, 12, 20, 50, 100 }, numberToShowClick,
						new Integer(numToShow) });
		/*
		 * numbers.add(five); numbers.add(twelve); numbers.add(twenty);
		 * numbers.add(fifty); numbers.add(hundred); numbers.add(fiveHundred);
		 */
		controls = new DockPanel();
		VerticalPanel vp = new VerticalPanel();
		vp.add(currentlyDisplayed);
		vp.setCellHorizontalAlignment(currentlyDisplayed,
				HasHorizontalAlignment.ALIGN_CENTER);
		((DockPanel) controls).add(vp, DockPanel.CENTER);
		((DockPanel) controls).setCellHorizontalAlignment(vp,
				HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setSpacing(10);
		hp.add(prevNextPanel);
		Label export = createExportButton();
		hp.add(export);
		((DockPanel) controls).add(hp, DockPanel.SOUTH);
		// numbers.setSpacing(10);
		// numbers.addStyleName("results-display-control");
		add(controls, DockPanel.NORTH);
		setCellHorizontalAlignment(controls,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	protected Label createExportButton() {
		Label export = AjaxSystem.getText("Export Metadata", "linked");
		export
				.setTitle("Download keyword metadata for all current search results");
		export.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Window
						.open(
								"/lazerweb/keyword_export.vtl?action=keyword_file_export",
								"keyword_export", "");
			}

		});
		return export;
	}

	protected void adjustNumShown(int newNum) {
		numToShow = newNum;
		if (newNum > center.size()) {
			BusyPopup.waitFor("Retrieving Images...");
			if (newNum > totalFound - offset) {
				offset = totalFound - newNum;
				if (offset <= 0) {
					offset = 0;
					restart = true;
				}
				Services.getServices().libraryInfoService.getProducts(
						selectedValues, family, newNum, offset,
						getSecondaryCallback());
			} else {
				Services.getServices().libraryInfoService.getProducts(
						selectedValues, family, newNum - center.size(), offset
								+ center.size(), getSecondaryCallback());
			}
		} else {
			center.changeNumShown(numToShow);
			updatePrevNextPanel();
			updateCurrentlyDisplayed();
		}
	}

	protected void updateCurrentlyDisplayed() {
		currentlyDisplayed.setText("Viewing Images " + (offset + 1) + " - "
				+ (offset + Math.min(center.numShown, center.size())) + " of "
				+ totalFound);
	}

	protected void adjustOffsetShown(int newOffset) {
		if (newOffset < 0)
			newOffset = 0;
		else if (newOffset > totalFound - numToShow)
			newOffset = totalFound - numToShow;
		if (newOffset == 0)
			restart = true;
		BusyPopup.waitFor("Retrieving Images...");
		offset = newOffset;
		Services.getServices().libraryInfoService.getProducts(selectedValues,
				family, numToShow, newOffset, getSecondaryCallback());
	}

}
