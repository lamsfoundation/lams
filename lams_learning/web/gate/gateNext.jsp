<c:set var="formAction"
	value="/gate?method=knockGate&activityID=${GateForm.map.activityID}&lessonID=${GateForm.map.lessonID }" />
<c:if test="${GateForm.map.monitorCanOpenGate}">
	<div class="voffset5">
		<fmt:message key="label.gate.refresh.message" />
	</div>
</c:if>

<c:if test="${GateForm.map.previewLesson == true}">
	<div class="voffset5">
		<c:set var="formAction">
			<c:out value="${formAction}" />&force=true</c:set>
		<em><fmt:message key="label.gate.preview.message" /></em>
	</div>
</c:if>

<html:form action="${formAction}" target="_self">
	<div class="voffset10 pull-right">
		<html:submit styleClass="btn btn-default">
			<fmt:message key="label.next.button" />
		</html:submit>
	</div>
</html:form>
