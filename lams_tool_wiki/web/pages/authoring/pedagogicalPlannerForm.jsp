<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css style="core" />
	<style type="text/css">
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
	</style>
	
	<script type="text/javascript">
	function prepareFormData(){
		//FCKeditor content is not submitted when sending by jQuery; we need to do this
		var content = FCKeditorAPI.GetInstance('wikiBody').GetXHTML();
		document.getElementById("wikiBody").value=content;
	}
	</script>
</lams:head>
<body>
	<logic:messagesPresent>
		<p class="warning">
	        <html:messages id="error">
	            <c:out value="${error}" escapeXml="false"/><br/>
	        </html:messages>
		</p>
	</logic:messagesPresent>	
	
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<h4 class="space-left"><fmt:message key="label.authoring.basic.title" /></h4>
		<html:text property="title" size="80" styleClass="item" />
		<h4 class="space-left"><fmt:message key="label.wiki.body" /></h4>
		
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<lams:FCKEditor id="wikiBody"
			value="${formBean.wikiBody}"
			contentFolderID="${formBean.contentFolderID}"
			toolbarSet="Custom-Wiki" height="200px">
		</lams:FCKEditor>
	</html:form>
</body>
</lams:html>