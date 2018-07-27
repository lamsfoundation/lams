<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
</lams:head>

<body class="stripes">

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}">

		<div class="panel">
			<lams:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
		</div>

		<div class="form-group">
			<html:form action="/learning?method=displayMc&validate=false" styleId="reflectionForm" method="POST">
				<html:hidden property="toolContentID" />
				<html:hidden property="toolSessionID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="userID" />
				<html:hidden property="submitReflection" />
				<html:textarea rows="4" property="entryText" styleClass="form-control" styleId="focusedInput">
					<c:if test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
						<lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true" />
					</c:if>
				</html:textarea>


				<html:link href="#" property="submitReflection" styleClass="btn btn-primary pull-right voffset10 na"
					onclick="javascript:document.McLearningForm.submit();return false">
					<fmt:message key="button.endLearning" />
				</html:link>

			</html:form>
	</lams:Page>
	
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focusedInput").focus();
		}
	</script>

</body>
</lams:html>
