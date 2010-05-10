/***********************************************************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA. Michael Stover can be reached via email at mstover1@rochester.rr.com or via snail mail at 130 Corwin
 * Rd. Rochester, NY 14610 The following exception to this license exists for Lazer Incorporated: Lazer Inc is excepted
 * from all limitations and requirements stipulated in the GPL. Lazer Inc. is the only entity allowed this limitation.
 * Lazer does have the right to sell this exception, if they choose, but they cannot grant additional exceptions to any
 * other entities.
 **********************************************************************************************************************/

package com.allarphoto.utils;

import java.util.Enumeration;
import java.util.Vector;

public class DataTree {

	char value;

	Vector link;

	int count, length;

	public static int MAXSIZE = 80;

	public char getValue() {
		return value;
	}

	/***************************************************************************
	 * This method returns the number of strings that pass through this node.
	 * 2/11/98 1:54PM
	 **************************************************************************/
	public int getCount() {
		return count;
	} // end of Method

	/***************************************************************************
	 * This method returns the value of length for the Tree node. Length for the
	 * DataTree measures how long the longest string is from that point on in
	 * the tree. 2/11/98 9:35AM
	 **************************************************************************/
	public int getLength() {
		return length;
	} // end of Method

	public DataTree getLink(int x) {
		return ((DataTree) link.elementAt(x));
	}

	public DataTree() {
		count = 0;
		length = 0;
		link = new Vector();
	}

	private DataTree createNode(char v) {
		DataTree t = new DataTree();
		t.value = v;
		t.count++;
		return t;
	}

	public int trimData() {
		int x = 0, length = 0;
		if (link.size() > 0) {
			while (x < link.size()) {
				// System.out.println("Before: size="+link.size()+"
				// count="+getLink(x).getCount());
				if (getLink(x).getCount() < 1) {

					link.removeElementAt(x);
				} else {
					int len = 1 + getLink(x).trimData();
					if (len == 2) {
						if (getLink(x).value != ' ') {
							link.removeElementAt(x);
							len = 1;
						}
					}
					if (len > length)
						length = len;
					x++;
				}
				// System.out.println("After: size="+link.size());
			}
			return length;
		} else {
			length = 1;
			return length;
		}
	}

	/***************************************************************************
	 * This method returns the common strings in descending order of length.
	 * After the DataTree has been trimmed (trimTree()), this method is used to
	 * get a list of all strings found by traversing to the end of each 'thread'
	 * in the tree. 2/9/98 10:51AM
	 **************************************************************************/
	public String[] getCommonStrings() {
		Vector v;
		v = getCommonStringsInternal();

		String[] s = new String[v.size()];
		Enumeration e = v.elements();
		int x = 0;

		x = 0;
		while (e.hasMoreElements())
			s[x++] = new String((StringBuffer) e.nextElement());
		int[] counts = new int[s.length];
		x = 0;
		while (x < counts.length) {
			counts[x] = findCount(s[x]);
			x++;
		}
		Functions.sortDesc(s, counts, 0, s.length);
		x = 0;
		while (x < s.length && s[x].length() > DataTree.MAXSIZE) {
			s = (String[]) Functions.pullOutOfArray(s, x);
			x++;
		}
		return s;
	}

	private int findCount(String s) {
		DataTree temp;
		int x;
		x = 0;

		while (x < link.size() && s.length() > 0
				&& getLink(x).value != s.charAt(0))
			x++;
		if (link.size() == 0)
			return 0;
		if (s.length() == 1)
			return getLink(x).count;
		else
			return getLink(x).findCount(s.substring(1, s.length()));
	}

	private Vector getCommonStringsInternal() {
		int x = 0, y;
		Vector holder = new Vector();
		DataTree temp;
		if (link.size() > 0) {
			while (x < link.size()) {
				temp = getLink(x);
				Vector tempHolder = temp.getCommonStringsInternal();
				y = 0;
				while (y < tempHolder.size()) {
					if (count > 0)
						((StringBuffer) tempHolder.elementAt(y)).insert(0,
								value);
					y++;
				}

				y = 0;
				while (y < tempHolder.size())
					holder.addElement(tempHolder.elementAt(y++));

				x++;
			}
		} else {
			StringBuffer tempBuffer;
			tempBuffer = new StringBuffer("");
			tempBuffer.append(value);
			holder.addElement(tempBuffer);
		}
		return holder;
	}

	private void addDataElementInner(String s) {
		int x = 0, y, end;
		char a;
		DataTree temp;
		if (s.length() > 0)
			a = s.charAt(0);
		else {
			System.out.println("Error");
			return;
		}
		if (s.length() > 1) {
			y = 1;
			addDataElementInner(s.substring(y, s.length()));
		}
		if (a != ' ') {
			return;
		}
		if (link.size() == 0) {
			temp = createNode(a);

			if (s.length() > 1) {
				y = 1;
				if (s.length() > DataTree.MAXSIZE)
					end = y + DataTree.MAXSIZE;
				else
					end = s.length();
				temp.addDataElementOnce(s.substring(y, end));
			}
			link.addElement(temp);
		} else {
			int pos = -1;
			x = 0;
			while (x < link.size()) {
				if (getLink(x).value == a)
					pos = x;
				x++;
			}
			if (pos >= 0) {
				temp = getLink(pos);
				temp.count++;
				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
			} else {
				temp = createNode(a);

				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
				link.addElement(temp);
			}
		}
	}

	public void addDataElement(String s) {
		int x = 0, y, end;
		char a;
		DataTree temp;
		s = s + " ";
		if (s.length() > 0)
			a = s.charAt(0);
		else {
			System.out.println("Error");
			return;
		}
		if (s.length() > 1) {
			y = 1;
			addDataElementInner(s.substring(y, s.length()));
		}
		if (link.size() == 0) {
			temp = createNode(a);

			if (s.length() > 1) {
				y = 1;
				if (s.length() > DataTree.MAXSIZE)
					end = y + DataTree.MAXSIZE;
				else
					end = s.length();
				temp.addDataElementOnce(s.substring(y, end));
			}
			link.addElement(temp);
		} else {
			int pos = -1;
			x = 0;
			while (x < link.size()) {
				if (getLink(x).value == a)
					pos = x;
				x++;
			}
			if (pos >= 0) {
				temp = getLink(pos);
				temp.count++;
				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
			} else {
				temp = createNode(a);

				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
				link.addElement(temp);
			}
		}
	}

	private void addDataElementOnce(String s) {
		int x = 0, y, end;
		char a;
		DataTree temp;
		if (s.length() > 0)
			a = s.charAt(0);
		else {
			System.out.println("Error");
			System.exit(-1);
			return;
		}
		if (link.size() == 0) {
			temp = createNode(a);

			if (s.length() > 1) {
				y = 1;
				if (s.length() > DataTree.MAXSIZE)
					end = y + DataTree.MAXSIZE;
				else
					end = s.length();
				temp.addDataElementOnce(s.substring(y, end));
			}
			link.addElement(temp);
		} else {
			int pos = -1;
			x = 0;
			while (x < link.size()) {
				if (getLink(x).value == a)
					pos = x;
				x++;
			}
			if (pos >= 0) {
				temp = getLink(pos);
				temp.count++;
				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
			} else {
				temp = createNode(a);

				if (s.length() > 1) {
					y = 1;
					if (s.length() > DataTree.MAXSIZE)
						end = y + DataTree.MAXSIZE;
					else
						end = s.length();
					temp.addDataElementOnce(s.substring(y, end));
				}
				link.addElement(temp);
			}
		}
	}

}
