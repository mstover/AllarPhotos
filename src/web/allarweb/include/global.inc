<%@ page import="com.lazerinc.servlet.*,com.lazerinc.client.beans.*,com.lazerinc.dbtools.*,com.lazerinc.beans.*,com.lazerinc.utils.*,org.apache.log4j.Logger,java.net.*,com.lazerinc.ecommerce.*,java.util.*" %>
<%@ taglib prefix="act" uri="/WEB-INF/webacttags.tld" %>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel" %>
<%@taglib uri="/WEB-INF/laz.tld" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%!
	Logger log = Logger.getLogger("com.lazerinc.lazerweb.JSP");	
%>
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at theproxy server
%>

<jsp:useBean id="configuration" scope="session" class="java.util.Properties"/>