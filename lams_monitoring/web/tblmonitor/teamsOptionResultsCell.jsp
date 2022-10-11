<%@ include file="/taglibs.jsp"%>

<c:forEach var="answersForOptionsDto" items="${answersForOptionsDtos}">
	<c:if test="${question.uid eq answersForOptionsDto.qbQuestionUid}">
		<c:forEach var="answersForOption" items="${answersForOptionsDto.optionAnswerPercent}">
			<c:if test="${option.uid eq answersForOption.key}">
				<td class="question-results-cell">
					<c:choose>
						<c:when test="${answersForOption.value >= 0}">
							${answersForOption.value}%
						</c:when>
						<c:otherwise>
							-
						</c:otherwise>
					</c:choose>
				</td>
			</c:if>
		</c:forEach>
	</c:if>
</c:forEach>