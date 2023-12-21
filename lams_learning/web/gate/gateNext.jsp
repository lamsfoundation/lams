<%@ include file="/common/taglibs.jsp"%>
<c:set var="formAction" value="knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID}${(gateForm.previewLesson == true) ? '&force=true' : ''}" />

<script>
	function submitGateForm(){
		if (typeof customSubmitGateForm == 'function') {
			return customSubmitGateForm();
		}
		return true;
	}
</script>

<div class="activity-bottom-buttons mt-3">
	<form:form action="${formAction}" modelAttribute="gateForm" target="_self" onSubmit="javascript:submitGateForm()" method="post">
		<form:hidden path="key" />
		<button type="submit" class="btn btn-primary na" id="submit-form-button">
			<fmt:message key="label.next.button" />
		</button>
	</form:form>
</div>
