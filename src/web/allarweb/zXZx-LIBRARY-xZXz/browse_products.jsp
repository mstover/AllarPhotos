<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%	
	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_CHANGE_NUMBER_SHOWN);
	addParams.put(RequestConstants.REQUEST_ACTION+"r",ActionConstants.ACTION_BROWSE_SET);   
%>
<%@ include file="/zXZx-LIBRARY-xZXz/include/actionsAndValidate.inc" %>
<jsp:useBean id="productsFound" scope="session" class="com.lazerinc.client.beans.ProductSetBean"/>
<jsp:useBean id="cart" scope="session" class="com.lazerinc.client.beans.ShoppingCartBean" />
<c:set var="history_index" value="${productsFound.historyIndex}"/>
<c:set var="defaultImage" value='${configuration[default_image]}'/>
<c:set var="title" value='${title} - Browse Images' />
<c:set var="help" value="browse" scope='session' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "browse";
%>
	<%@ include file="header.txt"%>
	<%@ include file="search_history.jsp"%>

<p>
<DIV ALIGN="center"><H3>Number of Matching Products: <c:out value="${productsFound.size}" /></H3></DIV>
<%
	int toggle = 0;
	 String fs = System.getProperty("file.separator");
	int setSize;
%>

<c:choose>
    <c:when test="${productsFound.size > productsFound.displaySize}">
        <div align="center">Please return to the <a href="catsearch.jsp">Image Search</a> page and narrow your search further</div>
    </c:when>
    <c:otherwise>
    <DIV ALIGN="center">
        <H3>Displaying products <c:out value="${productsFound.displaySet * productsFound.pageSize + 1}"/>
                through <c:out value="${productsFound.displayProductsSize + productsFound.displaySet * productsFound.pageSize}" /></H3>
    </DIV>
      <c:if test="${productsFound.numberPages > 1}">
        <table align="center">
        <tr valign="middle">
				<td width='75'>
					<c:choose>
						<c:when test="${productsFound.displaySet > 0}">
							<FORM ACTION="browse_products.jsp" METHOD="POST">
                <INPUT TYPE="Hidden" NAME="request_browse_set"
                  VALUE='<c:out value="${productsFound.displaySet - 1}" />'>
                <INPUT class='btnBkd' TYPE="submit" NAME="previous" VALUE="Previous" align="absmiddle">
                </form>
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="header" align="center">
<% 
	log.debug("displaySet = "+productsFound.getDisplaySet());
	log.debug("Number of pages = "+productsFound.getNumberPages());
%>
					<c:forEach varStatus="counter" begin="0" end="${productsFound.numberPages-1}">
						<c:choose>
							<c:when test="${counter.index == productsFound.displaySet}">
								&nbsp;<c:out value="${counter.count}"/>&nbsp;
							</c:when>
							<c:otherwise>
								&nbsp;<a href='browse_products.jsp?request_browse_set=<c:out value="${counter.index}"/>'><c:out value="${counter.count}"/></a>&nbsp;
							</c:otherwise>
						</c:choose>
						<c:if test="${!counter.last}">|</c:if>
					</c:forEach>
        </td>
				<td width='75'>
					<c:choose>
						<c:when test="${productsFound.displaySet + 1 < productsFound.numberPages}">
							<FORM ACTION="browse_products.jsp" METHOD="POST">
								<INPUT TYPE="Hidden" NAME="request_browse_set"
									VALUE='<c:out value="${productsFound.displaySet + 1}" />'>
								<INPUT class='btnBkd' TYPE="submit" NAME="next" VALUE="Next" align="absmiddle">
							</form>
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				</tr>
        </table>
      </c:if>
      <div align="center">
      <TABLE CELLSPACING=0 style='border: 0;'>
      <% log.debug("number of products in display set = "+productsFound.getDisplayProductsSize());%>
      <c:forEach var="currentProduct" varStatus="counter" items="${productsFound.displayProducts}">
				<c:set var="servletDir" value='${configuration["servlet_dir"]}'/>
				<c:set var="thumbDir" value='${currentProduct.value["Thumbnail Directory"]}'/>
        <c:if test="${(counter.count-1) % 3 == 0}">
          <tr valign="bottom">
        </c:if>
        <td class="small1" style='height: 150px; width: 150px; padding: 5px; text-align: center; vertical-align: bottom;'>
				<form action="browse_products.jsp" method="POST">
					<a href='display_product.jsp?request_product_id=<c:out value="${currentProduct.id}"/>&request_product_family=<c:out value="${currentProduct.productFamilyName}"/>'>
					<c:choose>
						<c:when test='${currentProduct.value["Height"] > currentProduct.value["Width"]}'>
							<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' BORDER="0" HEIGHT="130"><br>
						</c:when>
						<c:otherwise>
							<IMG SRC='<c:out value="${servletDir}"/>fetchpix?file=<c:out value="${thumbDir}"/>&mimetype=image/jpeg' BORDER="0" WIDTH="130"><br>
						</c:otherwise>
					</c:choose>
					<c:out value="${currentProduct.primary}" /></a><BR>
					<laz:ifBlock object="$_cart" method="hasProduct" arguments="$_currentProduct"> 
						<laz:if>
							<input type="hidden" name="removeFromCart.remove" 
								value='<c:out value="${currentProduct.id}|${currentProduct.productFamilyName}"/>'>
							<input type="hidden" name="action" value="action_remove_from_cart">
							<input class='btnBkd' type="submit" name="submit" value="Remove from Cart" border=0>
						</laz:if>
						<laz:else>
							<input type="hidden" name="request_product_id" value='<c:out value="${currentProduct.id}"/>'>
							<input type="hidden" name="request_product_family" value='<c:out value="${currentProduct.productFamilyName}"/>'>
							<input type="hidden" name="action" value="action_add_to_cart">
							<input class='btnBkd' type="submit" name="submit" value="Add to Cart" border=0>
						</laz:else>	
					</laz:ifBlock>
				</form>
        </td>
        <c:if test="${(counter.count-1) % 3 == 2}">
          </tr>
        </c:if>
      </c:forEach>
      </TABLE></div>
    </c:otherwise>
</c:choose>

<%@ include file="footer.jsp"%>
