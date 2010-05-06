select merchants.merchant_id #if(!$productFamily),
       merchants.name merchant_name,
       merchants.ordering_email,
       merchants.phone,
       merchants.fax,
       merchants.zip,
       merchants.address1,
       merchants.address2,
       merchants.city_id,
       merchants.state_id,
       merchants.country_id,
       merchants.fulfillment_email,
       merchants.order_processing,
       merchants.sales_tax  #end
from merchants
#if($productFamily) 
	inner join merchant_product_tables on merchants.merchant_id=merchant_product_tables.merchant_id
	where product_table_id=$productFamily.id
#end