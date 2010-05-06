select  security.resource_id,
        rights.name right_name,
        'true' right_value
      
from    security inner join
        rights on rights.right_id=security.right_id inner join
        resources on security.resource_id=resources.resource_id
        
where   resources.resource_type <> 2 and
        #if($group) group_id=$group.id
	    #elseif($resource) resources.resource_id=$resource.id
	    #elseif($user) group_id in (#idList($user.groups))#end