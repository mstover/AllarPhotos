package com.allarphoto.servlet;

import java.util.Hashtable;

/*******************************************************************************
 * A simple class to hold a chat message. It also holds the sender and
 * optionally, a list of receivers as well. The main purpose of the class is to
 * enable private messages within a public chat room.
 * 
 * @author Michael Stover
 * @version 6/4/98
 ******************************************************************************/
public class ChatMessage {

	String sender;

	Hashtable receivers;

	String message;

	/***************************************************************************
	 * Creates a ChatMessage object from a string giving the sender's name and
	 * the message itself. It assumes the receiver is "all".
	 * 
	 * @param snd
	 *            Sender's name (user ID)
	 * @param msg
	 *            Message.
	 **************************************************************************/
	public ChatMessage(String snd, String msg) {
		sender = snd;
		int x;
		message = msg;
		while ((x = message.indexOf("\n")) > -1)
			message = message.substring(0, x) + "<BR>"
					+ message.substring(x + 1, message.length());
		receivers = new Hashtable();
		receivers.put("all", "all");
	}// end method

	/***************************************************************************
	 * Creates a ChatMessage object from a string giving the sender's name and
	 * the message itself. Also, a list of receivers of the message
	 * 
	 * @param snd
	 *            Sender's name (user ID)
	 * @param msg
	 *            Message.
	 * @param rcv
	 *            Array of strings giving list of receivers for the message.
	 **************************************************************************/
	public ChatMessage(String snd, String msg, String[] rcv) {
		sender = snd;
		int x;
		message = msg;
		while ((x = message.indexOf("\n")) > -1)
			message = message.substring(0, x) + "<BR>"
					+ message.substring(x + 1, message.length());
		receivers = new Hashtable();
		int count = 0;
		while (count < rcv.length) {
			receivers.put(rcv[count], rcv[count]);
			count++;
		}
	}// end method

	/***************************************************************************
	 * Checks to see if a given specified user is registered to receive this
	 * message
	 * 
	 * @param check
	 *            name of user to be checked
	 * @return True if user is registered as a receiver for this message, false
	 *         otherwise.
	 **************************************************************************/
	public boolean checkReceiver(String check) {
		return (receivers.containsKey("all") || receivers.containsKey(check));
	}// end method

	/***************************************************************************
	 * Returns the message associated with this ChatMessage object
	 * 
	 * @return The message associated with this ChatMessage object
	 **************************************************************************/
	public String getMessage() {
		return message;
	}// end method

	/***************************************************************************
	 * Returns the sender of this message
	 * 
	 * @return The sender of the message Date: 6/4/98
	 **************************************************************************/
	public String getSender() {
		return sender;
	}// end method

}// end method
