<%@ include file="header.txt"%>
<% try{ 
	
	String title = "Demo Product Catalog";
%>
<DIV ALIGN="center"><H2>Your Credit Card Information</H2></DIV>
<FORM ACTION="cc_action.jsp" METHOD="POST">
<DIV ALIGN="center"><TABLE><TR VALIGN="TOP"><TD>Card Type</TD><TD>Visa<INPUT TYPE="Radio" NAME="type" VALUE="<%=CreditCard.VISA%>"> MasterCard<INPUT TYPE="Radio" NAME="type" VALUE="<%=CreditCard.MASTERCARD%>"> Amex<INPUT TYPE="Radio" NAME="type" VALUE="<%=CreditCard.AMEX%>"> Discover<INPUT TYPE="Radio" NAME="type" VALUE="<%=CreditCard.DISCOVER%>"></TD></TR>
<TR VALIGN="TOP"><TD>Card Number</TD><TD><INPUT TYPE="Text" NAME="number" SIZE="25"></TD></TR>
<TR VALIGN="TOP"><TD>Expiration Date (mm/yyyy)</TD><TD><INPUT TYPE="Text" NAME="expirationDate" SIZE="15"></TD></TR>
<TR VALIGN="TOP"><TD>Name on Card (if different from your own)</TD><TD>First Name: <INPUT TYPE="Text" NAME="firstName" SIZE="25"><BR>
Middle Initial: <INPUT TYPE="Text" NAME="middleInitial" SIZE="25"><BR>
Last Name: <INPUT TYPE="Text" NAME="lastName" SIZE="25"></TD></TR></TABLE></DIV>
<DIV ALIGN="center"><INPUT TYPE="Submit" NAME="submit" VALUE="Submit"></DIV>
</FORM>

<%@ include file="footer.jsp"%>
<%
	}catch(Exception e){e.printStackTrace();}
%>