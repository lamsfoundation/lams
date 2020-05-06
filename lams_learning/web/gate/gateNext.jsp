<c:set var="formAction" value="knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID }" />
<c:if test="${gateForm.monitorCanOpenGate}">
	<div class="voffset5">
		<fmt:message key="label.gate.refresh.message" />
	</div>
</c:if>

<c:if test="${gateForm.previewLesson == true}">
	<div class="voffset5">
		<c:set var="formAction" value="${formAction}&force=true"/>
		<em><fmt:message key="label.gate.preview.message" /></em>
	</div>
</c:if>

<script>
	function submitGateForm(){
		if (typeof customSubmitGateForm == 'function') {
			return customSubmitGateForm();
		}
		return true;
	}
</script>
<form:form action="${formAction}" modelAttribute="gateForm" target="_self" onSubmit="javascript:submitGateForm()" method="post">
	<form:hidden path="key" />
	<div class="voffset10 pull-right">
		<button class="btn btn-primary">
			<fmt:message key="label.next.button" />
		</button>
	</div>
</form:form>
