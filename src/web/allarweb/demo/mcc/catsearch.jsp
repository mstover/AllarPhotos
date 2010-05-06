<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="action_change_number_shown"/>
<act:request action="action_cat_search"/>
<act:request action="check_product_found_count" order="1"/>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	#set($history_index=$productsFound.historyIndex)

	#if($productsFound.size == 0)
		<b>No products were found using this search.<b><br>
		#set($previous = $history_index-1)
		<a href="catsearch.jsp?request_history_index=$previous">Please try again</a>.
	#else
		#searchHistory()		
    		<br>
    		<DIV class='categoryheader' style='text-align: center; padding-bottom: 8px;'><b>Number of 
    			Matching Images: $productsFound.size</b></DIV>
    		  <!-- if itemsInSearch < configBrowsableNumber then -->
    		  #if($productsFound.size <= $productsFound.displaySize)
    				<div style='text-align: center; padding-bottom: 8px;'><form action="browse_products.jsp" method="GET">
							#hidden("request_browse_set" "0")
    					<input class='btnBkd' type="submit" name="submit" value="View Search Now" align="absmiddle" border="0">
    				</form></div>
    		  #end
    		<!--Summary of currently available categories-->
    		<DIV style='text-align: center;'>
    		<TABLE CELLSPACING=0 style='padding: 0; border-width: 0;'>
    		<tr>
    		  <td colspan='2' style='text-align: center; padding: 0;'>
    			<FORM ACTION="catsearch.jsp" METHOD="POST">
    			<div class='categoryheader'>Category Search</div>
    			<img src='images/spacer.gif' style='height: 1px; width: 540px;'>
    			<TABLE CELLSPACING=0 style='padding: 0; margin: 0 auto; border-width: 0;'>
    			#foreach($category in $searchCategories.searchCategories.entrySet())
					<tr><td>
    			  #if($category.key == "Image Type")	
    				<!-- Display ImageType Category Thumbs-->
					#set($defaultImgType="def_imgtypes_dig_phot.gif")
					#foreach($searchValue in $searchCategories.getCategoryValues($category.key))
						<a class='imgTypBlock' href='catsearch.jsp?request_category_prefixImage%20Type|$DB_EQ|$DB_IS|or|and=$format.urlEncode($searchValue)'>$searchValue<br><img src='../images/catthumbs/#if($imgTypeIcons.get("${searchValue}_icon"))$imgTypeIcons.get("${searchValue}_icon")#else$defaultImgType#end'></a>	  
					#end
					</td></tr>
    		      #else	
    				  <tr>
    					<td style='padding-left: 10px;'><strong>$category.key:</strong></td>
    					<td style='padding-right: 10px;'><select name='request_category_prefix${category.key}|$DB_EQ|$DB_IS|or|and' size="1">
    					<option value="choose one">choose one</option>
    					#foreach($option in $category.value.entrySet())
    						<option value="$option.key">$option.key</option>
    					#end
    					</select>
    					<input class='btnBkd' type="submit" name="submit" value="Go!" align="absmiddle" border="0">
    				  </tr>
    			   #end
    			 #end
    			</TABLE></FORM>
    		  </TD>
    		</tr>
    	
    		<tr>
    		  <td colspan='2'><hr></td>
    		</tr>
    		#searchFooter()
    			
    		</TABLE>
    		</div>
  #end
#end



#set($helpText="search")
#page("Image Search" "mcc")
</vel:velocity>

<%
/************ Main search page **************
	searches available categories recursively, allowing the user to browse
	when the results reeach a certain threshold. In some implementations
	this threshold can be controlled by the user. Also browses automatically
	when the search result reaches a different thresholdThe history index tracks
	the current level of "drill-down".
	*/
%>