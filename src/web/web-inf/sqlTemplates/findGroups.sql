#if($user)
	select groups.group_id,groups.name,groups.description from groups inner join user_group on groups.group_id=user_group.group_id
	     where user_id=$!user.userID
#elseif($name)
	select * from groups where name='$!sql.escapeSql($name)'
#elseif($id)
	select * from groups where group_id=$id
#else
	select * from groups
#end
order by groups.name