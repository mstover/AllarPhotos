/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.ecommerce;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.Product;

/*******************************************************************************
 * Stores cost information for a number of Products. Costs stored are per
 * Product. Also provides information about shipping charge estimates, and tax
 * rates.
 ******************************************************************************/
@CoinjemaObject
public class CostReport implements Serializable {
	private static final long serialVersionUID = 1;

	Logger log;

	protected SortedMap<Product, ProductReport> products; // holds quantity and cost;

	protected Map<ProductFamily, FamilyReport> families; // holds shipping total and tax

	// rate.

	double downloadSize = 0D;

	public double getDownloadSize() {
		return downloadSize;
	}

	public void addToDownloadSize(double ds) {
		this.downloadSize += ds;
	}

	public void setDownloadSize(double downloadSize) {
		this.downloadSize = downloadSize;
	}

	public CostReport() {
		products = new TreeMap<Product, ProductReport>();
		families = new HashMap<ProductFamily, FamilyReport>();
	}

	/***************************************************************************
	 * Adds all the entries of one cost report to this cost report.
	 * 
	 * @param report
	 *            CostReport object to be added to this one.
	 **************************************************************************/
	public void add(CostReport report) {
		products.putAll(report.products);
		families.putAll(report.families);
		addToDownloadSize(report.getDownloadSize());
	}

	Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	/***************************************************************************
	 * Provides an iterator of the products in this cost report.
	 * 
	 * @return Iterator object for the products in the cost report.
	 **************************************************************************/
	public Iterator productIterator() {
		return products.keySet().iterator();
	}

	/***************************************************************************
	 * Provides an iterator of the ProductFamilies in this cost report.
	 * 
	 * @return Iterator object for the ProductFamilies in the cost report.
	 **************************************************************************/
	public Iterator familyIterator() {
		return families.keySet().iterator();
	}

	private ProductReport getProductReport(Product p) {
		ProductReport pr;
		if ((pr = (ProductReport) products.get(p)) == null) {
			pr = new ProductReport(p);
			products.put(p, pr);
		}
		return pr;
	}

	private FamilyReport getFamilyReport(ProductFamily pf) {
		FamilyReport fr;
		if ((fr = (FamilyReport) families.get(pf)) == null) {
			fr = new FamilyReport();
			families.put(pf, fr);
		}
		return fr;
	}

	/***************************************************************************
	 * Sets the per unit cost for a product.
	 * 
	 * @param p
	 *            Product to have cost set for.
	 * @param cost
	 *            Cost of product.
	 **************************************************************************/
	public void setCost(Product p, float cost) {
		ProductReport pr = getProductReport(p);
		pr.cost = cost;
	}

	/***************************************************************************
	 * Sets the quantity ordered for a product.
	 * 
	 * @param p
	 *            Product to have quantity set for.
	 * @param quantity
	 *            Quantity of product.
	 **************************************************************************/
	public void setQuantity(Product p, int quantity) {
		ProductReport pr = getProductReport(p);
		pr.quantity = quantity;
	}

	/***************************************************************************
	 * Sets the tax rate for a ProductFamily.
	 * 
	 * @param pf
	 *            ProductFamily to have tax rate set for.
	 * @param taxRate
	 *            Tax rate for product family.
	 **************************************************************************/
	public void setTaxRate(ProductFamily pf, float taxRate) {
		FamilyReport fr = getFamilyReport(pf);
		fr.taxRate = taxRate;
	}

	/***************************************************************************
	 * Sets the shipping cost for a ProductFamily.
	 * 
	 * @param pf
	 *            ProductFamily to have shipping cost set for.
	 * @param shipping
	 *            Shipping cost for all products in the product family.
	 **************************************************************************/
	public void setShipping(ProductFamily pf, float shipping) {
		FamilyReport fr = getFamilyReport(pf);
		fr.shipping = shipping;
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
		if (cost < 25)
			return 25;
		return cost;
	}

	/***************************************************************************
	 * Sets an indicator for whether the shipping cost is an estimate or if it
	 * is committed to.
	 * 
	 * @param pf
	 *            ProductFamily to have estimate indicator set for.
	 * @param estimate
	 *            Estimate indicator (true or false).
	 **************************************************************************/
	public void setEstimate(ProductFamily pf, boolean estimate) {
		FamilyReport fr = getFamilyReport(pf);
		fr.estimate = estimate;
	}

	/***************************************************************************
	 * Gets the cost per unit for a given Product.
	 * 
	 * @param p
	 *            Product to get cost for.
	 * @return Cost of product.
	 **************************************************************************/
	public float getCost(Product p) {
		ProductReport pr = getProductReport(p);
		return pr.cost;
	}

	/***************************************************************************
	 * Gets the quantity ordered for a given Product.
	 * 
	 * @param p
	 *            Product to get quantity of.
	 * @return Quantity of product.
	 **************************************************************************/
	public int getQuantity(Product p) {
		ProductReport pr = getProductReport(p);
		return pr.quantity;
	}

	/***************************************************************************
	 * Gets the tax rate for a ProductFamily.
	 * 
	 * @param pf
	 *            ProductFamily to get tax rate for.
	 * @return Tax rate of product family.
	 **************************************************************************/
	public float getTaxRate(ProductFamily pf) {
		FamilyReport fr = getFamilyReport(pf);
		return fr.taxRate;
	}

	/***************************************************************************
	 * Gets the shipping cost of all products for a given Product Family.
	 * 
	 * @param pf
	 *            Product family to get shipping cost for.
	 * @return Shipping cost of all products in product family.
	 **************************************************************************/
	public float getShipping(ProductFamily pf) {
		FamilyReport fr = getFamilyReport(pf);
		return fr.shipping;
	}

	/***************************************************************************
	 * Gets the indicator for whether shipping cost is an estimate or committed
	 * to for a product family.mate indicator for.
	 * 
	 * @return Shipping cost indicator.
	 **************************************************************************/
	public boolean isEstimate(ProductFamily pf) {
		FamilyReport fr = getFamilyReport(pf);
		return fr.estimate;
	}

	protected class ProductReport {

		public int quantity = 0;

		public float cost = 0;

		public Product product;

		public ProductReport(Product p) {
			product = p;
		}
	}

	protected class FamilyReport {

		public float shipping = 0;

		public float taxRate = 0;

		public boolean estimate = true;
	}
}
