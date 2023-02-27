<!--Paging-->
<c:set var="pageCount" value="${fn:length(sessionMap.pagedQuestions)}" />
<c:set var="useSections" value="${assessment.questionsPerPage eq -1}" />

<c:if test="${pageCount > 1}">
	<div id="pager" class="voffset10">
		<c:if test="${pageNumber > 1}">
			<button type="button" onClick="return nextPage(${pageNumber - 1})" class="btn btn-default roffset10"
					>
				<c:choose>
					<c:when test="${useSections}">
						<fmt:message key="label.learning.section.previous" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.learning.page.previous" />
					</c:otherwise>
				</c:choose>
			</button>
		</c:if>
		
		<c:choose>
			<c:when test="${useSections}">
				<fmt:message key="label.learning.section" />
				<c:forEach var="section" items="${assessment.sections}" varStatus="status">
					<c:choose>
						<c:when	test="${status.index + 1 eq pageNumber}">
							<a href="#nogo" onclick="return nextPage(${status.index + 1})" class="text-danger">
						</c:when>
						<c:otherwise>
							<a href="#nogo" onclick="return nextPage(${status.index + 1})">
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when	test="${empty section.name}">
							<fmt:message key="label.learning.section.default.name">	
								<fmt:param value="${status.index + 1}" />
							</fmt:message>
						</c:when>
						<c:otherwise>
							<c:out value="${section.name}" />
						</c:otherwise>
					</c:choose>
					</a>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<fmt:message key="label.learning.page" />
				<c:forEach items="${sessionMap.pagedQuestions}" varStatus="status">
					<c:choose>
						<c:when	test="${status.index + 1 eq pageNumber}">
							<a href="#nogo" onclick="return nextPage(${status.index + 1})" class="text-danger">
						</c:when>
						<c:otherwise>
							<a href="#nogo" onclick="return nextPage(${status.index + 1})">
						</c:otherwise>
					</c:choose>			
					${status.index + 1}
					</a>
				</c:forEach>	
			</c:otherwise>
		</c:choose>
			
		<c:if test="${pageNumber < pageCount}">
			<button type="button" onClick="return nextPage(${pageNumber + 1})" class="btn btn-default loffset10"
					>
				<c:choose>
					<c:when test="${useSections}">
						<fmt:message key="label.learning.section.next" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.learning.page.next" />
					</c:otherwise>
				</c:choose>
			</button>
		</c:if>
	</div>
</c:if>