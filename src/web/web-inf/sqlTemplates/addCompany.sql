insert into companies(company_id,name,industry_id)
values($obj.id,'$sql.escapeSql($obj.name)',$obj.industry.id)