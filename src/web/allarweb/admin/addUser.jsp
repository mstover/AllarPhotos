<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:onError error="DuplicateUsernameError" message="That username is taken.  You must choose another" url="addUser.jsp"/>
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
    				document.addUser.group_id[count++].value=id;
    				setElementClass(el,clazz.replace(/unsel_/,"sel_"));
    			}
    			else
    			{
    				var shift = false;
    				var len = document.addUser.group_id.length;
    				for(var i = 0;i < len;i++)
    				{
    					if(document.addUser.group_id[i].value == id)
    					{
    						if(i+1 < len)
    							document.addUser.group_id[i].value = document.addUser.group_id[i+1].value;
    						else
    							document.addUser.group_id[i].value = 0;
    						setElementClass(el,clazz.replace(/sel_/,"unsel_"));
    						count--;
    						shift = true;
    					}
    					else if(shift)
    					{
    						if(i+1 < len)
    							document.addUser.group_id[i].value = document.addUser.group_id[i+1].value;
    						else
    							document.addUser.group_id[i].value = 0;
    					}
    				}
    			}
			}
		}
		#if($editUser)
			function selectGroups()
			{
				#foreach($g in $editUser.groups)
					clickGroup($g.id);
				#end
			}
		#end
		
		function deleteUser(formEl)
		{
			formEl.actiona.value="delete_user";
		}

		function sendPassword(formEl)
		{
			formEl.actiona.value="action_password_request";
		}
	-->
	</script>
#end

#macro(bodyTag)
	<body #if($editUser) onload="javascript:selectGroups();"#end>
#end

#macro(mainContent)
	<h3 style='text-align: center;'>Add User</h3>
	<form name="addUser" method="POST" action="adminUsers.jsp" onsubmit="javascript:return verify(this);" >
		#if($editUser)
			<input type="hidden" name="actiona" value="action_modify_user">
			<input type="hidden" name="userID" value="$editUser.userID">
		#else
			<input type="hidden" name="actiona" value="add_user">
		#end
		<input type="hidden" name="DuplicateUsernameError" value="addUser.jsp">
		<input type="hidden" name="DuplicateUsernameError_msg" value="That username is taken.  You must choose another">
			<input type="hidden" name="group_id" value="">
		#foreach($g in $ugd.getGroups($user.permissions))
			<input type="hidden" name="group_id" value="">
		#end
	<div style="float:left;text-align:center;margin-right:30px;">
		<table border="0" cellpadding="0" cellspacing="5">
    <TR class="required"><TD class="item">First Name:</TD><TD><INPUT TYPE="Text" NAME="firstName" value="$!editUser.firstName$!newUser.firstName" SIZE="25"></TD></TR>
    <TR class="required"><td class="item">Last Name:</TD><TD><INPUT TYPE="Text" NAME="lastName" value="$!editUser.lastName$!newUser.lastName" SIZE="25"></TD></TR>
    <TR class="required"><td class="item">Email Address:</TD><TD><INPUT TYPE="Text" NAME="emailAddress" value="$!editUser.emailAddress$!newUser.emailAddress" SIZE="25"></TD></TR>
	<TR><TD class="item">Username:</TD><TD><INPUT TYPE="Text" NAME="username" value="$!editUser.username$!newUser.username" SIZE="25" optional="true"></TD></TR>
    <TR><TD class="item">Password:</TD><TD><INPUT TYPE="Password" NAME="password" SIZE="25" optional="true"></TD></TR>
    <TR><td class="item">Middle Initial:</TD><TD><INPUT TYPE="Text" NAME="middleInitial" value="$!editUser.middleInitial$!newUser.middleInitial" SIZE="4" optional="true"></TD></TR>
    <TR><td class="item">Phone:</TD><TD><INPUT TYPE="Text" NAME="phone" value="$!editUser.phone$!newUser.phone" SIZE="25" optional="true"></TD></TR>
    <TR><td class="item">Company:</TD><TD><INPUT TYPE="Text" NAME="company.name" value="$!editUser.company.name$!newUser.company.name" SIZE="25" optional="true"></TD></TR>
    <TR><TD COLSPAN="2" class="sectionHeader">Default Address</TD></TR>
    <TR><td class="item">Address Line 1:</TD><TD><INPUT TYPE="Text" NAME="billAddress1" value="$!editUser.billAddress1$!newUser.billAddress1" SIZE="25" optional="true"></TD></TR>
    <TR><td class="item">Address Line 2:</TD><TD><INPUT TYPE="Text" NAME="billAddress2" value="$!editUser.billAddress2$!newUser.billAddress2" SIZE="25" optional="true"></TD></TR>
    <TR><td class="item">City:</TD><TD><INPUT TYPE="Text" NAME="billCity.name" SIZE="25" value="$!editUser.billCity$!newUser.billCity" optional="true"></TD></TR>
    <TR><td class="item">State:</TD><TD><SELECT NAME="billState.id" optional="true">
    #foreach($state in $data.states)
    		
    			<OPTION VALUE='$!state.id'#if($editUser && $editUser.billState.id == $state.id) selected#end>$!state.name</OPTION>
    		
    	#end
    </SELECT></TD></TR>
    <TR><td class="item">Zip:</TD><TD><INPUT TYPE="Text" NAME="billZip" value="$!editUser.billZip$!newUser.billZip" SIZE="25" optional="true"></TD></TR>
    <TR><td class="item">Country:</TD><TD><SELECT NAME="billCountry.id" optional="true">
    #foreach($country in $data.countries)
    		<OPTION VALUE='$!country.id'#if($editUser && $editUser.billCountry.id == $country.id) selected#end>$!country.name</OPTION>    		
    	#end
    </SELECT></TD></TR>
    </TABLE>
	#if($editUser)
		<input type="hidden" name="InvalidPermission_msg" value="You do not have permission to modify that user">
		<input type="hidden" name="InvalidPermission" value="adminUsers.jsp">		
		<input type="submit" name="submit" value="Update User">
		<input type="submit" name="delete_User" value="Delete User" onclick="javascript:deleteUser(this.form);">
		<input type="submit" name="sendPass" value="Send Password" onclick="sendPassword(this.form)">
	#else
		<input type="submit" name="submit" value="Add User">
	#end</div>
	<div class="sectionHeader">Which groups should the user belong to?</div>
	<div class="admin">
	#groupAdminBox(3 true true true)</div>
	<div style="clear:left;"></div>
	</form>
#end
	#page("Administer Your Users" "admin")
</vel:velocity>

