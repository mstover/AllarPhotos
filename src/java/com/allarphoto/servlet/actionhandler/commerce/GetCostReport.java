package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_GET_COST_REPORT;

import java.util.Collection;
import java.util.Iterator;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.OrderingService;
import com.lazerinc.application.ServiceGateway;
import com.lazerinc.client.beans.CostReportBean;
import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.FatalException;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.ecommerce.CostReport;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

/**
 * Returns information about the cost of the currently ordered products in the
 * user's shopping cart. The information current in the user's shopping cart is
 * used to calculate the cost.
 * 
 * @author Administrator
 * @action action_get_cost_report
 * @bean CostReportBean costReport The action returns a Cost Report Bean that
 *       provides the necessary information about how much the current order
 *       will cost is the users proceeds as is.
 */
public class GetCostReport extends ActionHandlerBase {

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException {
		ShoppingCartBean cart = (ShoppingCartBean) actionInfo.getBean("cart");
		UserBean userBean = getUserBean(actionInfo);
		CostReport report = new CostReport();
		OrderingService model;
		Iterator it = cart.getFamilyLists().keySet().iterator();
		ProductFamily pf;
		while (it.hasNext()) {
			pf = (ProductFamily) it.next();
			try {
				model = ServiceGateway.getOrderService(pf.getOrderModelClass());
				model.setUser(userBean.getUser());
				model.setSecurity(userBean.getPermissions());
				report.add(model.getCost((Collection) cart.getFamilyLists()
						.get(pf), cart.getGlobalInstructions()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CostReportBean costBean = new CostReportBean();
		actionInfo.setRequestBean("costReport", costBean);
		costBean.setReport(report);
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_GET_COST_REPORT;
	}
}
