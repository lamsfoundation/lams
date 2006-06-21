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
		<html:hidden property="authSessionId" />

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

		<c:set var="defineLater" value="no" />
		<c:if test="${requestScope.mode == 'teacher'}">
			<c:set var="defineLater" value="yes" />
		</c:if>

		<%-- Form Controls --%>
		<!-- Button Row -->
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lachat11" cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save" toolContentID="${formBean.toolContentID}"
			accessMode="${requestScope.mode}" defineLater="${defineLater}" />
	</html:form>
</div>

<div id="footer"></div>

<lams:HTMLEditor />




