#if(!$fromDate)
	#if(!$family)
		select * from findOrderItems('$category','$value',#if($date)'$sql.calToString($date,"yyyy-MM-dd")'#else '1970-01-01'#end)
	#else
		select * from findLibraryOrderItems('$family.descriptiveName','$category','$value',#if($date)'$sql.calToString($date,"yyyy-MM-dd")'#else '1970-01-01'#end)
	#end
#else
	select * from findOrderItemsDateRange('$category','$value',#if($fromDate)'$sql.calToString($fromDate,"yyyy-MM-dd")'#else '1970-01-01'#end,#if($toDate)'$sql.calToString($toDate,"yyyy-MM-dd")'#else '1970-01-01'#end)
#end