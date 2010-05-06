#if(!$name)select referrer_id,name referrer_name from referrers
#else select referrer_id,name referrer_name from referrers where name='$sql.escapeSql($name)'
#end