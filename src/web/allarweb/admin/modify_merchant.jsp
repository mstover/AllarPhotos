<%@ include file="include.txt"%>
<%
	Merchant merchant = admin.getMerchant(request.getParameter("merchant")); 
	String[] fams = merchant.getProductFamilies();
	Set families = new HashSet();
	for(int x = 0;x < fams.length;families.add(fams[x++]));
	String[] states = admin.getStates();
	String[] countries = admin.getCountries();
	String[] allFamilies = admin.listFamilies();
	int size = allFamilies.length;
	if(size > 25)
		size = 25;
%>
<div align="center"><h3><%=merchant.getName()%></h3></div><form action="modify_merchant_action.jsp" method="POST">
<input type="Hidden" name="name" value="<%=merchant.getName()%>">
<div align="center"><table><tr valign="TOP">
<td><select name="assignFamilies" size="<%=size%>" multiple>
<% for(int x = 0;x < allFamilies.length;x++)
	{
		%><OPTION VALUE="<%=allFamilies[x]%>"<%
		if(families.contains(allFamilies[x]))
		{
			%> SELECTED><%
		}
		else {
			%>><%
		}
		%><%= allFamilies[x] %><%
	}
%>
</select></td>
<td><table>
<tr valign="TOP">
	<td>Merchant ID</td>	
	<td><input type="Text" name="merchantID" size=40 value="<%=merchant.getMerchantID()%>"></td>
</tr>
<tr valign="TOP">
	<td>Merchant Name</td>		
	<td><textarea name="newName" cols=40 rows=2 wrap="VIRTUAL"><%=merchant.getName()%></textarea></td>
</tr>
<tr valign="TOP">
	<td>Phone</td>		
	<td><input type="Text" size=40 name="phone" value="<%=merchant.getPhone()%>"></td>
</tr>
<tr valign="TOP">
	<td>Fax</td> 	
	<td><input type="Text" size=40 name="fax" value="<%=merchant.getFax()%>"></td>
</tr>
<tr valign="TOP">
	<td>Email for Ordering</td> 	
	<td><input type="Text" size=40 name="orderingEmail" value="<%=merchant.getOrderingEmail()%>"></td>
</tr>
<tr valign="TOP">
	<td>Email for Fulfilling</td> 	
	<td><input type="Text" size=40 name="fulfillmentEmail" value="<%=merchant.getFulfillmentEmail()%>"></td>
</tr>
<TR><TD>Address Line 1:</TD><TD><INPUT TYPE="Text" NAME="address1" VALUE="<%=merchant.getAddress1()%>" SIZE="25"></TD></TR>
<TR><TD>Address Line 2:</TD><TD><INPUT TYPE="Text" NAME="address2" VALUE="<%=merchant.getAddress2()%>" SIZE="25"></TD></TR>
<TR><TD>City:</TD><TD><INPUT TYPE="Text" NAME="city" VALUE="<%=merchant.getCity()%>" SIZE="25"></TD></TR>
<TR><TD>State:</TD><TD><SELECT NAME="state">
<%
	for(int x = 0;x < states.length; x++)
	{
		if(states[x].equals(merchant.getState()))
		{
			%><OPTION VALUE="<%=states[x]%>" SELECTED><%=states[x]%><%
		}
		else
		{
			%><OPTION VALUE="<%=states[x]%>"><%=states[x]%><%
		}
	}
%>
</SELECT></TD></TR>
<TR><TD>Zip:</TD><TD><INPUT TYPE="Text" NAME="zip" VALUE="<%=merchant.getZip()%>" SIZE="25"></TD></TR>
<TR><TD>Country:</TD><TD><SELECT NAME="country">
<%
	for(int x = 0;x < countries.length; x++)
	{
		if(countries[x].equals(merchant.getCountry()))
		{
			%><OPTION VALUE="<%=countries[x]%>" SELECTED><%=countries[x]%><%
		}
		else
		{
			%><OPTION VALUE="<%=countries[x]%>"><%=countries[x]%><%
		}
	}
%>
</SELECT></TD></TR>
<TR><TD>Supported Credit Cards:</TD>
<% int[] cards = merchant.getCreditCards(); 
	String visaChecked = "",mastercardChecked = "",discoverChecked = "",amexChecked = "";
	if(cards!=null){
	for(int x = 0;x < cards.length;x++)
	{
		switch(x)
		{
			case merchant.VISA : visaChecked = " SELECTED"; break;
			case merchant.MASTERCARD : mastercardChecked = " SELECTED"; break;
			case merchant.DISCOVER : discoverChecked = " SELECTED"; break;
			case merchant.AMEX : amexChecked = " SELECTED"; break;
		}
	}}
			%>
	<TD><SELECT NAME="creditCardsAccepted" MULTIPLE SIZE="4">
		<OPTION VALUE="<%=merchant.VISA%>"<%=visaChecked%>>VISA
		<OPTION VALUE="<%=merchant.MASTERCARD%>"<%=mastercardChecked%>>MasterCard
		<OPTION VALUE="<%=merchant.DISCOVER%>"<%=discoverChecked%>>Discover
		<OPTION VALUE="<%=merchant.AMEX%>"<%=amexChecked%>>American Express
		</SELECT></TD>
</TR>
<TR><TD>Order Processing Method:</TD>
	<TD><input type="Radio" name="orderProcessing" value="<%=merchant.MANUAL%>" checked>Manual<BR>
	<input type="Radio" name="orderProcessing" value="<%=merchant.AUTO%>">Automated</TD>
</TR>
<TR><TD>Sales Tax:</TD>
	<TD><input type="Text" name="taxRate" value="<%=merchant.getTaxRate()%>"></TD>
</TR>
</table></td></tr></table><input type="Submit" name="submit" value="Submit"></div>

</form>