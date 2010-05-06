<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:executeActions/>

<vel:velocity>
#macro(mainContent)
	
	#if($params.getParameter("request_email_to") && $params.getParameter("request_email_to") != "")
		#set($send_to = $params.getParameter("request_email_to"))
	#else
		#set($send_to = $configuration.email_webmaster)
	#end

    #if($feedbackAlert)
        <p>
        <table align='center' width='60%'><tr><td align='center'>
        	$!feedbackAlert
        </tr></td></table>
        <p>
    #end
    
    #if(!$feedbackSuccess || $feedbackSuccess != "true")
    	<DIV ALIGN="center"><H2>Send a question or comment to the Webmaster</H2></DIV>
    	<FORM ACTION="lazerweb_feedback.jsp" METHOD="POST">
    	<input type="hidden" name="actiona" value="action_lazerweb_feedback">
    
    	<DIV ALIGN="center"><TABLE>
    	
    		#if($user.username)
    			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name" value="$!user.firstName&nbsp;#if($user.middleInitial && $user.middleInitial != "N/A")$!user.middleInitial#end&nbsp;$!user.lastName" ></TD></TR>
    		#elseif($feedbackFullName)
    			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name" value="$!feedbackFullName" ></TD></TR>
    		#else
    			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name"></TD></TR>
    		#end
    	
    	
    		#if($user.username)
    			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email" value="$!user.emailAddress"></TD></TR>
    		#elseif($feedbackEmail)
    			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email" value="$!feedbackEmail"></TD></TR>
    		#else
    			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email"></TD></TR>	
    		#end
    	
    	
    		#if($feedbackPhoneNo)
    			<TR VALIGN="TOP"><TD>Your phone number (optional):</TD><TD><INPUT TYPE="Text" NAME="request_phone_no" value="$!feedbackPhoneNo"></TD></TR>
    		#else
    			<TR VALIGN="TOP"><TD>Your phone number (optional):</TD><TD><INPUT TYPE="Text" NAME="request_phone_no"></TD></TR>
    		#end
    	
    	<INPUT TYPE="Hidden" NAME="request_email_to" VALUE="$!send_to">
    		
    		#if($feedbackMessage)
    			<TR VALIGN="TOP"><TD>Your comments or questions:</TD><TD><TEXTAREA NAME="request_message" COLS="40" ROWS="6" WRAP="VIRTUAL" >$!feedbackMessage</TEXTAREA></TD></TR>
    		#else
    			<TR VALIGN="TOP"><TD>Your comments or questions:</TD><TD><TEXTAREA NAME="request_message" COLS="40" ROWS="6" WRAP="VIRTUAL"></TEXTAREA></TD></TR>
			#end
    	
    </TABLE>
    	<INPUT class='btnBkd' class='btnBkd' TYPE="Submit"></DIV>
    	</FORM>
    #end
#end
	
	#set($helpText="general")
	#page("Library Feedback Form" "mcc")
</vel:velocity>

