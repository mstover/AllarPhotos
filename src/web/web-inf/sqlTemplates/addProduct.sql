insert into ${product.productFamilyName}(product_id,primary_label,path_id,date_cataloged,date_modified,inventory)
values($product.id,'$sql.escapeSql($product.primary)',$product.path.id,'$sql.calToString($product.dateCataloged,"dd-MMM-yyyy")','$sql.calToString($product.dateModified,"dd-MMM-yyyy")',1)