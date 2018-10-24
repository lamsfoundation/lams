<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	
	<lams:css/>
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
		
		body {
			width: ${param.plannerCKEditorLongWidth};
		}
		
		table#itemTable td {
			margin: 0px;
			padding: 5px 0px 5px 0px;
			border-bottom: thin inset;
		}
		
		img.clearEntry {
			maring: 0px;
			padding: 0px;
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
  	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
	  	function prepareFormData(){
			//CKeditor content is not submitted when sending by jQuery; we need to do this
			var content = CKEDITOR.instances.instructions.getData();
			document.getElementById("instructions").value=content;
		}
  	</script>
</lams:head>
<body id="body">
	<lams:errors/>
	<form:form enctype="multipart/form-data" modelAttribute="pedagogicalPlannerForm" action="/lams/tool/lascrt11/authoring/saveOrUpdatePedagogicalPlannerForm.do" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.instructions"/></h4>
		<lams:CKEditor id="instructions"
			value="${pedagogicalPlannerForm.instructions}"
			contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
               toolbarSet="CustomPedplanner" height="150px"
               width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>
