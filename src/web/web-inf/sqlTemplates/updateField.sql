update ${obj.family}fields
set name = '$sql.escapeSql($obj.name)',
    search_order=$obj.searchOrder,
    display_order = $obj.displayOrder,
    field_type=$obj.type
where field_id=$obj.fieldID