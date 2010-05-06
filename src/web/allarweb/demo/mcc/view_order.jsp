<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="action_get_order_details"/>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	<DIV ALIGN="center"><H2>Order from $!order.infoMap.user:</H2>
    <form method="POST" action="view_orders.jsp?order_status=$order.status">
    
    	#if("$order.status" == 'awaiting_approval')
    	<h3>Order is Awaiting Approval</h3>
    		<input type="hidden" name="actiontt" value="action_approve_order">
    	#end
    	#if("$order.status" == 'awaiting_confirmation')
    	<h3>Order is Awaiting Confirmation</h3>
    		<input type="hidden" name="actiontt" value="action_confirm_order">
    	#end
    	#if("$order.status" == 'awaiting_fulfillment')
    	<h3>Order is Awaiting Fulfillment</h3>
    		#if($canFulfill == true)<input type="hidden" name="actiontt" value="action_mark_order_fulfilled">#end
    	#end
    	
    </DIV>
    
    <h3>Order Details</h3>
    
    
    <table cellpadding="0" cellspacing = "0" border="0">
    <tr><td><b>Order No:</b></td><td><div style="margin-left:10px;">$!order.orderNo</div></td></tr>
    <tr><td><b>Merchant:</b></td><td><div style="margin-left:10px;">$!order.merchant.name</div></td></tr>
    <tr><td><b>Product Library:</b></td><td><div style="margin-left:10px;">$!order.infoMap.family</div></td></tr>
    <tr><td><b>Status:</b></td><td><div style="margin-left:10px;">$!order.status</div></td></tr>
    <tr><td><b>User:</b></td><td><div style="margin-left:10px;">$!order.user.lastName, $!order.user.firstName</div></td></tr>
	<tr><td><b>Ship To:</b><td><div style="margin-left:10px;">ATTN: $!order.shippingAddress.attn<br>
					PHONE: $!order.shippingAddress.phone<br>
					$!order.shippingAddress.address1<br>
					#if($order.shippingAddress.address2 && $order.shippingAddress.address2.length() > 0)
						$!order.shippingAddress.address2<br>
					#end
					$!order.shippingAddress.city, $!order.shippingAddress.state.code $!order.shippingAddress.zip<br>
					$!order.shippingAddress.country</div></td></tr>
    #foreach($info in $order.infoSet)
    	
    		#if($info.key != 'family' && $info.key != 'Order.class' && $info.key != 'Total Cost')
    			<tr><td><b>$!info.key:</b></td><td><div style="margin-left:10px;">$!info.value</div></td></tr>
    		#end
    	
    #end
    </table>
    <p>
    
    
    #if("$order.status" == 'awaiting_confirmation')
    <h3>Do you want this partially approved order fulfilled, or would you like to cancel?</h3>
    <input class='btnBkd' type="submit" name="confirm_choice" value="Fulfill Partial Order">
    <input class='btnBkd' type="submit" name="confirm_choice" value="Cancel Partial Order">
    #end
    
    #if("$order.status" == 'awaiting_fulfillment' && $canFulfill == true)
    <input class='btnBkd' type="submit" name="confirm_choice" value="Mark As Fulfilled">
    #end
    
    <p>
    <input type="hidden" name="actionss" value="action_get_order_details">
    <input type="hidden" name="request_order_number" value="$order.orderNo">
    <h3>Files Ordered: $order.status</h3>
	#displayOrderItems($order.items "$order.status")
    	#if("$order.status" == 'awaiting_approval')<b>Comment (Please provide an explanation for any rejected files):</b> <br>
    <textarea cols="45" rows="5" name="approval_comment"></textarea>
    <p>
    <div style="margin-right:10px;margin-left:10px;float:left;"><input class='btnBkd' type="submit" name="approve_choice" value="Approve Entire Order"></div>
    <div style="margin-right:10px;margin-left:10px;float:left;"><input class='btnBkd' type="submit" name="approve_choice" value="Approve Partial Order"></div>
    <div style="margin-right:10px;margin-left:10px;float:left;"><input class='btnBkd' type="submit" name="approve_choice" value="Reject Entire Order"></div>
    <p style="clear:both;"/>#end
    </form>
#end
	
#set($helpText="verify")
#page("Order $order.orderNo" "mcc")

</vel:velocity>


