<!--Paging-->
<c:set var="pageCount" value="${fn:length(sessionMap.pagedQuestions)}" />
<c:set var="useSections" value="${assessment.questionsPerPage eq -1}" />

<c:if test="${pageCount > 1}">
	<div id="pager" class="py-3">

		<button type="button" onClick="nextPage(${pageNumber - 1})"
			class="btn btn-outline-secondary btn-sm btn-icon-previous me-2"
			<c:if test="${pageNumber == 1}">disabled</c:if>>
			<c:choose>
				<c:when test="${useSections}">
					<fmt:message key="label.learning.section.previous" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.learning.page.previous" />
				</c:otherwise>
			</c:choose>
		</button>

		<c:choose>
			<c:when test="${useSections}">
				<c:forEach var="section" items="${assessment.sections}" varStatus="status">
					<button type="button" onclick="nextPage(${status.index + 1})"
						<c:choose>
							<c:when	test="${status.index + 1 eq pageNumber}">
								 class="btn btn-outline-danger btn-sm ms-2" disabled
							</c:when>
							<c:otherwise>
								class="btn btn-outline-secondary btn-sm ms-2"
							</c:otherwise>
						</c:choose>
						>
						
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
					
						<c:if test="${pageNumber != status.index + 1}">
							<c:set var="questionsPage" value="${sessionMap.pagedQuestions[status.index]}" />
							<c:set var="answeredQuestions" value="0" />
							<c:forEach var="question" items="${questionsPage}">
								<c:if test="${question.questionAnswered}">
									<c:set var="answeredQuestions" value="${answeredQuestions + 1}" />
								</c:if>
							</c:forEach>
							[${answeredQuestions}/${fn:length(questionsPage)}]
						</c:if>
					</button>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach items="${sessionMap.pagedQuestions}" varStatus="status">
					<button type="button" onclick="nextPage(${status.index + 1})"
						<c:choose>
							<c:when	test="${status.index + 1 eq pageNumber}">
								 class="btn btn-outline-danger btn-sm ms-2" disabled
							</c:when>
							<c:otherwise>
								class="btn btn-outline-secondary btn-sm ms-2"
							</c:otherwise>
						</c:choose>		
					>
						${status.index + 1}
					</button>
				</c:forEach>	
			</c:otherwise>
		</c:choose>

		<button type="button" onClick="nextPage(${pageNumber + 1})"
			class="btn btn-outline-secondary btn-sm btn-icon-next ms-3"
			<c:if test="${pageNumber == pageCount}">disabled</c:if>>
			<c:choose>
				<c:when test="${useSections}">
					<fmt:message key="label.learning.section.next" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.learning.page.next" />
				</c:otherwise>
			</c:choose>
		</button>
	</div>
</c:if>