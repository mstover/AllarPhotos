#if($obj) delete from resources where resource_id=$obj.id
#else delete from resources where 1=0
#end