select  product_table_id family_id #if(!$descriptiveName && !$tableName),
	table_name,
	descriptive_name,
	description,
	primary_label,
	product_type,
	order_model,
	customer_name,
	remote_managed #end
		
from product_tables
#if($descriptiveName) where descriptive_name='$sql.escapeSql($descriptiveName)'
#elseif($tableName) where table_name='$sql.escapeSql($tableName)' #end