<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:request action="get_users_to_admin"/>
<act:executeActions/>

<vel:velocity>


#macro(pageJavascript)
	<script language="Javascript">
	<!--
	
	var selUsers = new Array();
	var selGroups = new Array();
	var unselGroups = new Array();
	var unselUsers = new Array();
	var unUserCount = 0;
	var unGroupCount = 0;
	var userCount = 0;
	var groupCount = 0;
	var doingUsers = false;
	var doingGroups = false;
	
	
	function clickUser(id)
	{
		messEl = document.getElementById("messages");
		if(messEl != null) messEl.innerHTML = '';
		var el = document.getElementById("user_"+id);
		var clazz = getElementClass(el);
		if(doingGroups)
		{
			if(clazz.indexOf("_com") > -1)
			{
				unselUsers[unUserCount++] = id;
				clazz = clazz.replace(/_com/,"_no");
				setElementClass(el,clazz);
				setElementClass(el,clazz.replace(/sel_/,"unsel_"));
				if(removeFromArray(selUsers,id)) userCount--;
			}
			else
			{
				selUsers[userCount++] = id;
				clazz = clazz.replace(/_no/,"_com");
				setElementClass(el,clazz);
				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
				if(removeFromArray(unselUsers,id)) unUserCount--;
			}
		}
		else
		{
			doingUsers = true;
			if(clazz.indexOf("unsel_") > -1)
			{
				selUsers[userCount++] = id;
				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
			}
			else
			{
				removeFromArray(selUsers,id);
				userCount--;
				setElementClass(el,clazz.replace(/sel_/,"unsel_"));
			}
			if(userCount > 0)
			{
				url = "doTmpl.vtl?action=get_groups";
				url = url + getQueryFragment(selUsers,"user_id",true,false);
        		var tmpl = "#";
        		tmpl = tmpl+"commonGroups_ajax()";
        		var func = updateGroups();
        		postToFunction(url,tmpl,func);
			}
			else
			   clearSelections();
		}	
		updateInfo();
	}
	
	function clickGroup(id)
	{
		messEl = document.getElementById("messages");
		if(messEl != null) messEl.innerHTML = '';
		var el = document.getElementById("group_"+id);
		var clazz = getElementClass(el);
		if(doingUsers)
		{
			if(clazz.indexOf("_com") > -1)
			{
				unselGroups[unGroupCount++] = id;
				clazz = clazz.replace(/_com/,"_no");
				setElementClass(el,clazz);
				setElementClass(el,clazz.replace(/sel_/,"unsel_"));
				if(removeFromArray(selGroups,id)) groupCount--;
			}
			else
			{
				selGroups[groupCount++] = id;
				clazz = clazz.replace(/_no/,"_com");
				setElementClass(el,clazz);
				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
				if(removeFromArray(unselGroups,id)) unGroupCount--;
			}
		}
		else
		{
			doingGroups = true;
			if(clazz.indexOf("unsel_") > -1)
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
			if(groupCount > 0)
			{
    			url = "doTmpl.vtl?action=get_users_to_admin";
				url = url + getQueryFragment(selGroups,"group_id",true,false);
        		var tmpl = "#";
        		tmpl = tmpl+"commonUsers_ajax()";
        		var func = updateUsers();
        		postToFunction(url,tmpl,func);
			}
			else
			   clearSelections();
		}	
		updateInfo();
	}
	
	function updateUsers(res)
	{
		if(arguments.length == 0) return updateUsers;
		var userElements = document.getElementsByName("user");
		var len = userElements.length;
		for(var i = 0;i < len;i++)
		{
			var clazz = getElementClass(userElements[i]);
			if(clazz.indexOf("unsel_") > -1)
			{
    			clazz = clazz.replace(/_com/,"_no");
    			setElementClass(userElements[i],clazz);
			}
		}
		var commonUsers = res.split("|");
		for(var i = 0;i < commonUsers.length;i++)
		{
			if(commonUsers[i].length > 0 && document.getElementById("user_"+commonUsers[i]) != null)
			{
				var el = document.getElementById("user_" + commonUsers[i]);
				var clazz = getElementClass(el);
				clazz = clazz.replace(/_no/,"_com");
				setElementClass(el,clazz);
			}
		}
	}
	
	function updateGroups(res)
	{
		if(arguments.length == 0) return updateGroups;
		var groupElements = document.getElementsByName("group");
		var len = groupElements.length;
		for(var i = 0;i < len;i++)
		{
			var clazz = getElementClass(groupElements[i]);
			if(clazz.indexOf("unsel_") > -1)
			{
    			clazz = clazz.replace(/_com/,"_no");
    			setElementClass(groupElements[i],clazz);
			}
		}
		var commonGroups = res.split("|");
		for(var i = 0;i < commonGroups.length;i++)
		{
			if(commonGroups[i].length > 0 && !inArray(unselGroups,commonGroups[i]) && document.getElementById("group_"+commonGroups[i]) != null)
			{
				var el = document.getElementById("group_" + commonGroups[i]);
				var clazz = getElementClass(el);
				clazz = clazz.replace(/_no/,"_com");
				setElementClass(el,clazz);
			}
		}
	}
	
	function submitChanges()
	{
		var url="adminUsers.jsp?action=mod_group_members";
		#foreach($g in $viewingGroups)
			url = url + "&group_id=$g.id";
		#end
		if(doingGroups)
		{
			url = url + getQueryFragment(selGroups,"mod_group_id",true,false) + 
			      getQueryFragment(selUsers,"add_user_id",true,false) +
			      getQueryFragment(unselUsers,"remove_user_id",true,false);
		}
		else if(doingUsers)
		{
			url = url + getQueryFragment(selUsers,"mod_user_id",true,false) + 
			      getQueryFragment(selGroups,"add_group_id",true,false) +
			      getQueryFragment(unselGroups,"remove_group_id",true,false);
		}
		document.location=url;
	}
	
	function updateInfo()
	{
		if((doingUsers && (groupCount > 0 || unGroupCount > 0)) ||
			(doingGroups && (userCount > 0 || unUserCount > 0)))
				document.getElementById("change_label").innerHTML='<input type="button" onclick="javascript:submitChanges();" value="Make Changes">';
		if(doingUsers || doingGroups)
			document.getElementById("clear").innerHTML='<input type="button" onclick="javascript:clearSelections();" value="Clear Selections">'
		if(doingUsers)
		{
			var userText = "<b>Modifying Users:</b> ";
			for(var i = 0;i < userCount;i++)
			{
				userText = userText + '<a href="addUser.jsp?action=get_user&user_id='+selUsers[i]+'">'+document.getElementById("user_"+selUsers[i]).innerHTML + '</a>';
				if(i+1 < userCount) userText = userText + "; ";
			}
    		document.getElementById("modifying").innerHTML = userText;
			var selGroupText = "";
			if(groupCount > 0)
				selGroupText = "<b>Add to Groups:</b> ";
			for(var i = 0;i < groupCount;i++)
			{
				selGroupText = selGroupText + document.getElementById("group_"+selGroups[i]).innerHTML;
				if(i+1 < groupCount) selGroupText = selGroupText + "; ";
			}
    		document.getElementById("adding").innerHTML = selGroupText;
			var unselGroupText = "";
			if(unGroupCount > 0)
				unselGroupText = "<b>Remove from Groups:</b> ";
			for(var i = 0;i < unGroupCount;i++)
			{
				unselGroupText = unselGroupText + document.getElementById("group_"+unselGroups[i]).innerHTML;
				if(i+1 < unGroupCount) unselGroupText = unselGroupText + "; ";
			}
    		document.getElementById("removing").innerHTML = unselGroupText;
		}
		else if(doingGroups)
		{
			var groupText = "<b>Modifying Groups:</b> ";
			for(var i = 0;i < groupCount;i++)
			{
				groupText = groupText + document.getElementById("group_"+selGroups[i]).innerHTML;
				if(i+1 < groupCount) groupText = groupText + "; ";
			}
    		document.getElementById("modifying").innerHTML = groupText;
			var selUserText = "";
			if(userCount > 0)
				selUserText = "<b>Add Users:</b> ";
			for(var i = 0;i < userCount;i++)
			{
				selUserText = selUserText + document.getElementById("user_"+selUsers[i]).innerHTML;
				if(i+1 < userCount) selUserText = selUserText + "; ";
			}
    		document.getElementById("adding").innerHTML = selUserText;
			var unselUserText = "";
			if(unUserCount > 0)
				unselUserText = "<b>Remove Users:</b> ";
			for(var i = 0;i < unUserCount;i++)
			{
				unselUserText = unselUserText + document.getElementById("user_"+unselUsers[i]).innerHTML;
				if(i+1 < unUserCount) unselUserText = unselUserText + "; ";
			}
    		document.getElementById("removing").innerHTML = unselUserText;
		}
		else
		{
			document.getElementById("change_label").innerHTML = "";
			document.getElementById("modifying").innerHTML = "";
			document.getElementById("adding").innerHTML = "";
			document.getElementById("removing").innerHTML = "";
			document.getElementById("clear").innerHTML = "";
		}
	}	
	
	function mouseoverUser(id,username,firstname,lastname,email)
	{
		document.getElementById('ugdInfoBox').innerHTML='<b>ID:</b> '+id+'<br><b>Username:</b> '+username+'<br><b>First Name:</b> '+firstname+'<br><b>Last Name:</b> '+lastname+'<br><b>Email:</b> '+email+'';
		mouseoverObj("user_"+id);
	}
	
	function clearSelections()
	{
		document.getElementById("messages").innerHTML = '';
		selUsers = new Array();
		selGroups = new Array();
		unselUsers = new Array();
		unselGroups = new Array();
		unUserCount = 0;
		unGroupCount = 0;
		doingGroups = false;
		doingUsers = false;
		userCount = 0;
		groupCount = 0;
		var userElements = document.getElementsByName("user");
		var len = userElements.length;
		for(var i = 0;i < len;i++)
		{
			var clazz = getElementClass(userElements[i]);
			setElementClass(userElements[i],"unsel_no_out");
		}
		var groupElements = document.getElementsByName("group");
		var len = groupElements.length;
		for(var i = 0;i < len;i++)
		{
			var clazz = getElementClass(groupElements[i]);
			setElementClass(groupElements[i],"unsel_no_out");
		}
		updateInfo();
	}
	-->
	</script>
#end



#macro(userAdminBox)
	<h3>Users of Groups: <span style="font-size:90%;">[#foreach($g in $viewingGroups)$g.name, #end]</span></h3> 
	<div id="userAdminBox">
	#set($quarter = $math.add($math.div($users.size(),$params.getParameterAsInt("user_cols",3)).intValue(),1))
	#foreach($u in $users)
		#if($velocityCount % $quarter == 0 && $velocityCount != 0)
		    </div><div style="float:left;">
		#elseif($velocityCount == 0)
			<div style="float:left;">
		#end
			<span id="user_$u.userID" name="user" class="unsel_no_out" onclick="javascript:clickUser($u.userID);" onmouseout="javascript:mouseoutObj('user_$u.userID');" onmouseover='javascript:mouseoverUser($u.userID,"$u.username","$u.firstName","$u.lastName","$u.emailAddress");'>$u.lastName, $u.firstName</span>
			<br>
	#end</div></div>
#end

#macro(mainContent)
	<h3 style='text-align: center;'>Administering Users</h3>
	<div class="admin"><div style="float:left;margin:5px;">
			#userAdminBox()</div>
	<div style="float:left;width:250px;margin:5px;"><h3>Info Box</h3>
		<div id="ugdInfoBox"></div>
		<div id="infoSection">
			<div id="change_label" style="margin-bottom:5px;margin-top:5px;text-align:center;"></div>
		<div id="modifying" style="margin-bottom:5px;"></div>
		<div id="adding" style="margin-bottom:5px;"></div>
		<div id="removing" style="margin-bottom:5px;"></div>
		<p>
		<div id="clear" style="margin-bottom:5px;margin-top:5px;text-align:center;"></div>
		</div>
	</div>
	<div style="float:left;margin:5px;">#groupAdminBox(2 true true true)</div>
	</div>
#end
	
	#page("Administer Your Users" "admin")
</vel:velocity>