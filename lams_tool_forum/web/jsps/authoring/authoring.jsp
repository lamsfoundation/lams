<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>

<html:form action="authoring/update" method="post"
	styleId="authoringForm" enctype="multipart/form-data">
	<html:hidden property="toolContentID" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="contentFolderID" />
	<html:hidden property="currentTab" styleId="currentTab" />
	<input type="hidden" name="mode" value="author">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<div id="header">
		<lams:Tabs useKey="true" control="true">
			<lams:Tab id="1" key="authoring.tab.basic" />
			<lams:Tab id="2" key="authoring.tab.advanced" />
			<lams:Tab id="3" key="authoring.tab.conditions" />
		</lams:Tabs>
	</div>
	<div id="content">

		<%@ include file="/common/messages.jsp"%>
		
		<lams:help toolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" module="authoring"/>

		<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
		<lams:TabBody id="2" titleKey="authoring.tab.advanced" page="advance.jsp" />
		<lams:TabBody id="3" titleKey="authoring.tab.conditions" page="conditions.jsp" />
		
		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do"
			toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>"
			toolContentID="${formBean.toolContentID}"
			customiseSessionID="${formBean.sessionMapID}"
			contentFolderID="${formBean.contentFolderID}" />
	</div>
</html:form>
