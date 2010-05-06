<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="action_bali_execute_order"/>
<act:onError error="InvalidOrderInfo">verify.jsp?success=false</act:onError>
<act:onError error="EmptyOrderError">verify.jsp?success=false</act:onError>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	<div align="left">

    <!-- Order Messages-->
    	$!orderResponse.message
    
    <!-- Download Listing -->
    	#downloadListing()
    
    <!-- Family Listing -->
    	#foreach($family in $orderResponse.families.entrySet())
    		<dl>
    			<DIV CLASS="eyecatching">Order from $family.key.descriptiveName</DIV>
    			#foreach($famInfo in $family.value.entrySet())
    				#if($famInfo.key != "family")
    					<dd><b>$famInfo.key:</b>&nbsp;$famInfo.value<br>
    				#end
    			#end
    		</dl>
    
    <!-- Ordered Products Listing -->
    		<dl>
    			<h4>Products Ordered:</h4>
    			#foreach($product in $orderResponse.products.entrySet())
    				#if($product.key.productFamily == $family.key)
    					<dt>$product.key.displayName<br>
    				#end
    				#foreach($prodInfo in $product.value.entrySet())
    					#if($prodInfo.key != "cost" && $prodInfo.key != "download")
    						<dd><b>$prodInfo.key:</b>&nbsp;$prodInfo.value<br>
    					#end
    				#end	
    			#end
    		</dl>
    	#end
    
    	#if(!$orderResponse.families.entrySet())
    	<!-- Order Information -->
    	<dl>
    		<h4>Additional Order Information:</h4>
    				<dd><b>Order No:</b>&nbsp;$orderResponse.orderNo<br>
    		#foreach($info in $orderResponse.orderInformation.entrySet())
    			#if($info.key != "Total Cost")
    				<dd><b>$info.key:</b>&nbsp;$info.value<br>
    			#end
    		#end
    	</dl>
    	#end
    </div>
#end
	
#set($helpText="order")
#page("Order Confirmation" "mcc")

</vel:velocity>	