<c:if test="${finishedLock}">

	<c:if test="${assessment.allowQuestionFeedback && (question.generalFeedback != null)}">
		<div style="padding: 10px 15px 0px; font-style: italic; color:#47bc23;">
			<c:out value="${question.generalFeedback}" escapeXml="false" />
		</div>
	</c:if>
	
	<c:if test="${assessment.allowGradesAfterAttempt}">
		<div style="padding: 10px 15px 10px; font-style: italic; color:#47bc23;">
			<fmt:message key="label.learning.marks" >
				<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
				<fmt:param>${question.defaultGrade}</fmt:param>
			</fmt:message>
			<c:if test="${(question.mark != question.defaultGrade) && (fn:length(question.questionResults) > 1)}">
				<fmt:message key="label.learning.penalty" >
					<fmt:param><fmt:formatNumber value="${question.penalty}" maxFractionDigits="2"/></fmt:param>
				</fmt:message>
			</c:if>
		</div>	
	</c:if>
</c:if>
