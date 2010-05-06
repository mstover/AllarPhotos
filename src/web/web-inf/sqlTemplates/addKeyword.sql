insert into ${product.productFamilyName}${keyTable}(#if($keyTable == "_key")keyword_id#else description_id#end,
#if($keyTable == "_key")keyword#else description#end)
values($keyId,'$sql.escapeSql($keyword)')