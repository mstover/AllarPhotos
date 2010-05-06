select #if($keyTable == "_key")keyword_id#else description_id#end id from ${product.productFamilyName}${keyTable} 
where #if($keyTable == "_key")keyword#else description#end='$sql.escapeSql($keyword)'