<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login" order="1"/>
<act:executeActions/>

<vel:velocity>

#macro(navigation)
	<td><div style='text-align: right; padding: 4px;'>| 
		<a href='help.jsp'>Help</a> | 
		<a href='whats_new.jsp'>Search</a> | 
		<a href='index.jsp?action=action_logout'>Logout</a> |
	</div></td>
	<tr>
#end

#macro(mainContent)
	#if(!$params.getParameter("helpText"))
	   #set($helpText="general.txt")
	#else
	   #set($helpText="${params.getParameter('helpText')}.txt")		
	#end
	<div>
  <b>Help Topics</b>
  <ul style='text-align: left; padding: 2px 10px; margin: 0 0 0 30px;'>
	<li><a href="help.jsp?helpText=login">Login</a>
	<li><a href="help.jsp?helpText=search">Searching for Images</a>
	<li><a href="help.jsp?helpText=browse">Viewing Images</a>
	<li><a href="help.jsp?helpText=detail">Image Detail</a>
	<li><a href="help.jsp?helpText=defs">Definitions</a>
	<li><a href="help.jsp?helpText=cart">Shopping Cart</a>
	<li><a href="help.jsp?helpText=tips">Tips for Placing Orders</a>
	<li><a href="help.jsp?helpText=verify">Verifying Your User/Order Info</a>
	<li><a href="help.jsp?helpText=order">Confirming Your Order</a>
	<li><a href="help.jsp?helpText=modify">Editing Your User Information</a>
  </ul>
</div>
<p>&nbsp;</p>

#midsplit()
<blockquote>
  <div align="left"><!-- $helpText -->
  	#include($helpText)
  </div>
  <br>
</blockquote>

#end
#page("Help Page" "hbi")
</vel:velocity>