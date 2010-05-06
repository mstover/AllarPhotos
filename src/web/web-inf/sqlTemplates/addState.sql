insert into states(state_id,name,abbr,valid_state)
values($obj.id,'$sql.escapeSql($obj.name)','$sql.escapeSql($obj.code)',1)