#if(!$name && !$code && !$id)select state_id,name state_name,abbr state_code from states where valid_state=1
#elseif($name) select state_id,name state_name,abbr state_code from states where name='$sql.escapeSql($name)'
#elseif($code) select state_id,name state_name,abbr state_code from states where abbr='$sql.escapeSql($code)'
#elseif($id) select state_id,name state_name,abbr state_code from states where state_id=$id
#end
order by name