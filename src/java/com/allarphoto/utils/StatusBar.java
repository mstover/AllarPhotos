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

package com.lazerinc.utils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/*******************************************************************************
 * Use this class to add a status bar to your application. The status bar is
 * updated by sending two numbers - the total, and the amount done so far. The
 * StatusBar computes the percentage done and draws it accordingly.
 * 
 * @author Michael Stover
 ******************************************************************************/
public class StatusBar extends Canvas {
	private static final long serialVersionUID = 1;

	Dimension d;

	int progress = 0;

	Image im;

	Graphics imG;

	Graphics g;

	/***************************************************************************
	 * Create the StatusBar by giving it a desired height and width.
	 * 
	 * @param width
	 *            Width of StatusBar
	 * @param height
	 *            Height of StatusBar
	 **************************************************************************/
	public StatusBar(int width, int height) {
		super();
		setSize(width, height);
		d = getSize();

	}

	/***************************************************************************
	 * This method is necessary in order to grab the graphics object of the
	 * StatusBar without getting a NullPointer error.
	 **************************************************************************/
	public void addNotify() {
		super.addNotify();
		im = createImage(d.width, d.height);
		imG = im.getGraphics();
		g = this.getGraphics();
		/*
		 * This was necessary so that I could force paint to be called
		 * immediately, rather than relying on <I>repaint</I> to do it. I was
		 * finding that the paint thread's priority was too low and the status
		 * bar wasn't keeping up with the application. Now I can call paint
		 * directly, send it a graphics object, but it will not use it, it will
		 * use its own.
		 */
	}

	/***************************************************************************
	 * Draws the StatusBar status according to the total and done numbers given
	 * it. It calculates the percentage done and fills in the StatusBar
	 * appropriately.
	 * 
	 * @param total
	 *            Number representing the total number of things to do
	 * @param done
	 *            Number representing the number of things finished
	 **************************************************************************/
	public synchronized void drawProgress(int total, int done) {
		progress = (int) (d.width * ((float) ((float) (done) / (float) (total))));
		imG.setColor(Color.lightGray);
		imG.fillRect(0, 0, d.width, d.height);
		imG.setColor(Color.black);
		imG.fillRect(0, 0, progress, d.height);

		paint(imG);
	}

	/***************************************************************************
	 * Clears the StatusBar
	 **************************************************************************/
	public synchronized void clear() {
		imG.setColor(Color.lightGray);
		imG.fillRect(0, 0, d.width, d.height);
		progress = 0;
		paint(imG);
	}

	public void update(Graphics a) {
		paint(a);
	}

	public void paint(Graphics a) {

		g.drawImage(im, 0, 0, this);
	}
}
