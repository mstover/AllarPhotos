/*******************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA. Michael Stover can be reached via email at
 * mstover1@rochester.rr.com or via snail mail at 130 Corwin Rd. Rochester, NY
 * 14610 The following exception to this license exists for Lazer Incorporated:
 * Lazer Inc is excepted from all limitations and requirements stipulated in the
 * GPL. Lazer Inc. is the only entity allowed this limitation. Lazer does have
 * the right to sell this exception, if they choose, but they cannot grant
 * additional exceptions to any other entities.
 ******************************************************************************/

// Title: BitPatternFinder
// Author: Michael Stover
// Company: Lazer inc.
package com.allarphoto.utils;

import java.util.BitSet;

public class BitPatternFinder {

	static int depth = 10;

	Node top = new Node(0);

	static boolean LEFT = false, RIGHT = true;

	public BitPatternFinder() {
	}

	public void submitBitsForPatternRecognition(BitSet bits, BitBreak[] breaks) {
		if (breaks != null)
			System.out.println("breaks.length = " + breaks.length);
		int count = 0;
		boolean go = true;
		int size = bits.size();
		Node temp;
		for (int z = 0; z < size; z++) {
			temp = top;
			go = true;
			for (int x = 0; x < depth && x + z < size; x++) {
				if (breaks != null && count < breaks.length) {
					if ((z + x >= breaks[count].getPos())
							&& (z + x < breaks[count].getPos()
									+ breaks[count].getLength())) {
						if (z >= breaks[count].getPos()
								&& z < breaks[count].getPos()
										+ breaks[count].getLength()) {
							z = breaks[count].getPos()
									+ breaks[count].getLength();
							count++;
						}
						x = depth;
						go = false;
					}
				}
				if (go) {
					if (bits.get(z + x)) {
						if (temp.right == null) {
							temp.right = new Node(1);
							temp.right.blockedTill = z + x;
							temp = temp.right;
						} else {
							if (z > temp.right.blockedTill) {
								temp.right.blockedTill = z + x;
								temp.right.count++;
							}
							temp = temp.right;
						}
					} else {
						if (temp.left == null) {
							temp.left = new Node(1);
							temp.left.blockedTill = z + x;
							temp = temp.left;
						} else {
							if (z > temp.left.blockedTill) {
								temp.left.blockedTill = z + x;
								temp.left.count++;
							}
							temp = temp.left;
						}
					}
				}
			}
		}
	}

	public BitSet findBigestPattern(int codeSize) {
		int savings = 0;
		Node temp;
		int dep = -1;
		Traverse trav = new Traverse();
		savings = trav.findBest(codeSize, top, dep, false);
		System.out.println("Savings = " + savings);
		return trav.getBest();
	}

	private class Traverse {

		public BitSet bit = new BitSet(depth + 2), bestBit;

		int savings = 0;

		public BitSet getBest() {
			return bestBit;
		}

		public int findBest(int codeSize, Node temp, int dep, boolean set) {
			int possibleSavings = Integer.MIN_VALUE;
			if (dep >= 0) {
				if (set)
					bit.set(dep);
				else
					bit.clear(dep);
			}
			bit.set(dep + 1);
			bit.clear(dep + 2);
			if (dep != -1 && temp.count > 0) {
				possibleSavings = (int) (((dep + 1) - codeSize) * temp.count - (dep
						+ 1 + codeSize + 16)) / 8;
				if (possibleSavings > savings) {
					savings = possibleSavings;
					bestBit = (BitSet) bit.clone();
				}
				temp.count = 0;
			}
			if (temp.left != null && temp.left.count != 0)
				findBest(codeSize, temp.left, dep + 1, LEFT);
			temp.count = -1;
			bit.set(dep + 1);
			bit.clear(dep + 2);
			if (temp.right != null && temp.right.count != -1)
				findBest(codeSize, temp.right, dep + 1, RIGHT);
			bit.set(dep + 1);
			bit.clear(dep + 2);
			return savings;
		}
	}

	private class Node {

		public int count;

		public Node left, right;

		public int blockedTill = 0;

		public Node(int i) {
			count = i;
		}

		public String toString() {
			StringBuffer ret = new StringBuffer();
			ret.append("Node{count=" + count + "}");
			return ret.toString();
		}
	}

}
