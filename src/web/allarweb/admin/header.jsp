<%@ include file="include.txt"%>
<%
	String title = (String)request.getAttribute("title");
	String[] userList = admin.listUsers();
	String[] groupList = admin.listGroups();
	String[] merchantList = admin.listMerchants();
	String[] familyList = admin.listFamilies();
	String base = controller.getConfigValue("href-base");
	
	boolean showReports = false;
	for(int i=0;i<groupList.length;i++){
		if(admin.getGroupRights(groupList[i]).getPermission("SLU Admin",Resource.GROUP,Rights.ADMIN)){
			showReports=true;
		}	
	}
%>
<html>
<head>
	<LINK REL="STYLESHEET" TYPE="text/css" HREF="style.css">
<title>Development Admin <%=title%></title>
	
</head>

<body BGCOLOR="#ffffff">
<DIV ALIGN="center">
<TABLE WIDTH="800" CELLPADDING="2" CELLSPACING="0" BORDER="0" BGCOLOR="#ffcc66">
<TR>
  <TD class="lrg">
		<a href="index.jsp" class="navigation">Home</a>
  <% if(userList.length > 0)
  	 {
	 	 %> | <a href="users.jsp" class="navigation">Users</a><%
	 }
	 if(groupList.length > 0)
  	 {
	 	 %> | <a href="groups.jsp" class="navigation">Groups</a><%
	 }
	 if(merchantList.length > 0)
  	 {
	 	 %> | <a href="merchants.jsp" class="navigation">Merchants</a><%
	 }
	 if(showReports){
	 	 %> | <a href="reports.jsp" class="navigation">Reports</a><%
	 }
	 if(familyList.length > 0)
  	 {
	 	 %> | <a href="libraries.jsp" class="navigation">Libraries</a> |
		 <a href="fields.jsp" class="navigation">Fields</a> |
	 	 <a href="log.jsp" class="navigation">Log</a> | 
		 <a href="re_initialize.jsp" class="navigation">Re-Initialize</a><%
	 }
  %> 
  <div align="CENTER" class="title">Online Library Administration</DIV>
<P>
<HR></TD>
</TR>
<TR VALIGN="TOP">
  <TD>


