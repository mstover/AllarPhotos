<%@ include file="include.txt"%>
<%
	Enumeration names;
	String name;
	int compare,counter = 10;
	Set queries = new HashSet();
	LogModel logger = controller.getLogModel();
	QueryItem query;
	String[] values;
	SecurityModel security = admin.getSecurity();
	names = request.getParameterNames();
	SortedMap sortBy = new TreeMap();
	while(names.hasMoreElements())
	{
		name = (String)names.nextElement();
		if(name.startsWith("order_"))
		{
			try{
				int order = Integer.parseInt(request.getParameter(name));
				if(order != 0)
				{
					order = Integer.parseInt(""+order+(counter++));
					name = name.substring(6);
					sortBy.put(new Integer(order),name);
				}
			}catch(NumberFormatException e){}
		}
	}
	Iterator keys = sortBy.keySet().iterator();
	String[] sortOrder = new String[sortBy.size()];
	for(int x = 0;keys.hasNext();x++)
		sortOrder[x] = (String)sortBy.get(keys.next());	
	names = request.getParameterNames();
	while(names.hasMoreElements())
	{
		name = (String)names.nextElement();
		if(name.startsWith("criteria_"))
		{
			values = request.getParameterValues(name);
			name = name.substring(9);
			query = new QueryItem();	
			query.setCategory(name);
			query.setValues(values);
			if(values.length > 1)
				compare = DBConnect.IN;
			else
				compare = DBConnect.EQ;
			if(values.length > 0 && values[0].equals("ALL"))
				continue;
			query.setCompareType(compare);
			query.setSearchType(DBConnect.IS);
			query.setAnd(false);
			query.setExternalLogic(QueryItem.AND);
			queries.add(query);
		}
	}
	String fromDate = request.getParameter("start_date"), toDate = request.getParameter("end_date");
	GregorianCalendar from = Functions.getDate(fromDate);
	GregorianCalendar to = Functions.getDate(toDate);
	LogItem[] items = logger.queryLog(queries,sortOrder,from,to);
%>				
<%
	String[] categories = {"Nothing Found"};
	if(items.length > 0)
		categories = items[0].getSortBy();
%>
<TABLE CELLPADDING=5 CELLSPACING=0 BORDER=1>
<TR VALIGN="TOP">
<%
	for(int x = 0;x < categories.length;x++)
	{
		%><TH><%=upperCase(categories[x])%></TH><%
	}
%>
</TR>
<%
	for(int x = 0;x < items.length;x++)
	{
		String resource;
		if((resource = items[x].getValue("user")) != null && !security.getPermission(resource,Resource.USER,Rights.ADMIN))
			continue;
		if((resource = items[x].getValue("family")) != null && !security.getPermission(resource,Resource.DATATABLE,Rights.ADMIN))
			continue;
		%><TR VALIGN="TOP"><%
		for(int y = 0;y < categories.length;y++)
		{
			String val = items[x].getValue(categories[y]);
			if(val != null)
			{
				%><TD><%=val%></TD><%
			}
			else
			{
				%><TD>&nbsp;</TD><%
			}
		}
		%></TR><%
	}
%>
</TABLE>

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
%>