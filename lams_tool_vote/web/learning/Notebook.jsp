<!DOCTYPE html>

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
	<title><fmt:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>

</lams:head>

<body class="stripes">
	<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">

		<html:hidden property="dispatch" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="userLeader" />
		<html:hidden property="groupLeaderName" />
		<html:hidden property="useSelectLeaderToolOuput" />

		<div id="content">
			<h1>
				<lams:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeHtml="true" />
			</h1>

			<p>
				<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true"/>

			</p>

			<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area"></html:textarea>

			<div class="space-bottom-top align-right">
				<html:link href="#" property="submitReflection"
					onclick="javascript:submitMethod('submitReflection');return false"
					styleClass="button" styleId="finishButton">
					<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
				</html:link>
			</div>

		</div>
	</html:form>
</body>
</lams:html>








