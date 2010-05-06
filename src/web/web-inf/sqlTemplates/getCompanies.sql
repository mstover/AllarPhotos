#if(!$name)select company_id,name company_name,industry_id from companies
#else select company_id,name company_name,industry_id from companies where name = '$sql.escapeSql($name)'
#end