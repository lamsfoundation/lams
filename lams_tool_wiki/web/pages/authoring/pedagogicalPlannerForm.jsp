<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css/>
	<style type="text/css">
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
		
		body {
			width: ${param.plannerCKEditorLongWidth};
		}
	</style>
	
	<script type="text/javascript">
	function prepareFormData(){
		//CKeditor content is not submitted when sending by jQuery; we need to do this
		var content = CKEDITOR.instances["wikiBody"].getData();
		document.getElementById("wikiBody").value=content;
	}
	</script>
</lams:head>
<body>
	
	<form:form action="saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="plannerForm" id="plannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		<h4 class="space-left"><fmt:message key="label.authoring.basic.title" /></h4>
		<input type="text" name="title" size="80" class="item" />
		<h4 class="space-left"><fmt:message key="label.wiki.body" /></h4>
		
		<lams:CKEditor id="wikiBody"
			value="${plannerForm.wikiBody}"
			contentFolderID="${plannerForm.contentFolderID}"
			toolbarSet="CustomPedplanner" height="150px"
			width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</form:form>
</body>
</lams:html>