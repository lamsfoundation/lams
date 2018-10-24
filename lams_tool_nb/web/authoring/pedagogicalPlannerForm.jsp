<!DOCTYPE html>

<%@ include file="/includes/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript">
		function prepareFormData(){
			//CKeditor content is not submitted when sending by jQuery; we need to do this
			var basicContent = CKEDITOR.instances.basicContent.getData();
			document.getElementById("basicContent").value=basicContent;
		}
	</script>
</lams:head>
<body style="width: 550px">
	<h4 class="space-left"><fmt:message key="basic.content" /></h4>
	<form:form action="/pedagogicalPlanner/saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="pedagogicalPlannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<lams:CKEditor id="basicContent"
			value="${NbPedagogicalPlannerForm.basicContent}"
			contentFolderID="${NbPedagogicalPlannerForm.contentFolderID}"
               toolbarSet="CustomPedplanner" height="190px"
               width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>