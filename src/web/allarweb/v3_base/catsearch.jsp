<%@ include file="/bali/include/global.inc" %>
<%	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_CHANGE_NUMBER_SHOWN);
    addParams.put(RequestConstants.REQUEST_ACTION+"r",ActionConstants.ACTION_CATEGORY_SEARCH);   %>
<%@ include file="/bali/include/actionsAndValidate.inc" %>

<jsp:useBean id="productsFound" scope="session" class="com.allarphoto.client.beans.ProductSetBean"/>
<jsp:useBean id="messages" scope="request" class="com.allarphoto.client.beans.MessageBean"/>
<jsp:useBean id="searchCategories" scope="session" class="com.allarphoto.client.beans.SearchCategoryBean"/>
<% String title = "Image Search", banner = "search", help = "search"; // page title and banner image %>
<!--Header that every page contains-->
<%@ include file="header.txt"%>

<c:set var="history_index" value="${productsFound.historyIndex}"/>
<c:choose>
	<c:when test="${productsFound.size == 0}">
		<b>No products were found using this search.<b><br>
		<a href="catsearch.jsp?request_history_index=<c:out value='${history_index -1}'/>">Please try again</a>.
	</c:when>
	<c:when test='${productsFound.size <= configuration["default_product_display_number"] ||
                       searchCategories.size == 0}' >
			<jsp:forward page="browse_products.jsp"/>
	</c:when>
<c:otherwise>
	<div align="left">
		<b>Search History:</b><br>&nbsp;&nbsp;&nbsp;
		<c:if test="${history_index > 0}">
			<b><a href="catsearch.jsp?request_history_index=0">Reset Search</a></b>&nbsp;&gt;&gt;&nbsp;				
			<c:forEach items="${productsFound.searchHistory}" var="historyValue" varStatus="count" begin="0" end="${history_index-1}">
				<c:choose>
					<c:when test="${count.count == history_index}">
						<c:out value="${historyValue}"/>
					</c:when>
					<c:otherwise>
						<a href="catsearch.jsp?request_history_index=<c:out value='${count.count}'/>"><c:out value="${historyValue}"/></a>&nbsp;&gt;&gt;&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>
	</div><br>
	<DIV ALIGN="center"><b>Number of Matching Images: <c:out value="${productsFound.size}" /></b></DIV>
	<!--Summary of currently available categories-->
	<TABLE CELLPADDING=10 CELLSPACING=0 BORDER=0 align="center">
	<tr>
		<td colspan="2" ALIGN="center">
		<FORM ACTION="catsearch.jsp" METHOD="POST">
		<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
		<br><h4>Category Drill-Down Search</h4>
		<TABLE CELLSPACING=0 CELLPADDING=10 BORDER=0>
		<c:forEach var="category" items="${searchCategories.searchCategories}">
		<tr valign="top">
			<td><strong><c:out value="${category.key}"/>:</strong></td>
			<td><select name='request_category_prefix<c:out value="${category.key}"/>|<%=DBConnect.EQ%>|<%=DBConnect.IS%>|or|and' size="1">
			<option value="choose one">choose one
			<c:catch var="treeError">
			<c:forEach var="option" items="${category.value}">
				<option value='<c:out value="${option.key}"/>'><c:out value="${option.key}"/>
			</c:forEach>
			</c:catch>
			<c:if test="${treeError != null}">
				<% log.error("Couldn't access value: "+pageContext.getAttribute("category"),(Exception)pageContext.getAttribute("treeError"));%>
			</c:if>
			</select>
			<input type="submit" name="submit" value="Go!" align="absmiddle" border="0">
			</tr>
		</c:forEach> 

	</TABLE></FORM><hr></TD>
	</tr>
	<TR VALIGN="TOP">
		<td align="center">
		<form action="catsearch.jsp" method="POST">
			<INPUT TYPE="Hidden" NAME="actions" VALUE="action_simple_search">
			<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
			<h4>Keyword Search:</h4><input type="Text" size="20" name="request_simple_search">
				<input type="submit" name="submit" value="Go!" align="absmiddle" border="0"></form></td>
			<td>
	<form action="catsearch.jsp" method="POST">
				<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
				<strong>Images&nbsp;&nbsp;<br>
								Added Since:&nbsp;</strong><br>
				<select name="sinceDate_search">
					<option value="0" selected>choose one
					<option value="1">Yesterday
					<option value="2">Last Week
					<option value="3">Last Month
				</select>
				<input type="submit" name="submit" value="Go!" align="absmiddle" border="0">
			</form>
			</td>

		</TD></tr>
		<laz:ifBlock boolean='<%=searchCategories.hasCommonCategories()%>'>
			<tr>
				<td align="center" colspan="2">
				<DIV align="center"><h3>Common Elements</h3>
				<table cellspacing="0" cellpadding="2" border="1">
				<c:forEach var="category" items="${searchCategories.commonCategoryTree}">
					<tr valign="top"><th><c:out value="${category.key}"/></th>
					<td>
					<c:forEach var="value" items="${category.value}">
						<c:out value="${value.key}"/>
					</c:forEach>
					</td>
					</tr>
				</c:forEach>
				</TABLE></DIV><P></td>
			</tr>
		</laz:ifBlock>
	</TABLE>

</c:otherwise>
</c:choose>

<!--Footer that every page contains-->
<%@ include file="footer.jsp"%>

<%
/************ Main search page **************
	searches available categories recursively, allowing the user to browse
	when the results reeach a certain threshold. In some implementations
	this threshold can be controlled by the user. Also browses automatically
	when the search result reaches a different thresholdThe history index tracks
	the current level of "drill-down".
	*/
%>