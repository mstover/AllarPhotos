<%@ include file="include.txt"%>
<% try{ 
	WebBean.setValues(commerce,request);
	CreditCard cc = new CreditCard();
	WebBean.setValues(cc,request);
	boolean valid = true;
	int month=0,year=0;
	String[] date = Functions.split(request.getParameter("expirationDate"),"/");
	if(date.length != 2)
		valid = false;
	else
	{
		try{
			month = Integer.parseInt(date[0]);
			year = Integer.parseInt(date[1]);
			year = Functions.y2kConvert(year);
		}catch(NumberFormatException e){valid = false;}
	}
	if(!valid){
		%><jsp:forward page="bad_cc.jsp"/><%
	}
	GregorianCalendar cal = new GregorianCalendar(year,month-1,28);
	cc.setExpDate(cal);
	commerce.setCreditCard(cc);
%>
	<jsp:forward page="order.jsp?submit=Yes"/>


<%
	}catch(Exception e){e.printStackTrace();}
%>