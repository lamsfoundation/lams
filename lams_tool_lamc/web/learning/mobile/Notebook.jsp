<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div><!-- /header -->

	<html:form action="/learning?method=displayMc&validate=false"
		styleId="reflectionForm" method="POST">
		<div data-role="content">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="submitReflection" />
		
			<p>
				<lams:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area">
				<c:if test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
					<lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true"/>
				</c:if>
			</html:textarea>
		</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<a href="#" name="submitReflection"
				onclick="javascript:document.McLearningForm.submit();return false" data-role="button" data-icon="arrow-r" data-theme="b">
				<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
			</a>
		</span>
	</div><!-- /footer -->
	</html:form>

</div>
</body>
</lams:html>








