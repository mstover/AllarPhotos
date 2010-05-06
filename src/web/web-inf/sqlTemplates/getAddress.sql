select address_id,
       attn,
       company_id,
       address_1,
       address_2,
       city_id,
       state_id,
       country_id,
       zip,
       phone,
       fax,
       in_use
     
from address
             
where #if($address) coalesce(attn,'')='$sql.escapeSql($address.attn)' and
		companies.name='$sql.escapeSql($address.company.name)' and
		cities.name='$sql.escapeSql($address.city.name)' and
		countries.name='$sql.escapeSql($address.country.name)' and
		coalesce(address_1,'')='$sql.escapeSql($address.address1)' and
		coalesce(address_2,'')='$sql.escapeSql($address.address2)' and
		coalesce(phone,'')='$sql.escapeSql($address.phone)' and
		coalesce(fax,'')='$sql.escapeSql($address.fax)' and
		coalesce(zip,'')='$sql.escapeSql($address.zip)'
      #elseif($address1) coalesce(address_1,'')='$sql.escapeSql($address1)'		
      #elseif($id) address_id=$id
      #else 1=0#end