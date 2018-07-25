<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript">
	function prepareFormData(){
		//CKeditor content is not submitted when sending by jQuery; we need to do this
		var content = CKEDITOR.instances.instruction.getData();
		document.getElementById("instruction").value=content;
	}
	</script>
</lams:head>
<body style="width: 550px">
	<h4 class="space-left"><fmt:message key="label.authoring.basic.instruction" /></h4>
	<form:form action="saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="plannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<lams:CKEditor id="instruction"
			value="${pedagogicalPlannerForm.instruction}"
			contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
            toolbarSet="CustomPedplanner" height="190px"
            width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>
