#if($group)
	select * from findUsersByGroup($group.id)
#elseif($NULL)
	select * from searchUsers('$NULL')
#elseif($email)
	select user_id from users where email='$!sql.escapeSql($email)'
#elseif(!$id && !$username)
	select * from users where user_id=0
#else
	select * from findUsers(#if($id)$!id#else null#end,#if($username)'$!sql.escapeSql($username)'#else null#end)
#end