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

// Title: BitReader
// Author: Michael Stover
// Company: Lazer inc.
package com.lazerinc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;

public class BitReader {

	private BitSet bits;

	private static int[] series = { 128, 64, 32, 16, 8, 4, 2, 1 };

	public BitReader() {
	}

	public static void main(String[] args) {
		BitReader reader = new BitReader();
		System.out.println("Reading in file");
		reader.getFileAsBits(args[0]);
		BitPatternFinder pattern = new BitPatternFinder();
		pattern.submitBitsForPatternRecognition(reader.getBits(), null);
		BitSet pat = pattern.findBigestPattern(2);
		System.out.print("Bit pattern = " + (pat.length() - 1) + "{");
		for (int x = 0; x < pat.length() - 1; x++) {
			if (pat.get(x))
				System.out.print("1");
			else
				System.out.print("0");
		}
		BitBreak[] breaks = reader.getBreaks(reader.getBits(), pat);
		pattern.submitBitsForPatternRecognition(reader.getBits(), breaks);
		pat = pattern.findBigestPattern(3);
		System.out.print("Bit pattern = " + (pat.length() - 1) + "{");
		for (int x = 0; x < pat.length() - 1; x++) {
			if (pat.get(x))
				System.out.print("1");
			else
				System.out.print("0");
		}

		System.out.println("}");
		System.out.println("Writing file");
		reader.saveFileFromBits(args[1]);
	}

	public BitSet getBits() {
		return bits;
	}

	public void getFileAsBits(String filename) {
		int temp, progress, numBytes = 0;
		BufferedInputStream in;
		File f = new File(filename);
		progress = (int) f.length() / 100;
		for (int x = 0; x < 100; x++)
			System.out.print(".");
		System.out.println();
		bits = new BitSet((int) f.length() * 8);
		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			int count = 0;
			while ((temp = in.read()) != -1) {
				count = convertToBinary(temp, count);
				if (count / 8 % progress == 0)
					System.out.print(".");
				numBytes++;
			}
			in.close();
		} catch (java.io.IOException e) {
		}
		System.out.println();
		System.out.println("Num Bytes read = " + numBytes);
	}

	public void saveFileFromBits(String filename) {
		int temp, progress, numBytes = 0;
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(new FileOutputStream(filename));
			int size = bits.size();
			progress = size / 100;
			for (int x = 0; x < 100; x++)
				System.out.print(".");
			System.out.println();
			for (int x = 0; x < size; x += 8) {
				temp = convertFromBinary(x);
				out.write(temp);
				if (x % progress < 8)
					System.out.print(".");
				numBytes++;
			}
			out.flush();
			out.close();
		} catch (IOException e) {
		}
		System.out.println();
		System.out.println("Num Bytes read = " + numBytes);
	}

	private int convertToBinary(int num, int start) {
		for (int x = 0; x < 8; x++) {
			if (num >= series[x]) {
				bits.set(start);
				num -= series[x];
			} else
				bits.clear(start);
			start++;
		}
		return start;
	}

	private int convertFromBinary(int start) {
		int value = 0;
		for (int x = 0; x < 8; x++) {
			if (bits.get(x + start))
				value += series[x];
		}
		return value;
	}

	/***************************************************************************
	 * The pattern ends with a 1 indicating the end. The pattern itself is all
	 * the bits up to and excluding the last 1.
	 **************************************************************************/
	private BitBreak[] getBreaks(BitSet set, BitSet pattern) {
		int l = pattern.length() - 1;
		LinkedList list = new LinkedList();
		int save = pattern.length() - 1;
		for (int x = 0; x < set.size(); x++) {
			for (int y = 0; y < save; y++) {
				if (!((set.get(x + y) && pattern.get(y)) || (!set.get(x + y) && !pattern
						.get(y))))
					break;
				else if (y == save - 1)
					list.add(new BitBreak(x, l));
			}
		}
		return (BitBreak[]) list.toArray(new BitBreak[0]);
	}

}
