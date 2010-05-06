<%@ include file="include.txt"%>
<%
String[] data;
	String cr = System.getProperty("file.separator");
	String family = request.getParameter("family");
	if(null!=request.getParameter("since_date")&&!request.getParameter("since_date").equals("")){
		data = admin.exportProductDataByDate(family, Functions.getDate(request.getParameter("since_date")));
	}else{
		data = admin.exportProductData(family);
	}
	writeFile(data,controller.getConfigValue("baseDir")+family+".txt");
	response.sendRedirect("libraries.jsp?page=download_file&download="+family);
%>

<%!
	private synchronized void writeFile(String[] data,String fileName)
	{
		Filer.writeFile(fileName,data);
	}
%>