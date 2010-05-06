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

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*******************************************************************************
 * This class creates a modal dialog box to get a single field of customer
 * input. It returns the input via the getInput() method.
 * 
 * @author Michael Stover
 * @version 1.0 10/14/1998
 ******************************************************************************/
public class SingleDialog extends java.awt.Dialog implements ActionListener {
	private static final long serialVersionUID = 1;

	TextField input;

	Label instructions;

	/***************************************************************************
	 * Constructor takes a label for instructions to the user.
	 * 
	 * @param f
	 *            Parent Frame for dialogue box.
	 * @param instruct
	 *            String giving user instructions on what info is being
	 *            requested.
	 **************************************************************************/
	public SingleDialog(Frame f, String instruct) {
		super(f, instruct, true);
		instructions = new Label(instruct);
		setSize(200, 100);
		setBackground(Color.lightGray);
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridBag);
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0;
		gridBag.setConstraints(instructions, c);
		add(instructions);
		input = new TextField(20);
		gridBag.setConstraints(input, c);

		input.addActionListener(this);
		add(input);
	} // end of Method

	/***************************************************************************
	 * This method returns the input generated by the box.
	 * 
	 * @return Text typed in by user.
	 **************************************************************************/
	public String getInput() {
		return input.getText();
	} // end of Method

	/***************************************************************************
	 * This method satisfies the ActionListener interface requirements. Makes
	 * window invisible and returns control.
	 * 
	 * @param e
	 *            ActionEvent object.
	 **************************************************************************/
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	} // end of Method
} // end of Method
