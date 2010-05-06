package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.lazerinc.application.Product;
import com.lazerinc.ecommerce.CostReport;
import com.lazerinc.ecommerce.ProductFamily;

/**
 * @author Administrator To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates.
 */
@CoinjemaObject
public class CostReportBean implements Serializable {
	private static final long serialVersionUID = 1;

	CostReport report;

	Logger log;

	/**
	 * @see com.lazerinc.client.beans.ResponseBean#clear()
	 */
	public void clear() {
		report = null;
	}

	public void setReport(CostReport report) {
		this.report = report;
	}

	public CostReport getReport() {
		return report;
	}

	public float calculateTotal() {
		try {
			return report.calculateTotal();
		} catch (RuntimeException e) {
			getLog().error("problem calculating total for cost report", e);
			return (float) 25.00;
		}
	}

	Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	public Iterator familyIterator() {
		return report.familyIterator();
	}

	public float getCost(Product p) {
		return report.getCost(p);
	}

	public int getQuantity(Product p) {
		return report.getQuantity(p);
	}

	public float getShipping(ProductFamily pf) {
		return report.getShipping(pf);
	}

	public float getTaxRate(ProductFamily pf) {
		return report.getTaxRate(pf);
	}

	public boolean isEstimate(ProductFamily pf) {
		return report.isEstimate(pf);
	}

	public Iterator productIterator() {
		return report.productIterator();
	}
}
