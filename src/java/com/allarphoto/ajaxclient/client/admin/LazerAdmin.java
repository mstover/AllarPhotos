package com.lazerinc.ajaxclient.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.CommandFactory;
import com.lazerinc.ajaxclient.client.ComponentFactory;
import com.lazerinc.ajaxclient.client.MainPanel;
import com.lazerinc.ajaxclient.client.Redrawable;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.basic.PngLoadListener;

public class LazerAdmin implements EntryPoint, MainPanel, WindowResizeListener {
	private DockPanel dock;

	Services services = Services.getServices();

	CommandFactory commands;

	VerticalPanel mainPanel;

	LazerMenu mainMenu;

	ScrollPanel westScroller, eastScroller, centerScroller, southScroller;

	Widget[] topNonDockWidgets, bottomNonDockWidgets;

	String startingSection = null;

	public void onModuleLoad() {
		Services.getServices().customService
				.getComponentFactory(new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object arg0) {
						Services.getServices().factory = (ComponentFactory) arg0;
						init();
					}

				});

		Services.getServices().userInfoService.getBrowser(new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning("Failed to determine browser");

			}

			public void onSuccess(Object arg0) {
				Services.getServices().browser = (String) arg0;

			}

		});

	}

	public int getCenterWidth() {
		if (centerScroller != null)
			return centerScroller.getOffsetWidth();
		else
			return 0;
	}

	public int getAbsoluteCenterLeft() {
		if (centerScroller != null)
			return centerScroller.getAbsoluteLeft();
		else
			return 0;
	}

	public int getAbsoluteCenterTop() {
		if (centerScroller != null)
			return centerScroller.getAbsoluteTop();
		else
			return 0;
	}

	public int getCenterHeight() {
		if (centerScroller != null)
			return centerScroller.getOffsetHeight();
		else
			return 0;
	}

	public int getEastWidth() {
		if (eastScroller != null)
			return eastScroller.getOffsetWidth();
		else
			return 0;
	}

	public int getSouthWidth() {
		if (southScroller != null)
			return southScroller.getOffsetWidth();
		else
			return 0;
	}

	public RootPanel getTopParent() {
		return RootPanel.get("comp");
	}

	private void init() {
		Services.getServices().userInfoService
				.getCurrentUser(new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						setup();
					}

					public void onSuccess(Object result) {
						if (result != null) {
							Services.getServices().user = (AjaxUser) result;
							Services.getServices().getUserPerms();
							Services.getServices().getShoppingCart();
							setup();
							runStartingSection();
						} else {
							setup();
							commands.get("Login").execute();
						}
					}
				});
	}
	
	protected void setup()
	{
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		FocusPanel fp = new FocusPanel();
		fp.add(mainPanel);
		RootPanel.get("comp").add(fp);
		// fp.addMouseListener(Drag.dragger);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		dock = new DockPanel();
		commands = Services.getServices().factory.getCommands();
		mainMenu = Services.getServices().factory.getMainMenu();
		Services.getServices().menu = mainMenu;
		Services.getServices().mainPanel = this;

		topNonDockWidgets = Services.getServices().factory
				.getTopComponentStack();
		addComponentStack(topNonDockWidgets);
		mainPanel.setCellVerticalAlignment(mainMenu,
				HasVerticalAlignment.ALIGN_TOP);
		// dock.add(getTitle(),DockPanel.NORTH);
		// dock.add(treeControl.getLibraryTree(),DockPanel.WEST);
		// dock.add(getCenterPanel(),DockPanel.CENTER);
		mainPanel.add(dock);
		dock.setSpacing(0);
		dock.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		mainPanel
				.setCellVerticalAlignment(dock, HasVerticalAlignment.ALIGN_TOP);
		bottomNonDockWidgets = Services.getServices().factory
				.getBottomComponentStack();
		addComponentStack(bottomNonDockWidgets);
		mainPanel
				.setCellVerticalAlignment(dock, HasVerticalAlignment.ALIGN_TOP);
		startingSection = getStartSection();
		redraw();
		Window.addWindowResizeListener(this);
	}

	public void redraw() {
		DeferredCommand.add(new Command() {

			public void execute() {
				setComponentHeights();
				PngLoadListener.runPngJavascript();
			}

		});
	}

	private void addComponentStack(Widget[] stack) {
		for (int i = 0; i < stack.length; i++) {
			if (stack[i] != null) {
				mainPanel.add(stack[i]);
				mainPanel.setCellVerticalAlignment(stack[i],
						HasVerticalAlignment.ALIGN_TOP);
				if ("centered".equals(stack[i].getStyleName()))
					mainPanel.setCellHorizontalAlignment(stack[i],
							HasHorizontalAlignment.ALIGN_CENTER);
			}
		}
	}

	private int getNonDockSpace() {
		int space = 0;
		for (int i = 0; i < topNonDockWidgets.length; i++) {
			if (topNonDockWidgets[i] != null)
				space += topNonDockWidgets[i].getOffsetHeight();
		}
		for (int i = 0; i < bottomNonDockWidgets.length; i++) {
			if (bottomNonDockWidgets[i] != null)
				space += bottomNonDockWidgets[i].getOffsetHeight();
		}
		return space;
	}

	public void recreateMenus() {
		mainPanel.clear();
		dock.clear();
		commands = Services.getServices().factory.regetCommands();
		mainMenu = Services.getServices().factory.getMainMenu();
		Services.getServices().menu = mainMenu;
		topNonDockWidgets = Services.getServices().factory
				.getTopComponentStack();
		addComponentStack(topNonDockWidgets);
		mainPanel.add(dock);
		bottomNonDockWidgets = Services.getServices().factory
				.getBottomComponentStack();
		addComponentStack(bottomNonDockWidgets);
		westScroller = null;
		eastScroller = null;
		southScroller = null;
		centerScroller = null;
		runStartingSection();
		redraw();
	}

	private void runStartingSection() {
		Timer t = new Timer() {
			public void run() {
				if (startingSection != null) {
					if (!Services.getServices().factory.isCommandsSet())
						this.schedule(1000);
					Command c = commands.get(startingSection);
					if (c != null) {
						c.execute();
					}
				}
			}
		};
		t.schedule(500);
	}

	private void setComponentHeights() {
		mainPanel.setWidth(AjaxSystem.getWidthToWindowLeft(mainPanel
				.getAbsoluteLeft() + 5));
		for (int i = 0; i < topNonDockWidgets.length; i++) {
			if (topNonDockWidgets[i] != null) {
				if (topNonDockWidgets[i] instanceof LazerMenu)
					topNonDockWidgets[i]
							.setWidth(AjaxSystem.getWidthToWindowLeft(mainPanel
									.getAbsoluteLeft() - 4));
				else
					topNonDockWidgets[i]
							.setWidth(AjaxSystem.getWidthToWindowLeft(mainPanel
									.getAbsoluteLeft() + 4));
			}
		}
		mainPanel.setHeight(AjaxSystem.getHeightToWindowBottom(mainPanel
				.getAbsoluteTop() + 4));
		dock.setHeight(AjaxSystem
				.getHeightToWindowBottom(getNonDockSpace() + 5));
		dock.setWidth(AjaxSystem
				.getWidthToWindowLeft(dock.getAbsoluteLeft() + 4));
		refreshSouth();
		refreshWest();
		refreshEast();
		refreshCenter();
	}

	private String getStartSection() {
		String query = getQueryString();
		int index = query.indexOf("section=");
		if (index > -1) {
			query = query.substring(index + "section=".length());
			index = query.indexOf("&");
			if (index > -1)
				query = query.substring(0, index).trim();
			return query;
		} else
			return Services.getServices().factory.getStartingSection();
	}


    private static native String getQueryString () /*-{
        return $wnd.location.search;
    }-*/;

	public void setScreen(String title, Widget center, Widget west,
			Widget east, Widget south) {
		Services.getServices().factory.setSectionTitle(title);
		dock.clear();
		southScroller = westScroller = centerScroller = eastScroller = null;
		setSouth(south, false);
		setWest(west);
		setEast(east, false);
		setCenter(center);
		redraw();
	}

	public void setSouth(Widget south) {
		setSouth(south, true);
	}

	private void setSouth(Widget south, boolean redraw) {
		if (southScroller != null) {
			southScroller.clear();
			if (south != null)
				southScroller.add(south);
			else {
				dock.remove(southScroller);
				southScroller = null;
			}
		} else if (south != null) {
			southScroller = new ScrollPanel(south);
			try {
				southScroller.addStyleName("south");
			} catch (Exception e) {
			}
			dock.add(southScroller, DockPanel.SOUTH);
			dock.setCellVerticalAlignment(southScroller,
					HasVerticalAlignment.ALIGN_TOP);
			dock.setCellHorizontalAlignment(southScroller,
					HasHorizontalAlignment.ALIGN_LEFT);
		}
		if (redraw)
			redraw();
	}

	public void setWest(Widget west) {
		if (westScroller != null) {
			westScroller.clear();
			if (west != null)
				westScroller.add(west);
			else {
				dock.remove(westScroller);
				westScroller = null;
			}
		} else if (west != null) {
			westScroller = new ScrollPanel(west);
			try {
				westScroller.addStyleName("west");
			} catch (Exception e) {
			}
			dock.add(westScroller, DockPanel.WEST);
			dock.setCellVerticalAlignment(westScroller,
					HasVerticalAlignment.ALIGN_TOP);
			dock.setCellHorizontalAlignment(westScroller,
					HasHorizontalAlignment.ALIGN_LEFT);
		}
	}

	private int calcIdealWestWidth() {
		int width = ((int) (Window.getClientWidth() * .20));
		if (width > 400)
			width = 400;
		if (width < 250)
			width = 250;
		return width;
	}

	private void refreshWest() {
		if (westScroller != null) {
			westScroller.setWidth(calcIdealWestWidth() + "px");
			westScroller.setHeight(AjaxSystem
					.getHeightToWindowBottom(getNonDockSpace()
							+ (southScroller != null ? southScroller
									.getOffsetHeight() : 0) + 10));
			if (westScroller.getWidget() instanceof Redrawable)
				((Redrawable) westScroller.getWidget()).redraw();
		}
	}

	public void setCenter(Widget center) {
		if (centerScroller != null) {
			centerScroller.clear();
			if (center != null)
				centerScroller.add(center);
			else {
				dock.remove(centerScroller);
				centerScroller = null;
			}
		} else if (center != null) {
			centerScroller = new ScrollPanel(center);
			try {
				centerScroller.addStyleName("center");
			} catch (Exception e) {
			}
			dock.add(centerScroller, DockPanel.CENTER);
			dock.setCellVerticalAlignment(centerScroller,
					HasVerticalAlignment.ALIGN_TOP);
			dock.setCellHorizontalAlignment(centerScroller,
					HasHorizontalAlignment.ALIGN_LEFT);
		}
	}

	public Widget getCenter() {
		if (centerScroller != null)
			return centerScroller.getWidget();
		return null;
	}

	public Widget getWest() {
		if (westScroller != null)
			return westScroller.getWidget();
		return null;
	}

	private void refreshCenter() {
		if (centerScroller != null) {
			centerScroller.setWidth(AjaxSystem
					.getWidthToWindowLeft(5
							+ (eastScroller != null ? eastScroller
									.getOffsetWidth() : 0)
							+ (westScroller != null ? westScroller
									.getOffsetWidth() : 0)));
			centerScroller.setHeight(AjaxSystem
					.getHeightToWindowBottom(getNonDockSpace()
							+ (southScroller != null ? southScroller
									.getOffsetHeight() : 0) + 10));
			if (centerScroller.getWidget() instanceof Redrawable)
				((Redrawable) centerScroller.getWidget()).redraw();
		}
	}

	private void setEast(Widget east, boolean redraw) {
		if (eastScroller != null) {
			eastScroller.clear();
			if (east != null)
				eastScroller.add(east);
			else {
				dock.remove(eastScroller);
				eastScroller = null;
			}
		} else if (east != null) {
			eastScroller = new ScrollPanel(east);
			dock.add(eastScroller, DockPanel.EAST);
			dock.setCellVerticalAlignment(eastScroller,
					HasVerticalAlignment.ALIGN_TOP);
			dock.setCellHorizontalAlignment(eastScroller,
					HasHorizontalAlignment.ALIGN_LEFT);
			eastScroller.addStyleName("east");
		}
		if (redraw)
			redraw();
	}

	public void setEast(Widget east) {
		setEast(east, true);
	}

	private void refreshEast() {
		if (eastScroller != null) {
			eastScroller.setWidth(((int) (Window.getClientWidth() * .20))
					+ "px");
			eastScroller.setHeight(AjaxSystem
					.getHeightToWindowBottom(getNonDockSpace()
							+ (southScroller != null ? southScroller
									.getOffsetHeight() : 0) + 10));
			if (eastScroller.getWidget() instanceof Redrawable)
				((Redrawable) eastScroller.getWidget()).redraw();
		}
	}

	private void refreshSouth() {
		if (southScroller != null) {
			southScroller.setWidth(((int) (Window.getClientWidth())) + "px");
			southScroller.setHeight("150px");
			if (southScroller.getWidget() instanceof Redrawable)
				((Redrawable) southScroller.getWidget()).redraw();
		}
	}

	public void onWindowResized(int arg0, int arg1) {
		redraw();
	}

}
