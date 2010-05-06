props = new java.util.Properties()
props.putAll(["username":"pv",
    "password":"password",
    "url":"jdbc:odbc:mediabankimages",
    "driverClassName":"sun.jdbc.odbc.JdbcOdbcDriver"])

org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props)