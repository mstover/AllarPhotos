package com.lazerinc.client.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lazerinc.application.CartObject;
import com.lazerinc.application.Product;

/*******************************************************************************
 * Description of the Class
 * 
 * @author default
 * @created January 2, 2002
 ******************************************************************************/
public class Cart implements CartObject {
	private static final long serialVersionUID = 1;

	public Product prod;

	public Map<String, Set<String>> instructions;

	public int quantity;

	public float cost;

	/***************************************************************************
	 * Constructor for the Cart object
	 * 
	 * @param p
	 *            Description of Parameter
	 * @param q
	 *            Description of Parameter
	 **************************************************************************/
	public Cart(Product p, int q) {
		prod = p;
		quantity = q;
		instructions = new HashMap<String, Set<String>>();
	}

	/***************************************************************************
	 * Gets the Product attribute of the Cart object
	 * 
	 * @return The Product value
	 **************************************************************************/
	public Product getProduct() {
		return prod;
	}

	/***************************************************************************
	 * Gets the Instructions attribute of the Cart object
	 * 
	 * @return The Instructions value
	 **************************************************************************/
	public Map<String, Set<String>> getInstructions() {
		return instructions;
	}

	/***************************************************************************
	 * Gets the Quantity attribute of the Cart object
	 * 
	 * @return The Quantity value
	 **************************************************************************/
	public int getQuantity() {
		return quantity;
	}

	public void addInstruction(String key, String instr) {
		Set<String> set = instructions.get(key);
		if (set == null) {
			set = new HashSet<String>();
			instructions.put(key, set);
		}
		set.add(instr);
	}
}