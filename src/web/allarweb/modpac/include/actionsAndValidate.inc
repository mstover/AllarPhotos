<% 
	addParams.put("actionzzz","action_validate_login"); 
	base.handleActions(request,response,addParams); 
%>
<jsp:useBean id="errors" scope="request" class="com.lazerinc.client.beans.ErrorsBean"/>
<c:if test="${ !(empty errors.redirectPage) }">
	<jsp:forward page="<%=errors.getRedirectPageAndClear()%>"/>
</c:if>