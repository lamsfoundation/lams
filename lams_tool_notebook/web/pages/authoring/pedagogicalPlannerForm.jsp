<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript">
	function prepareFormData(){
		//CKeditor content is not submitted when sending by jQuery; we need to do this
		var content = CKEDITOR.instances.instructions.getData();
		document.getElementById("instructions").value=content;
	}
	</script>
</lams:head>
<body style="width: 550px">
	<lams:errors/>
	<h4 class="space-left" style="float:left"><fmt:message key="label.authoring.basic.instructions" /></h4>
	<form:form action="/pedagogicalPlanner/saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="pedagogicalPlannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" i="vdalid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		<br />
		
		<lams:CKEditor id="instructions"
			value="${authoringForm.instructions}"
			contentFolderID="${authoringForm.contentFolderID}"
			toolbarSet="CustomPedplanner" height="190px"
			width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>
