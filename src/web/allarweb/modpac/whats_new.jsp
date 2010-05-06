<%@ include file="/modpac/include/global.inc" %>
<%@ include file="/modpac/include/actionsAndValidate.inc" %>
<% 	
	/******* Welcome - gateway to the Online Library ********
	* This page will be a sort of home page for the online 
	* library. There will be links for tools, news flashes 
	* in regards to the most recently added images, and 
	* anything else that may help the client communicate 
	* more effectively with their vendors.
	********************************************************/
%>
<c:set var="help" value="search" scope='session' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "search";
%>
<!--Header that each page contains-->
<%@ include file="header.txt"%>

<%--
	/**************************************************************
	* We will need to reacquire the ability to have General Department Users 
	* to be forced to setup their own personal accounts.
	**************************************************************/

    // Get the security level of the current user.
    SecurityModel security = commerce.getSecurity();

    // Check to see if user logged in with a general dept account, then redirect
    // them to create a new account if this is the case.
    CommerceUser user = commerce.getUser();
    String[] groups = user.getGroups();	
    if(groups.length<1) 
    {
		response.sendRedirect("/lazerweb/modpac/index.jsp?relogin=true"); // ALERT UPON TIMEOUT
    }else if((groups[0].equals("modpac_Dept") )){ // && security.getPermission("modpac_User",Resource.GROUP,Rights.ADMIN))){
		response.sendRedirect("policy.jsp");
    }

	String[] secureCheck = commerce.getUser().getGroups();
--%>
<%
	boolean showResearchLink = false;
	boolean marcomCheck = false;
%><%--
	/**************************************************************
	* We will need to reacquire the ability to decipher the groups  
	* in which the user belongs to disable the admin context for 
	* the common user.
	**************************************************************/

	for(int i=0; i<secureCheck.length; i++) 
	{
		if(secureCheck[i].equals("admin") || secureCheck[i].equals("modpac_Admin")) 
		{
			showResearchLink = true;
		}
	}
	if(security.getPermission("admin",Resource.GROUP,Rights.ADMIN)) 
	{
		showResearchLink = true;
	}--%>
<div class='sideSubBox'>
  <b>USER TIPS</b>
  <UL id='sideTips'>
	<LI>Please click buttons only once and allow the system to process your request.  
	This could take several seconds depending on your connection speed and the size 
	of your request.  Errors may occur if processes are interrupted by hitting your 
	"Back" button on your browser or trying to leave the page 
	before your request is complete.
	<LI>Orders delivered in the United States are usually received within one to two 
	business days.  International deliveries may take three to four business days.
  </UL>
			<dl>
				<dt class='wnSideLinks'><b>Tool Box</b>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=expander">Alladin Stuffit Expander</a></dd>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=winzip">WinZip</a></dd>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=reader">Adobe Acrobat Reader</a></dd>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=player">RealNetworks RealPlayer</a></dd>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=quicktime">Apple Quicktime</a></dd>
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=shockwave">Macromedia Shockwave</a></dd><%
			if(showResearchLink)
			{ %><br><br>
				<dt><b>Admin Section</b>
					<!--dd class='wnSideLinks'><a href="whats_new.jsp?page_view=welcome">Welcome Message</a></dd-->
					<!--dd class='wnSideLinks'><a href="whats_new.jsp?page_view=new_features">New Features</a></dd-->
					<!--dd class='wnSideLinks'><a href="whats_new.jsp?page_view=faqs">F.A.Q.'s</a></dd-->
					<!--dd class='wnSideLinks'><a href="#" onClick='javaScript:window.open("../../WA_Shockwave/WA.html", "Tutorial", "left=0,screenX=0,top=0,screenY=0,width=800,height=600,menubar=no,scrollbar");'>Online Tutorial</a><br>
						<span class='small3'>- Link will open a new window (Shockwave required).</span></dd-->
					<dd class='wnSideLinks'><a href="whats_new.jsp?page_view=naming_convention">Naming Convention</a></dd>
					<dd class='wnSideLinks'><a href="asset_submissions.jsp" target="_blank">Asset Submission Form</a></dd><%
				if(null != request.getQueryString())
				{
				    session.setAttribute("retFromUserAdmin", request.getRequestURI()+"?"+request.getQueryString());
				}else{
				    session.setAttribute("retFromUserAdmin", request.getRequestURI());
				}
					%><%--dd class='wnSideLinks'><a href="../user_admin/login_action.jsp?username=<%=user.getUsername()%>&password=<%=user.getPassword()%>"
					    onMouseOver ="MM_displayStatusMsg('Users Administration');return document.MM_returnValue">Users Admin Tool</a></dd--%><%
			} 
				%>
				</dl>
</div>		
<div id='wnContent'>
<% 
	String pageView = request.getParameter("page_view");
	if(null != request.getParameter("page_view") && !pageView.equals("") && !pageView.equals(" "))
	{ 
		pageView = "help/" + pageView + ".txt";%>
		<jsp:include page="<%=pageView%>" /><%
	}else{ %> 
		<jsp:include page="help/welcome.txt" /><%
	}%>
	<p>
		<div align="center"><form action="catsearch.jsp" method='POST'>
			<input type="hidden" name="historyIndex" value="0">
			<input class='btnBkd' type="submit" name="submit" value="Continue to the Library" onClick="doPost('whats_new.jsp')">
			</form>
		</div>
</div>
<!--Footer that each pages contains-->
<%@ include file="footer.jsp"%>
