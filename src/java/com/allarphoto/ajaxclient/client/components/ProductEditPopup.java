package com.allarphoto.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductField;
import com.allarphoto.ajaxclient.client.components.basic.ComboBox;

public class ProductEditPopup extends ProductPopup {

	public ProductEditPopup(AjaxProduct p) {
		super(p);
	}

	protected Panel getKeywordPanel(List displayFields) {
		Grid keyPanel = new Grid(displayFields.size() + 1, 2);
		final List textboxes = new ArrayList();
		TextBox filename = new TextBox();
		filename.setName("FILENAME");
		textboxes.add(filename);
		filename.setText(product.getName());
		keyPanel.setWidget(0, 0, AjaxSystem.getLabel("Filename:"));
		keyPanel.setWidget(0, 1, filename);
		for (int i = 1; i < displayFields.size() + 1; i++) {
			AjaxProductField field = (AjaxProductField) displayFields
					.get(i - 1);
			keyPanel
					.setWidget(i, 0, AjaxSystem.getLabel(field.getName() + ":"));
			if (field.getType().equals("Category")
					|| field.getType().equals("Protected")) {
				TextBox text = new TextBox();
				text.setName(field.getName());
				textboxes.add(text);
				text.setText(product.getValue(field.getName()));
				keyPanel.setWidget(i, 1, text);
			} else if (field.getType().equals("Description")) {
				TextArea area = new TextArea();
				area.setName(field.getName());
				textboxes.add(area);
				area.setText(product.getValue(field.getName()));
				keyPanel.setWidget(i, 1, area);
			} else if (field.getType().equals("Tag")) {
				ComboBox combo = new ComboBox(field.getName(), true);
				textboxes.add(combo);
				populateCombo(combo, product, field);
				keyPanel.setWidget(i, 1, combo);
			}
		}
		keyPanel.setCellPadding(0);
		keyPanel.setCellSpacing(0);
		VerticalPanel vp = new VerticalPanel();
		vp.add(keyPanel);
		vp.add(new Button("Update", new ClickListener() {

			public void onClick(Widget arg0) {
				final Map values = createMap(textboxes);
				Services.getServices().libraryInfoService.updateProduct(product
						.getId(), product.getFamilyName(), values,
						new AsyncCallback() {

							public void onFailure(Throwable arg0) {
								new PopupWarning(arg0.toString());

							}

							public void onSuccess(Object result) {
								if (((Boolean) result).booleanValue()) {
									Iterator iter = values.keySet().iterator();
									while (iter.hasNext()) {
										String key = (String) iter.next();
										String value = (String) values.get(key);
										if (product.getRawValues(key) != null) {
											String[] vals = value.split("\\|");
											ArrayList list = new ArrayList();
											for (int i = 0; i < vals.length; i++) {
												list.add(vals[i]);
											}
											product.setRawValues(key, list);
										}
										product.setValue(key, value);

									}
									new PopupWarning(
											"Product successfully updated");
								} else
									new PopupWarning("Failed to update product");

							}

						});
			}

		}));
		return vp;
	}

	private Map createMap(List text) {
		Map values = new HashMap();
		for (int i = 0; i < text.size(); i++) {
			Object box = text.get(i);
			if (box instanceof TextBoxBase)
				values.put(((TextBoxBase) box).getName(), ((TextBoxBase) box)
						.getText());
			else {
				ComboBox cbox = (ComboBox) box;
				String value = "";
				for (int j = 0; j < cbox.getItemCount(); j++) {
					if (cbox.isItemSelected(j)) {
						if (value.length() == 0) {
							value = cbox.getItemText(j);
						} else
							value = value + "|" + cbox.getItemText(j);
					}
				}
				values.put(((ComboBox) box).getLabel(), value);
			}
		}
		return values;
	}

	protected void initFields(AjaxProduct product, final HorizontalPanel hp) {

		Services.getServices().libraryInfoService.getEditableFields(product
				.getFamilyName(), new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(arg0.toString());

			}

			public void onSuccess(Object results) {
				hp.add(getKeywordPanel((List) results));

			}

		});
	}

	protected void populateCombo(final ComboBox box, final AjaxProduct prod,
			final AjaxProductField field) {
		Services.getServices().libraryInfoService.getCategoryValues(prod
				.getFamilyName(), field.getName(), new AsyncCallback() {

			public void onFailure(Throwable caught) {
				new PopupWarning(caught.toString());

			}

			public void onSuccess(Object result) {
				Collection c = (Collection) result;
				Iterator iter = c.iterator();
				int count = 1;
				while (iter.hasNext()) {
					String v = (String) iter.next();
					if (v != null && !v.trim().equals("")) {
						box.addItem(v);
						if (prod.getRawValues(field.getName()).contains(v))
							box.setItemSelected(count, true);
						count++;
					}
				}

			}

		});
	}

}
