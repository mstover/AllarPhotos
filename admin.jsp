<%@ include file="/include/global.inc" %>
<act:request action="mark_calling_page" order="1"/>
<act:request action="action_validate_login" order="2"/>
<act:onError error="UserNotLoggedInError" ignore="true"></act:onError>
<act:executeSession/>


<vel:velocity>
	#if(!$coinjemaContext) 
		#set($styleName = "lazerweb/")
	#else
		#set($styleName = $coinjemaContext.name)
	#end
<html>
	<head>	
	<script language="Javascript" src="/lazerweb/javascript/ts_picker.js" type="text/javascript"></script>	
<LINK REL="STYLESHEET" TYPE="text/css" HREF="/${styleName}gwt.css" media="screen">
	
		<!--                                           -->
		<!-- Any title is fine                         -->
		<!--                                           -->
		<title>Wrapper HTML for LazerWeb</title>



		<!--                                           -->
		<!-- The module reference below is the link    -->
		<!-- between html and your Web Toolkit module  -->		
		<!--                                           -->
		<meta name='gwt:module' content='com.allarphoto.ajaxclient.LazerAdmin'>
		
	</head>

	<!--                                           -->
	<!-- The body can have arbitrary html, or      -->
	<!-- you can leave the body empty if you want  -->
	<!-- to create a completely dynamic ui         -->
	<!--                                           -->
	<body>

		<!--                                            -->
		<!-- This script is required bootstrap stuff.   -->
		<!-- You can put it in the HEAD, but startup    -->
		<!-- is slightly faster if you include it here. -->
		<!--                                            -->
		<script language="javascript" src="gwt.js"></script>


		<div id="comp"></div>
	</body>
</html>
</vel:velocity>
