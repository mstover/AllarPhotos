<%@ include file="/modpac/include/global.inc" %>
<%	
	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_SET_CART_SET); 
	// **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "cart";
%>
<%@ include file="/modpac/include/actionsAndValidate.inc" %>
<jsp:useBean id="cart" scope="session" class="com.allarphoto.client.beans.ShoppingCartBean" />
<c:set var="defaultImage" value='${configuration.properties["default_image"]}'/>
<%@ include file="header.txt"%>
<script language="JavaScript">
<!--
function doPost(action)
	{
		document.cartForm.action = action;
		document.cartForm.submit();
	}
	function doChangeCartSet(action,set)
	{
		document.cartForm.request_cart_set.value = set;
		doPost(action);
	}	
// -->		
</script>

<DIV ALIGN="center"><H3>Number of Products in your Shopping Cart: <c:out value="${cart.size}"/></H3>

<c:choose>
	<c:when test="${cart.size == 0}">
		<DIV ALIGN="center"><h3>Your Shopping Cart is empty.</h3>
		<p>Please <a href="catsearch.jsp">resume your search</a>.
		</DIV>
	</c:when>
	<c:otherwise>		
	<form name="cartForm" method="POST">
		<input class='btnBkd' type="button" name="go" value="Proceed" onClick="doPost('check_user.jsp')">
		<input type="hidden" name="action" value="action_order_items">
			<input type="hidden" name="actiona" value="action_remove_from_cart">
		<input type="hidden" name="request_cart_set" value='<c:out value="${cart.currentSetNumber}"/>'>
		
		<DIV ALIGN="center"><H3>Displaying products <c:out value="${cart.startIndexOfSet}"/>
			through <c:out value="${cart.endIndexOfSet}"/></H3></DIV>
		<DIV ALIGN="center">			
			
		<c:if test="${cart.currentSetNumber > 0}">
			<input type="hidden" name="actionc" value="action_set_cart_set">
			<INPUT TYPE="button" NAME="previous" VALUE="Previous" onClick='doChangeCartSet("shopping_cart.jsp","<c:out value='${cart.currentSetNumber - 1}'/>")'>
		</c:if>
		<c:if test="${cart.currentSetNumber < cart.maxSetNumber}">
			<input type="hidden" name="actionc" value="action_set_cart_set">
			<INPUT TYPE="button" NAME="next" VALUE="Next" onClick='doChangeCartSet("shopping_cart.jsp","<c:out value='${cart.currentSetNumber + 1}'/>")'>
		</c:if>
			
		<TABLE CELLPADDING=5 CELLSPACING=0 BORDER=0>
			<c:forEach var="product" varStatus="counter" items="${cart.currentSet}">
				<c:set var="servletDir" value='${configuration["servlet_dir"]}'/>
				<c:set var="thumbDir" value='${product.value["Thumbnail Directory"]}'/>   
				<c:set var="productName" value='${product.value["Original Directory"]}'/>
				<c:if test="${(counter.count-1) % 3 == 0}">
					<tr valign="top">
				</c:if>
				<TD>
					<input type="Hidden" name="request_product" value='<c:out value="${product.id}"/>|<c:out value="${product.productFamilyName}"/>'>
					<a href='display_cart_product.jsp?request_product_id=<c:out value="${product.id}"/>&request_product_family=<c:out value="${product.productFamilyName}"/>'>
					<c:choose>
						<c:when test='${product.value["Height"] > product.value["Width"]}'>
							<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' BORDER="0" HEIGHT="130">
						</c:when>
						<c:otherwise>
							<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' BORDER="0" WIDTH="130">
						</c:otherwise>
					</c:choose><br>
					<c:out value="${product.primary}" /></a><BR>
					<div class="small1">
						<input type="Checkbox" name="addToOrder.download_jpg" value='<c:out value="${product.id}"/>|<c:out value="${product.productFamilyName}"/>'<laz:if object="$_cart" method="hasInstruction" arguments="$_product,regular"> CHECKED</laz:if>>Download JPG <BR>
						<input type="Checkbox" name="addToOrder.order" value='<c:out value="${product.id}"/>|<c:out value="${product.productFamilyName}"/>'<laz:if object="$_cart" method="hasInstruction" arguments="$_product,order"> CHECKED</laz:if>>Order CD-ROM <BR>
						<input type="Checkbox" name="removeFromCart.remove" value='<c:out value="${product.id}"/>|<c:out value="${product.productFamilyName}"/>'>Remove From Cart
					</div>
				</td>
				<c:if test="${(counter.count-1) % 3 == 2}">
					</tr>
				</c:if>
			</c:forEach>
		</TABLE></form>
	</c:otherwise>
</c:choose>
<!--<div align="center"><p>Total downloadable size of files in cart: <c:out value="${cart.totalDownloadSize / 1024}"/>k</div>
-->
<%@ include file="footer.jsp"%>