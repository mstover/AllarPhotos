insert into $product.productFamilyName(product_id,primary_label,path_id,inventory,date_cataloged,date_created)
values($product.id,'$sql.escapeSql($product.primary)',$product.path.id,1,'$sql.calToString($product.dateCataloged,"dd-MMM-yyyy")','$sql.calToString($product.dateCreated,"dd-MMM-yyyy")')