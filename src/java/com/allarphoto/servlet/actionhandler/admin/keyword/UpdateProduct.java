package com.allarphoto.servlet.actionhandler.admin.keyword;

import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_FAMILY;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_ID;

import java.util.Set;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;
import strategiclibrary.util.Files;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class UpdateProduct extends ActionHandlerBase {

	public UpdateProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		ProductFamily family = dbUtil.getProductFamily(actionInfo
				.getParameter(REQUEST_PRODUCT_FAMILY));
		SecurityModel perms = getCurrentUserPerms(actionInfo);
		if (!perms.getPermission(family.getTableName(), Resource.DATATABLE,
				Right.ADMIN))
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
		Product p = family.getProduct(actionInfo
				.getParameter(REQUEST_PRODUCT_ID), perms);
		for (String param : actionInfo.getParamNames()) {
			ProductField field = family.getField(param);
			getLog().debug("Looking for field " + param);
			if (field != null) {
				p.removeValue(param);
				for (String val : splitValues(actionInfo.getParameter(param,
						null, getNullValues()))) {
					if (field.getType() == ProductField.NUMERICAL)
						p.setValue(param, Converter.convert(val.trim(),
								Double.class));
					else
						p.setValue(param, val.trim());
				}
				getLog().debug(
						"Set field value to " + actionInfo.getParameter(param));
			} else if (param.equals("FILENAME")) {
				family.renameAsset(p.getId(), actionInfo.getParameter(param),
						perms);
			}
		}
		family.updateProduct(p);
		addMessage("Product " + p.getName() + " successfully updated", p,
				actionInfo);
	}

	protected String[] splitValues(String s) {
		if (s == null)
			return new String[0];
		else
			return s.split("\\|");
	}

	public String getName() {
		return "update_product_keywords";
	}

	@CoinjemaDynamic(method = "productNullValues")
	public Set<String> getNullValues() {
		return null;
	}
}
