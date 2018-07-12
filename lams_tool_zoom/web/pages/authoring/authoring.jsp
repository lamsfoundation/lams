<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.zoom.util.ZoomConstants"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">

	<c:set var="title">
		<fmt:message key="activity.title" />
	</c:set>

	<!--  TITLE KEY PAGE GOES HERE -->
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ZoomConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="button.basic" />
		<lams:Tab id="2" key="button.advanced" />
	</lams:Tabs>
	<!--closes header-->

	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />

	<lams:TabBodyArea>
		<logic:messagesPresent>
			<lams:Alert id="errorMessages" type="danger" close="false">
				<html:messages id="error">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</html:messages>
			</lams:Alert>
		</logic:messagesPresent>

		<%-- Page tabs --%>
		<lams:TabBodys>
			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		</lams:TabBodys>

		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lazoom10"
			cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save" toolContentID="${sessionMap.toolContentID}"
			accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode == 'teacher'}" customiseSessionID="${sessionMap.sessionID}"
			contentFolderID="${sessionMap.contentFolderID}" />

	</lams:TabBodyArea>
	<div id="footer"></div>
</html:form>
