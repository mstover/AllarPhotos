package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.components.EnterKeyListener;
import com.allarphoto.ajaxclient.client.components.basic.ComboBox;

public class ComboEditIcon extends BaseIcon {
	ComboBox combo;

	String label;

	public ComboEditIcon() {
		addStyleName("button-icon");
		init();
	}

	public ComboEditIcon(ComboBox cb, String l) {
		super();
		combo = cb;
		label = l;
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget sender) {
				final PopupPanel pop = new PopupPanel();
				VerticalPanel vp = new VerticalPanel();
				vp.add(AjaxSystem.getLabel(label));
				vp.add(combo.getTextBox());
				pop.add(vp);
				pop.addStyleName("popup-info");
				pop.setPopupPosition(combo.getAbsoluteLeft(), combo
						.getAbsoluteTop());
				pop.setVisible(true);
				pop.show();
				combo.getTextBox().addKeyboardListener(new EnterKeyListener() {

					protected void onEnterPress(Widget source) {
						pop.hide();
						pop.setVisible(false);
						combo.getTextBox().removeKeyboardListener(this);
						combo.valueEntered();
					}

				});
			}

		};
	}

	public String getIconName() {
		return "edit.gif";
	}

	public String getIconUrl() {
		return "edit.gif";
	}

	public String getToolTip() {
		return "Add new value to list";
	}

}
