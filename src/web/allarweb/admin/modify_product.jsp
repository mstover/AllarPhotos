<%@ include file="include.txt"%>
<%
	String fs = System.getProperty("file.separator");
	Product product = commerce.getSearchedProduct(request.getParameter("product"),request.getParameter("family"));	 
	ProductFamily family = product.getProductFamily();
	Object temp;
	String title = product.getPrimary(), banner = "detailBanner.gif";
        session.putValue("helpTitle","Help for Image Detail");	      //added for help
        session.putValue("helpBanner","kdkCiHelpDetailBanner.gif");   //added for help
        session.putValue("helpText","../../help/helpDetail.txt");     //added for help
	String thumb = (String)product.getValue("Thumbnail Directory");
	int brk = thumb.lastIndexOf(fs);
	String pv = thumb.substring(0,brk+1)+"pv"+thumb.substring(brk);
	File testFile = new File(pv);
	if(testFile.exists())
		thumb = pv;
	Dimension dim = Functions.resizeDimension(Functions.getImageDimensions(thumb,controller.getConfigValue("default_image")),new Dimension(500,500));
%>
<%@ include file="header.txt"%>
	<table><tr>
	<td><TABLE CELLPADDING=1 CELLSPACING=1 BORDER=0>
<%
	ProductField[] fields = family.getFields();
	String fieldName;
 	for(int x = 0;x < fields.length;x++)
	{
		%><TR VALIGN="TOP"><%
		if(fields[x].getDisplayOrder() > 0)
		{
			fieldName = fields[x].getName();
			temp = product.getValue(fieldName);
			if (fieldName.equals("File Size"))
				temp = new String(Functions.round(((Float)temp).floatValue()/1024, 2) + "k");
			else if (fieldName.equals("Height") || fieldName.equals("Width"))
				temp = new String(Functions.round(((Float)temp).floatValue()/25403969.54993, 2) + "in");
			else if (fieldName.equals("Resolution"))
				temp = new String(Functions.round(((Float)temp).floatValue(), 2) +"dpi");
			if(temp instanceof String)
			{
				if(temp != null && ((String)temp).length() > 0 && !temp.equals("N/A")) {
					%><TD><B><%=fields[x].getName()%></B></TD>
					<TD><%=inputType(fields[x],temp)%></TD>
					<TD><%=selectType(commerce,fields[x],temp)%><%
				}
			}
			else
			{
				if(temp != null && !temp.equals("N/A")) {
					%><TD><B><%=fields[x].getName()%></B></TD>
					<TD><%=inputType(fields[x],temp)%></TD>
					<TD><%=selectType(commerce,fields[x],temp)%><% 
				}
			}
		}
	}
%>
</UL></td>
	<td align="center">
		<h3><%=title %></h3>
		<IMG SRC='<%=controller.getConfigValue("servlet_dir")%>fetchpix?file=<%=URLEncoder.encode(thumb)%>&mimetype=image/jpeg' BORDER="0" HEIGHT="<%=(int)dim.getHeight()%>" WIDTH="<%=(int)dim.getWidth()%>">
		<h4><A HREF="javascript:history.back()">Go Back</a></h4>
	</td></tr></table>

<%@ include file="footer.txt"%>

<%!
	String inputType(ProductField field,Object value)
	{
		String retVal;
		if(Math.abs(field.getType()) == ProductField.DESCRIPTION)
			retVal = "<TEXTAREA NAME='"+field.getName()+"' ROWS='3' COLS='30'>"+value+"</TEXTAREA>";
		else
			retVal = "<INPUT NAME='"+field.getName()+"' SIZE=25 VALUE="+value+">";
		return retVal;
	}

	String selectType(CommerceWebInterface commerce, ProductField field, Object value)
	{
		StringBuffer retVal = new StringBuffer();
		String selected = "";
		String temp;
		if(Math.abs(field.getType()) == ProductField.CATEGORY)
		{
			Set valueList = commerce.getAvailableValues(null,null,null);
			retVal.append("<SELECT NAME='"+field.getName()+"_select' SIZE=1>");
			for(Iterator i = valueList.iterator();i.hasNext();)
			{
				temp = (String)i.next();
				if(temp.equals(value))
					selected = " SELECTED";
				else
					selected = "";
				retVal.append("<OPTION VALUE='"+temp+"'"+selected+">"+temp);
			}
		}
		else
			retVal.append("&nbsp;");
		return retVal.toString();
	}
%>

