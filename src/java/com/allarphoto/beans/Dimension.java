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

// Title: Dimension
// Author: Michael Stover
// Company: Lazer inc.
package com.allarphoto.beans;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Dimension implements Serializable {
	private static final long serialVersionUID = 1;

	public Dimension() {
	}

	public Dimension(int height, int width) {
		this.height = height;
		this.width = width;
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setWidth(float newWidth) {
		width = newWidth;
	}

	public float getWidth() {
		return width;
	}

	public void setHeight(float newHeight) {
		height = newHeight;
	}

	public float getHeight() {
		return height;
	}

	private float height;

	private float width;
}
