<c:if test="${(result.startDate != null)}">
	<div style="padding: 10px 15px 0px; font-style: italic">
		<c:out value="${question.generalFeedback}" escapeXml="false" />
	</div>
</c:if>

<c:choose>
	<c:when test="${(result.startDate != null) && (result.finishDate == null)}">
		<div style="padding: 10px 15px 20px; font-style: italic">
			<fmt:message key="label.learning.marks.penalty" >
				<fmt:param><fmt:formatNumber value="${result.mark}" maxFractionDigits="3"/></fmt:param>
				<fmt:param>${question.defaultGrade}</fmt:param>
				<fmt:param>${result.penalty}</fmt:param>
			</fmt:message>
		</div>
	</c:when>
	<c:when test="${result.finishDate != null}">
		<div style="padding: 10px 15px 7px; font-style: italic">
			<fmt:message key="label.learning.marks" >
				<fmt:param><fmt:formatNumber value="${result.mark}" maxFractionDigits="3"/></fmt:param>
				<fmt:param>${question.defaultGrade}</fmt:param>
			</fmt:message>
		</div>
	</c:when>
</c:choose>