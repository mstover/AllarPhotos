<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="action_get_orders"/>
<act:executeActions/>

<vel:velocity>
	
#macro(selectedStyle $sDate)
	#if($dateTool.formatDate($selectedDate).equals($dateTool.formatDate($sDate))) class="selected" #end
#end
	
#macro(mainContent)
	<DIV ALIGN="center"><H2>Orders</H2></DIV>
    <div class='center small2'>|&nbsp;
	<a href="?order_status=awaiting_approval">Awaiting Approval</a> |
	<a href="?order_status=awaiting_fulfillment">Awaiting Fulfillment</a> |
	<a href="?order_status=fulfilled&order_date=$dateTool.formatDate($dateTool.lastWeek())">Fulfilled</a> |
	<a href="?order_status=awaiting_confirmation">Awaiting Confirmation</a> |
	<a href="?order_status=rejected&order_date=$dateTool.formatDate($dateTool.lastWeek())">Rejected</a> |
	<a href="?order_status=cancelled&order_date=$dateTool.formatDate($dateTool.lastWeek())">Cancelled</a>
	&nbsp;|
</div>
#viewOrderDateRanges()
    
    <p class="categoryHeader">$format.capitalizeAll($!status) Orders:</p>
    
    <ul>
		#foreach($order in $orders)
        	#if(!$familyPrev || $order.infoMap.family != $familyPrev)
        		<h2 class='orderViewHeader'>$!order.infoMap.family</h2>
        		#set($familyPrev="$order.infoMap.family")	
        	#end
        	<li><a href="view_order.jsp?request_order_number=$order.orderNo">$order.orderNo</a> 
        		($!order.user.lastName, $!order.user.firstName) 
        		<!-- - $format.formatCalendar($order.dateTime,"MM/dd/yyyy") --> </li>
		#end
    </ul>
#end

	#set($helpText="verify")
	#page("View Orders" "mcc")
</vel:velocity>