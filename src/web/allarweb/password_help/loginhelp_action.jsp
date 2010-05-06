<%@ include file="include.txt" %>
<% Functions.javaLog("If I can get this far I can always cause more trouble." +
		Calendar.getInstance().getTime());
String title="Login Help Verification";
%>

<%@ include file="header.txt" %>

<blockquote>
<%	// Get the parameters from the form.
	String recipient = request.getParameter("recipient");
	String email = request.getParameter("email");
	String password;

	String subject = request.getParameter("subject");
	//String name = request.getParameter("name");
	String username = request.getParameter("username");
	//String phone = request.getParameter("phone");
	
// Basic control parameters
	boolean sentPW = false;
	String nl = System.getProperty("line.separator");
	
// Handle the form information here.
	StringBuffer message = new StringBuffer("Username: " + username);
	//message.append(nl + "Name: " + name);
	//message.append(nl + "Phone: " + phone);

// Notify the user that their information has been forwarded on to the 
// appropriate people.

  if((username != null) && (!username.equals("")) 
  		// && (name != null) && (!name.equals(""))
		&& (email != null) && (!email.equals(""))
		// && (!phone.equals("")) && (phone != null)
		) {
	// Handle the login, check usernames, forward to the proper destination
	Functions.javaLog("I'm in and ready to raise a ruckus!");
  try{
	CommerceUser user = new CommerceUser();
	WebBean.setValues(user,request);
	commerce.setUser(user);
	//password = user.getPassword();
	//Functions.javaLog("Username: " + username + "Real Name: " + user.getFirstName() +
	//	" " + user.getLastName() + " email: " + email + " password: " + password);
	sentPW = commerce.getLostPassword(email);
  } catch(Exception e) {
  	e.printStackTrace();
  }
	if(sentPW) { %>
  	<H1>Your request has been processed.</H1><BR>
	We have received the following information:<BR>
	<!--Name: <= name %><BR>-->
	Username: <%= username %><BR>
	<!--Email: <= email %><BR>
	Phone: <= phone %><BR>-->
	Your password was emailed to your account that we have on record.<BR> <%
	} else { %>
		<H1>Your request has not been processed.</H1><BR>
		<div class="small3">
		The username or email that you have supplied does not match any of
		those in our database. Please double check the username you have entered 
		by hitting the back button.<BR><BR>
		Should this error persist or if your email address 
		has never been put on file, please contact your system administrator.<%
	}
  } else { %>
  	<H1>Your request has not been processed.</H1><BR>
	<div class="small3">
	Please press the back button and enter all of the information so that 
	we may better assist you.</div><BR><%=message %><% 
  } %><br>
  <B>To return to the on-line library please 
  <%
  String backPath = (String)session.getValue("bounceBack"); 
  %>
  <a href="<%= backPath %>">click here</a>
  .</B>
</blockquote>
<%@ include file="footer.txt" %>