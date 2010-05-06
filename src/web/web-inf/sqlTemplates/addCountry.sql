insert into countries(country_id,name,country_code,valid_country)
values($obj.id,'$sql.escapeSql($obj.name)','$sql.escapeSql($obj.code)',1)