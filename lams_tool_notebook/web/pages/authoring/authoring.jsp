<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	
<lams:Page title="${title}" type="navbar">

	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= NotebookConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="button.basic" />
		<lams:Tab id="2" key="button.advanced" />
		<lams:Tab id="3" key="button.conditions" />
	</lams:Tabs>  
	
	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
	   
	    <!--  Set up tabs  -->
	     <lams:TabBodys>
			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			<lams:TabBody id="3" titleKey="button.conditions" page="conditions.jsp" />
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
</html:form>

