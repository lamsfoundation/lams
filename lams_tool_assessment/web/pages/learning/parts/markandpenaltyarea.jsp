<c:if test="${finishedLock}">

	<c:if test="${assessment.allowQuestionFeedback && (question.generalFeedback != null)}">
		<div style="padding: 10px 15px 0px; font-style: italic; color:#47bc23;">
			<c:out value="${question.generalFeedback}" escapeXml="false" />
		</div>
	</c:if>
	
	<c:if test="${assessment.allowGradesAfterAttempt}">
		<div style="padding: 10px 15px 20px; font-style: italic; color:#47bc23;">
			<fmt:message key="label.learning.marks.penalty" >
				<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
				<fmt:param>${question.defaultGrade}</fmt:param>
				<fmt:param><fmt:formatNumber value="${question.penalty}" maxFractionDigits="2"/></fmt:param>
			</fmt:message>
		</div>	
	</c:if>
</c:if>
