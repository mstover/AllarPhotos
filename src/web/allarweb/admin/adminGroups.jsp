<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="get_groups_for_admin" order="1"/>
<act:executeActions/>

<vel:velocity>
#macro(pageJavascript)
	<script language="javascript">
	<!--
	
	var selGroups = new Array();
	var groupCount = 0;
		function clickGroup(id)
		{
			if(document.getElementById("messages") != null)
			{
				document.getElementById("messages").innerHTML = '';
			}
			var el = document.getElementById("group_"+id);
			var clazz = getElementClass(el);
			if(clazz.indexOf("unsel") > -1)
			{
				selGroups[groupCount++] = id;
				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
			}
			else
			{
				removeFromArray(selGroups,id);
				groupCount--;
				setElementClass(el,clazz.replace(/sel_/,"unsel_"));
			}
			var url = "groupControls.vtl?action=get_groups" + getQueryFragment(selGroups,"group_id",true,false);
			postToDiv("group_control_box",url,"#"+"groupControlBox()");			
		}
		
		

        function addGroupToPerms(resName,ownerId)
        {
			var url = "groupControls.vtl?action=add_resource_to_perms&actionb=get_groups&resource_type=1&resource_name="+resName+"&group_id="+ownerId;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleView('perms_on_groups');
        }
        function addLibraryToPerms(resName,ownerId)
        {
			var url = "groupControls.vtl?action=add_resource_to_perms&actionb=get_groups&resource_type=3&resource_name="+resName+"&group_id="+ownerId;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleAdminView('Libraries','library_view','perms_on_libraries');
        }
        function addProtFieldToPerms(resName,ownerId,fieldFilter)
        {
			var url = "groupControls.vtl?action=add_resource_to_perms&actionb=get_groups&filter="+fieldFilter+"&resource_type=7&resource_name="+resName+"&group_id="+ownerId;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleAdminView('Libraries','library_view','perms_on_libraries');
			toggleAdminView('Protected Fields','protFields_view','perms_on_protFields');
        }
        function addExpFieldToPerms(resName,ownerId,fieldFilter)
        {
			var url = "groupControls.vtl?action=add_resource_to_perms&actionb=get_groups&resource_type=8&resource_name="+resName+"&group_id="+ownerId;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleView('perms_on_expFields');
			toggleView('avail_expFields');
        }
        function addMerchantToPerms(resName,ownerId)
        {
			var url = "groupControls.vtl?action=add_resource_to_perms&actionb=get_groups&resource_type=4&resource_name="+resName+"&group_id="+ownerId;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleView('perms_on_merchants');
        }
		
		function reloadPermsWithFilter(filter)
		{
			var url = "groupControls.vtl?action=get_groups" + getQueryFragment(selGroups,"group_id",true,false)+"&filter="+filter;
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
			toggleAdminView('Libraries','library_view','perms_on_libraries');
			toggleAdminView('Protected Fields','protFields_view','perms_on_protFields');
		}
		
		function reloadPerms()
		{
			var url = "groupControls.vtl?action=get_groups" + getQueryFragment(selGroups,"group_id",true,false);
			postToDiv("group_control_box",url,"#"+"groupControlBox()");	
		}
		
		function toggleAdminView(display,view,divId)
		{
			var viewEl = document.getElementById(divId+"_view");
			var baseEl = document.getElementById(divId);
			var header = document.getElementById(view);
			if(viewEl.innerHTML.length > 0)
			{
				if(view == "library_view")
				{
					var fieldViewEl = document.getElementById("perms_on_protFields_view");
					var fieldBaseEl = document.getElementById("perms_on_protFields");
					if(fieldViewEl.innerHTML.length > 0)
					{
						fieldBaseEl.innerHTML = fieldViewEl.innerHTML;
						fieldViewEl.innerHTML = '';
					}
				}
				header.innerHTML='View ' + display;
				baseEl.innerHTML=viewEl.innerHTML;
				viewEl.innerHTML='';
			}
			else
			{
				header.innerHTML=display;
				viewEl.innerHTML=baseEl.innerHTML;
				baseEl.innerHTML='';
			}
		}
		
		function clickUser(id)
		{
			document.location="addUser.jsp?action=get_user&user_id="+id;
		}
	-->
	</script>
#end

#macro(mainContent)
	<h3 style='text-align: center;'>Administering Groups</h3>
	<table style="width:100%;margin:0px;" cellpadding="0" border="0" cellspacing="5">
		<tr valign="top">
			<td style="width:20%;">#groupAdminBox(1 true true true)</td>
			<td style="width:80%;"><div id="group_control_box"></div></td>
		</tr>
	</table>
#end

	#page("Administer Your Users" "admin")
</vel:velocity>