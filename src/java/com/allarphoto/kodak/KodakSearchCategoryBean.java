package com.lazerinc.kodak;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.coinjema.collections.HashTree;

import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.client.beans.SearchCategoryBean;

public class KodakSearchCategoryBean extends SearchCategoryBean {

	public HashTree getSearchCategories(ProductSetBean productsFound) {
		HashTree tree = new HashTree();
		tree.set(super.getSearchCategories());
		if (productsFound.getSearchCategoryValue("Business Category") == null
				|| !productsFound.getSearchCategoryValue("Business Category")
						.equals("Inkjet Media")) {
			tree.remove("Region");
		}
		tree.remove("DFIS Product");
		tree.remove("Series");
		return tree;
	}

	@Override
	public Collection getCommonCategories() {
		Collection c = super.getCommonCategories();
		if (c != null) {
			List l = new LinkedList(c);
			l.remove("File Format");
			l.remove("Business Unit");
			return l;
		}
		return null;
	}

}
