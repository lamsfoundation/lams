<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" />
	</title>
</lams:head>

<body class="stripes">

	<html:form action="/learning?method=displayMc&validate=false" styleId="reflectionForm" method="POST">
		<html:hidden property="toolContentID" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="submitReflection" />
		<div id="content">

			<h1>
				<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
			</h1>

			<p>
				<lams:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area">
				<c:if test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
					<lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true"/>		
				</c:if>
			</html:textarea>

			<div class="space-bottom-top align-right">
			    <html:link href="#" property="submitReflection" styleClass="button"
			   		onclick="javascript:document.McLearningForm.submit();return false">
					<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
			    </html:link>
			</div>

		</div>
	</html:form>

</body>
</lams:html>
