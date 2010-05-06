<%@ include file="include.txt"%>
<%
	DatabaseApplicationController topController = (DatabaseApplicationController)application.getAttribute("lazerweb.controller");

synchronized(syncObj)
{
	Properties tables = new Properties(),props = new Properties();;
	try{
		tables.load(new FileInputStream(controller.getConfigValue("baseDir") + "/" + controller.getConfigValue("config_directory")+"tables.cfg"));
	}catch(IOException e){response.sendRedirect("bad_file.jsp?file="+controller.getConfigValue("config_directory")+"tables.cfg");}
	ProductFamily family = admin.getProductFamily(request.getParameter("tableName"));
	try{
		props.load(new FileInputStream(topController.getConfigValue("baseDir")+tables.get(family.getTableName())));
	}catch(IOException e){response.sendRedirect("bad_file.jsp?file="+topController.getConfigValue("fileRootDir")+tables.get(family.getTableName()));}
	ProductField[] fields = family.getFields();
	String newName;
	for(int i=0; i<fields.length; i++){
		newName = request.getParameter("fieldName"+i);
		Iterator it = props.keySet().iterator();
		while(it.hasNext())
		{
			String name = (String)it.next();
			if(props.getProperty(name).equals(fields[i].getName()))
			{
				props.setProperty(name,newName);
				break;
			}
		}
		fields[i].setName(newName);
		fields[i].setSearchOrder(new Integer(request.getParameter("searchOrder"+i)).intValue());
		fields[i].setDisplayOrder(new Integer(request.getParameter("displayOrder"+i)).intValue());
		if(request.getParameter("protected"+i) != null)
		{
			if(fields[i].getType() > 0)
				fields[i].setType(fields[i].getType() * (-1));
		}
		else if(fields[i].getType() < 0)
			fields[i].setType(fields[i].getType() * (-1));
		if(request.getParameter("delete"+i) != null)
			admin.deleteField(family.getTableName(),fields[i].getName());
		else
			admin.updateProductField(family, fields[i]);
	}
	try {
		props.save(new FileOutputStream(topController.getConfigValue("fileRootDir")+tables.get(family.getTableName())),family.getDescriptiveName()+" config file");
	}catch(IOException e){response.sendRedirect("bad_file.jsp");}
	props = null;
	response.sendRedirect("fields.jsp");
}
admin.reInit();
%>

<%! Object syncObj = new Object(); %>
