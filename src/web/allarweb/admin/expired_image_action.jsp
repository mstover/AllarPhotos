<%@ include file="include.txt"%>
<%
	String library = "kdk_dai";
	String[] fields = {"Usage Expiration"};
	String startDate = "";
	String endDate = "";
	String contact = "Debbie Maier (585-781-9407 or debra.maier@kodak.com)";
	boolean publish = false;
	List fileList = new ArrayList();
	Iterator it = fileList.iterator();
	Set userList = new HashSet();
	
	if(null != request.getParameter("submit"))
	{
		library = request.getParameter("library");
		fields = request.getParameterValues("expire_fields");
		startDate = request.getParameter("start_date");
		endDate = request.getParameter("end_date");
		contact = request.getParameter("contact");
		publish = new Boolean(request.getParameter("publish")).booleanValue();
		fileList =	admin.getExpiredImages(library,fields,startDate,endDate);
		it = fileList.iterator();
		//String[] pNames = (String[])x.toArray(new String[0]);
		userList = admin.getUsersForExpired(library,(String[])fileList.toArray(new String[0]), contact, publish);
	}


%>



<jsp:include page="header.jsp"/>

<h3>Enter Expiration Search Terms:</h2>
<form action="expired_image_action.jsp" method="post">
<table>
	<tr>
		<td>Library: </td>
		<td><input type="text" name="library" value="<%=library%>"></td>
	</tr>
	<tr>
		<td>Expiration Field: </td>
		<td><input type="text" name="expire_fields" value="<%=fields[0]%>"></td>
	</tr>
	<tr>
		<td>Start (yyyy-mm-dd) </td>
		<td><input type="text" name="start_date" value="<%=startDate%>"></td>
	</tr>
	<tr>
		<td>End: (yyyy-mm-dd)</td>
		<td><input type="text" name="end_date" value="<%=endDate%>"></td>
	</tr>
	<tr>
		<td>Contact Email:</td>
		<td><input type="text" name="contact" value="<%=contact%>"></td>
	</tr>
	<tr>
		<td>Publish?: </td>
			<td>Yes <input type="radio" name="publish" value="true"><br>
				 No <input type="radio" name="publish" value="false" checked></td>
	</tr>
</table>
<input type="submit" name="submit" value="submit">
</form>

<h2>Results:</h2>
<%
	it = userList.iterator();
	while(it.hasNext()){%>
		<%=(String)it.next() %><br><%
	}
	%>




	