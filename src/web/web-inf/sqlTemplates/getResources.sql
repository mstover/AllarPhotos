#if(!$name)select resource_id,name resource_name,resource_type from resources where resource_type <> 2 order by name
#else select resource_id,name resource_name,resource_type from resources where name='$sql.escapeSql($name)'
and resource_type=$type order by name#end