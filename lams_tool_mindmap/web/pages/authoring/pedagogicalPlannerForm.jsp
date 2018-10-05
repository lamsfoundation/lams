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
	<form:form action="saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="plannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		<br />
		
		<lams:CKEditor id="instructions"
			value="${pedagogicalPlannerForm.instructions}"
			contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
			toolbarSet="CustomPedplanner" height="150px"
			width="545px" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>
