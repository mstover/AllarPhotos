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

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * This class creates a modal dialog box that allows a user to select an item
 * from a list. It returns the input via the getInput() method. Only one item
 * from the list may be selected.
 * 
 * @author Michael Stover
 * @version 1.0 10/14/1998
 ******************************************************************************/
public class ListDialog extends java.awt.Dialog implements ItemListener {
	private static final long serialVersionUID = 1;

	List input;

	Label instructions;

	Panel p;

	/***************************************************************************
	 * Constructor takes a label for instructions to the user, and a list of
	 * items to add to selectable list.
	 * 
	 * @param f
	 *            Parent frame.
	 * @param instruct
	 *            Instructions to display with dialog box.
	 * @param listItems
	 *            Array of strings to populate the list.
	 **************************************************************************/
	public ListDialog(Frame f, String instruct, String[] listItems) {
		super(f, instruct, true);
		p = new Panel();
		instructions = new Label(instruct);
		setBackground(Color.lightGray);
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		p.setLayout(gridBag);
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0;
		gridBag.setConstraints(instructions, c);
		p.add(instructions);
		input = new List(listItems.length, false);
		gridBag.setConstraints(input, c);
		int x = 0;
		while (x < listItems.length)
			input.add(listItems[x++]);
		input.addItemListener(this);
		p.add(input);
		setSize(200, 300);
		add(p);
	} // end of Method

	/***************************************************************************
	 * This method returns the item selected by the user.
	 * 
	 * @return List item selected by the user.
	 **************************************************************************/
	public String getInput() {
		return input.getSelectedItem();
	} // end of Method

	/***************************************************************************
	 * Satisfies the ItemListener interface requirements. Catches the event
	 * generated when the user makes a choice and makes the window invisible and
	 * returns control to the parent frame.
	 * 
	 * @param e
	 *            ItemEvent object.
	 **************************************************************************/
	public void itemStateChanged(ItemEvent e) {
		this.setVisible(false);
	} // end of Method
} // end of Method
