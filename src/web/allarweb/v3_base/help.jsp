<%@ include file="/bali/include/global.inc" %>
<%
String helpText = "",title="Help Page",banner="help";
if(null != request.getParameter("help")){
	helpText=request.getParameter("help");
}
%>
<%@ include file="header.txt"%>

	<div class="pageheader" align="center">
		<a href="help.jsp?help=general">General Help</a> - <a href="help.jsp?help=tips">Tips for Placing Orders</a> - <a href="help.jsp?help=defs">Definitions</a></div><br>
		<div align="left"><%
			if(helpText.equals("login")){%>
				<jsp:include page="help/login.txt"/><%
			}else if(helpText.equals("login")){%>
				<jsp:include page="help/login.txt"/><%
			}else if(helpText.equals("search")){%>
				<jsp:include page="help/search.txt"/><%
			}else if(helpText.equals("browse")){%>
				<jsp:include page="help/browse.txt"/><%
			}else if(helpText.equals("detail")){%>
				<jsp:include page="help/detail.txt"/><%
			}else if(helpText.equals("modify")){%>
				<jsp:include page="help/editUser.txt"/><%
			}else if(helpText.equals("cart")){%>
				<jsp:include page="help/cart.txt"/><%
			}else if(helpText.equals("order")){%>
				<jsp:include page="help/order.txt"/><%
			}else if(helpText.equals("verify")){%>
				<jsp:include page="help/verify.txt"/><%
			}else if(helpText.equals("general")){%>
				<jsp:include page="help/general.txt"/><%
			}else if(helpText.equals("tips")){%>
				<jsp:include page="help/tips.txt"/><%
			}else if(helpText.equals("defs")){%>
				<jsp:include page="help/defs.txt"/><%
			}else if(helpText.equals("jpeg")){%>
				<jsp:include page="help/jpeg.txt"/><%
			}else if(helpText.equals("eps")){%>
				<jsp:include page="help/eps.txt"/><%
			}else if(helpText.equals("pdf")){%>
				<jsp:include page="help/pdf.txt"/><%
			}else{%>
				<p> Sorry, we don't have a help file for the page you are linking from.<br>
				<jsp:include page="help/general.txt"/><%				
			}%></div>	<br>
	<A HREF="javascript:history.back()">Go Back</a>
<%@ include file="footer.jsp"%>