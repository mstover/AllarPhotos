package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class LazerCheckBox extends CheckBox {

	public LazerCheckBox() {
		super();
		addClickListener(getSelectionListener());
	}

	public LazerCheckBox(String arg0) {
		super(arg0);
		addClickListener(getSelectionListener());
	}

	public LazerCheckBox(Element arg0) {
		super(arg0);
		addClickListener(getSelectionListener());
	}

	public LazerCheckBox(String arg0, boolean arg1) {
		super(arg0, arg1);
		addClickListener(getSelectionListener());
	}

	public void setChecked(boolean arg0) {
		super.setChecked(arg0);
		if (arg0)
			addStyleName("selected-checkbox");
		else
			removeStyleName("selected-checkbox");
	}

	private ClickListener getSelectionListener() {
		return new ClickListener() {

			public void onClick(Widget sender) {
				DeferredCommand.add(null);
				DeferredCommand.add(new Command() {

					public void execute() {
						setChecked(isChecked());
					}

				});

			}

		};
	}

}
