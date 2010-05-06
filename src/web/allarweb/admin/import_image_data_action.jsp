<%@ include file="include.txt"%>
<%
	DatabaseApplicationController topController = (DatabaseApplicationController)application.getAttribute("lazerweb.controller");
	String cr = System.getProperty("file.separator");
	String reqTable = request.getParameter("family");
	ImageDataCollector dataCollector = new ImageDataCollector();
	dataCollector.setController(controller);
	Properties tables = new Properties(),props;
	try{
		Functions.javaLog("Trying to load the tables");
		tables.load(new FileInputStream(controller.getConfigValue("baseDir") + "/" + controller.getConfigValue("config_directory")+"tables.cfg"));
	}catch(IOException e){
		Functions.javaLog("Got an exception");
		response.sendRedirect("bad_file.jsp?file="+controller.getConfigValue("config_directory")+"tables.cfg");
	}
	Functions.javaLog(controller.getConfigValue("config_directory"));
	Iterator it = tables.keySet().iterator();
	String data;
	String nl = System.getProperty("line.separator");
	byte[] bytes;
	ByteArrayInputStream inputData;
	
	
	StringBuffer mess = new StringBuffer("User Importing: "+admin.getUser().getUsername()+nl+nl+"Libraries imported:"+nl+nl);
	while(it.hasNext()) {
		Functions.javaLog("Does it get to the iterator?");
		String tablename = (String)it.next();
		if(reqTable != null && (reqTable.equals("all tables") || reqTable.equals(tablename))) {
			props = new Properties();
			try{
				props.load(new FileInputStream(topController.getConfigValue("baseDir")+tables.get(tablename)));
			}catch(IOException e){response.sendRedirect("bad_file.jsp?file="+topController.getConfigValue("fileRootDir")+tables.get(tablename));}
			data = Functions.unsplit(dataCollector.getImageData(tablename,props),nl);
			bytes = data.getBytes();
			
	//Temporarily disable auto-import to investigate cause of bad data import. -ks 10/25
			//inputData = new ByteArrayInputStream(bytes);
			//admin.importProductData(inputData);
			
			Functions.javaLog(data); %>
		<p><%=tablename %>: lines added=<%=(Functions.split(data,nl)).length-5%> <%
		
			mess.append(tablename +": lines added="+((Functions.split(data,nl)).length-5)+nl);
		}
	}
	Functions.email("mail.lazerinc-image.com","webmaster@lazerinc.com","ksmoker@lazerinc.com,tomc@lazerinc.com","LIBRARY IMPORT INFO",mess.toString());
	//response.sendRedirect("libraries.jsp");
%>
