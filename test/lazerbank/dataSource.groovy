x = new Properties()
x.putAll([maxActive:"1",
                   maxIdle:"1",
                   username:"@DB_USERNAME@",
                   password:"@DB_PASSWORD@",
                   driverClassName:"org.firebirdsql.jdbc.FBDriver",
                   url:"jdbc:firebirdsql://@DB_URL@"])

org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(x)