<%@ include file="/bali/include/global.inc" %>
<% addParams.put(RequestConstants.REQUEST_ACTION+"w",ActionConstants.ACTION_GET_COST_REPORT);%>
<%@ include file="/bali/include/actionsAndValidate.inc" %>
<jsp:useBean id="cart" scope="session" class="com.lazerinc.client.beans.ShoppingCartBean" />
<jsp:useBean id="user" scope="session" class="com.lazerinc.client.beans.UserBean" />
<jsp:useBean id="costReport" scope="request" class="com.lazerinc.client.beans.CostReportBean"/>
<% 
	String title = "Verifying Order/Download",banner="verify",help="verify";
%>
<%@ include file="header.txt"%>
<c:choose>
	<c:when test='${param["success"] == "false"}'>
		<div align="center" class="eyecatching">There was an error with the Ship To or Send To information.</div>
		<div align="left"><p>Please re-enter the Ship To and Send To numbers, making sure there are no more than seven digits, and resubmit the form.<br>
		<p>If there are no valid numbers for this account, please specify the Account Name.</div>
	</c:when>
	<c:otherwise>
		<div align="center"><h3>Please verify your order</h3>
	</c:otherwise>
</c:choose>

<form action="order.jsp" method="POST">
<table border="0" cellspacing="0" cellpadding="10" width="450"><tr valign="TOP">
<c:if test='${cart.orderRequest}'>
<td>The following files will be sent to your shipping address on CD (if you need it on different format or sent to a different location, please write in a special instruction):
	<ul>
	<c:forEach var="product" items="${cart.orderedProducts}">
		<li><c:out value="${product.primary}"/></li>
	</c:forEach>
	</ul>
	Ship to #:<br><input type="text" name="ShipTo"><br>
	Sold to #:<br><input type="text" name="SoldTo"><br>
	Account Name:<br><input type="text" name="accountName"><br>
	Special instructions:<BR><textarea name="instructions" cols="30" rows="5"></textarea><BR>
	<div align="center"><a href="shopping_cart.jsp">Edit Order</a></div></td>
</c:if>
	
<c:if test='${cart.downloadRequest}'>
	<TD>You have chosen the following files for download:
	<UL>
	<c:forEach var="product" items="${cart.downloadProducts}">
		<li><c:out value="${product.primary}"/></li>
	</c:forEach>
	</UL>
	<DIV align="center">
	<c:set var="totalDLSize" value="${cart.totalDownloadSize}"/>
	<P>Total download size: <c:out value="${totalDLSize/1024}"/>KB</P>
	</DIV>
	<!--Table added for showing estimated time of file download-->	
	<DIV align="center" class="small3">
	<table border="2" cellspacing="2" cellpadding="2">
	<TR><TD><DIV align="center" class="small3">Speed (kbps)</div></td>
	<TD><DIV align="center" class="small3">Estimated download time</div></td></tr>
	<TR><TD><DIV align="center" class="small3">14.4</div></TD>
	<TD><DIV align="center" class="small3"><laz:downloadSpeed size="${totalDLSize}" speed="14.4"/></div></td></tr>
	<TR><TD><DIV align="center" class="small3">28.8</div></TD>
	<TD><DIV align="center" class="small3"><laz:downloadSpeed size="${totalDLSize}" speed="28.8"/></div></td></tr>
	<TR><TD><DIV align="center" class="small3">128</div></TD>
	<TD><DIV align="center" class="small3"><laz:downloadSpeed size="${totalDLSize}" speed="128"/></div></td></tr>
	<TR><TD><DIV align="center" class="small3">1536</div></TD>
	<TD><DIV align="center" class="small3"><laz:downloadSpeed size="${totalDLSize}" speed="1536"/></div></td></tr>   
	</table></div>
	<div align="center"><a href="shopping_cart.jsp">Edit Order</a></div>
</c:if>

</tr></table>
<br>
<c:choose>
	<c:when test='${!cart.orderRequest && cart.downloadRequest}'>
			<input type="hidden" name="dlOnly" value="true">
	</c:when>
	<c:otherwise>
		<input type="hidden" name="dlOnly" value="false">
	</c:otherwise>
</c:choose>
<input type="Submit" name="submit" value="Process Request"></form></div>	


<%@ include file="footer.jsp"%>