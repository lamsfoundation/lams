<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>

<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="toolContentID" />
	<html:hidden property="currentTab" styleId="currentTab" />

	<lams:Tabs useKey="true" control="true">
		<lams:Tab id="1" key="authoring.tab.basic" />
		<lams:Tab id="2" key="authoring.tab.advanced" />
		<lams:Tab id="3" key="authoring.tab.instructions" />
	</lams:Tabs>

	<div class="tabbody">
		<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
		<lams:TabBody id="2" titleKey="authoring.tab.advanced" page="advance.jsp" />
		<lams:TabBody id="3" titleKey="authoring.tab.instructions" page="instructions.jsp" />
		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" />
		
	</div>

</html:form>

<lams:HTMLEditor/>