insert into address(address_id,company_id,attn,address_1,address_2,
city_id,state_id,country_id,phone,fax,zip,in_use) 
values($obj.id,
       $obj.company.id,
       '$sql.escapeSql($obj.attn)',
       '$sql.escapeSql($obj.address1)',
       '$sql.escapeSql($obj.address2)',
       $obj.city.id,
       $obj.state.id,
       $obj.country.id,
       '$sql.escapeSql($obj.phone)',
       '$sql.escapeSql($obj.fax)',
       '$sql.escapeSql($obj.zip)',
       '$obj.isInUse()')