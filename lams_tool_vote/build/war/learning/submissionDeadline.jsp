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

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>

<lams:Page type="learner" title="${title}">

	<html:form action="/learning?validate=false" method="POST">
		<html:hidden property="dispatch" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
			
			<lams:Alert id="submissionDeadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert>
			
				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true'}">

					<html:link href="#nogo" property="endLearning" styleId="finishButton" 
				      onclick="javascript:submitMethod('learnerFinished');return false"
				      styleClass="btn btn-primary voffset10 pull-right na">
							<c:choose>
								<c:when test="${activityPosition.last}">
									<fmt:message key="button.submitActivity" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
					</html:link>

				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}">
					<html:submit property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="btn btn-primary voffset10 pull-right">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:if>

	</html:form>
</lams:Page>
</body>
</lams:html>
