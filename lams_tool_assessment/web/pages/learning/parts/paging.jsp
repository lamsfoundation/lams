<!--Paging-->
<c:if test="${fn:length(sessionMap.pagedQuestions) > 1}">
	<div id="pager" class="voffset10">
		<fmt:message key="label.learning.page" />
		<c:forEach var="questions" items="${sessionMap.pagedQuestions}" varStatus="status">
			<c:choose>
				<c:when	test="${(status.index+1) == pageNumber}">
					<a href="#nogo" onclick="return nextPage(${status.index + 1})" class="text-danger">
				</c:when>
				<c:otherwise>
					<a href="#nogo" onclick="return nextPage(${status.index + 1})">
				</c:otherwise>
			</c:choose>				
				${status.index + 1} 
			</a>
		</c:forEach>		
	</div>
</c:if>