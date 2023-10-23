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

<div class="col-sm-6 float-end">
	<c:if test="${gateForm.monitorCanOpenGate}">
		<lams:Alert5 type="info" id="refresh-message">	
			<fmt:message key="label.gate.refresh.message" />
		</lams:Alert5>
	</c:if>

	<c:if test="${gateForm.previewLesson == true}">	
		<lams:Alert5 type="info" id="preview-message">
			<em><fmt:message key="label.gate.preview.message" /></em>
		</lams:Alert5>
	</c:if>
</div>

<div class="activity-bottom-buttons mt-3">
	<form:form action="${formAction}" modelAttribute="gateForm" target="_self" onSubmit="javascript:submitGateForm()" method="post">
		<form:hidden path="key" />
		<button class="btn btn-primary na">
			<fmt:message key="label.next.button" />
		</button>
	</form:form>
</div>
