<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="action_get_product" order="1"/>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	<h3 style='text-align: center;'>$product.displayName</h3>
<div>
  <div class='center' style="float:left;display:inline-block;;">
	#displayIMG($product 200) <br />
	</div>
  <div class='sideSubBox' style='width: 300px;'>
	#nonEditFields($product $isAdmin $isSu)
	<!-- GO BACK BUTTON TO GO HERE -->
	<div style='text-align: center;'><form action="display_product.jsp">
		<input type="hidden" name="request_product_id" value="$product.id">
		<input type="hidden" name="request_product_family" value="$product.productFamilyName">
		<input type="hidden" name="listName" value="$params.getParameter("listName")">
		<input class='btnBkd' type="submit" value="Go Back">
	  </form>
	</div>
  </div>
  <div style='clear: both;'>
  	<h3 style='border-width: 0 0 1px 0; border-color: #CCC; border-style: solid;'>Editable Fields</h3>
  	#editFields($product $isAdmin $isSu)
  </div>
</div>
#end
	#set($isAdmin = $user.permissions.getPermission("ia_bali",3,"admin"))
	#set($isSu = $user.permissions.getPermission("all",6,"admin"))
	#set($product = $productBean.product)
	#set($helpText="detail")
	#page($productBean.displayName "mcc")
</vel:velocity>

