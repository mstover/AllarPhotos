package com.allarphoto.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*******************************************************************************
 * A real simple chat servlet. It allows private messages to be sent, and it
 * keeps track of what users are currently on the site. Anyone who leaves the
 * chat page for over a minute is off the list. 20 messages max are stored.
 * 
 * @author Michael Stover
 ******************************************************************************/
public class ChatServlet extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 1;

	Vector chatData;

	Hashtable userSessions;

	static String[] allowedUsers = { "Lisa", "Mike", "mike", "lisa" };

	String thisServlet = "/servlet/talk";

	/***************************************************************************
	 * Initializes the servlet.
	 * 
	 * @param cfg
	 *            Servlet configuration object
	 **************************************************************************/
	public void init(ServletConfig cfg) throws ServletException {
		super.init(cfg);
		chatData = new Vector(25);
		userSessions = new Hashtable();

	}

	/***************************************************************************
	 * Returns a list of current users
	 * 
	 * @return Array of Strings of current users
	 **************************************************************************/
	public String[] getUsers() {
		Enumeration enumer = userSessions.keys();
		Vector temp = new Vector();
		while (enumer.hasMoreElements())
			temp.addElement(enumer.nextElement());
		String[] returns = new String[temp.size()];
		temp.copyInto(returns);
		return returns;

	}// end method

	/***************************************************************************
	 * Handles HTTP Get requests. Prints out chat messages to registered
	 * receivers.
	 * 
	 * @param req
	 *            Serlvet request object automatically passed
	 * @param res
	 *            Servlet response object automatically passed
	 **************************************************************************/
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Thread userSideCheck = new Thread(this);
		/*
		 * starts thread that determines which users are no longer connected -
		 * removes those users from the list
		 */
		userSideCheck.start();

		StringBuffer out = new StringBuffer("");
		String[] messages = new String[chatData.size()];
		ChatMessage temp;
		// get the session
		HttpSession session = req.getSession(true);
		String user = (String) session.getValue("com.mrstover.name");
		int count = messages.length;
		String[] users;
		int x = 0;

		// set content type and other response header fields first
		res.setContentType("text/html");

		// then write the data of the response
		out.append("<HEAD>");
		out
				.append("<TITLE>ChatServlet Output </TITLE><META HTTP-EQUIV='Refresh'"
						+ " CONTENT='10;url="
						+ thisServlet
						+ "'></HEAD>"
						+ "<BODY BGCOLOR='#FFFFFF'>");
		out.append("<TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0 WIDTH=700>");
		out.append("<TR VALIGN='TOP'><TD WIDTH=50><TD WIDTH=300><TD WIDTH=80 "
				+ "ALIGN='CENTER' ROWSPAN=" + (count + 1) + ">"
				+ "<FONT SIZE='+2'><B>Users</B></FONT><BR>");
		users = getUsers();
		// first, print column that holds names of all current users
		while (x < users.length)
			out.append(users[x++] + "<BR>");
		out.append("</TD></TR>");
		/*
		 * write a row for each message, but only if current user is registered
		 * as a reciever
		 */
		if (user != null) {

			while (--count >= 0) {
				temp = (ChatMessage) chatData.elementAt(count);
				if (temp.checkReceiver(user))
					out.append("<TR VALIGN='TOP'><TD><B>" + temp.getSender()
							+ "</B></TD><TD>" + temp.getMessage()
							+ "</TD></TR>");
				else if (temp.getSender().equals(user))
					out.append("<TR VALIGN='TOP'><TD><B>" + temp.getSender()
							+ "</B></TD><TD>" + temp.getMessage()
							+ "</TD></TR>");
			}
		}
		out.append("</TABLE>");
		out.append("<A NAME='bottom'></BODY></HTML>");
		outputToBrowser(res, out.toString());
	}

	/***************************************************************************
	 * Method to run in sub thread. Thread is started in doPost().
	 **************************************************************************/
	public void run() {
		checkUsers();
	}// end method

	/***************************************************************************
	 * Checks all users to find which ones are still current. Non-current users
	 * are dropped from the list
	 **************************************************************************/
	public synchronized void checkUsers() {
		HttpSession temp;
		Date now = new Date();
		String tempUser;
		Enumeration userList = userSessions.keys();
		while (userList.hasMoreElements()) {
			tempUser = (String) userList.nextElement();
			try {
				temp = (HttpSession) userSessions.get(tempUser);
				if ((now.getTime() - temp.getLastAccessedTime()) > 60000)
					userSessions.remove(tempUser);
			} catch (Exception e) {
				userSessions.remove(tempUser);
			}
		}
	}// end method

	/***************************************************************************
	 * Adds a user to the list safely
	 * 
	 * @param user
	 *            name of current user
	 * @param session
	 *            current HttpSession
	 **************************************************************************/
	public synchronized void addUser(String user, HttpSession session) {
		if (!userSessions.containsKey(user))
			userSessions.put(user, session);
	}// end method

	/***************************************************************************
	 * Checks if user name is already in use by another user
	 * 
	 * @param user
	 *            Name of user to check
	 * @return True if user name already in use, false otherwise
	 **************************************************************************/
	public boolean checkUserExist(String user) {
		return userSessions.containsKey(user);
	}// end method

	/***************************************************************************
	 * Handles HTTP Post requests. For the chat servlet, it collects user input -
	 * messages and user names and saves them to the list of messages.
	 * 
	 * @param yadda-yadda
	 *            automatic servlet stuff
	 **************************************************************************/
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Thread userSideCheck = new Thread(this);
		/*
		 * starts thread that determines which users are no longer connected -
		 * removes those users from the list
		 */
		userSideCheck.start();
		String user;
		String message;
		String[] receivers, users;
		int count = 0;
		StringBuffer out = new StringBuffer("");
		// get current session
		HttpSession session = req.getSession(true);

		out
				.append("<HTML><HEAD><TITLE>Chat form</TITLE></HEAD><BODY BGCOLOR='#FFFFFF'>");
		/*
		 * if user session doesn't have a name, and one is provided from form,
		 * well - use it!
		 */
		if ((session.getValue("com.mrstover.name") == null)
				&& ((user = req.getParameter("name")) != null)) {
			// but only if no one else is using that name
			boolean allowed = false;
			int x = 0;
			while (x < allowedUsers.length)
				if (user.equals(allowedUsers[x++]))
					allowed = true;

			if (!checkUserExist(user) && allowed)
				session.putValue("com.mrstover.name", user);
			else
				out
						.append("A user with that name already exists.  Please choose another<P>");
		}

		/*
		 * If we know user's name, we have a message, and name is not blank,
		 * then we graciously accept the message and add to our list
		 */
		if ((session.getValue("com.mrstover.name") != null)
				&& ((message = req.getParameter("message")) != null)
				&& (!((user = (String) session.getValue("com.mrstover.name"))
						.equals("")))) {
			// adds the user's session to our list of sessions, if necessary
			addUser(user, session);
			if (!message.equals("")) {
				// add message and the desired receivers to our list
				if ((receivers = req.getParameterValues("receivers")) != null)
					addMessage(user, message, receivers);
				else
					addMessage(user, message);
			}
			// reprint the message form
			out.append("<FORM ACTION='" + thisServlet + "' METHOD='POST'>");
			out.append("<CENTER><TABLE>");
			out
					.append("<TR><TD>Message:<BR> <TEXTAREA NAME='message'"
							+ " COLS='50' ROWS='2' WRAP='VIRTUAL'></TEXTAREA></TD>"
							+ "<TD><B>Send to:</B><BR><SELECT NAME='receivers' MULTIPLE><OPTION VALUE='all'>all");
			users = getUsers();
			// select box with list of current chat users
			while (count < users.length) {
				out
						.append("<OPTION VALUE=" + users[count] + ">"
								+ users[count]);
				count++;
			}

			out.append("</SELECT></TR>");
			out.append("</TABLE>");
			out.append("<INPUT TYPE='SUBMIT' VALUE='SUBMIT'>");
			out.append("</CENTER></FORM></BODY></HTML>");
		}
		// else we need to know who you are first
		else {
			out.append("<FORM ACTION='" + thisServlet + "' METHOD='POST'>");
			out.append("<CENTER><TABLE>");
			out
					.append("<TR><TD>Please Tell me your name: <INPUT TYPE='TEXT' NAME='name' VALUE=''></TD></TR>");
			out
					.append("<TR><TD>Message:<BR> <TEXTAREA NAME='message'"
							+ " COLS='50' ROWS='2' WRAP='VIRTUAL'></TEXTAREA></TD></TR>");
			out.append("</TABLE>");
			out.append("<INPUT TYPE='SUBMIT' VALUE='SUBMIT'>");
			out.append("</CENTER></FORM></BODY></HTML>");
		}
		outputToBrowser(res, out.toString());
	}

	/***************************************************************************
	 * Add message - 20 max messages stored.
	 **************************************************************************/
	private synchronized void addMessage(String user, String message) {
		chatData.addElement(new ChatMessage(user, message));
		if (chatData.size() > 20)
			chatData.removeElementAt(0);
	}

	/***************************************************************************
	 * Add message - 20 max messages stored.
	 **************************************************************************/

	private synchronized void addMessage(String user, String message,
			String[] receivers) {
		if (receivers.length > 0)
			chatData.addElement(new ChatMessage(user, message, receivers));
		else
			chatData.addElement(new ChatMessage(user, message));
		if (chatData.size() > 20)
			chatData.removeElementAt(0);
	}

	public String getServletInfo() {
		return "A chat servlet";
	}

	/***************************************************************************
	 * Method to send all output to the browser. It sets the content length
	 * 
	 * @param res
	 *            servlet response object
	 * @param output
	 *            String to write to browser
	 **************************************************************************/
	public void outputToBrowser(HttpServletResponse res, String output) {
		PrintWriter out;
		try {
			out = new PrintWriter(res.getOutputStream());
			int length = output.length();
			res.setContentType("text/html");
			res.setContentLength(length);
			out.println(output);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End Method

}
