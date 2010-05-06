<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login" order="1"/>
<act:executeActions/>

<% 	
	/******* Welcome - gateway to the Online Library ********
	* This page will be a sort of home page for the online 
	* library. There will be links for tools, news flashes 
	* in regards to the most recently added images, and 
	* anything else that may help the client communicate 
	* more effectively with their vendors.
	****************************************************** **/
%>
<c:set var="help" value="search" scope='session' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "search";
%>
<vel:velocity>

#macro(mainContent)
<h2 class='center' style='clear: both;'>Welcome to the Monroe Community College <BR/>Digital Online Library Main Portal</h2>
<hr id='content_spacer' style='height: 0;' />

## Conditional on Upload privs for any of the Libs
#if($user && $user.permissions.getAvailableResourceList(3,"upload").size() > 0)
	<h4 class='center alertColor'>You have the ability to <a href="/lazerweb/admin.jsp">upload images</a> for one or more libraries.</h4>
#end

	#if(!$params.getParameter("helpText") || $params.getParameter("helpText") == "")
	   #set($helpText="general.txt")
	#else
	   #set($helpText="${params.getParameter('helpText')}.txt")		
	#end
	<div>
  <b>Help Topics</b>
  <ul style='text-align: left; padding: 2px 10px; margin: 0 0 0 30px;'>
	<li><a href="whats_new.jsp?helpText=login">Login</a>
	<li><a href="whats_new.jsp?helpText=search">Searching for Images</a>
	<li><a href="whats_new.jsp?helpText=browse">Viewing Images</a>
	<li><a href="whats_new.jsp?helpText=detail">Image Detail</a>
	<li><a href="whats_new.jsp?helpText=defs">Definitions</a>
	<li><a href="whats_new.jsp?helpText=cart">Shopping Cart</a>
	<li><a href="whats_new.jsp?helpText=tips">Tips for Placing Orders</a>
	<li><a href="whats_new.jsp?helpText=verify">Verifying Your User/Order Info</a>
	<li><a href="whats_new.jsp?helpText=order">Confirming Your Order</a>
	<li><a href="whats_new.jsp?helpText=modify">Editing Your User Information</a>
  </ul>
</div>
<p>&nbsp;</p>

<p>
<blockquote>
  <div align="left"><!-- $helpText -->
  	#include($helpText)
  </div>
  <br>
</blockquote>
</p>

<div align='left'>
  <p><b>Need additional access? ...</b><br>
	<a href="../../password_help/request.jsp?actiona=mark_calling_page">Click here</a> to request an account or access to a library.</p>
</div>


#end

#page("" "mcc")



</vel:velocity>