<!--Paging-->
<c:set var="pageCount" value="${fn:length(sessionMap.pagedQuestions)}" />
<c:if test="${pageCount > 1}">
	<div id="pager" class="voffset10">
		<c:if test="${pageNumber > 1}">
			<button type="button" onClick="return nextPage(${pageNumber - 1})" class="btn btn-default roffset10"
					>
				<fmt:message key="label.learning.page.previous" />
			</button>
		</c:if>
					
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
			
		<c:if test="${pageNumber < pageCount}">
			<button type="button" onClick="return nextPage(${pageNumber + 1})" class="btn btn-default loffset10"
					>
				<fmt:message key="label.learning.page.next" />
			</button>
		</c:if>
	</div>
</c:if>