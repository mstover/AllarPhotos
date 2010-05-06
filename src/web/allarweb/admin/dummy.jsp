<%@ include file="/include/global.inc" %>
<act:executeActions/>
<vel:velocity>
#if($errorBean.errorMessages.size() > 0)
	#foreach($error in $errorBean.errorMessages)
					#if($error.length() > 0) 
						<div class="message">$!error</div>
					#end
	#end
#else success_success #end
</vel:velocity>