<%@ include file="/bali/include/global.inc" %>
<% addParams.put(RequestConstants.REQUEST_ACTION+"w",ActionConstants.ACTION_EXECUTE_ORDER);
	addParams.put("InvalidOrderInfo","verify.jsp?success=false");%>
<%@ include file="/bali/include/actionsAndValidate.inc" %>

<jsp:useBean id="orderResponse" scope="session" class="com.lazerinc.client.beans.OrderResponseBean"/>

<% String title = "Order Confirmation",banner="",help="order"; %>
<%@ include file="header.txt"%>
<div align="left">

<!-- Order Messages-->
	<c:out value="${orderResponse.message}"/>

<!-- Download Listing -->
	<dl>
		<c:if test="${!empty orderResponse.downloadFiles}">
			<dt><DIV CLASS="eyecatching">Files for Download:</DIV>
		</c:if>
		<c:forEach var="file" items="${orderResponse.downloadFiles}">
			<dd><b>Download <c:out value="${file.key}"/>:</b>
			<a href='files/<c:out value="${file.value}"/>'><c:out value="${file.value}"/></a><br>
		</c:forEach>
	</dl>

<!-- Family Listing -->
	<c:forEach var="family" items="${orderResponse.families}">
		<dl>
			<DIV CLASS="eyecatching">Order from <c:out value="${family.key.descriptiveName}"/></DIV>
			<c:forEach var="famInfo" items="${family.value}">
				<c:if test='${famInfo.key != "family"}'>
					<dd><b><c:out value="${famInfo.key}"/>:</b>&nbsp;<c:out value="${famInfo.value}"/><br>
				</c:if>
			</c:forEach>
		</dl>

<!-- Ordered Products Listing -->
		<dl>
			<h4>Products Ordered:</h4>
			<c:forEach var="product" items="${orderResponse.products}">
				<c:if test='${product.key.productFamily == family.key}'>
					<dt><c:out value="${product.key.primary}"/><br>
				</c:if>
				<c:forEach var="prodInfo" items="${product.value}">
					<c:if test='${prodInfo.key != "cost"}'>
						<dd><b><c:out value="${prodInfo.key}"/>:</b>&nbsp;<c:out value="${prodInfo.value}"/><br>
					</c:if>
				</c:forEach>	
			</c:forEach>
		</dl>
	</c:forEach>


<!-- Order Information -->
	<dl>
		<h4>Additional Order Information:</h4>
		<c:forEach var="info" items="${orderResponse.orderInformation}">
			<c:if test='${info.key != "Total Cost"}'>
				<dd><b><c:out value="${info.key}"/>:</b>&nbsp;<c:out value="${info.value}"/><br>
			</c:if>
		</c:forEach>
	</dl>
</div>
<%@ include file="footer.jsp"%>
	