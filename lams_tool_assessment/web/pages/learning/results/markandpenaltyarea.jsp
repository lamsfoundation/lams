<%@ include file="/common/taglibs.jsp"%>

<c:if test="${assessment.allowQuestionFeedback && (question.feedback != null)}">
	<div class="card-subheader mt-2">
		<fmt:message key="label.learning.summary.feedback" />
	</div>
	<div class="feedback">
		<c:out value="${question.feedback}" escapeXml="false" />
	</div>
</c:if>
	
<c:if test="${assessment.allowGradesAfterAttempt}">
	<div class="card-subheader text-primary mt-2">
		<fmt:message key="label.learning.marks" >
			<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
			<fmt:param>${question.maxMark}</fmt:param>
		</fmt:message>&nbsp;
			
		<c:if test="${(question.penalty ne 0) && (question.mark != question.maxMark) && (fn:length(question.questionResults) > 1) }">
			<fmt:message key="label.learning.penalty" >
				<fmt:param><fmt:formatNumber value="${question.penalty}" maxFractionDigits="2"/></fmt:param>
			</fmt:message>
		</c:if>
	</div>	
</c:if>
