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
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" styleId="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<lams:CKEditor id="basicContent"
			value="${formBean.basicContent}"
			contentFolderID="${formBean.contentFolderID}"
               toolbarSet="CustomPedplanner" height="190px"
               width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
	</html:form>
</body>
</lams:html>