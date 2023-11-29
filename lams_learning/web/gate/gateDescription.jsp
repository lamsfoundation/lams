<%@ taglib uri="tags-core" prefix="c"%>

<c:if test="${gateForm.previewLesson}">	
	<lams:Alert5 type="info" id="preview-message">
		<em><fmt:message key="label.gate.preview.message" /></em>
	</lams:Alert5>
</c:if>

<c:if test="${!empty gateForm.gate.description}">
	<div class="instructions" id="instructions">
		<lams:out value="${gateForm.gate.description}" escapeHtml="true" />
	</div>
</c:if>

