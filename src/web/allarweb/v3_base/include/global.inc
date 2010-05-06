<%@ page import="com.lazerinc.servlet.*,com.lazerinc.client.beans.*,com.lazerinc.dbtools.*,
com.lazerinc.beans.*,com.lazerinc.utils.*,org.apache.log.Logger,java.net.*,com.lazerinc.ecommerce.*,java.util.*" %>

<%@taglib uri="http://www.lazerinc-image.com/" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%!
	JspBase base;
	public void init() throws ServletException
	{
		super.init();
		base = JspBase.getWebRouter("@basedir@/@app.name@/properties/lazerweb.properties","@basedir@/@app.name@/bali/properties/bali.properties");
	}
	Logger log = LoggingManager.getLoggerFor(LoggingManager.GUI);
	
	private Object get(String key)
	{
		return pageContext.getAttribute(key);
	}
%>

<c:set var="go_back" value="login.jsp" />
<c:if test="${null != sessionScope.page_return}" >
	<c:set var="go_back" value="${sessionScope.page_return}" />
</c:if>

<%
		if(null == request.getQueryString()){
			session.putValue("page_return", request.getRequestURI());
		}else{
			session.putValue("page_return",(request.getRequestURI()+"?"+request.getQueryString()));
		}
 
Map addParams = new HashMap(); %>
<jsp:useBean id="configuration" scope="session" class="com.lazerinc.client.beans.ConfigBean"/>