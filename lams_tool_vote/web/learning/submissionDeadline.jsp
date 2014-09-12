<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

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

	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>

</lams:head>

<body class="stripes">

	<html:form action="/learning?validate=false" method="POST">
		<html:hidden property="dispatch" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />

		<div id="content">

			<h1>
				<fmt:message key="activity.title" />
			</h1>
			
			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
			
			<div class="space-bottom-top align-right">

				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true'}">

					<html:link href="#nogo" property="endLearning" styleId="finishButton" 
				      onclick="javascript:submitMethod('learnerFinished');return false"
				      styleClass="button">
						<span class="nextActivity">
							<c:choose>
								<c:when test="${activityPosition.last}">
									<fmt:message key="button.submitActivity" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
						</span>
					</html:link>

				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}">
					<html:submit property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:if>

			</div>

		</div>
	</html:form>

</body>
</lams:html>
