<%@ include file="include.txt"%>
<%
	String[] states = admin.getStates();
	String[] countries = admin.getCountries();
	String[] allFamilies = admin.listFamilies();
	int size = allFamilies.length;
	if(size > 25)
		size = 25;
%>
<div align="center"></div><form action="add_merchant_action.jsp" method="POST">
<div align="center"><table><tr valign="TOP">
<td><select name="assignFamilies" size="<%=size%>" multiple>
<% for(int x = 0;x < allFamilies.length;x++)
	{
		%><OPTION VALUE="<%=allFamilies[x]%>"><%= allFamilies[x] %><%
	}
%>
</select></td>
<td><table>
<tr valign="TOP">
	<td>Merchant ID</td>	
	<td><input type="Text" name="merchantID" size=40></td>
</tr>
<tr valign="TOP">
	<td>Merchant Name</td>		
	<td><input type="Text" name="name" size=40></td>
</tr>
<tr valign="TOP">
	<td>Phone</td>		
	<td><input type="Text" size=40 name="phone"></td>
</tr>
<tr valign="TOP">
	<td>Fax</td> 	
	<td><input type="Text" size=40 name="fax"></td>
</tr>
<tr valign="TOP">
	<td>Email for Ordering</td> 	
	<td><input type="Text" size=40 name="orderingEmail"></td>
</tr>
<tr valign="TOP">
	<td>Email for Fulfilling</td> 	
	<td><input type="Text" size=40 name="fulfillmentEmail"></td>
</tr>
<TR><TD>Address Line 1:</TD><TD><INPUT TYPE="Text" NAME="address1" SIZE="25"></TD></TR>
<TR><TD>Address Line 2:</TD><TD><INPUT TYPE="Text" NAME="address2" SIZE="25"></TD></TR>
<TR><TD>City:</TD><TD><INPUT TYPE="Text" NAME="city" SIZE="25"></TD></TR>
<TR><TD>State:</TD><TD><SELECT NAME="state">
<%
	for(int x = 0;x < states.length; x++)
	{
		%><OPTION VALUE="<%=states[x]%>"><%=states[x]%><%
	}
%>
</SELECT></TD></TR>
<TR><TD>Zip:</TD><TD><INPUT TYPE="Text" NAME="zip" SIZE="25"></TD></TR>
<TR><TD>Country:</TD><TD><SELECT NAME="country">
<%
	for(int x = 0;x < countries.length; x++)
	{
		%><OPTION VALUE="<%=countries[x]%>"><%=countries[x]%><%
	}
%>
</SELECT></TD></TR>
<TR><TD>Supported Credit Cards:</TD>
	<TD><SELECT NAME="creditCardsAccepted" MULTIPLE SIZE="4">
		<OPTION VALUE="<%=Merchant.VISA%>">VISA
		<OPTION VALUE="<%=Merchant.MASTERCARD%>">MasterCard
		<OPTION VALUE="<%=Merchant.DISCOVER%>">Discover
		<OPTION VALUE="<%=Merchant.AMEX%>">American Express
		</SELECT></TD>
</TR>
<TR><TD>Order Processing Method:</TD>
	<TD><input type="Radio" name="orderProcessing" value="<%=Merchant.MANUAL%>">Manual<BR>
	<input type="Radio" name="orderProcessing" value="<%=Merchant.AUTO%>">Automated</TD>
</TR>
<TR><TD>Sales Tax:</TD>
	<TD><input type="Text" name="taxRate"></TD>
</TR>
</table></td></tr></table><input type="Submit" name="submit" value="Submit"></div>

</form>