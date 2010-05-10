<%@ include file="/v3_base/include/global.inc" %>
<%@ include file="/v3_base/include/handleActions.inc" %>
<jsp:useBean id="errors" scope="request" class="com.allarphoto.client.beans.ErrorsBean"/>

<% 	/******* Login Page - gateway to the Online Library ********/

	// Variables for page title and the banner image, if necessary
	String banner="login",help="login"; 
	
	// insert a session variable as a passthrough to the search page
	//session.putValue("placeholder","catsearch.jsp?historyIndex=0");
%>
<!--Header that each page contains-->
<%@ include file="header.txt"%>



<c:if test="${!empty errors.errors}">
	<h3>An Error Occurred</h3>
	<c:forEach var="err" items="${errors.errors}">
		<c:if test='${err.message == "InvalidLogInError"}'>
			<c:set var="passwordHelp" value="true"/>
		</c:if>
	</c:forEach>
	<c:choose>
		<c:when test='${passwordHelp == "true"}'>
			<p>Your login information was incorrect.<br>
					Please try again:<br>
		</c:when>
		<c:otherwise>
			<p>Your session may have timed out.<br>
					Please login and try again:
		</c:otherwise>
	</c:choose>
</c:if>

<div class="title2">Online Advertising and Image Archive.</div> 
This archive contains ads, brochures, collateral literature, presentations, 
images and logos produced by different regions worldwide, intended for both reference and reuse.<br><hr>
 
<FORM action="catsearch.jsp" METHOD="POST">
<input type="hidden" name="actiona" value="action_login"/>
<input type="hidden" name="request_history_index" value="0"/>
<DIV ALIGN="center"><H3>Login:</H3>
<TABLE CELLSPACING=0 CELLPADDING=5 BORDER=0>
<TR VALIGN="TOP"><TD><H4>Username:</H4></TD><TD><INPUT TYPE="Text" NAME="request_username" SIZE="25"></TD></TR>
<TR VALIGN="TOP"><TD><H4>Password:</H4></TD><TD><INPUT TYPE="Password" NAME="request_password" SIZE="25"></TD></TR>
</TABLE>
<INPUT TYPE="Submit" NAME="go" VALUE="Login" onClick="doPost('catsearch.jsp')"></DIV></FORM>
<c:if test='${passwordHelp == "true"}'>
			<b>Forget your password?</b><br>
				<a href="password_request.jsp">Click here to request an email reminder</a>.
</c:if>
This is a secure site, intended for use by authorized Kodak Polychrome Graphics employees only. 
Usernames and passwords are required to continue. They must be authorized and obtained from 
Beth Hogan Scott at Worldwide Headquarters in Norwalk, CT USA at 203-845-7115, or via email 
to <a href="mailto:Hoganscotte@kpgraphics.com">Hoganscotte@kpgraphics.com</a>.<hr><br>

<div class="title2">IMPORTANT: PLEASE READ</div>
All production files in this archive remain exactly as created by the 
originating region. Please check them carefully before reusing them. You may need to alter 
the size or other printing specifications, change local addresses and telephone numbers, 
revise other text to adapt them for use in your region or obtain photographic rights.<hr>
<b>How to use this archive</b>
<OL>
	<LI>You may view any document file while online.
	<LI>You may also download low resolution &quot;FPO&quot; 
		images for later review offline [as .pdf's].
	<LI>If you wish to obtain the full [high-resolution] production files for re-use, 
		then you may &quot;Order High-Res&quot; by placing them in your &quot;shopping cart&quot;. 
		You will be invoiced for any hi-res production file you request. 
		A cost structure is conveniently located on the order form.
	<LI>Production files you have ordered will be shipped within 24 hours on CD-ROM.
	<LI>All print-ready files (Ads and Literature) ordered through the Kodak Polychrome
		Graphics Online Digital Archive Library will include the fonts used in the
		creation of those files. These fonts may be used only if the end user has
		already purchased the appropriate font licenses from the font manufacturer.
		Fonts are included only to prevent potential conflicts between different
		versions of the same licensed font.
</OL>

<!--Footer that each pages contains-->

<%@ include file="footer.jsp"%>