package com.lazerinc.ajaxclient.client.components.irwin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.MultiProductPanel;
import com.lazerinc.ajaxclient.client.components.NumberSelector;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.SearchResultsViewer;
import com.lazerinc.ajaxclient.client.components.VerticalProductPanel;
import com.lazerinc.ajaxclient.client.components.icons.SimpleIcon;

public class IrwinSearchResultsViewer extends SearchResultsViewer {

	public IrwinSearchResultsViewer(String[][] selectedValues, String familyName) {
		family = familyName;
		currentlyDisplayed = AjaxSystem.getText("", "header");
		numToShow = 12;
		addStyleName("results-view");
		center = new MultiProductPanel();
		this.selectedValues = selectedValues;
		initControls();
		resetImages(selectedValues, familyName);
		center = new MultiProductPanel(130);
	}

	public void resetImages(String[][] selectedValues, String familyName) {
		if (selectedValues != null && selectedValues.length > 0) {
			family = familyName;
			this.selectedValues = selectedValues;
			BusyPopup.waitFor("Loading Images...");
			offset = 0;
			Services.getServices().libraryInfoService.getProducts(
					selectedValues, familyName, numToShow, offset,
					getInitCallback());
		}
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
		controls = new VerticalPanel();
		((VerticalPanel) controls)
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.setSpacing(10);
		hp.add(currentlyDisplayed);
		hp.setCellHorizontalAlignment(currentlyDisplayed,
				HasHorizontalAlignment.ALIGN_LEFT);
		hp.add(getSortControls());
		hp.add(getViewControls());
		hp.add(prevNextPanel);
		Label l = createExportButton();
		hp.add(l);
		hp.setCellHorizontalAlignment(l, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(prevNextPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setWidth(String.valueOf((int) (center.getWidth() * .90)) + "px");
		controls.setWidth(String.valueOf((int) (center.getWidth() * .90))
				+ "px");
		controls.add(hp);
		// numbers.setSpacing(10);
		// numbers.addStyleName("results-display-control");
		add(controls, DockPanel.NORTH);
		setCellHorizontalAlignment(controls,
				HasHorizontalAlignment.ALIGN_CENTER);
		if (selectedValues == null) {
			add(getWelcomeScreen(), DockPanel.CENTER);
			controls.setVisible(false);
		} else
			welcomeScreen = null;
	}

	protected void updateCurrentlyDisplayed() {
		currentlyDisplayed.setText(totalFound + " Results " + " Viewing "
				+ (offset + 1) + " - "
				+ (offset + Math.min(center.getNumShown(), center.size())));
	}

	public void redraw() {
		clear();
		initControls();
		updatePrevNextPanel();
		if (welcomeScreen == null)
			add(center, DockPanel.CENTER);
		center.redraw();
	}

	protected AsyncCallback getInitCallback() {
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				BusyPopup.done("Loading Images...");
				new PopupWarning(arg0.toString());
			}

			public void onSuccess(Object results) {
				controls.setVisible(true);
				if (!center.isAttached()) {
					if(welcomeScreen != null && welcomeScreen.isAttached())
						remove(welcomeScreen);
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

	VerticalPanel welcomeScreen;

	protected Widget getWelcomeScreen() {
		Label welcome = AjaxSystem.getTitle("Welcome to the");
		Image logo = new Image("irwin/images/irwin-industrial-lrg.gif");
		Label tagline = AjaxSystem
				.getTitle("Hand Tools & PTA Image Toolbox");
		Label instructions = AjaxSystem
				.getText("To get started, simply click on the logo of the brand listed above that you wish to search through.");
		welcomeScreen = new VerticalPanel();
		welcomeScreen.add(welcome);
		welcomeScreen.add(logo);
		welcomeScreen.add(tagline);
		welcomeScreen.add(instructions);
		welcomeScreen.addStyleName("welcome-screen");
		welcomeScreen.addStyleName("image-viewer");
		return welcomeScreen;
	}

	protected void updatePrevNextPanel() {
		prevNextPanel.clear();
		prevNextPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
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
	}

	VerticalPanel sortControls, viewControls;

	protected Widget getViewControls() {
		if (viewControls != null)
			return viewControls;
		viewControls = new VerticalPanel();
		viewControls
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		viewControls.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		Label l = AjaxSystem.getText("View Style:");
		viewControls.add(l);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		RadioButton listView = new RadioButton("view-type", "list");
		hp.add(listView);
		RadioButton galleryView = new RadioButton("view-type", "gallery");
		hp.add(galleryView);
		galleryView.setChecked(true);
		listView.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				if (!(center instanceof VerticalProductPanel)) {
					center = new VerticalProductPanel(100);
					redraw();
					adjustOffsetShown(offset);
				}

			}

		});
		galleryView.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				if (center instanceof VerticalProductPanel) {
					center = new MultiProductPanel(130);
					redraw();
					adjustOffsetShown(offset);
				}

			}

		});
		viewControls
				.setTitle("Toggle between a list or gallery view of the search results");
		viewControls.add(hp);
		return viewControls;
	}

	protected Widget getSortControls() {
		if (sortControls != null)
			return sortControls;
		sortControls = new VerticalPanel();
		sortControls
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		sortControls.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		Label l = AjaxSystem.getText("Sort by");
		sortControls.add(l);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		RadioButton byDate = new RadioButton("sort-type", "date");
		hp.add(byDate);
		RadioButton byName = new RadioButton("sort-type", "name");
		hp.add(byName);
		byName.setChecked(true);
		byDate.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Services.getServices().libraryInfoService.setProductSorter(
						"date", new AsyncCallback() {

							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Object result) {
								adjustOffsetShown(offset);
							}

						});

			}

		});
		byName.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Services.getServices().libraryInfoService.setProductSorter(
						"name", new AsyncCallback() {

							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Object result) {
								adjustOffsetShown(offset);
							}

						});

			}

		});
		sortControls
				.setTitle("Toggle between sorting search results by date or by name");
		sortControls.add(hp);
		return sortControls;
	}

}
