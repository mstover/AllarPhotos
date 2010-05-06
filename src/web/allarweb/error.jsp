 
<%
/*This is the default error page as set in the IIS webserver preferences.
This page comp[ensates for a bug in JRUN where socket connections are sporadically broken in
pages with multiple redirect directives. If the lazerweb application has been initialized,
This page redirects users to the browse thumbnails page of the current library. If not, this 
page redirects users to the index.jsp page otherwise, the error message below is displayed.

This relies on the fact that the lazerweb.context value match the 
directory structure of the current library.
-ks 08.15.01
*/


	String redirect = "/lazerweb3/error.jsp";
	String temp = "";
	if(null != session.getValue("lazerweb.context")){
		temp = (String)session.getValue("lazerweb.context");
	}
	if(!temp.equals("lazerweb") && !temp.equals("")){
		redirect = "/"+com.lazerinc.utils.Functions.unsplit(com.lazerinc.utils.Functions.split(temp,"."),"/") + "/browse_products.jsp";
		if(temp.equals("lazerweb.wgr") && null != session.getValue("switch_approve")){
			redirect = "/"+com.lazerinc.utils.Functions.unsplit(com.lazerinc.utils.Functions.split(temp,"."),"/") + "/admin_browse_products.jsp";			
		}
		com.lazerinc.utils.Functions.javaLog("Redirecting from error page: "+redirect);
		response.sendRedirect(redirect);
	}else{
		com.lazerinc.utils.Functions.javaLog("Default redirect from error page: /lazerweb3/index.jsp");
		response.sendRedirect("/lazerweb3/index.jsp");
	}
%>
<html>
<head>
	<title>Lazerweb Error Redirect</title>
</head>

<body>
<table width="400" align="left">
<tr><td>
<h2>Online Library Browse Error</h2></td></tr>
<tr><td>
<p><b>This error message was generated by the online library system.</b><br>
<p>Please send an email to the <a href="mailto:webmaster@lazerinc.com">library administrator</a> and click the "go back" link. We will respond to your email right away.<br></td></tr>
<tr><td align="center">
<h3><a href="javascript:history.back()">Go Back</a></h3>
</td></tr>
<tr><td>
<p>We apologize for any inconvenience and we appreciate your patience as we work on fixing this bug in our server software.<br>
<p>Thank you<br>
- <a href="mailto:webmaster@lazerinc.com">The Webmaster<a></a></td></tr>
</table>

</body>
</html>