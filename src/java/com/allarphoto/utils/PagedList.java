package com.lazerinc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PagedList<T> implements List<T>, PagedCollection<T>, Serializable {
	private static final long serialVersionUID = 1;

	int pagingSize = 12;

	int pageNo = 0;

	List<List<T>> list;

	public PagedList() {
		list = new ArrayList<List<T>>();
	}

	public PagedList(List<T> c) {
		list = new ArrayList<List<T>>();
		list.add(Collections.unmodifiableList(c));
	}

	public PagedList(List<T> c, int pagingSize) {
		this(c);
		setPagingSize(pagingSize);
	}

	public void setList(Collection<T> listToPage) {
		list.clear();
		addAll((listToPage == null) ? (List<T>) Collections.EMPTY_LIST
				: listToPage);
		if (getPageNo() >= getNumberPages()) {
			setPageNo(0);
		}
	}

	public Collection<T> getPage() {
		int start = getPagingSize() * getPageNo();
		int end = getPagingSize() * (getPageNo() + 1);
		if (end <= size())
			return subList(start, end);
		else if (start < size())
			return subList(start, size());
		else
			return Collections.EMPTY_LIST;
	}

	public int getStartIndex() {
		return ((getPagingSize() * getPageNo() + 1) < size()) ? getPagingSize()
				* getPageNo() + 1 : size();
	}

	public int getEndIndex() {
		return ((getPagingSize() * (getPageNo() + 1)) <= size()) ? (getPagingSize() * (getPageNo() + 1))
				: size();
	}

	public int getNumberPages() {
		return size() / getPagingSize()
				+ ((size() % getPagingSize() > 0) ? 1 : 0);
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPagingSize() {
		return pagingSize;
	}

	public void setPageNo(int no) {
		if (no < getNumberPages()) {
			System.out.println("page no = " + no + " number pages =  "
					+ getNumberPages());
			pageNo = no;
		} else
			pageNo = 0;

	}

	public void setPagingSize(int ps) {
		pagingSize = ps;

	}

	public void add(int index, T element) {
		int[] indexes = normalizeIndex(index);
		list.get(indexes[0]).add(indexes[1], element);
	}

	public boolean add(T o) {
		return list.get(list.size() - 1).add(o);
	}

	public boolean addAll(Collection<? extends T> c) {
		return list.add(Collections.unmodifiableList(new ArrayList<T>(c)));
	}

	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		list.clear();
	}

	public boolean contains(Object o) {
		for (List<T> sublist : list) {
			if (sublist.contains(o))
				return true;
		}
		return false;
	}

	public boolean containsAll(Collection c) {
		for (Object o : c) {
			if (!contains(o))
				return false;
		}
		return true;
	}

	public boolean equals(Object o) {
		return list.equals(o);
	}

	public T get(int index) {
		int[] n = normalizeIndex(index);
		return list.get(n[0]).get(n[1]);
	}

	public int hashCode() {
		return list.hashCode();
	}

	public int indexOf(Object o) {
		int index = 0;
		for (List<T> sublist : list) {
			int temp = sublist.indexOf(o);
			if (temp > -1)
				index += temp;
			else
				index += sublist.size();
		}
		if (index >= size())
			return -1;
		return index;
	}

	public boolean isEmpty() {
		return list.isEmpty() || size() == 0;
	}

	public Iterator iterator() {
		List<T> temp = new LinkedList<T>();
		for (List<T> sub : list)
			temp.addAll(sub);
		return temp.iterator();
	}

	public int lastIndexOf(Object o) {
		int index = size() - 1;
		while (index >= 0) {
			if (get(index).equals(o))
				return index;
		}
		return -1;
	}

	public ListIterator listIterator() {
		throw new UnsupportedOperationException();
	}

	public ListIterator listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		int size = 0;
		if (list == null)
			return 0;
		for (List<T> sublist : list) {
			if (sublist != null)
				size += sublist.size();
		}
		return size;
	}

	public List subList(int fromIndex, int toIndex) {
		int[] from = normalizeIndex(fromIndex);
		int[] to = normalizeIndex(toIndex);
		if (from[0] == to[0]) {
			return list.get(from[0]).subList(from[1], to[1]);
		} else {
			List<T> splice = new ArrayList<T>();
			while (from[0] < to[0]) {
				List<T> sublist = list.get(from[0]);
				splice.addAll(sublist.subList(from[1], sublist.size()));
				from[0]++;
				from[1] = 0;
			}
			splice.addAll(list.get(to[0]).subList(0, to[1]));
			return splice;
		}
	}

	public Object[] toArray() {
		List<T> megaList = new ArrayList<T>(size());
		for (List<T> sub : list)
			megaList.addAll(sub);
		return megaList.toArray();
	}

	public Object[] toArray(Object[] a) {
		List<T> megaList = new ArrayList<T>(size());
		for (List<T> sub : list)
			megaList.addAll(sub);
		return megaList.toArray(a);
	}

	private int[] normalizeIndex(int index) {
		int[] normalized = new int[] { 0, 0 };
		if (list.size() <= normalized[0]) {
			throw new IndexOutOfBoundsException();
		}
		while (index >= list.get(normalized[0]).size()) {
			index -= list.get(normalized[0]).size();
			normalized[0]++;
			if (list.size() <= normalized[0]) {
				if (index == 0) {
					normalized[0]--;
					index = list.get(normalized[0]).size();
					break;
				} else
					throw new IndexOutOfBoundsException();
			}
		}
		normalized[1] = index;
		return normalized;
	}

}
