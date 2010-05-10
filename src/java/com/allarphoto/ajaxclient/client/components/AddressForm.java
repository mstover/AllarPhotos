package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.ajaxclient.client.beans.AjaxCompany;

public class AddressForm extends VerticalPanel {
	TextBox address1, address2, attn, city, phone, zip, company;

	StateList states = StateList.states;

	CountryList countries = CountryList.countries;

	public AddressForm() {
		init();
	}

	private void init() {
		states.setState("");
		countries.setCountry("");
		attn = new TextBox();
		address1 = new TextBox();
		address2 = new TextBox();
		city = new TextBox();
		phone = new TextBox();
		zip = new TextBox();
		company = new TextBox();
		Grid grid = new Grid(9, 2);
		int row = 0;
		grid.setWidget(row, 0, AjaxSystem.getLabel("Attention"));
		grid.setWidget(row++, 1, attn);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Company"));
		grid.setWidget(row++, 1, company);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Address 1"));
		grid.setWidget(row++, 1, address1);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Address2"));
		grid.setWidget(row++, 1, address2);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Phone"));
		grid.setWidget(row++, 1, phone);
		grid.setWidget(row, 0, AjaxSystem.getLabel("City"));
		grid.setWidget(row++, 1, city);
		grid.setWidget(row, 0, AjaxSystem.getLabel("State/Province"));
		grid.setWidget(row++, 1, states);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Postal Code (zip)"));
		grid.setWidget(row++, 1, zip);
		grid.setWidget(row, 0, AjaxSystem.getLabel("Country"));
		grid.setWidget(row++, 1, countries);
		add(grid);
	}

	public void populate(AjaxAddress aa) {
		attn.setText(aa.getAttn());
		address1.setText(aa.getAddress1());
		address2.setText(aa.getAddress2());
		city.setText(aa.getCity());
		phone.setText(aa.getPhone());
		zip.setText(aa.getZip());
		company.setText(aa.getCompany().getName());
		countries.setCountry(aa.getCountry());
		states.setState(aa.getState());
	}

	public AjaxAddress getAddress() {
		AjaxAddress aa = new AjaxAddress();
		aa.setAddress1(address1.getText());
		aa.setAddress2(address2.getText());
		aa.setAttn(attn.getText());
		aa.setCity(city.getText());
		aa.setCompany(new AjaxCompany(company.getText(), ""));
		aa.setCountry(countries.getCountry());
		aa.setPhone(phone.getText());
		aa.setState(states.getState());
		aa.setZip(zip.getText());
		return aa;
	}

}
