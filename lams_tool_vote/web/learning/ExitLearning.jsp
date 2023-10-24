<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL /></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL /></c:set>
<c:set var="isUserLeader" value="${voteLearningForm.userLeader}" />
<c:set var="isLeadershipEnabled" value="${voteLearningForm.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:PageLearner title="${voteGeneralLearnerFlowDTO.activityTitle}" toolSessionID="${voteLearningForm.toolSessionID}">
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet" />

	<!--<lams:JSImport src="includes/javascript/common.js" />-->
	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
	<script type="text/javascript">
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.voteLearningForm.action = actionMethod + '.do';
			document.forms.voteLearningForm.submit();
		}
	</script>

	<div id="container-main">
		<form:form modelAttribute="voteLearningForm" method="POST">
			<form:hidden path="toolSessionID" />
			<form:hidden path="userID" />
			<form:hidden path="revisitingUser" />
			<form:hidden path="previewOnly" />
			<form:hidden path="maxNominationCount" />
			<form:hidden path="minNominationCount" />
			<form:hidden path="allowTextEntry" />
			<form:hidden path="lockOnFinish" />
			<form:hidden path="reportViewOnly" />
			<form:hidden path="userEntry" />
			<form:hidden path="showResults" />
			<form:hidden path="userLeader" />
			<form:hidden path="groupLeaderName" />
			<form:hidden path="groupLeaderUserId" />
			<form:hidden path="useSelectLeaderToolOuput" />

			<c:if test="${voteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<lams:Alert5 id="lockedActivity" type="info" close="false">
					<fmt:message key="message.activityLocked" />
				</lams:Alert5>
			</c:if>

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
			</c:if>

			<c:if test="${voteLearningForm.showResults == 'true'}">
				<jsp:include page="/learning/RevisitedDisplay.jsp" />
			</c:if>

			<c:if test="${voteLearningForm.showResults != 'true'}">
				<jsp:include page="/learning/RevisitedNoDisplay.jsp" />
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.reflection}">
				<lams:NotebookReedit
					reflectInstructions="${voteGeneralLearnerFlowDTO.reflectionSubject}"
					reflectEntry="${voteGeneralLearnerFlowDTO.notebookEntry}"
					isEditButtonEnabled="${voteGeneralLearnerFlowDTO.lockOnFinish == 'false' && hasEditRight && voteGeneralLearnerFlowDTO.learningMode != 'teacher'}"
					notebookHeaderLabelKey="label.reflection"/>
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<div class="activity-bottom-buttons">
					<button type="submit" class="btn btn-primary na" id="finishButton" onclick="submitMethod('learnerFinished')">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submitActivity" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.endLearning" />
							</c:otherwise>
						</c:choose>
					</button>
				</div>
			</c:if>

		</form:form>
	</div>
</lams:PageLearner>
