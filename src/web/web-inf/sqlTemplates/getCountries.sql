#if(!$name && !$code) select country_id, name country_name,country_code from countries where valid_country=1
#elseif($name) select country_id,name country_name,country_code from countries where name='$sql.escapeSql($name)'
#else select country_id,name country_name,country_code from countries where code = '$sql.escapeSql($code)'
#end
order by name