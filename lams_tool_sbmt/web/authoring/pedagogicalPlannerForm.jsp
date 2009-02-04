<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css style="core" />
	
	<script type="text/javascript">
	function prepareFormData(){
		//FCKeditor content is not submitted when sending by jQuery; we need to do this
		var content = FCKeditorAPI.GetInstance('instruction').GetXHTML();
		document.getElementById("instruction").value=content;
	}
	</script>
</lams:head>
<body style="width: 550px">
	<h4 class="space-left"><fmt:message key="label.authoring.basic.instruction" /></h4>
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<lams:FCKEditor id="instruction"
			value="${formBean.instruction}"
			contentFolderID="${formBean.contentFolderID}"
            toolbarSet="Custom-Pedplanner" height="150px"
            width="545px" displayExpanded="false">
		</lams:FCKEditor>
	</html:form>
</body>
</lams:html>
