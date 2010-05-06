package com.lazerinc.ajaxclient.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lazerinc.category.ProductField;

public class SearchFieldFilter {

	public static List<ProductField> resortFields(List<ProductField> fields) {
		List<ProductField> filteredFields = new ArrayList<ProductField>();
		for (ProductField field : fields) {
			if (field.getSearchOrder() > 0
					&& (field.getType() == ProductField.CATEGORY
							|| field.getType() == ProductField.DESCRIPTION
							|| field.getType() == ProductField.PROTECTED || field
							.getType() == ProductField.TAG))
				filteredFields.add(field);
		}
		Collections.sort(filteredFields, new Comparator<ProductField>() {

			public int compare(ProductField o1, ProductField o2) {
				int ret = o2.getSearchOrder() - o1.getSearchOrder();
				if (ret == 0)
					ret = o1.getDisplayOrder() - o2.getDisplayOrder();
				return ret;
			}
		});
		return filteredFields;
	}
}
