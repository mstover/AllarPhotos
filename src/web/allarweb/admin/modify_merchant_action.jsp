<%@ include file="include.txt"%>
<%
	String name = request.getParameter("name");
	String newName = request.getParameter("newName");
	String[] families = request.getParameterValues("assignFamilies");
	Merchant merchant = new Merchant();
	WebBean.setValues(merchant,request);
	if(!newName.equals(name) && !newName.equals(""))
		merchant.setName(newName);
	String[] cc = request.getParameterValues("creditCardsAccepted");
	if(cc != null)
	{
		int[] ccint = new int[cc.length];
		for(int x = 0;x < cc.length;ccint[x] = Integer.parseInt(cc[x++]));
		merchant.setCreditCards(ccint);
	}
	else
		merchant.setCreditCards(new int[]{0});
	admin.updateMerchant(merchant);
	if(families != null)
	{
		for(int x = 0;x < families.length;x++)
			admin.assignFamilyToMerchant(families[x],merchant.getName());
	}			
	response.sendRedirect("merchants.jsp");
%>