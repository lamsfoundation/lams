<%@ include file="/common/taglibs.jsp"%>

<div id="header">

	<!--  TITLE KEY PAGE GOES HERE -->
	<lams:Tabs control="true">
		<lams:Tab id="1" key="button.basic" />
		<lams:Tab id="2" key="button.advanced" />
		<lams:Tab id="3" key="button.instructions" />
	</lams:Tabs>

</div>
<!--closes header-->

<div id="content">
	<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
		<html:hidden property="toolContentID" />
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" value="updateContent" />
		<html:hidden property="sessionMapID" />

		<div id="message" align="center">
			<c:if test="${unsavedChanges}">
				<img src="${tool}images/warning.png" />
				<bean:message key="message.unsavedChanges" />
			</c:if>
		</div>

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
		<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		<lams:TabBody id="3" titleKey="button.instructions" page="instructions.jsp" />

		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}"/>	

		<c:set var="defineLater" value="no" />
		<c:if test="${sessionMap.mode == 'teacher'}">
			<c:set var="defineLater" value="yes" />
		</c:if>
		
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lantbk11" cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save" toolContentID="${formBean.toolContentID}"
			accessMode="${sessionMap.mode}" defineLater="${defineLater}" customiseSessionID="${sessionMap.sessionID}"/>

	</html:form>
</div>

<div id="footer"></div>

<lams:HTMLEditor />
