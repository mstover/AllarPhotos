<%@ include file="/modpac/include/global.inc" %>
<c:set var='title' value='${title} - Help Page' />

<%@ include file="header.txt" %>
<!--a href="help.jsp?help=general">General Help</a> -  - </div><br-->
<div class='sideSubBox'>
  <b>Help Topics</b>
  <ul>
	<li><a href="help.jsp?help=login">Login</a>
	<li><a href="help.jsp?help=search">Searching for Images</a>
	<li><a href="help.jsp?help=browse">Viewing Images</a>
	<li><a href="help.jsp?help=detail">Image Detail</a>
	<li><a href="help.jsp?help=defs">Definitions</a>
	<li><a href="help.jsp?help=cart">Shopping Cart</a>
	<li><a href="help.jsp?help=tips">Tips for Placing Orders</a>
	<li><a href="help.jsp?help=verify">Verifying Your Order</a>
	<li><a href="help.jsp?help=order">Confirming Your Order</a>
	<li><a href="help.jsp?help=modify">Editing Your User Information</a>
  </ul>
</div>
<div align="left">
	<jsp:include page='<c:out value="${helpText}"/>' />
</div><br>
<A HREF="javascript:history.back()">Go Back</a>
<%@ include file="footer.jsp" %>