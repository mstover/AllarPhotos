ds = new
		org.firebirdsql.jdbc.FBWrappingDataSource();
		ds.setDatabase("da_vinci/3050:/opt/firebird/lazerweb.fdb");
		ds.setMinSize(1);
		ds.setMaxSize(5);
		ds.setIdleTimeoutMinutes(5);
		ds.setPooling(true);
		ds.setUserName("damuser");
		ds.setPassword("r!VAld0");
ds