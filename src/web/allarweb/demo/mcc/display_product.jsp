<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="action_get_product" order="1"/>
<act:request action="action_set_previous_product" order="2"/>
<act:request action="action_set_next_product" order="3"/>
<act:executeActions/>


<vel:velocity>
#macro(mainContent)
	<h3 style='text-align: center;'>$product.displayName</h3>
	<div>
  <div class='sideSubBox' style='margin: 0;'>
	#moveImage($product $isAdmin $isSu $params.getParameter("listName"))
  <UL>
  #foreach($field in $productBean.productFamily.fields)
	 #if($field.displayOrder % 1000 >= 100 && $product.getValue($field.name))
			<LI><B>$field.name:</B>&nbsp;
				<br>$!productBean.getFieldValue($field)
	 #end
  #end
	  #showProductDates($productBean)
  </UL>
	<!-- GO BACK BUTTON TO GO HERE -->
	<div style='text-align: center;'><form action=#if($params.getParameter("listName") == "cartList")"shopping_cart.jsp"#else"browse_products.jsp"#end>
		<input class='btnBkd' type="submit" value="Go Back">
	  </form>
	</div>
  </div>
  <div>
	<!-- IMAGE OF ASSET -->
	<div class='center'>#displayIMG($product 400)<br>
	</div><br><br>
	<div id='displayProdButtons' style='text-align: right; width: 400px;'>
	  <div class='floatLft'>
	  #if($previousProductBean.product)
		  <form action='display_product.jsp' method='post'>
			<input type="hidden" name="request_product_id" value='$previousProductBean.id'>
			<input type='hidden' name='request_product_family' value='$previousProductBean.productFamilyName'>
			<input type="hidden" name="listName" value="$params.getParameter("listName")">
			<input class='btnBkd' type='submit' name='submit' value='Previous'>
		  </form>
	  #else
			<input type='submit' name='submit' value='Previous' disabled='true'>
	  #end
	  </div>
	  <div class='floatRght'>
	  #if($nextProductBean.product)
		<form action='display_product.jsp' method='post'>
			<input type="hidden" name="request_product_id" value='$nextProductBean.id'>
			<input type='hidden' name='request_product_family' value='$nextProductBean.productFamilyName'>
			<input type="hidden" name="listName" value="$params.getParameter("listName")">
			<input class='btnBkd' type='submit' name='submit' value='Next'>
		  </form>
	  #else
			<input type='submit' name='submit' value='Next' disabled='true'>
	  #end
	  </div>
	  <div style='text-align: center;'><FORM ACTION="browse_products.jsp" METHOD="post"> 
		<input type="hidden" name="request_product_id" value='$product.id'>
		<input type="hidden" name="request_product_family" value='$product.productFamilyName'>
		#if($productBean.inCart)
				<input type="hidden" name="action" value="action_remove_from_cart">
				<input type="hidden" name="removeFromCart.remove" 
						value='$product.id|$product.productFamilyName'>
				<input class='btnBkd' type="submit" align="top" src="buttonImages/removeFromCartButton.gif" value="Remove from Cart"
					border="0" name="submit">
		#else
				<input type="hidden" name="action" value="action_add_to_cart">
				<input class='btnBkd' type="submit" align="top" src="buttonImages/addToCartButton.gif"
						name="submit" value="Add to Cart" border=0>
		#end
	  </form>
	  </div>
	</div><!-- END OF DISPLAY PRODUCT BUTTONS -->
  </div>
</div>
#end

	#set($isAdmin = $user.permissions.getPermission("ia_bali",3,"admin"))
	#set($isSu = $user.permissions.getPermission("all",6,"admin"))
#set($product = $productBean.product)
#set($helpText="detail")
#page($productBean.displayName "mcc")
</vel:velocity>

