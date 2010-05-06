package com.lazerinc.utils;

import java.util.Collection;

public interface PagedCollection<T> {
	int getPagingSize();

	void setPagingSize(int size);

	Collection<T> getPage();

	int getPageNo();

	void setPageNo(int no);

	int getStartIndex();

	int getEndIndex();

	int getNumberPages();
}
