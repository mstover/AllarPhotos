<%@ page import = "com.lazerinc.utils.*;" %>
<%
	// Why does this seem to work on port 8008?
	String home = "@download_url@";  // "http://localhost:8080/wfd/";	 // "http://lweb.ws:8008/";
	String dlBase = "@download_dir@";  // "/usr/local/apache-tomcat-5.5.20/webapps/wfd/"; 		// "d:/WebFileDownload/";
	String dlFile = request.getParameter("f");
	if(null == dlFile)
	{
		dlFile = request.getParameter("file");
		if(null == dlFile)
			dlFile = "";
	}		

	String filename = dlFile.substring(dlFile.lastIndexOf('/')+1);
	String previewFile = dlFile.substring(0,dlFile.lastIndexOf('.')+1)+"jpg";
	boolean myFileExists = true, previewFileExists=true;
	myFileExists = Filer.exist(dlBase+dlFile);
	previewFileExists = Filer.exist(dlBase+previewFile);
	if(dlFile.endsWith("/"))
		myFileExists = false;
	String mimetype = "";

	if(filename.endsWith(".pdf"))
	{
		mimetype = "application/pdf";
	}else if(filename.endsWith(".jpg")){
		mimetype = "image/jpeg";
	}else if(filename.endsWith(".tif")){
		mimetype = "image/tiff";
	}else if(filename.endsWith(".txt")){
		mimetype = "application/txt";
	}else if(filename.endsWith(".xls")){
		mimetype = "application/vnd.ms-excel";
	}else if(filename.endsWith(".doc")){
		mimetype = "application/vnd.msword";
	}
%>



<html>
<head>
<style>
.coloredBkd {background: #DDE;}
DIV, TD {vertical-align: top;}
.aCenter {text-align: center;}
.small1 {font-size: 11px;}
UL, LI {margin-top: 0; padding-top: 0;}
a#fileLink {padding: 10px; text-align: center;
	text-decoration: underline;
	background-color: #EED;
	font-weight: bold; color: green;}
a#fileLink:hover {color: red;}
</style>
</head>
<body style='margin: 0; font: 12px Verdana, Arial, sans-serif;'>

<table style='width: 780px; padding: 0; border: 0;' cellspacing='0'>
  <tr>
	<td colspan='2' class='coloredBkd' style='text-align: center;'>
		<h2>File Downloads</h2>
	</td>
  </tr>
  <tr>
	<td style='padding: 10px;'><%	
	if(null != dlFile && !dlFile.equals("") && myFileExists)
	{ %>
		<p>
		  <div><u>To Download:</u><br>
			<blockquote>Right-click* the following link to the file, select 
			"Save Target As" and save the file to your desired location.</blockquote></div>
		  <div><u>To View in Browser:</u>**<br>
			<blockquote>Left click the following link to the file.</blockquote></div>
		</p>
		<div>&nbsp;</div>
		<div class='aCenter'>
			<a id='fileLink' href="<%=home+dlFile%>" type="<%=mimetype%>"><%=filename%></a>
		</div>
		<p><br>
		<hr>
		<u>Note(s):</u> 
		<ul>
			<li class='small1'>* Mac users with a one button mouse can simulate a right-click by 
			holding down the control-key while pressing the mouse button.
			<li class='small1'>** Depending on the type of file and the browser 
			used, some files may open in the browser without any messages, or 
			may open in another application (such as Preview or Acrobat Reader 
			if a .pdf file that is opened on Mac OS X) whereas others may give 
			a dialog box with a message with more information.<br>
			If you receive a message such as "this browser doesn't know how to 
			handle the type of file you have selected" you should download the	
			file.
			<!--
			<li class='small1'>Viewing PDFs in a browser does require an 
			<a href='http://www.adobe.com/products/acrobat/readstep2.html' target='_blank'>Adobe 
			Acrobat</a> plug-in.
			-->
		</ul>
		<% if(previewFileExists){%>
			<p><u>File preview:</u></p>
			<div class='aCenter'><img src="<%=home+previewFile%>" width="500" /></div><%
		}
	}else{%>
		<p>The file that you have requested cannot be found.</p> 

		<p>Please  ensure you've copied and supplied the entire URL...</p>

		<p>Please contact <a href='mailto:webmaster@lazerinc.com?subject=DOWNLOAD|<%= dlFile %>'>
			webmaster@lazerinc.com</a> for assistance.</p>

		<p>NOTE: For security and server maintenance reasons, files through this system 
			are made available for three business days and purged thereafter.</p>
		<!-- There was an error with the download file. --><%
	} %>
	</td>
	<td style='width: 172px; padding: 4px;' class='coloredBkd small1'>
		<b>Please email 
		<a href='mailto:webmaster@lazerinc.com?subject=Assistance with Download for <%= dlFile %>'>webmaster@lazerinc.com</a> 
		for assistance.</b>
	</td>
  </tr>
</table>

<div style='font-size: 9px; color: #CCC;'>
<%
	if(myFileExists)
	{ %>
		<!-- Wahoo! --> <%
	}else{ %>
		<!-- DOOOOOHHTTT! --><%
	}
%>
</div>	

</body>
</html>
