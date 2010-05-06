<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="get_families"/>
<act:executeActions/>

<vel:velocity>
	
#macro(mainContent)
	<div class="page_nav"><h2>Product Families</h2>
	<ul>
		#foreach($fam in $productFamilies)
			<li class="label"><a href="?actiona=select_family&product_family=$format.urlEncode($fam)">$fam</a></li>
		#end
	</ul></div>
	#if($productFamily)
		<h2>$productFamily.descriptiveName</h2>
		$productFamily.description
		<h3>Fields</h3>
		<table border="0" cellspacing="10"><tr><th><a href="?action=toggleSortField&sort_field=name">Name</a></th>
						<th><a href="?action=toggleSortField&sort_field=type">Type</a></th>
						<th><a href="?action=toggleSortField&sort_field=displayOrder">Display Order</a></th>
						<th><a href="?action=toggleSortField&sort_field=searchOrder">Search Order</a></th></tr>
				#foreach($field in $fields)
				<tr><td align="center">$field.name </td>
					<td align="center">#if($field.type == 1)Category
						#elseif($field.type == -1)Protected Field
						#elseif($field.type == 2) Description Field
						#elseif($field.type == 6) Primary Label
						#elseif($field.type == 8) Numerical
						#elseif($field.type == 10) Path
						#elseif($field.type == 9) Expired
					#end</td>
					<td align="right">$field.displayOrder</td>
					<td align="right">$field.searchOrder</td>
				</tr>
				#end
		</table>
	#end
				
	
	
#end
	#page("Administer Libraries" "admin")
</vel:velocity>