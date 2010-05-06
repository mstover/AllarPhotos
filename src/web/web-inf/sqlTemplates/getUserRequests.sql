#if($user)select * from user_requests where exists(select security.resource_id from security
    inner join rights on security.right_id=rights.right_id
    inner join user_group on security.group_id=user_group.group_id
    inner join resources on security.resource_id=resources.resource_id
    where user_group.user_id=$user.userID
    and rights.name='admin'
    and (security.resource_id=user_requests.resource_id or resources.name='all'))
 #elseif($id)
    select * from user_requests where request_id=$id
   #end