#if(!$family) select * from lw_demofields where 1=0
#else 
    select field_id,
       name field_name,
       field_type,
       display_order,
       search_order,
       '$family' family
from	${family}fields
	#if($name || $type || $id) where 1>0 #end
	#if($name) and name='$sql.escapeSql($name)' #end
	#if($type) and field_type=$type #end
	#if($id) and field_id=$id #end
#end