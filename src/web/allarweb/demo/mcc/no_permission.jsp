<%@ include file="/include/global.inc" %>

<act:request action="action_validate_login"/>
<act:executeActions/>

<vel:velocity>
	
#macro(mainContent)
	<div style="font-size:16pt;">You do not have permission to use that resource.</div>
#end

#set($helpText="verify")
#page("No Permission!" "mcc")
</vel:velocity>