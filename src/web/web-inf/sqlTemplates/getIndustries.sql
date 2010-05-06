#if(!$name) select industry_id,name industry_name from industries
#else select industry_id,name industry_name from industries where name='$sql.escapeSql($name)'
#end