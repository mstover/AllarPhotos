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

// Title: SimpleSocket
// Author: Michael Stover
// Company: Lazer Inc.
// Description: Class to give simple methods for dealing with socket input
// and output.
package com.allarphoto.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/*******************************************************************************
 * This is a class to simplify basic socket input and output. It gets the input
 * and output streams of a socket and uses DataInputStream to get responses and
 * PrintStream to send commands or requests.
 ******************************************************************************/
public class SimpleSocket {

	// Typical mail port is 25
	private Socket socket;

	private InputStream in;

	private OutputStream out;

	private BufferedReader din;

	private PrintStream prout;

	public String incoming;

	String statusCode;

	/***************************************************************************
	 * Constructor. Takes as argument the host and port to connect to.
	 * 
	 * @param host
	 *            Name of machine to connect to.
	 * @param port
	 *            Port number to connect to.
	 **************************************************************************/
	public SimpleSocket(String host, int port) throws IOException,
			NullPointerException {
		socket = new Socket(host, port);
		if (socket != null) {
			in = socket.getInputStream();
			din = new BufferedReader(new InputStreamReader(in));
			out = socket.getOutputStream();
			prout = new PrintStream(out);
		}
	} // End Method

	/***************************************************************************
	 * Returns true or false depending on whether a response was received from
	 * the host.
	 * 
	 * @return True if a response was received from the host, false if error.
	 **************************************************************************/
	public boolean getQuickResponse() {
		try {
			incoming = din.readLine();
			// Functions.javaLog("SimpleSocket: incoming="+incoming );
		} catch (IOException e) {
			return false;
		}
		// 2 AND 3 means success .... we can extract the 3 digits and
		// know more about the response from the server
		this.statusCode = incoming.substring(0, 1);
		return true;
	} // End Method

	/***************************************************************************
	 * Gets the status code of after the last getQuickResponse call.
	 * 
	 * @return status code string.
	 **************************************************************************/
	public String getStatusCode() {
		return statusCode;
	}

	/***************************************************************************
	 * Gets the incoming message of after the last getQuickResponse call.
	 * 
	 * @return status code string.
	 **************************************************************************/
	public String getIncoming() {
		return incoming;
	}

	/***************************************************************************
	 * Sends a command through the socket to the host.
	 * 
	 * @param command
	 *            Command, as a string, to send to host.
	 **************************************************************************/
	public void sendCommand(String command) {
		// Functions.javaLog("SimpleSocket: command="+command );
		prout.print(command + "\r\n");
		prout.flush();
	} // End Method

	/***************************************************************************
	 * Closes this socket.
	 * 
	 * @return True if successful, false otherwise.
	 **************************************************************************/
	public boolean closeSocket() throws IOException {
		try {
			socket.close();
		} catch (IOException e) {
			Functions.javaLog("EmailSocket: Error closing socket.");
			return false;
		}
		return true;
	}
}
