<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="action_get_cost_report"/>
<act:request action="action_check_cart_empty"/>
<act:onError error="EmptyCartError" message="">shopping_cart.jsp</act:onError>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	<c:choose>
	#if($params.getParameter("success") == "false")
		<div align="center" class="eyecatching"><STRONG>There was an error with the 
			information you supplied with your order.</STRONG></div>
		<div align="left"><p>Please re-enter ...
			<ul>
            	<li>the &quot;Recipient/Account Name&quot; for your order
                <li>any &quot;Special Instructions&quot; you might have - limit to 250 characters. 
			</ul>
			<br>
			and resubmit the form.</div>
		<div>&nbsp;</div>
	#else
            <h3 align="center">Please review usage information below</h3>
	#end
	
	<div align="center">
		#usageAgreement()
	</div>
	
    <form action="order.jsp" method="POST" onsubmit='this.request_instructions.optional=true;return verify(this);'>
    <table border="0" cellspacing="0" cellpadding="10" width="490"><tr valign="TOP">
    #if($cart.orderRequest)
    	<td><div class='small1'>You will not be able to proceed unless you agree to 
        all terms listed. Your order is also subject to approval by the Intimate 
        Apparel Online Library Administrator.</div>
    	#displayOrderItems($cart.orderedProducts $NULL)
    
    	Recipient/Account Name: <i style='color: red; font-size: 10px;'>(required)</i><br>
    	<input type="text" name='request_account_name'>
    	<div>Special instructions (limit 250 char):</div>
    	<div style='text-align: left;'>NOTE: To expedite your order, you may request a digital transfer.</div>
            <textarea name='request_instructions' cols="30" rows="5"></textarea>
            <div>Notes on Digital Transfers:</div>
            <div style='font-size: 11px;'>- Email image requests are fulfilled with hires JPG (JPEG-9) files.</div> 
            <div style='font-size: 11px;'>- HTTP download requests will allow for larger files and better resolution.</div>
    	<div align="center"><a href="shopping_cart.jsp">Edit Order</a></div></td>
    #end
    	
    #if($cart.downloadRequest)
    	<TD>You have chosen the following files for download:
    	<UL>
    	#foreach($product in $cart.downloadProducts)
    		<li>$!product.displayName</li>
    	#end
    	</UL>
    	<DIV align="center">
    	<P>Total download size: $format.formatNumber($math.div($costReport.report.downloadSize,1024),"#,000.00") KB</P>
    	</DIV>
    
    	<div align="center"><a href="shopping_cart.jsp">Edit Order</a></div>
    #end
    
    </tr></table>
    <br>
    	#if(!$cart.orderRequest && $cart.downloadRequest)
    			<input type="hidden" name="dlOnly" value="true">
    	#else
    		<input type="hidden" name="dlOnly" value="false">
    	#end
        <div align='center'><input class='btnBkd' type="Submit" name="submit" value="I Accept the Usage Rights"></div>
    </form></div>
    <div align='center'><form action='shopping_cart.jsp' method='POST'><input class='btnBkd' type="Submit" name="submit" value="Cancel Order"></form></div>
#end

#set($helpText="verify")
#page("Verify Order" "mcc")
</vel:velocity>
