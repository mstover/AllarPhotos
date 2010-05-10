<%@ page import="com.allarphoto.servlet.*,com.allarphoto.client.beans.*,com.allarphoto.dbtools.*,com.allarphoto.beans.*,com.allarphoto.utils.*,org.apache.log4j.Logger,java.net.*,com.allarphoto.ecommerce.*,java.util.*" %>
<%@ taglib prefix="act" uri="/WEB-INF/webacttags.tld" %>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel" %>
<%@taglib uri="/WEB-INF/laz.tld" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%!
	Logger log = Logger.getLogger("com.allarphoto.lazerweb.JSP");	
%>
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at theproxy server
%>

<jsp:useBean id="configuration" scope="session" class="java.util.Properties"/>