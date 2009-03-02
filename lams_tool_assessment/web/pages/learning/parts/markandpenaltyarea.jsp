<c:if test="${finishedLock}">
	<div style="padding: 10px 15px 0px; font-style: italic">
		<c:out value="${question.generalFeedback}" escapeXml="false" />
	</div>
	
	<div style="padding: 10px 15px 20px; font-style: italic">
		<fmt:message key="label.learning.marks.penalty" >
			<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
			<fmt:param>${question.defaultGrade}</fmt:param>
			<fmt:param>${question.penalty}</fmt:param>
		</fmt:message>
	</div>	
</c:if>
