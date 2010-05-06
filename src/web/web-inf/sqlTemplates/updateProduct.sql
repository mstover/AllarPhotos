update ${product.productFamilyName}
set primary_label='$sql.escapeSql($product.primary)',
    path_id=$product.path.id,
    date_modified='$sql.calToString($product.dateModified,"dd-MMM-yyyy")',
    date_created='$sql.calToString($product.dateCreated,"dd-MMM-yyyy")',
    is_active=1
where product_id=$product.id