package com.allarphoto.application.impl;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.ExpirationTester;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.category.ProductField;
import com.allarphoto.server.ResourceService;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

@CoinjemaObject
public class DefaultExpirationTester implements ExpirationTester {

	protected Logger log;

	public boolean isExpired(Product p) {
		return false;
	}

	public boolean hasExpiredPermission(Product p, Right right,
			SecurityModel perms) {
		boolean hasRight = true;
		if (hasRight && isExpired(p))
			hasRight = perms.getPermission(p.getProductFamilyName(),
					Resource.DATATABLE, Right.ADMIN)
					|| perms.getPermission(p.getProductFamilyName()
							+ ".expired", Resource.EXPIRED_ITEMS, right);
		return hasRight;
	}

	public boolean hasExpiredPermission(Product p, String right,
			SecurityModel perms) {
		return hasExpiredPermission(p, Right.getRight(right), perms);
	}

	public boolean hasPermission(Product p, String right, SecurityModel perms) {
		return hasPermission(p, Right.getRight(right), perms);
	}

	public boolean willExpire(Product p) {
		return false;
	}

	public boolean pastExpiredThreshold(Product p) {
		return false;
	}

	public boolean hasUsageRights(Product p) {
		return false;
	}

	public boolean hasPermission(Product p, Right right, SecurityModel security) {
		boolean hasRight = security.getPermission(p.getProductFamilyName(),
				Resource.DATATABLE, right);
		if (!security.getPermission(p.getProductFamilyName(),
				Resource.DATATABLE, Right.ADMIN)) {
			for (ProductField field : p.getProductFamily().getFields()) {
				if (field.getType() == ProductField.PROTECTED) {
					Resource res = resService.getResource(p.getProductFamily()
							.getTableName()
							+ "."
							+ field.getName()
							+ "."
							+ p.getValue(field.getName()),
							Resource.PROTECTED_FIELD);
					hasRight = hasRight && security.getPermission(res, right);
				}
			}
		}
		if (hasRight && isExpired(p))
			hasRight = security.getPermission(p.getProductFamilyName(),
					Resource.DATATABLE, Right.ADMIN)
					|| security.getPermission(p.getProductFamilyName()
							+ ".expired", Resource.EXPIRED_ITEMS, right);
		return hasRight;
	}

	public Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

}
