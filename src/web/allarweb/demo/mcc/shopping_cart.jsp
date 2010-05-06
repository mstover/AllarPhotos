<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="action_set_cart_set"/>
<act:executeActions/>

<vel:velocity>
#macro(pageJavascript)
	<script language="JavaScript">
    <!--
    function doPost(action)
    	{
    		document.cartForm.action = action;
    		document.cartForm.submit();
    	}
    	function doChangeCartSet(action,set)
    	{
    		document.cartForm.request_browse_set.value = set;
    		doPost(action);
    	}	
    // -->		
    </script>
#end

#macro(previousButton $pagedList)
  <input onclick="javascript:doChangeCartSet('shopping_cart.jsp','$math.sub($pagedList.pageNo,1)');" type="button" name="unused" value="Previous" align="absmiddle" class="btnBkd">
#end

#macro(nextButton $pagedList)
	<input onclick="javascript:doChangeCartSet('shopping_cart.jsp','$math.add($pagedList.pageNo,1)');" type="button" name="unused" value="Next" align="absmiddle" class="btnBkd">
#end

#macro(browsedProductControls $cart)
	#set($orderOption=false)
	#set($jpgDownload=true)
	#set($originalsDownload=true)
	#set($epsDownload=false)
	#set($pdfDownload=false)
	#set($pngDownload=false)
	#if(($cart.product.productFamilyName == 'hb_hos' || $cart.product.productFamilyName == 'hb_leggs') && ($cart.product.getValue("Image Type") == 'Packaging' || $cart.product.getValue("Image Type") == 'Photography' || $cart.product.getValue("Image Type") == 'Logos' || $cart.product.getValue("Image Type") == 'Logo'))
		#set($pngDownload=true)
	#end
	#if($cart.product.productFamilyName == 'hb_logos') 
		#set($epsDownload=true)
	#end


	<div class="small2" style="text-align:left;margin-left:5px;">
       #if($jpgDownload)
         <input type="Checkbox" name="addToOrder.download_jpg" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.get("download").contains("jpg")) CHECKED#end>Download JPG <BR>
       #end
       #if($originalsDownload)
         <input type="Checkbox" name="addToOrder.download_originals" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.get("download").contains("originals")) CHECKED#end>Download Original <BR>
       #end       
       #if($pdfDownload)
         <input type="Checkbox" name="addToOrder.download_pdf" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.get("download").contains("pdf")) CHECKED#end>Download PDF <BR>
       #end
       #if($epsDownload)
         <input type="Checkbox" name="addToOrder.download_eps" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.get("download").contains("eps")) CHECKED#end>Download EPS <BR>
       #end
       #if($pngDownload)
         <input type="Checkbox" name="addToOrder.download_png" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.get("download").contains("png")) CHECKED#end>Download PNG (PowerPoint) <BR>
       #end
       #if($orderOption)
         <input type="Checkbox" name="addToOrder.order" value='$cart.product.id|$cart.product.productFamilyName'#if($cart.instructions.containsKey("order")) CHECKED#end>Order Original <BR>
       #end
       <input type="Checkbox" name="removeFromCart.remove" value='$cart.product.id|$cart.product.productFamilyName'>Remove From Cart
    </div>
#end

#macro(mainContent)
	<DIV ALIGN="center">
<input type="button" name="unused" onclick="javascript:document.location='?action=clear_cart'" class="btnBkd" value="Clear Entire Shopping Cart"> &nbsp; | &nbsp; 
<input type="button" name="unused" onclick="javascript:document.location='?action=save_cart'" class="btnBkd" value="Save Shopping Cart">
<H3>Number of Products in your Shopping Cart: $cart.size</H3>

    #if($cart.size == 0)
    		<DIV ALIGN="center"><h3>Your Shopping Cart is empty.</h3>
    		<p>Please <a href="catsearch.jsp">resume your search</a>.
    		</DIV>
    #else
		<form name="cartForm" method="POST">
    		<input class='btnBkd' type="button" name="go" value="Proceed" onClick="doPost('verify.jsp')">
    		<input type="hidden" name="actiona" value="action_order_items">
    			<input type="hidden" name="actionb" value="action_remove_from_cart">
    		<input type="hidden" name="request_browse_set" value='$pagedList.pageNo'>
			<p>
    	#browseControls($cartList)    	
    	#showProductTable($cartList 3 130 "cartList")
    	</form>
    	<div style='text-align: left; padding-top: 10px;'><hr>
      <STRONG>NOTE:</STRONG> To expedite your order, you may request a digital transfer of files. 
      Please make this request in the &quot;Special Instructions&quot; area 
      when verifying your order.
    </div>
    #end

#end
#set($helpText="cart")
#page("Cart" "mcc")
</vel:velocity>
