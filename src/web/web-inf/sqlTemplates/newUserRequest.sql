insert into user_requests(username,first_name,last_name,email,company,phone,reason,address1,address2,city,state,country,zip,resource_id)
values('$!sql.escapeSql($request.username)','$!sql.escapeSql($request.firstName)',
'$!sql.escapeSql($request.lastName)','$!sql.escapeSql($request.email)','$!sql.escapeSql($request.company)',
'$!sql.escapeSql($request.phone)','$!sql.escapeSql($request.reason)','$!sql.escapeSql($request.address1)',
'$!sql.escapeSql($request.address2)','$!sql.escapeSql($request.city)','$!sql.escapeSql($request.state)',
'$!sql.escapeSql($request.country)','$!sql.escapeSql($request.zip)',$request.requestedResource.id)