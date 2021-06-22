<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<lams:html>
	<lams:head>
		<lams:headItems/>
		<lams:JSImport src="includes/javascript/authoring.js" relative="true" />
	</lams:head>
	<body class="stripes">
		<form:form action="/lams/tool/lantbk11/authoring/updateContent.do" modelAttribute="authoringForm" id="authoringForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
			<form:hidden path="sessionMapID" />
	
		<lams:Page title="${title}" type="navbar" formID="authoringForm">

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= NotebookConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="button.basic" />
			<lams:Tab id="2" key="button.advanced" />
			<lams:Tab id="3" key="button.conditions" />
		</lams:Tabs>  
	
		<lams:TabBodyArea>
		<lams:errors/>
	   
	    <!--  Set up tabs  -->
	     <lams:TabBodys>
			<lams:TabBody id="1" page="basic.jsp" />
			<lams:TabBody id="2" page="advanced.jsp" />
			<lams:TabBody id="3" page="conditions.jsp" />
	    </lams:TabBodys>


		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do"
			toolSignature="<%=NotebookConstants.TOOL_SIGNATURE%>"
			cancelButtonLabelKey="button.cancel"
			saveButtonLabelKey="button.save"
			accessMode="${sessionMap.mode}"
			defineLater="${sessionMap.mode == 'teacher'}"
			toolContentID="${sessionMap.toolContentID}"
			customiseSessionID="${sessionMap.sessionID}" 
			contentFolderID="${sessionMap.contentFolderID}" />
	</lams:TabBodyArea>
	
	<div id="footer"></div>
	
</lams:Page>
</form:form>
		<div class="footer"></div>					
	</body>
</lams:html>
