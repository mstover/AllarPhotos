<!-- Search History HTML Buffer -->
<div class='small2' style='text-align: left;'>
	<b>Search History:</b>&nbsp;&nbsp;&nbsp;
	<c:if test="${history_index > 0}">
		<b><a href="catsearch.jsp?request_history_index=0">Reset Search</a></b>&nbsp;&gt;&gt;&nbsp;				
		<c:forEach items="${productsFound.searchHistory}" var="historyValue" varStatus="count" begin="0" end="${history_index-1}">
			<c:choose>
				<c:when test="${count.count == history_index}">
					<c:out value="${historyValue}"/>
				</c:when>
				<c:otherwise>
					<a href="catsearch.jsp?request_history_index=<c:out value='${count.count}'/>"><c:out value="${historyValue}"/></a>&nbsp;&gt;&gt;&nbsp;
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:if>
</div>