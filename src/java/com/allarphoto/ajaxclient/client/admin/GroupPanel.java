package com.lazerinc.ajaxclient.client.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.components.LazerCheckBox;

public class GroupPanel extends VerticalPanel implements FocusListener {
	Map groupBoxes;

	AjaxGroup[] groups;

	String title;

	ClickListener selectionListener;

	ScrollPanel descriptionPanel = new ScrollPanel();

	public GroupPanel(AjaxGroup[] groups, String title) {
		this.groups = groups;
		this.title = title;
		init();
	}

	public GroupPanel(AjaxGroup[] groups, String title,
			ClickListener selectionListener) {
		this.selectionListener = selectionListener;
		this.groups = groups;
		this.title = title;
		init();

	}

	private void init() {
		VerticalPanel[] groupLists = new VerticalPanel[] { new VerticalPanel(),
				new VerticalPanel(), new VerticalPanel() };
		groupBoxes = new HashMap();
		for (int i = 0; i < groups.length; i++) {
			CheckBox groupBox = new LazerCheckBox();
			groupBox.setText(groups[i].getName());
			groupLists[i % 3].add(groupBox);
			if (selectionListener != null)
				groupBox.addClickListener(selectionListener);
			groupBox.addFocusListener(this);
			groupBoxes.put(groups[i].getName(), groupBox);
		}
		Label l = new Label(title);
		l.addStyleName("title");
		add(l);
		HorizontalPanel big = new HorizontalPanel();
		big.addStyleName("grouplist");
		big.add(groupLists[0]);
		big.add(groupLists[1]);
		big.add(groupLists[2]);
		big.add(descriptionPanel);
		descriptionPanel.setWidth("20em");
		descriptionPanel.setHeight("500px");
		big.setSpacing(10);
		add(big);
	}

	public String[] getSelectedGroups() {
		List gps = new ArrayList();
		Iterator iter = groupBoxes.values().iterator();
		while (iter.hasNext()) {
			CheckBox gb = (CheckBox) iter.next();
			if (gb.isChecked()) {
				gps.add(gb.getText());
			}
		}
		if(groupBoxes.size() == 0) return null;
		return AjaxSystem.toStringArray(gps);
	}

	public void clear() {
		Iterator iter = groupBoxes.values().iterator();
		while (iter.hasNext()) {
			((CheckBox) iter.next()).setChecked(false);
		}
	}

	public void setSelectedGroups(String[] selectedGroups) {
		clear();
		for (int i = 0; i < selectedGroups.length; i++) {
			CheckBox box = (CheckBox) groupBoxes.get(selectedGroups[i]);
			if (box != null) {
				box.setChecked(true);
			}
		}
	}

	public void onFocus(Widget box) {
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].getName().equals(((CheckBox) box).getText())) {
				descriptionPanel.clear();
				if (groups[i].getDescription() != null)
					descriptionPanel.add(AjaxSystem.getText(groups[i].getName()
							+ ": " + groups[i].getDescription()));
				break;
			}
		}
	}

	public void onLostFocus(Widget arg0) {
		// TODO Auto-generated method stub

	}

}
