<%@ include file="include.txt"%>
<%
	String file = request.getParameter("download")+".txt";
	String path = controller.getConfigValue("baseDir")+file;
 %>
 Download
<a href="/servlet/fetchpix?file=<%=URLEncoder.encode(path)%>&mimetype=text/plain"><%=file%></a>