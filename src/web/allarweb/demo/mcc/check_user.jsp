<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="action_check_user_info"/>
<act:request action="action_check_cart_empty"/>
<act:onError error="IncompleteUserInfoError">edit_user_info.jsp?badInfo=true</act:onError>
<act:onError error="EmptyCartError" message="">shopping_cart.jsp</act:onError>
<act:executeActions/>

<vel:velocity>
	
#macro(pageJavascript)
	<!--                                            -->
		<!-- This script is required bootstrap stuff.   -->
		<!-- You can put it in the HEAD, but startup    -->
		<!-- is slightly faster if you include it here. -->
		<!--                                            -->
		<meta name='gwt:module' content='com.allarphoto.ajaxclient.AddressFragment'>
		<script language="javascript" src="gwt.js"></script>
#end
	
#macro(mainContent)
	<div align="center">
    <h3>Please verify your user information:</h3>
    <table><tr valign="TOP"><td align="right"><b>Name:</b>&nbsp;&nbsp;</td>
    <td>$user.firstName
    	#if($user.middleInitial != "N/A")
    		$!user.middleInitial
    	#end
    	$user.lastName</td></tr>
    <tr valign="TOP"><td align="right"><b>Email Address:</b>&nbsp;&nbsp;</td><td>$!user.emailAddress</td></tr>
    <tr valign="TOP"><td align="right"><b>Phone:</b>&nbsp;&nbsp;</td><td>$!user.phone</td></tr><TR>
    <tr valign="TOP"><td align="right"><b>Company:</b>&nbsp;&nbsp;</td><td>$!user.company.name</td></tr>
    <tr valign="TOP"><td align="center" colspan="2"><form action="edit_user_info.jsp"><input class='btnBkd' border="0" type="submit" value="Edit User Information"></form></td></tr>
    </table>
    
    <p><br>
    
    <FORM id="addr_form" action="verify.jsp" METHOD="POST">
    
    #if($cart.orderRequest)
		<div id="address_fragment"></div>
	<input type="hidden" name="actiona" value="save_order_ship_address">
	<table align="center">
	<TR><TD COLSPAN="2" align="center"><H3>Shipping Address for this Order:</H3></TD></TR>
	<TR valign="top"><TD COLSPAN="2" align="center">Items in <b>bold</b> are required.</TD></TR>
	<TR><td align="right"><b>Company:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="company.name" VALUE="$!user.company.name" SIZE="25"></TD></TR>
	<TR><td align="right">ATTN:</TD><TD><INPUT TYPE="Text" NAME="attn" VALUE="$!user.referrer" SIZE="25"></TD></TR>
	<TR><td align="right">Phone:</TD><TD><INPUT TYPE="Text" NAME="phone" VALUE="$!user.fax" SIZE="25"></TD></TR>
	<TR><td align="right"><b>Address Line 1:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="address1" VALUE="$!user.shipAddress1" SIZE="25"></TD></TR>
	<TR><td align="right">Address Line 2:&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="address2" VALUE="$!user.shipAddress2" SIZE="25"></TD></TR>
	<TR><td align="right"><b>City:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="City.name" VALUE="$!user.shipCity" SIZE="25"></SELECT></TD></TR>
	<TR><td align="right"><b>State:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="hidden" NAME="shipState_text" value=""><SELECT NAME="State.name">
	#foreach($state in $data.states)
		#if($state == $user.shipState)
				<OPTION VALUE='$!state' SELECTED>$!state
		#else
				<OPTION VALUE='$!state'>$!state
		#end
	#end
	</SELECT></TD></TR>
	<TR><td align="right"><b>Zip:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="zip" VALUE="$!user.shipZip" SIZE="25"></TD></TR>
	<TR><td align="right"><b>Country:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="hidden" NAME="shipCountry_text" value=""><SELECT NAME="Country.name">
	#foreach($country in $data.countries)
		#if($country == $user.shipCountry)
				<OPTION VALUE='$!country' SELECTED>$!country
		#else
				<OPTION VALUE='$!country'>$!country
		#end
	#end
	</SELECT></TD></TR>
	</table>
    #end
    <br>
      <div style='margin-top: 10px;'>
    	<input class='btnBkd' type="submit" border="0" value="Continue with Checkout">
      </div>
    </form>
    </div>
#end
	
#set($helpText="verify")
#page("Check User Information" "mcc")
</vel:velocity>


