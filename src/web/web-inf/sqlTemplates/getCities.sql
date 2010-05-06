#if(!$name)select city_id,name city_name from cities order by name
#else select city_id,name city_name from cities where name='$sql.escapeSql($name)' order by name
#end