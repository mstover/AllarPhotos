ds = new
		org.firebirdsql.jdbc.FBWrappingDataSource();
		ds.setDatabaseName("da_vinci/3050:/opt/firebird/lazerweb.fdb");
		ds.setMinSize(1);
		ds.setMaxSize(1);
		ds.setBlockingTimeout(100);
		ds.setIdleTimeout(1000);
		ds.setPooling(true);
		ds.setUser("damuser");
		ds.setPassword("r!VAld0");
ds