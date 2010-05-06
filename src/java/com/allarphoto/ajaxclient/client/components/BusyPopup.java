package com.lazerinc.ajaxclient.client.components;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BusyPopup extends PopupPanel {

	static Map waiting = new HashMap();

	static VerticalPanel mainPanel = new VerticalPanel();

	static BusyPopup one = new BusyPopup();
	
	static Image progress;

	private BusyPopup() {
		super(true);
		add(mainPanel);
		progress = new Image("in-progress.gif");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		progress.addStyleName("busy");
		mainPanel.add(progress);
		addStyleName("popup-info");
		setPopupPosition(Window.getClientWidth() / 3,
				Window.getClientHeight() / 3);
		setVisible(false);
	}

	public static void waitFor(String w) {
		if (!waiting.containsKey(w)) {
			Label l = new Label(w);
			waiting.put(w, l);
			mainPanel.add(l);
		}
		if (!one.isAttached() || !one.isVisible()) {
			DeferredCommand.add(new Command() {

				public void execute() {
					one.setVisible(true);
					one.show();
					hideIfEmpty();
				}
			});

		}
		// label.setText(waiting.toString());
	}

	private static void hideIfEmpty() {
		if (waiting.size() == 0) {
			one.setVisible(false);
			one.hide();
		}
	}

	public static void done(String w) {
		Label l = (Label) waiting.remove(w);
		if (l != null)
			mainPanel.remove(l);
		// label.setText(waiting.toString());
		DeferredCommand.add(new Command() {

			public void execute() {
				hideIfEmpty();
			}
		});
	}

}