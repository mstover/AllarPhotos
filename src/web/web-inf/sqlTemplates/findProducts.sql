#if($primaryValue && !$path)
	select * from findProductsByPrimary('$table','$sql.escapeSql($sql.escapeSql($primaryValue))')
#elseif($name && $path)
	select * from findProductsByPrimaryPath('$table','$sql.escapeSql($sql.escapeSql($name))','$sql.escapeSql($sql.escapeSql($path))')
#elseif($name && !$path)
	select * from findProductsByPrimaryPath('$table','$sql.escapeSql($sql.escapeSql($name))',null)
#elseif($id)
	select * from findProductsById('$table',$id)
#elseif($numberValue)
	select * from findProductsByNumber('$table','$sql.escapeSql($sql.escapeSql($category))',$numberValue)
#elseif($dateValue)
	select * from findProductsByDate('$table','$sql.calToString($dateValue,"yyyy-MMM-dd")')
#elseif($dateValueString)
	select * from findProductsByDate('$table','$dateValueString')
#elseif($onDateValue)
	select * from findProductsPostedOn('$table','$sql.calToString($onDateValue,"yyyy-MMM-dd")')
#elseif($onDateValueString)
	select * from findProductsPostedOn('$table','$onDateValueString')
#elseif($modDateValue)
	select * from findProductsByModDate('$table','$sql.calToString($modDateValue,"yyyy-MMM-dd")')
#elseif($onModDateValue)
	select * from findProductsModifiedOn('$table','$sql.calToString($onModDateValue,"yyyy-MMM-dd")')
#elseif($modDateValueString)
	select * from findProductsByModDate('$table','$modDateValueString')
#elseif($onModDateValueString)
	select * from findProductsModifiedOn('$table','$onModDateValueString')
#elseif($searchTerm)
	select * from searchProducts('$table','%$sql.escapeSql($sql.escapeSql($searchTerm))%')
#elseif($exactSearchTerm)
	select * from searchProductsWithExact('$table','$sql.escapeSql($sql.escapeSql($exactSearchTerm))')
#elseif(!$category)
	select * from findAllProducts('$table')
#elseif($like_search)
	select * from searchProductsWithCategory('$table','$sql.escapeSql($sql.escapeSql($category))','$sql.escapeSql($sql.escapeSql($value))')
#else
	select * from findProducts('$table','$sql.escapeSql($sql.escapeSql($category))','$sql.escapeSql($sql.escapeSql($value))')
#end