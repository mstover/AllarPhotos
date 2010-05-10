package com.allarphoto.client.beans;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.CartObject;
import com.allarphoto.application.Product;
import com.allarphoto.ecommerce.CommerceProduct;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.server.ProductService;

/*******************************************************************************
 * Description of the Class
 * 
 * @author default
 * @created January 2, 2002
 ******************************************************************************/
@CoinjemaObject
public class ShoppingCartBean implements Serializable, Iterable<Product> {
	private static final long serialVersionUID = 1;

	Map instr = new HashMap();

	SortedMap<Product, Cart> items = new TreeMap<Product, Cart>();

	Map<ProductFamily, Collection<CartObject>> familyList = new HashMap<ProductFamily, Collection<CartObject>>();

	transient DatabaseUtilities dbutil;

	/***************************************************************************
	 * Constructor for the ShoppingCartBean object
	 **************************************************************************/
	public ShoppingCartBean() {
		super();
	}

	public boolean isOrderRequest() {
		return getOrderedProducts().size() > 0;
	}

	public boolean isDownloadRequest() {
		return getDownloadProducts().size() > 0;
	}

	public Set getOrderedProducts() {
		Set order = new TreeSet();
		Iterator it = iterator();
		while (it.hasNext()) {
			Product product = (Product) it.next();
			if (hasInstruction(product, "order")) {
				order.add(product);
			}
		}
		return order;
	}

	public Set getDownloadProducts() {
		Set download = new TreeSet();
		Iterator it = iterator();
		while (it.hasNext()) {
			Product product = (Product) it.next();
			if (hasInstruction(product, "download")) {
				download.add(product);
			}
		}
		return download;
	}

	public float getTotalOrderSize() {
		Iterator it = getOrderedProducts().iterator();
		float downloadSize = 0;
		while (it.hasNext()) {
			Product product = (Product) it.next();
			downloadSize += ((Number) product.getValue("File Size"))
					.floatValue();
			// added for totalling size of downloaded files
		}
		return downloadSize;
	}

	/***************************************************************************
	 * Sets the quantity desired for a particular Product.
	 * 
	 * @param p
	 *            Product to set quantity for.
	 * @param q
	 *            Value to set quantity to.
	 **************************************************************************/
	public void setQuantity(Product p, int q) {
		Cart c = (Cart) getItems().get(p);
		if (c != null) {
			c.quantity = q;
		}
	}

	/***************************************************************************
	 * Gets the CartObject attribute of the ShoppingCartBean object
	 * 
	 * @param p
	 *            Description of Parameter
	 * @return The CartObject value
	 **************************************************************************/
	public CartObject getCartObject(Product p) {
		return (CartObject) getItems().get(p);
	}

	/***************************************************************************
	 * Gets the GlobalInstructions attribute of the ShoppingCartBean object
	 * 
	 * @return The GlobalInstructions value
	 **************************************************************************/
	public Map getGlobalInstructions() {
		return instr;
	}

	public Object getInstruction(String key) {
		return instr.get(key);
	}

	public boolean hasInstructionValue(String key, String detail) {
		Object in = getInstruction(key);
		if (in instanceof Set) {
			if (((Set) in).contains(detail))
				return true;
		}
		return false;
	}

	public boolean containsInstructionString(String key, String detail) {

		Object in = getInstruction(key);
		if (in instanceof Set) {
			for (String value : (Set<String>) in) {
				value = value.toLowerCase();
				if (value.indexOf(detail) > -1)
					return true;
			}
		}
		return false;
	}

	public Map<String, Set<String>> getInstructions(Product p) {
		return getCartObject(p) != null ? getCartObject(p).getInstructions()
				: null;
	}

	public Set<String> getInstructions(Product p, String key) {
		return getInstructions(p) != null ? getInstructions(p).get(key) : null;
	}

	public boolean containsInstruction(Product p, String key, String instruction) {
		return getInstructions(p, key) != null
				&& getInstructions(p, key).contains(instruction);
	}

	public boolean containsProductInstruction(String key, String instruction) {
		for (Cart cartObj : items.values()) {
			if (cartObj.instructions.get(key) != null
					&& cartObj.instructions.get(key).contains(instruction))
				return true;
		}
		return false;
	}

	/***************************************************************************
	 * Gets all products with a given instruction.
	 * 
	 * @param instr
	 *            Instruction to search for.
	 * @return Set of Products
	 **************************************************************************/
	public Set<Product> getProducts(String instr) {
		Iterator it = getItems().keySet().iterator();
		Set<Product> ret = new HashSet<Product>();
		Product p;
		Cart c;
		while (it.hasNext()) {
			p = (Product) it.next();
			c = (Cart) getItems().get(p);
			if (c.instructions.containsKey(instr)) {
				ret.add(p);
			}
		}
		return ret;
	}

	public Collection<Product> getProducts() {
		return items.keySet();
	}

	/***************************************************************************
	 * Gets the FamilyLists attribute of the ShoppingCartBean object
	 * 
	 * @return The FamilyLists value
	 **************************************************************************/
	public Map<ProductFamily, Collection<CartObject>> getFamilyLists() {
		return getFamilyList();
	}

	/***************************************************************************
	 * Gets the current quantity for the given Product.
	 * 
	 * @param p
	 *            Product to get quantity for.
	 * @return quantity value.
	 **************************************************************************/
	public int getQuantity(Product p) {
		Cart c = (Cart) getItems().get(p);
		return c.quantity;
	}

	/**
	 * Put this here for now - should probably be a taglib instead.
	 */
	public long getTotalDownloadSize() {
		Iterator it = getDownloadProducts().iterator();
		long downloadSize = 0;
		while (it.hasNext()) {
			Product product = (Product) it.next();
			downloadSize += ((Number) product.getValue("File Size"))
					.longValue();
			// added for totalling size of downloaded files
		}
		return downloadSize;
	}

	public long getDownloadableSize() {
		long size = 0;
		for (Product p : this) {
			size += ((Number) p.getValue("File Size")).longValue();
		}
		size = size / 1024;
		return size;
	}

	/***************************************************************************
	 * Gets the Product attribute of the ShoppingCartBean object
	 * 
	 * @param p
	 *            Description of Parameter
	 * @param tName
	 *            Description of Parameter
	 * @return The Product value
	 **************************************************************************/
	public Product getProduct(String p, String tName) {
		Product pr = new CommerceProduct();
		pr.setPrimary(p);
		ProductFamily pf = new ProductFamily(tName);
		pr.setProductFamily(pf);
		if (getItems().get(pr) == null) {
			return null;
		}
		return ((Cart) getItems().get(pr)).prod;
	}

	/***************************************************************************
	 * Determines if a product is present in the ShoppingCartBean object
	 * 
	 * @param pString
	 *            primary value and table name separated by "|"
	 * @return whether product is present
	 **************************************************************************/
	public boolean hasProduct(Product product) {
		boolean retVal = false;
		if (getItems().get(product) != null)
			retVal = true;
		return retVal;
	}

	/***************************************************************************
	 * Description of the Method
	 **************************************************************************/
	public void clear() {
		getItems().clear();
		instr.clear();
		familyList.clear();

	}

	public void clearInstructions() {
		getGlobalInstructions().clear();
		for (Cart item : items.values()) {
			item.instructions.clear();
		}
	}

	public void clearGlobalInstructions() {
		getGlobalInstructions().clear();
	}

	/***************************************************************************
	 * Method to discover if a product has a given instruction associated with
	 * it in the shopping cart.
	 * 
	 * @param p
	 *            Product in question.
	 * @param instr
	 *            Instruction in question.
	 * @return True if it has the instruction, false otherwise.
	 **************************************************************************/
	public boolean hasInstruction(Product p, String instr) {
		Cart c = (Cart) getItems().get(p);
		if (c != null) {
			return c.instructions.containsKey(instr);
		} else {
			return false;
		}
	}

	/***************************************************************************
	 * Gets the number of Products in the shopping cart.
	 * 
	 * @return Number of Products in the shopping cart.
	 **************************************************************************/
	public int size() {
		return getItems().size();
	}

	/***************************************************************************
	 * Gets the number of Products in the shopping cart.
	 * 
	 * @return Number of Products in the shopping cart.
	 **************************************************************************/
	public int getSize() {
		return size();
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 **************************************************************************/
	public String saveCartToString() {
		StringBuffer cartString = new StringBuffer();
		boolean first = true;
		for (Iterator x = getItems().keySet().iterator(); x.hasNext();) {
			Product p = (Product) x.next();
			if (p != null) {
				if (!first) {
					cartString.append("|");
				} else {
					first = false;
				}
				cartString.append(p.getProductFamily().getTableName());
				cartString.append("~");
				cartString.append(p.getId());
				if (getInstructions(p, "order") != null)
					cartString.append("~order");
				if (getInstructions(p, "download") != null) {
					for (String inst : getInstructions(p, "download"))
						cartString.append("~download:" + inst);
				}
			}
		}
		return cartString.toString();
	}

	/***************************************************************************
	 * Removes an instruction for the given Product.
	 * 
	 * @param p
	 *            Product to remove instruction for.
	 * @param instr
	 *            Name of instruction to remove.
	 **************************************************************************/
	public void removeInstruction(Product p, String instr) {
		Cart c = (Cart) getItems().get(p);
		if (c != null) {
			c.instructions.remove(instr);
		}
	}

	/***************************************************************************
	 * Removes an instruction for the whole order.
	 * 
	 * @param instr
	 *            Name of instruction to remove.
	 **************************************************************************/
	public void removeInstruction(String instr) {
		getGlobalInstructions().remove(instr);
	}

	/***************************************************************************
	 * Gets an iterator object to iterate through the list of products.
	 * 
	 * @return An appropriate iterator.
	 **************************************************************************/
	public Iterator<Product> iterator() {
		return getItems().keySet().iterator();
	}

	/***************************************************************************
	 * Method to discover if the shopping cart contains a given instruction.
	 * 
	 * @param instr
	 *            Instruction in question.
	 * @return True if it has the instruction, false otherwise.
	 **************************************************************************/
	public boolean hasInstruction(String instr) {
		return getGlobalInstructions().containsKey(instr);
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param p
	 *            Description of Parameter
	 **************************************************************************/
	public void add(Product p) {
		if (/*
			 * security.getPermission(pf.getTableName(),Rights.DATATABLE,Rights.READ) &&
			 */
		!getItems().containsKey(p)) {
			Cart c = new Cart(p, 1);
			getItems().put(p, c);
			addToFamilyList(c);
		}
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param p
	 *            Description of Parameter
	 **************************************************************************/
	public void remove(Product p) {
		this.removeFromFamilyList((Cart) getCartObject(p));
		getItems().remove(p);
	}

	/***************************************************************************
	 * Adds a feature to the Instruction attribute of the ShoppingCartBean
	 * object
	 * 
	 * @param p
	 *            The feature to be added to the Instruction attribute
	 * @param instr
	 *            The feature to be added to the Instruction attribute
	 * @param details
	 *            The feature to be added to the Instruction attribute
	 **************************************************************************/
	public void addInstruction(Product p, String instr, String details) {
		Cart c = (Cart) getCartObject(p);
		if (c != null) {
			Set<String> current = (Set<String>) c.instructions.get(instr);
			if (current == null) {
				Set set = new HashSet<String>();
				set.add(details);
				c.instructions.put(instr, set);
			} else {
				current.add(details);
			}
		}
	}

	/***************************************************************************
	 * Adds a feature to the All attribute of the ShoppingCartBean object
	 * 
	 * @param c
	 *            The feature to be added to the All attribute
	 **************************************************************************/
	public void addAll(Collection c) {
		Iterator it = c.iterator();
		while (it.hasNext()) {
			try {
				Product p = (Product) it.next();
				add(p);
			} catch (ClassCastException e) {
			}
		}
	}

	/***************************************************************************
	 * Adds all product objects to the shopping cart.
	 * 
	 * @param a
	 *            Array of products to add.
	 **************************************************************************/
	public void addAll(Product[] a) {
		for (int x = 0; x < a.length; x++) {
			add(a[x]);
		}
	}

	/***************************************************************************
	 * Adds an instruction for the whole order.
	 * 
	 * @param instr
	 *            String instruction name.
	 * @param details
	 *            The feature to be added to the Instruction attribute
	 **************************************************************************/
	public void addInstruction(String instr, String details) {
		if (null != details) {
			Set current = (Set) getGlobalInstructions().get(instr);
			if (current == null) {
				Set set = new HashSet();
				set.add(details);
				getGlobalInstructions().put(instr, set);
			} else {
				current.add(details);
			}
		}
	}

	protected SortedMap getItems() {
		return items;
	}

	private Map<ProductFamily, Collection<CartObject>> getFamilyList() {
		return familyList;
	}

	public Collection<CartObject> getProductsByFamily(String pf) {
		return familyList.get(getDbUtil().getProductFamily(pf));
	}

	private void addToFamilyList(Cart c) {
		ProductFamily pf = c.prod.getProductFamily();
		ArrayList list;
		if ((list = (ArrayList) getFamilyList().get(pf)) == null) {
			list = new ArrayList();
			getFamilyList().put(pf, list);
		}
		list.add(c);
	}

	private void removeFromFamilyList(Cart c) {
		if (c == null)
			return;
		ProductFamily pf = c.prod.getProductFamily();
		ArrayList list;
		if ((list = (ArrayList) getFamilyList().get(pf)) != null) {
			list.remove(c);
		} else {
			return;
		}
		if (list.size() == 0) {
			getFamilyList().remove(pf);
		}
	}

	public DatabaseUtilities getDbUtil() {
		return dbutil;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities db) {
		this.dbutil = db;
	}

	private void readObject(ObjectInputStream in) {
		try {
			in.defaultReadObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
