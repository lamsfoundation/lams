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
	<lams:css />
	<title><fmt:message key="activity.title" />
	</title>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.voteLearningForm.action=actionMethod + '.do'; 
			document.forms.voteLearningForm.submit();
		}
	</script>

</lams:head>

<body class="stripes">

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>

<lams:Page type="learner" title="${title}">

	<form:form modelAttribute="voteLearningForm" method="POST">
		<form:hidden path="toolSessionID" />
		<form:hidden path="userID" />
			
			<lams:Alert id="submissionDeadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert>
			
			<div class="activity-bottom-buttons">
				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true'}">

					<button type="submit"  id="finishButton" 
				      onclick="javascript:submitMethod('learnerFinished')"
				      class="btn btn-primary na">
							<c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submitActivity" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
					</button>

				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}">
					<input type="submit" name="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						class="btn btn-primary"
						value='<fmt:message key="label.continue" />' />
				</c:if>
			</div>

	</form:form>
</lams:Page>
</body>
</lams:html>
