insert into ${obj.family}fields(field_id,name,display_order,search_order,field_type)
values($obj.fieldID,'$!sql.escapeSql($obj.name)',$obj.displayOrder,$obj.searchOrder,$obj.type)