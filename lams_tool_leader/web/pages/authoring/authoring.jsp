<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants"%>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

	<c:set var="defineLater" value="no" />
	<c:if test="${sessionMap.mode == 'teacher'}">
		<c:set var="defineLater" value="yes" />
	</c:if>

	<div id="header">

		<!--  TITLE KEY PAGE GOES HERE -->
		<lams:Tabs control="true">
			<lams:Tab id="1" key="button.basic" />
		</lams:Tabs>

	</div>
	<!--closes header-->

	<div id="content">
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" value="updateContent" />
		<html:hidden property="sessionMapID" />

		<div id="message" style="text-align: center;">
			<logic:messagesPresent>
				<p class="warning">
				        <html:messages id="error">
				            <c:out value="${error}" escapeXml="false"/><br/>
				        </html:messages>
				</p>
			</logic:messagesPresent>			
		</div>

		<lams:help toolSignature="<%=LeaderselectionConstants.TOOL_SIGNATURE%>" module="authoring" />

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />

		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do" toolSignature="lalead11"
			cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
			toolContentID="${sessionMap.toolContentID}"
			accessMode="${sessionMap.mode}" defineLater="${defineLater}"
			customiseSessionID="${sessionMap.sessionID}" 
			contentFolderID="${sessionMap.contentFolderID}" />
	</div>
</html:form>

<div id="footer"></div>

