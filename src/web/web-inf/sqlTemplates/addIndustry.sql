insert into industries(industry_id,name)
values($obj.id,'$sql.escapeSql($obj.name)')