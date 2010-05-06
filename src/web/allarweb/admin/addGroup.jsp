<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:onError error="DuplicateUsernameError" message="That username is taken.  You must choose another" url="addGroup.jsp"/>
<act:executeActions/>

<vel:velocity>
	#macro(pageJavascript)
	<script language="javascript">
	<!--
	var count = 0;
		function clickGroup(id)
		{		
			var el = document.getElementById("group_"+id);
			if(el != null)
			{
    			var clazz = getElementClass(el);
    			if(clazz.indexOf("unsel_") > -1)
    			{
    				document.addGroup.admingroup_id[count++].value=id;
    				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
    			}
    			else
    			{
    				var shift = false;
    				var len = document.addGroup.admingroup_id.length;
    				for(var i = 0;i < len;i++)
    				{
    					if(document.addGroup.admingroup_id[i].value == id)
    					{
    						if(i+1 < len)
    							document.addGroup.admingroup_id[i].value = document.addGroup.admingroup_id[i+1].value;
    						else
    							document.addGroup.admingroup_id[i].value = 0;
    						setElementClass(el,clazz.replace(/sel_/,"unsel_"));
    						count--;
    						shift = true;
    					}
    					else if(shift)
    					{
    						if(i+1 < len)
    							document.addGroup.admingroup_id[i].value = document.addGroup.admingroup_id[i+1].value;
    						else
    							document.addGroup.admingroup_id[i].value = 0;
    					}
    				}
    			}
			}
		}
	-->
	</script>
#end
	
#macro(mainContent)
	<h3>Add Group</h3>
	<form name="addGroup" method="POST" action="adminGroups.jsp" onsubmit="javascript:return verify(this);" >
		<span class="item">Group Name:</span> <input type="text" name="name">
		<input type="submit" name="submit" value="Add Group">
		<input type="hidden" name="actiona" value="add_group">
		<input type="hidden" name="IncompleteInfoError" value="addGroup.jsp">
		<input type="hidden" name="IncompleteInfoError_msg" value="You must select at least one administrating group">
		<input type="hidden" name="DuplicateObjectException" value="addGroup.jsp">
		<input type="hidden" name="DuplicateObjectException_msg" value="A group with that name already exists">
		#foreach($g in $ugd.getGroups($user.permissions))
			<input type="hidden" name="admingroup_id" value="">
		#end
	</form>
	<div class="admin"><h3>Choose which groups will have admin rights over the new group</h3>
	#groupAdminBox(3 true true true)</div>
#end
	#page("Add Group" "admin")
</vel:velocity>