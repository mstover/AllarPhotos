package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.cached.functions.ProductFieldAdd;
import com.lazerinc.category.ProductField;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class AddProductField extends ActionHandlerBase {

	public AddProductField() {
		super();
	}

	public String getName() {
		return "add_product_field";
	}

	public void performAction(HandlerData info) throws ActionException {
		ProductFamily family = dbUtil.getProductFamily(info
				.getParameter("product_family"));
		SecurityModel perms = getCurrentUserPerms(info);
		if (!perms.getPermission(family.getTableName(), Resource.DATATABLE,
				Right.ADMIN))
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
		int fieldType = info.getParameterAsInt("field_type", 1);
		ProductField field = ProductField.createField(family.getTableName(),
				info.getParameter("field_name"), fieldType, info
						.getParameterAsInt("display_order", 1), info
						.getParameterAsInt("search_order", 0));
		ProductFieldAdd adder = new ProductFieldAdd();
		field = adder.addOrGet(field);
		addMessage("Field " + field.getName() + " successfully added", field,
				info);
	}

}
