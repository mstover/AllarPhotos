<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="action_change_number_shown"/>
<act:request action="action_browse_set"/>
<act:executeActions/>

<vel:velocity>
	
#macro(pageJavascript)
	<script type="text/javascript">
<!--
	function rewriteDiv(divId,adding,productId,productFamily)
	{
		if(adding)
		{
			document.getElementById(divId).innerHTML='<input type="button" class="btnBkd" border="0" name="unused" value="Remove from Cart" onclick="javascript:sendRequest(\'http://$host/$contextDir/dummy.jsp?removeFromCart.remove='+productId+'|'+productFamily+'&actiond=action_remove_from_cart\');rewriteDiv(\'image_control_'+productId+'\',false,\''+productId+'\',\''+productFamily+'\');">'
		} else
		{
			document.getElementById(divId).innerHTML='<input type="button" class="btnBkd" border="0" name="unused" value="Add to Cart" onclick="javascript:sendRequest(\'http://$host/$contextDir/dummy.jsp?request_product_id='+productId+'&request_product_family='+productFamily+'&actiond=action_add_to_cart\');rewriteDiv(\'image_control_'+productId+'\',true,\''+productId+'\',\''+productFamily+'\');">'
		}
	}
	-->
	</script>
#end

#macro(mainContent)

    #set($history_index=$productsFound.historyIndex)
    #set($defaultImage="$configuration.default_image")
    #searchHistory()
    
    <p>
    <DIV ALIGN="center"><H3>Number of Matching Products: $productsFound.size</H3></DIV>
    #set($toggle = 0)
    #set($setSize = 0)
    #if($productsFound.size > $productsFound.displaySize)
            <div align="center">Please return to the <a href="catsearch.jsp">Image Search</a> page and narrow your search further</div>
    #else
        
    	#browseControls($browseProducts)
      #showProductTable($browseProducts 3 130 "browseProducts")
    #end

#end
#set($helpText="browse")
#page("Browse Images" "mcc")

</vel:velocity>
