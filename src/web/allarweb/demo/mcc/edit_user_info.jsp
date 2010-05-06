<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:executeActions/>

<vel:velocity>
	
#macro(mainContent)
	<FORM action="check_user.jsp" METHOD="POST">
    <input type="hidden" name="actiona" value="action_modify_user">
    <DIV ALIGN="center">Please fill in the following information in order to proceed. <BR>
    Items in <b>bold</b> are required.
    
    <table>#if($param.badInfo == "true")
    	<tr valign="TOP">
    		<td><div class="eyecatching">You are missing some required user info.</div></td>
    	</tr>
    	#end
    <tr valign="TOP">
    <td><TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0 width="500" align="center">
    <TR><TD width="200"><b>Username:</b></TD><TD width="300"><INPUT TYPE="Text" NAME="username" VALUE='$!user.username' SIZE="25"></TD></TR>
    <TR><TD align="right"><b>Password:</b></TD><TD><INPUT TYPE="Password" NAME="password" VALUE='' SIZE="25"></TD></TR>
    <TR><TD align="right"><b>First Name:</b></TD><TD><INPUT TYPE="Text" NAME="firstName" VALUE='$!user.firstName' SIZE="25"></TD></TR>
    <TR><td align="right">Middle Initial:</TD><TD><INPUT TYPE="Text" NAME="middleInitial" VALUE='$!user.middleInitial' SIZE="4"></TD></TR>
    <TR><td align="right"><b>Last Name:</b></TD><TD><INPUT TYPE="Text" NAME="lastName" VALUE='$!user.lastName' SIZE="25"></TD></TR>
    <TR><td align="right"><b>Email Address:</b></TD><TD><INPUT TYPE="Text" NAME="emailAddress" VALUE='$!user.emailAddress' SIZE="25"></TD></TR>
    <TR><td align="right"><b>Phone:</b></TD><TD><INPUT TYPE="Text" NAME="phone" VALUE='$!user.phone' SIZE="25"></TD></TR>
    <TR><td align="right"><b>Company:</b></TD><TD><INPUT TYPE="Text" NAME="company.name" VALUE='$!user.company.name' SIZE="25"></TD></TR>
    </SELECT></TD></TR>
    <TR><TD COLSPAN="2"><H3>Default Address</H3></TD></TR>
    <TR><td align="right"><b>Address Line 1:</b></TD><TD><INPUT TYPE="Text" NAME="billAddress1" VALUE='$!user.billAddress1' SIZE="25"></TD></TR>
    <TR><td align="right">Address Line 2:</TD><TD><INPUT TYPE="Text" NAME="billAddress2" VALUE='$!user.billAddress2' SIZE="25"></TD></TR>
    <TR><td align="right"><b>City:</b></TD><TD><INPUT TYPE="Text" NAME="billCity.name" VALUE='$!user.billCity' SIZE="25"></TD></TR>
    <TR><td align="right"><b>State:</b></TD><TD><SELECT NAME="billState.id">
    #foreach($state in $data.states)
    		
    			#if($state.id == $user.billState.id)
					<OPTION VALUE='$state.id' SELECTED>$!state.name</OPTION>
    			
    			#else
					<OPTION VALUE='$!state.id'>$!state.name</OPTION>
    			#end
    		
    	#end
    </SELECT></TD></TR>
    <TR><td align="right"><b>Zip:</b></TD><TD><INPUT TYPE="Text" NAME="billZip" VALUE='$!user.billZip' SIZE="25"></TD></TR>
    <TR><td align="right"><b>Country:</b></TD><TD><SELECT NAME="billCountry.id">
    #foreach($country in $data.countries)
    		
    			#if($country.id == $user.billCountry.id)
					<OPTION VALUE='$!country.id' SELECTED>$!country.name</OPTION>
    			
    			#else
					<OPTION VALUE='$!country.id'>$!country.name</OPTION>
    			#end
    		
    	#end
    </SELECT></TD></TR>
    </TABLE></td></tr></table>
    <INPUT class='btnBkd' TYPE="Submit" NAME="submit" VALUE="Update User Info">
    </FORM>
#end

	#set($helpText="verify")
	#page("Edit User Info" "mcc")
</vel:velocity>

