package com.allarphoto.ajaxclient.client.components.abi;

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
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.MultiProductPanel;
import com.allarphoto.ajaxclient.client.components.PageSelector;
import com.allarphoto.ajaxclient.client.components.PopupWarning;
import com.allarphoto.ajaxclient.client.components.SearchResultsViewer;
import com.allarphoto.ajaxclient.client.components.VerticalProductPanel;
import com.allarphoto.ajaxclient.client.components.icons.SimpleIcon;

public class ABISearchResultsViewer extends SearchResultsViewer {

	protected Label categoryShown;

	public ABISearchResultsViewer(String[][] selectedValues, String familyName) {
		this.numToShow = 30;
		family = familyName;
		currentlyDisplayed = AjaxSystem.getText("", "header");
		addStyleName("results-view");
		center = new MultiProductPanel(100);
		this.selectedValues = selectedValues;
		initControls();
		resetImages(selectedValues, familyName);
	}

	VerticalPanel welcomeScreen;

	protected Widget getWelcomeScreen() {
		//Label welcome = AjaxSystem.getTitle("Welcome to the");
		//Image logo = new Image("wa/images/wa-logo.gif");
		Label bookmark_tag = AjaxSystem.getText("Please ensure that all bookmarks go " +
				"to http://abi.scanbank.com","bookmarkAlert");
		Label instructions = AjaxSystem
				.getText("Please begin your search by selecting a folder on the left. " +
						"You may also fine tune your search by entering a keyword " +
						"under Quick Image Search.");
		welcomeScreen = new VerticalPanel();
		welcomeScreen
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		welcomeScreen.add(instructions);
		welcomeScreen.add(bookmark_tag);
		welcomeScreen.addStyleName("welcome-screen");
		welcomeScreen.addStyleName("image-viewer");
		return welcomeScreen;
	}

	PageSelector pageSelector;

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

		};
		controls = new VerticalPanel();
		categoryShown = AjaxSystem.getTitle("");
		pageSelector = new PageSelector(0, 1, 1, new ClickListener() {

			public void onClick(Widget sender) {
				Label l = (Label) sender;
				pageSelector.unselectAll();
				l.addStyleName("selected-page");
				int off = Integer.parseInt(l.getText());
				adjustOffsetShown(numToShow * (off - 1));
			}

		});
		// controls.add(categoryShown);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.add(currentlyDisplayed);
		currentlyDisplayed.removeStyleName("header");
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
		hp.setSpacing(10);
		hp.setWidth(String.valueOf((int) (Services.getServices().mainPanel
				.getCenterWidth() * .90))
				+ "px");
		controls.setWidth(String
				.valueOf((int) (Services.getServices().mainPanel
						.getCenterWidth() * .90))
				+ "px");
		((VerticalPanel) controls).setCellHorizontalAlignment(categoryShown,
				HasHorizontalAlignment.ALIGN_LEFT);
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
		pageSelector.update(totalFound, offset + 1, center.getNumShown());
	}

	public void redraw() {
		clear();
		initControls();
		updatePrevNextPanel();
		if (welcomeScreen == null) {
			add(center, DockPanel.CENTER);
			center.redraw();
			updateCurrentlyDisplayed();
		}
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
		prevNextPanel.add(pageSelector);
		prevNextPanel.setCellHorizontalAlignment(pageSelector,
				HasHorizontalAlignment.ALIGN_CENTER);
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
		Label l = AjaxSystem.getText("View Style:", "text");
		viewControls.add(l);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		RadioButton galleryView = new RadioButton("view-type", "Gallery");
		galleryView.setChecked(true);
		hp.add(galleryView);
		RadioButton listView = new RadioButton("view-type", "List");
		hp.add(listView);
		galleryView.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				if (center instanceof VerticalProductPanel) {
					center = new MultiProductPanel(100);
					redraw();
					adjustOffsetShown(offset);
				}

			}

		});
		listView.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				if (!(center instanceof VerticalProductPanel)) {
					center = new VerticalProductPanel(130);
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
		Label l = AjaxSystem.getText("Sort By", "text");
		l.setWordWrap(false);
		sortControls.add(l);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		RadioButton byDate = new RadioButton("sort-type", "Date");
		hp.add(byDate);
		RadioButton byName = new RadioButton("sort-type", "Model&nbsp;#", true);
		hp.add(byName);
		byName.setChecked(true);
		byDate.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Services.getServices().libraryInfoService.setProductSorter(
						"Date", new AsyncCallback() {

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
						"Model Number", new AsyncCallback() {

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
