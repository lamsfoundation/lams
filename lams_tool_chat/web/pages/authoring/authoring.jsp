<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.chat.util.ChatConstants"%>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ChatConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="button.basic" />
			<lams:Tab id="2" key="button.advanced" />
			<lams:Tab id="3" key="button.conditions" />
		</lams:Tabs>

		<lams:TabBodyArea>
		
			<div id="message" style="text-align: center;">
				<logic:messagesPresent>
					 <lams:Alert id="errorMessages" type="danger" close="false">
			            <c:out value="${error}" escapeXml="false"/><br/>
			         </lams:Alert>
				</logic:messagesPresent>
			</div>
		
			<%-- Page tabs --%>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
				<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
				<lams:TabBody id="3" titleKey="button.conditions" page="conditions.jsp" />
			</lams:TabBodys>
			
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do" toolSignature="lachat11"
				cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
				toolContentID="${sessionMap.toolContentID}"
				accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"
				customiseSessionID="${sessionMap.sessionID}"
				contentFolderID="${sessionMap.contentFolderID}" />
				
		</lams:TabBodyArea>

		<div id="footer"></div>

	</lams:Page>

</html:form>

