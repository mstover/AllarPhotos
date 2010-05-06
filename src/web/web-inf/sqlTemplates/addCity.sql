insert into cities(city_id,name)
values($obj.id,'$sql.escapeSql($obj.name)')