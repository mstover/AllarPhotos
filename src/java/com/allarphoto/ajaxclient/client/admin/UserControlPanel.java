package com.lazerinc.ajaxclient.client.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.DeleteListener;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.DatePicker;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.basic.ComboBox;

public class UserControlPanel extends DockPanel {

	GroupPanel groupPanel;

	TextBox firstName, lastName, email, username, middleInitial, phone,
			company, address1, address2, city, zip;

	DatePicker expiration;

	List deleteListeners = new ArrayList();

	static ComboBox country = new ComboBox();

	static ListBox state = new ListBox();
	static {
		country.addItem("");
		state.addItem("");
		getCountries();
		getStates();
	}

	AjaxUser user;

	PasswordTextBox password;

	Label responseArea = new Label("");

	public UserControlPanel() {
		super();
		init(new AjaxGroup[0]);
	}

	public UserControlPanel(AjaxGroup[] groups) {
		super();
		init(groups);
	}

	public UserControlPanel(AjaxGroup[] groups, AjaxUser user) {
		super();
		this.user = user;
		init(groups);
		groupPanel.setSelectedGroups(user.getGroups());
	}

	public void addDeleteListener(DeleteListener dl) {
		deleteListeners.add(dl);
	}

	public void removeDeleteListener(DeleteListener dl) {
		deleteListeners.remove(dl);
	}

	private static void getCountries() {
		BusyPopup.waitFor("countries");
		Services.getServices().addressInfo.getCountries(new AsyncCallback() {

			public void onFailure(Throwable caught) {
				BusyPopup.done("countries");
			}

			public void onSuccess(Object result) {
				String[] countries = (String[]) result;
				AjaxSystem.addToComboList(country, countries);
				BusyPopup.done("countries");
			}

		});
	}

	protected void fireDelete(String username) {
		Iterator iter = deleteListeners.iterator();
		while (iter.hasNext()) {
			DeleteListener dl = (DeleteListener) iter.next();
			dl.deleted(username);
		}
	}

	private static void getStates() {
		BusyPopup.waitFor("states");
		Services.getServices().addressInfo.getStates(new AsyncCallback() {

			public void onFailure(Throwable caught) {
				BusyPopup.done("states");
			}

			public void onSuccess(Object result) {
				String[] states = (String[]) result;
				AjaxSystem.addToList(state, states);
				BusyPopup.done("states");
			}

		});
	}

	public void setGroupsAndUser(AjaxGroup[] g, AjaxUser u) {
		clear();
		user = u;
		init(g);
		groupPanel.setSelectedGroups(user.getGroups());
	}

	public void setUser(AjaxUser u) {
		user = u;
		populateForm();
		groupPanel.setSelectedGroups(user.getGroups());
	}

	public AjaxUser getUser() {
		return user;
	}

	private void init(AjaxGroup[] groups) {
		BusyPopup.waitFor("Loading User Control Screen");
		this.setSpacing(20);
		addUserFormElements();
		addSubmitButton();
		groupPanel = new GroupPanel(groups, "Select Groups For User");
		add(groupPanel, DockPanel.CENTER);
		BusyPopup.done("Loading User Control Screen");
	}

	private void addSubmitButton() {
		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button submit = new Button((user == null ? "Add User" : "Update User"),
				new ClickListener() {

					public void onClick(Widget sender) {
						BusyPopup.waitFor((user == null ? "adding user"
								: "Updating User"));
						responseArea.setText("");
						final String[] selectedGroups = groupPanel
								.getSelectedGroups();
						if (requiredFieldsComplete()) {
							BusyPopup.done((user == null ? "adding user"
									: "Updating User"));
							new PopupWarning(
									"You must fill in values for First Name, Last Name, and Email Address");
						} else if (selectedGroups != null && selectedGroups.length == 0) {
							BusyPopup.done((user == null ? "adding user"
									: "Updating User"));
							new PopupWarning(
									"You must select at least one group for the new user to belong to");
						} else if (user == null) {
							String uname = username.getText();
							if (uname.length() < 1) {
								uname = firstName.getText().substring(0, 1)
										.toLowerCase()
										+ lastName.getText().toLowerCase();
								username.setText(uname);
							}
							addUser(selectedGroups, uname);
						} else {
							updateUser(selectedGroups);
						}
					}

				});
		if (user != null) {
			buttonPanel.add(new Button("Send Password", new ClickListener() {

				public void onClick(Widget sender) {
					Services.getServices().userInfoService.sendPassword(user
							.getUsername(), new AsyncCallback() {

						public void onFailure(Throwable caught) {
							new PopupWarning("Failed to send password "
									+ caught.toString());
						}

						public void onSuccess(Object result) {
							if (((Boolean) result).booleanValue()) {
								new PopupWarning("Password sent");
							} else {
								new PopupWarning("Failed to send password");
							}

						}

					});
				}
			}));
		}
		Button clearButton = new Button("Clear Form", new ClickListener() {

			public void onClick(Widget sender) {
				clearForm();
			}

		});
		buttonPanel.add(clearButton);
		buttonPanel.add(submit);
		if (user != null) {

			Button delete = new Button("Delete User", new ClickListener() {

				public void onClick(Widget sender) {
					BusyPopup.waitFor("Deleting User...");
					Services.getServices().userInfoService.deleteUser(user
							.getUsername(), new AsyncCallback() {

						public void onFailure(Throwable caught) {
							new PopupWarning("Failed to delete user");
							BusyPopup.done("Deleting User...");
						}

						public void onSuccess(Object result) {
							if (((Boolean) result).booleanValue()) {
								fireDelete(user.getUsername());
								clearForm();
							} else {
								new PopupWarning("Failed to delete user");
							}
							BusyPopup.done("Deleting User...");
						}

					});
				}

			});
			buttonPanel.add(delete);
		}
		buttonPanel.setSpacing(10);
		add(buttonPanel, DockPanel.SOUTH);
	}

	private void clearForm() {
		groupPanel.clear();
		firstName.setText("");
		lastName.setText("");
		email.setText("");
		username.setText("");
		middleInitial.setText("");
		phone.setText("");
		company.setText("");
		address1.setText("");
		address2.setText("");
		city.setText("");
		state.setSelectedIndex(0);
		zip.setText("");
		country.setSelectedIndex(0);
		password.setText("");
		responseArea.setText("");
	}

	private void addUserFormElements() {
		HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel vp = new VerticalPanel();

		Label label = AjaxSystem.getText("First Name:");
		label.addStyleName("required");
		vp.add(label);
		firstName = new TextBox();
		vp.add(firstName);

		label = AjaxSystem.getText("Middle Initial:");
		vp.add(label);
		middleInitial = new TextBox();
		vp.add(middleInitial);

		label = AjaxSystem.getText("Last Name:");
		label.addStyleName("required");
		vp.add(label);
		lastName = new TextBox();
		vp.add(lastName);

		label = AjaxSystem.getText("Email Address:");
		label.addStyleName("required");
		vp.add(label);
		email = new TextBox();
		vp.add(email);

		label = AjaxSystem.getText("Username:");
		vp.add(label);
		username = new TextBox();
		vp.add(username);

		label = AjaxSystem.getText("Password:");
		vp.add(label);
		password = new PasswordTextBox();
		vp.add(password);

		label = AjaxSystem.getText("Expiration:");
		vp.add(label);
		expiration = new DatePicker();
		vp.add(expiration);

		label = AjaxSystem.getText("Phone:");
		vp.add(label);
		phone = new TextBox();
		vp.add(phone);

		label = AjaxSystem.getText("Company:");
		vp.add(label);
		company = new TextBox();
		vp.add(company);
		vp.setSpacing(5);

		hp.add(vp);

		vp = new VerticalPanel();

		label = AjaxSystem.getText("Address Line 1:");
		vp.add(label);
		address1 = new TextBox();
		vp.add(address1);

		label = AjaxSystem.getText("Address Line 2:");
		vp.add(label);
		address2 = new TextBox();
		vp.add(address2);

		label = AjaxSystem.getText("City:");
		vp.add(label);
		city = new TextBox();
		vp.add(city);

		label = AjaxSystem.getText("State:");
		vp.add(label);
		state.setSelectedIndex(0);
		vp.add(state);

		label = AjaxSystem.getText("Zip:");
		vp.add(label);
		zip = new TextBox();
		vp.add(zip);

		label = AjaxSystem.getText("Country:");
		vp.add(label);
		country.setSelectedIndex(0);
		vp.add(country);
		vp.setSpacing(5);
		hp.add(vp);
		hp.setSpacing(20);
		add(hp, DockPanel.WEST);
		if (user != null)
			populateForm();
	}

	private void populateForm() {
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		email.setText(user.getEmailAddress());
		middleInitial.setText(user.getMiddleInitial());
		username.setText(user.getUsername());
		username.setEnabled(false);
		password.setText(user.getPassword());
		phone.setText(user.getPhone());
		company.setText(user.getCompany());
		address1.setText(user.getBillAddress1());
		address2.setText(user.getBillAddress2());
		city.setText(user.getBillCity());
		zip.setText(user.getBillZip());
		expiration.setText(user.getExpiration());
		if (user != null) {
			state.setSelectedIndex(AjaxSystem.findIndexBinarySearch(state, user
					.getBillState()));
			country.setSelectedIndex(AjaxSystem.findComboIndexBinarySearch(
					country, user.getBillCountry()));
		}
	}

	private boolean requiredFieldsComplete() {
		return firstName.getText().length() == 0
				|| lastName.getText().length() == 0
				|| email.getText().length() == 0;
	}

	private void updateUser(final String[] selectedGroups) {
		Services.getServices().userInfoService.updateUser(firstName.getText(),
				lastName.getText(), email.getText(), middleInitial.getText(),
				username.getText(), password.getText(), phone.getText(),
				company.getText(), address1.getText(), address2.getText(), city
						.getText(), zip.getText(), state.getItemText(state
						.getSelectedIndex()), country.getItemText(country
						.getSelectedIndex()), expiration.getText(),
				selectedGroups, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						BusyPopup.done("Updating User");
						responseArea.setText("Failure! " + firstName.getText()
								+ " " + lastName.getText() + " not updated.");
					}

					public void onSuccess(Object result) {
						if (((Boolean) result).booleanValue()) {
							new PopupWarning("Successfully Updated User");
							user.setBillAddress1(address1.getText());
							user.setBillAddress2(address2.getText());
							user.setLastName(lastName.getText());
							user.setFirstName(firstName.getText());
							user.setMiddleInitial(middleInitial.getText());
							user.setBillCity(city.getText());
							user.setBillCountry(country.getItemText(country
									.getSelectedIndex()));
							user.setBillState(state.getItemText(state
									.getSelectedIndex()));
							user.setPhone(phone.getText());
							user.setCompany(company.getText());
							user.setEmailAddress(email.getText());
							user.setBillZip(zip.getText());
							if(selectedGroups != null)
								user.setGroups(selectedGroups);
							user.setExpiration(expiration.getText());
						} else
							new PopupWarning("Failed to update User");
						BusyPopup.done("Updating User");
					}

				});
	}

	private void addUser(final String[] selectedGroups, String uname) {
		responseArea.setText("Adding User ...");
		Services.getServices().userInfoService.getUser(uname,
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						responseArea.setText(caught.toString());
						BusyPopup.done("adding user");
					}

					public void onSuccess(Object result) {
						if (result != null) {
							responseArea.setText("");
							BusyPopup.done("adding user");
							new PopupWarning(
									"Username "
											+ username.getText()
											+ " is already taken.  Please choose another login name.");
						} else {
							Services.getServices().userInfoService.addUser(
									firstName.getText(), lastName.getText(),
									email.getText(), middleInitial.getText(),
									username.getText(), password.getText(),
									phone.getText(), company.getText(),
									address1.getText(), address2.getText(),
									city.getText(), zip.getText(), state
											.getItemText(state
													.getSelectedIndex()),
									country.getItemText(country
											.getSelectedIndex()), expiration
											.getText(), selectedGroups,
									new AsyncCallback() {

										public void onFailure(Throwable caught) {
											BusyPopup.done("adding user");
											responseArea.setText("Failure! "
													+ firstName.getText() + " "
													+ lastName.getText()
													+ " not added.");
										}

										public void onSuccess(Object result) {
											responseArea.setText("New user "
													+ firstName.getText() + " "
													+ lastName.getText()
													+ " successfully added.");
											BusyPopup.done("adding user");
										}

									});
						}

					}

				});
	}

}
