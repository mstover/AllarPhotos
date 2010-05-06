<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String[] randBkd = {
		"search.jpg", "download.jpg", "browse.jpg"
	};
	String[] randBlockPos = {
		"UL"
	};
	boolean use_image=true;
	int randIndex = (int)Math.round(Math.random()*(randBkd.length - 1));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Welcome to the Image Library</title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
<script language="javascript" src="../../lazerweb/javascript/fix_png.js" type="text/javascript"></script>

	<!--<script src="/javascripts/global.js" type="text/javascript"></script>-->
	<script src="../../lazerweb/javascript/prototype.js" type="text/javascript"></script>
	<script src="../../lazerweb/javascript/scriptaculous.js?load=effects" type="text/javascript"></script>

</head>

<body onload="new Effect.Fade('usageAgreement', 0.1)">

<div id='wrapper'>
<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td class='midBlue' style='width: 300px;'>&nbsp;</td>
  	<td class='ltBlue' style='width: 655px;'>
  		<div id='poweredBy'>Powered by LazerWeb&trade;</div>
  	</td>
  </tr>
  <tr>
  	<td id='mastHead' colspan='2'>
		<img src='images/wa-logo.gif' style='padding: 10px 20px 10px 0; width: 200px; height: 40px;' alt="Welch Allyn Inc." />
		
	</td>
  </tr>
  <tr>
  <td id="mainContent" style=''><!-- Go to midsplit from here. -->