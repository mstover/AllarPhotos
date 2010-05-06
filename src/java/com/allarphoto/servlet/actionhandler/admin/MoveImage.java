package com.lazerinc.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.LogItem;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.client.util.LogUtil;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;

public class MoveImage extends ActionHandlerBase {

	public LogUtil logUtil = new LogUtil();

	public MoveImage() {
	}

	public String getName() {
		return "move_image";
	}

	public void performAction(HandlerData info) throws ActionException {
		String newDir = info.getParameter("new_dir");
		String category = info.getParameter("category");
		String library = info.getParameter("request_product_family");
		int productId = info.getParameterAsInt("request_product_id", 0);
		if (productId == 0)
			throw new LazerwebException(LazerwebException.NO_SUCH_PRODUCT);
		SecurityModel perms = ((UserBean) info.getBean("user"))
				.getPermissions();
		if (!perms.getPermission(library, Resource.DATATABLE, "admin"))
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
		LogItem productMove = moveAsset(dbUtil.getProductFamily(library),
				productId, newDir, category, perms);
		productMove.setValue("user", this.getUserBean(info).getUsername());
		logUtil.logItem(productMove);
		addMessage("This image will be moved to " + newDir, library, info);
	}

	/**
	 * @deprecated Use
	 *             {@link com.lazerinc.ecommerce.ProductFamily#moveAsset(com.lazerinc.servlet.actionhandler.admin.MoveImage,int,String,String,SecurityModel)}
	 *             instead
	 */
	private LogItem moveAsset(ProductFamily family, int productId,
			String newDir, String category, SecurityModel perms) {
		return family.moveAsset(productId, newDir, category, perms);
	}

}
