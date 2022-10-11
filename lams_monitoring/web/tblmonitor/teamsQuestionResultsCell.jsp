<%@ include file="/taglibs.jsp"%>

<c:forEach var="answersForOptionsDto" items="${answersForOptionsDtos}">
	<c:if test="${question.uid eq answersForOptionsDto.qbQuestionUid}">
		<c:set var="answerPercent" value="${answersForOptionsDto.correctAnswerPercent}" />
		<c:set var="highlightClass" value="" />
		<c:set var="highlightClass">
			<c:choose>
				<c:when test="${answerPercent > 95}">bg-success text-white</c:when>
				<c:when test="${answerPercent < 40 and answerPercent >= 0}">bg-danger text-white</c:when>
				<c:when test="${answerPercent < 75 and answerPercent >= 0}">bg-warning</c:when>
			</c:choose>
		</c:set>
		<td class="question-results-cell ${highlightClass}">
			<c:choose>
				<c:when test="${answerPercent >= 0}">
					${answerPercent}%
				</c:when>
				<c:otherwise>
					-
				</c:otherwise>
			</c:choose>
		</td>
	</c:if>
</c:forEach>