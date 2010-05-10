<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%
	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_CHANGE_NUMBER_SHOWN);
    addParams.put(RequestConstants.REQUEST_ACTION+"r",ActionConstants.ACTION_CATEGORY_SEARCH);   
%>
<%@ include file="/zXZx-LIBRARY-xZXz/include/actionsAndValidate.inc" %>

<jsp:useBean id="productsFound" scope="session" class="com.allarphoto.client.beans.ProductSetBean"/>
<jsp:useBean id="messages" scope="request" class="com.allarphoto.client.beans.MessageBean"/>
<jsp:useBean id="searchCategories" scope="session" class="com.allarphoto.client.beans.SearchCategoryBean"/>

<c:set var="title" value='${title} - Image Search' />
<c:set var="help" value="search" scope='session' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "search";
%>
<c:set var="history_index" value="${productsFound.historyIndex}"/>
<!--Header that every page contains-->
<%@ include file="header.txt"%>
<c:choose>
	<c:when test="${productsFound.size == 0}">
		<b>No products were found using this search.<b><br>
		<a href="catsearch.jsp?request_history_index=<c:out value='${history_index -1}'/>">Please try again</a>.
	</c:when>
	<c:when test='${productsFound.size <= configuration["default_product_display_number"] ||
                       searchCategories.size == 0}' >
		<%-- Need to set the Display Set to 0 --%>			   
		<jsp:forward page="browse_products.jsp"/>
	</c:when>
<c:otherwise>
    <%@ include file="search_history.jsp"%>
	<br>
	<DIV class='categoryheader' style='text-align: center;'><b>Number of 
		Matching Images: <c:out value="${productsFound.size}" /></b></DIV>
  <!-- if itemsInSearch < configBrowsableNumber then -->
  <c:choose>
    <c:when test="${productsFound.size <= productsFound.displaySize}">
		<div style='text-align: center;'><form action="browse_products.jsp" method="POST">
			<input class='purpBkd' type="submit" name="submit" value="View Search Now" align="absmiddle" border="0">
		</form></div>
	</c:when>
  </c:choose>
	<!--Summary of currently available categories-->
	<DIV style='text-align: center;'>
	<TABLE CELLSPACING=0 style='padding: 6px; border-width: 0;'>
	<tr>
	  <td style='text-align: center; padding: 10px;'>
		<form action="catsearch.jsp" method="POST">
			<INPUT TYPE="Hidden" NAME="actions" VALUE="action_simple_search">
			<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
			<div class='categoryheader'>Keyword Search:</div>
			<input type="Text" size="20" name="request_simple_search">
			<input class='btnBkd' type="submit" name="submit" value="Go!" align="absmiddle" border="0">
		</form>
	  </td>
	  <td style='text-align: center; padding=10px;'>
		<FORM ACTION="catsearch.jsp" METHOD="POST">
		<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
		<div class='categoryheader'>Category Drill-Down Search</div>
		<TABLE CELLSPACING=0 style='padding: 10px; border-width: 0;'>
		<c:forEach var="category" items="${searchCategories.searchCategories}">
		  <tr>
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
			<input class='btnBkd' class='btnBkd' type="submit" name="submit" value="Go!" align="absmiddle" border="0">
		  </tr>
		</c:forEach>
		</TABLE></FORM>
	  </TD>
	</tr>
	<tr>
	  <td colspan='2'><hr></td>
	</tr>
	<TR VALIGN="TOP">
		<td align="center"></td>
		<td>
	<form action="catsearch.jsp" method="POST">
				<INPUT TYPE="Hidden" NAME="request_history_index" VALUE='<c:out value="${productsFound.historyIndex}"/>'>
				<div class='categoryheader'>Images Added Since:</div>
				<select name="sinceDate_search">
					<option value="0" selected>choose one
					<option value="1">Yesterday
					<option value="2">Last Week
					<option value="3">Last Month
				</select>
				<input class='btnBkd' type="submit" name="submit" value="Go!" align="absmiddle" border="0">
			</form>
			</td>

		</TD></tr>
		<%--
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
		--%>
	</TABLE>
	</div>
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