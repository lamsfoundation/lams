<%@ include file="/common/taglibs.jsp"%>

<div id="header">
	<lams:Tabs control="true">
		<lams:Tab id="1" key="button.basic" />
		<lams:Tab id="2" key="button.advanced" />
		<lams:Tab id="3" key="button.instructions" />
	</lams:Tabs>
</div>
<!--closes header-->

<div id="content">
	<html:form action="/authoring" styleId="authoringForm" method="post"
		enctype="multipart/form-data">
		<div>
			<html:hidden property="toolContentID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="dispatch" value="updateContent" />
			<html:hidden property="sessionMapID" />
		</div>

		<div id="message" style="text-align: center;">
			<c:if test="${unsavedChanges}">
				<fmt:message key="message.unsavedChanges" />
			</c:if>
		</div>

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
		<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		<lams:TabBody id="3" titleKey="button.instructions"
			page="instructions.jsp" />

		<c:set var="formBean"
			value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

		<c:set var="defineLater" value="no" />
		<c:if test="${sessionMap.mode == 'teacher'}">
			<c:set var="defineLater" value="yes" />
		</c:if>

		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do" toolSignature="lachat11"
			cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
			toolContentID="${formBean.toolContentID}"
			accessMode="${sessionMap.mode}" defineLater="${defineLater}"
			customiseSessionID="${sessionMap.sessionID}"
			contentFolderID="${formBean.contentFolderID}" />

	</html:form>
</div>
<!--closes content-->

<div id="footer"></div>
