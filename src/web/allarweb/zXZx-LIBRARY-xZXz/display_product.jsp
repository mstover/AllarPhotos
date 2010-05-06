<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%	
	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_GET_PRODUCT);  
	addParams.put(RequestConstants.REQUEST_ACTION+"y",ActionConstants.ACTION_SET_PREVIOUS_PRODUCT); 
	addParams.put(RequestConstants.REQUEST_ACTION+"z",ActionConstants.ACTION_SET_NEXT_PRODUCT);  
%>
<%@ include file="/zXZx-LIBRARY-xZXz/include/actionsAndValidate.inc" %>
<jsp:useBean id="productBean" scope="session" class="com.lazerinc.client.beans.ProductBean"/>
<jsp:useBean id="previousProductBean" scope="session" class="com.lazerinc.client.beans.ProductBean"/>
<jsp:useBean id="nextProductBean" scope="session" class="com.lazerinc.client.beans.ProductBean"/>
<c:set var="product" value="${productBean.product}"/>
<c:set var="defaultImage" value='${configuration.properties["default_image"]}'/>
<c:set var="thumbDir" value='${product.value["Thumbnail Directory"]}'/>
<c:set var="servletDir" value='${configuration["servlet_dir"]}'/>

<c:set var="title" value='${productBean.primary}' />
<c:set var="help" value="detail" scope='session' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "detail";
%>
<%@ include file="header.txt"%>
<h3 style='text-align: center;'><c:out value='${title}' /></h3>
<div>
  <div class='sideSubBox' style='width: 185px; margin: 0;'>
  <UL>
	<c:forEach var="field" items="${productBean.productFamily.fields}">
		<c:set var="prodKeyword" value="${product.value[field.name]}"/>
		<c:if test="${field.displayOrder >= 1000 && null!= prodKeyword}" >
			<LI><B><c:out value="${field.name}"/>:</B>&nbsp;<c:out value="${prodKeyword}"/>
		</c:if>
	</c:forEach>
  </UL>
	<!-- GO BACK BUTTON TO GO HERE -->
	<div style='text-align: center;'><form action="browse_products.jsp">
		<input class='btnBkd' type="submit" value="Go Back">
	  </form>
	</div>
  </div>
  <div>
	<% log.debug("start display image "+ productBean.getPrimary());%>
	<!-- IMAGE OF ASSET -->
	<c:choose>
		<c:when test='${product.value["Height"] > product.value["Width"]}'>
			<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' style='height: 400px; border-width: 0; text-align: center;'><br>
		</c:when>
		<c:otherwise>
			<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' style='width: 400px; border-width: 0; text-align: center;'><br>
		</c:otherwise>
	</c:choose><br><br>
	<% log.debug("end display image "+ productBean.getPrimary());%>
	<div id='displayProdButtons' style='text-align: right; width: 400px;'>
	  <div class='floatLft'>
	  <c:choose>
		<c:when test="${!empty previousProductBean.product}">
		  <form action='display_product.jsp' method='post'>
			<input type="hidden" name="request_product_id" value='<c:out value="${previousProductBean.id}"/>'>
			<input type='hidden' name='request_product_family' value='<c:out value="${previousProductBean.productFamilyName}"/>'>
			<input class='btnBkd' type='submit' name='submit' value='Previous'>
		  </form>
		</c:when>
		<c:otherwise>
			<input type='submit' name='submit' value='Previous' disabled='true'>
		</c:otherwise>
	  </c:choose>
	  </div>
	  <div class='floatRght'>
	  <c:choose>
		<c:when test="${!empty nextProductBean.product}">
		  <form action='display_product.jsp' method='post'>
			<input type="hidden" name="request_product_id" value='<c:out value="${nextProductBean.id}"/>'>
			<input type='hidden' name='request_product_family' value='<c:out value="${nextProductBean.productFamilyName}"/>'>
			<input class='btnBkd' type='submit' name='submit' value='Next'>
		  </form>
		</c:when>
		<c:otherwise>
			<input type='submit' name='submit' value='Next' disabled='true'>
		</c:otherwise>
	  </c:choose>
	  </div>
	  <div style='text-align: center;'><FORM ACTION="browse_products.jsp" METHOD="post"> 
		<input type="hidden" name="request_product_id" value='<c:out value="${product.id}"/>'>
		<input type="hidden" name="request_product_family" value='<c:out value="${product.productFamilyName}"/>'>
		<c:choose> 
			<c:when test="${productBean.inCart}">
				<input type="hidden" name="action" value="action_remove_from_cart">
				<input type="hidden" name="removeFromCart.remove" 
						value='<c:out value="${product.id}"/>|<c:out value="${product.productFamilyName}"/>'>
				<input class='btnBkd' type="submit" align="top" src="buttonImages/removeFromCartButton.gif"
						name="submit" value="Remove from Cart" border=0>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="action" value="action_add_to_cart">
				<input class='btnBkd' type="submit" align="top" src="buttonImages/addToCartButton.gif"
						name="submit" value="Add to Cart" border=0>
			</c:otherwise>
		</c:choose>
	  </form>
	  </div>
	</div><!-- END OF DISPLAY PRODUCT BUTTONS -->
  </div>
</div>
<%@ include file="footer.jsp"%>