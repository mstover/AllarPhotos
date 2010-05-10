package com.allarphoto.fineart;

import com.allarphoto.ecommerce.CostReport;

public class FineArtCostReport extends CostReport {

	public FineArtCostReport() {
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
