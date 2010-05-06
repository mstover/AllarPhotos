<%@ include file="include.txt"%>
<%
	LogModel logger = controller.getLogModel();
	request.setAttribute("title","Log View");
	Map categoryValueMap = logger.getValues();
	Set values;
	String value,category,displayName;
	SecurityModel security = admin.getSecurity();
	String tempInputValue;
	String[] tempInputValues;
	Set tempInputValueSet;
	String selected = "";
%>
<jsp:include page="header.jsp"/>
<TABLE CELLPADDING=5 CELLASPACING=5 BORDER=0>
<TR VALIGN="TOP">
	<TD><!-- Search parameters -->
	<H3>Choose your Search Criteria</H3>
	<FORM ACTION="log.jsp" METHOD="POST">
	<TABLE CELLPADDING=2 CELLSPACING=0 BORDER=0>
	<% Iterator vals,cats = categoryValueMap.keySet().iterator();
	while(cats.hasNext())
	{
		category = (String)cats.next();
		values = new TreeSet((Set)categoryValueMap.get(category));
		values.remove("N/A");
		if(checkValues(category,values,security,admin))
		{
			displayName = new String(category);
			if(displayName.equals("family"))
				displayName = "Database";
			displayName = upperCase(displayName);
			if((tempInputValues = request.getParameterValues("criteria_"+category)) == null)
				tempInputValues = new String[]{"ALL"};
			tempInputValueSet = new HashSet();
			for(int x = 0;x < tempInputValues.length;
							tempInputValueSet.add(tempInputValues[x++]));
			%><TR VALIGN="TOP"><TD CLASS="sml"><%=displayName%>:</TD>
			<TD CLASS="sml">
			<SELECT CLASS="sml" NAME="criteria_<%=category%>" 
			SIZE=<%=values.size()<3 ? (values.size()+1) : 3%> MULTIPLE><%
			if(tempInputValueSet.contains("ALL"))
				selected = " SELECTED";
			else selected = "";
			%><OPTION VALUE="ALL" CLASS="sml"<%=selected%>>ALL<%
			vals = values.iterator();
			while(vals.hasNext())
			{
				value = (String)vals.next();
				if(tempInputValueSet.contains(value))
					selected = " SELECTED";
				else selected = "";
				%><OPTION VALUE="<%=value%>"<%=selected%>><%=value%><%
			}
			%></SELECT></TD></TR><%
		}
	}
	if((tempInputValue = request.getParameter("start_date")) == null)
		tempInputValue = "";
	%><TR VALIGN="TOP"><TD CLASS="sml">From Date (ie 12/5/99):</TD>
					<TD CLASS="sml"><INPUT TYPE="text" NAME="start_date" 
					VALUE="<%=tempInputValue%>"></TD></TR><%
	if((tempInputValue = request.getParameter("end_date")) == null)
		tempInputValue = "";				
	%><TR VALIGN="TOP"><TD CLASS="sml">To Date (ie 3/1/00):</TD>
					<TD CLASS="sml"><INPUT TYPE="text" NAME="end_date"
					VALUE="<%=tempInputValue%>"></TD></TR>	
	<TR VALIGN="TOP"><TD COLSPAN=3 CLASS="sml">Choose Output Order (output will be sorted in this order as well).  
	Setting a category's value to "0" will prevent it from displaying</TD></TR>
	<%
		cats = categoryValueMap.keySet().iterator();
		int count = 1;
		while(cats.hasNext())
		{
			category = (String)cats.next();
			displayName = new String(category);
			if(displayName.equals("family"))
				displayName = "Database";
			displayName = upperCase(displayName);
			if((tempInputValue = request.getParameter("order_"+category)) == null)
				tempInputValue = ""+(count++);
			%><TR VALIGN="TOP"><TD CLASS="sml"><%=displayName%>:</TD>
				<TD CLASS="sml"><INPUT TYPE="text" NAME="order_<%=category%>" 
				VALUE="<%=tempInputValue%>" SIZE="4"></TD></TR><%
		}
	if((tempInputValue = request.getParameter("order_date")) == null)
				tempInputValue = ""+(count++);
	%>
	<TR VALIGN="TOP"><TD CLASS="sml">Date:</TD>
				<TD CLASS="sml"><INPUT TYPE="text" NAME="order_date" 
				VALUE="<%=tempInputValue%>" SIZE=3></TD></TR>
	</TABLE>
	<DIV ALIGN="center"><INPUT TYPE="submit" VALUE="Submit Query"></DIV>
	<INPUT TYPE="hidden" NAME="page" VALUE="logview">
	</FORM>
	</td>
	<TD>
	<%
		String view = request.getParameter("page");
		if(view != null)
		{
			view += ".jsp";
			%><jsp:include page="<%=view%>"/><%
		}
	%>
	</td>
</tr></table>

<%@ include file="footer.txt" %>

<%!

	private String upperCase(String value)
	{
		String[] tokens = Functions.split(value," ");
		for(int x = 0;x < tokens.length;x++)
		{
			tokens[x] = tokens[x].substring(0,1).toUpperCase() + tokens[x].substring(1);
		}
		return Functions.unsplit(tokens," ");
	}

	private boolean checkValues(String category,Set values,SecurityModel security,CommerceAdminWebInterface admin)
	{
		String value;
		String[] families = admin.listFamilies();
		Set pfs = new HashSet();
		for(int x = 0;x < families.length;pfs.add(admin.getProductFamily(families[x++]).getDescriptiveName()));
		List remove = new LinkedList();
		Iterator vals = values.iterator();
		if(category.equals("total cost") || category.equals("filesize"))
			return false;
		boolean numbers = true;
		while(vals.hasNext())
		{
			value = (String)vals.next();
			if(numbers)
			{
				try{
					Float.parseFloat(value);
				}catch(NumberFormatException notANumber){numbers = false;}
			}
			if(category.equals("user"))
				if(!security.getPermission(value,Resource.USER,Rights.ADMIN))	
					remove.add(value);
			else if(category.equals("family"))
				if(!pfs.contains(value))
					remove.add(value);
		}
		vals = remove.iterator();
		while(vals.hasNext())
			values.remove(vals.next());
		return (values.size() > 0) && !numbers;
	}
					
			
%>		