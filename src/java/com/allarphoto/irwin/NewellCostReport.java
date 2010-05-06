package com.lazerinc.irwin;

import com.lazerinc.ecommerce.CostReport;

public class NewellCostReport extends CostReport {

	public NewellCostReport() {
		// TODO Auto-generated constructor stub
	}

	public float calculateTotal() {
		float cost = 0;
		for (ProductReport rep : products.values()) {
			cost += rep.cost + rep.cost
					* getTaxRate(rep.product.getProductFamily());
		}
		for (FamilyReport rep : families.values()) {
			cost += rep.shipping;
		}
		if (cost < 12)
			return 12;
		return cost;
	}

}
