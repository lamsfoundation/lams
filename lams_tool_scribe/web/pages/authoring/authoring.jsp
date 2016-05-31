<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scribe.util.ScribeConstants"%>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

	<c:set var="defineLater" value="no" />
	<c:if test="${sessionMap.mode == 'teacher'}">
		<c:set var="defineLater" value="yes" />
	</c:if>

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ScribeConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="button.basic" />
				<c:if test="${sessionMap.mode == 'author'}">
					<lams:Tab id="2" key="button.advanced" />
				</c:if>
		</lams:Tabs>

		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" value="updateContent" />
		<html:hidden property="sessionMapID" />

		
        <lams:TabBodyArea>
			<div id="message" style="text-align: center;">
				<logic:messagesPresent>
					<lams:Alert id="errorMessages" type="danger" close="false">
					        <html:messages id="error">
					            <c:out value="${error}" escapeXml="false"/><br/>
					        </html:messages>
					</lams:Alert>
				</logic:messagesPresent>			
			</div>
	
			<%-- Page tabs --%>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
				<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			</lams:TabBodys>

			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do" toolSignature="lascrb11"
				cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
				toolContentID="${sessionMap.toolContentID}"
				accessMode="${sessionMap.mode}" defineLater="${defineLater}"
				customiseSessionID="${sessionMap.sessionID}"
				contentFolderID="${sessionMap.contentFolderID}" />
		</lams:TabBodyArea>

		<div id="footer"></div>

	</lams:Page>

</html:form>
