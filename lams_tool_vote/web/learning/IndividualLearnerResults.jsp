<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="isUserLeader" value="${voteLearningForm.userLeader}" />
<c:set var="isLeadershipEnabled" value="${voteLearningForm.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:PageLearner title="${voteGeneralLearnerFlowDTO.activityTitle}" toolSessionID="${voteLearningForm.toolSessionID}">
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${voteLearningForm.toolSessionID}', '', function() {
			submitMethod('learnerFinished');
		});
		
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
				<lams:Alert5 id="lockWhenFinished" type="info" close="false">
					<fmt:message key="message.warnLockOnFinish" />
				</lams:Alert5>
			</c:if>

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
			</c:if>

			<div class="card lcard">
				<div class="card-header">
					<fmt:message key="label.learning.reportMessage" />
				</div>

				<div class="card-body">
					<c:forEach var="entry"
						items="${requestScope.mapGeneralCheckedOptionsContent}">
						<div class="d-flex">
							<div class="flex-shrink-0">
								<i class="fa fa-check text-success"></i>
							</div>
							<div class="flex-grow-1 ms-3">
								<c:out value="${entry.value}" escapeXml="false" />
							</div>
						</div>
					</c:forEach>

					<c:if test="${not empty voteLearningForm.userEntry}">
						<div class="d-flex">
							<div class="flex-shrink-0">
								<i class="fa fa-check text-success"></i>
							</div>
							<div class="flex-grow-1 ms-3">
								<c:out value="${voteLearningForm.userEntry}" escapeXml="true" />
							</div>
						</div>
					</c:if>

					<c:if test="${hasEditRight}">
						<button type="button" name="redoQuestions"
							class="btn btn-sm btn-secondary btn-icon-return float-end mt-2"
							onclick="submitMethod('redoQuestions');">
							<fmt:message key="label.retake" />
						</button>
					</c:if>
				</div>
			</div>

			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${voteLearningForm.showResults=='true'}">
						<button type="button" name="viewAllResults" class="btn btn-primary na" onclick="submitMethod('viewAllResults');">
							<fmt:message key="label.overAllResults" />
						</button>
					</c:when>
	
					<c:when test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
						<button type="button" name="learnerFinished" id="finishButton" class="btn btn-primary na">
							<c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submitActivity" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
						</button>
					</c:when>
	
					<c:otherwise>
						<button type="button" name="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:otherwise>
				</c:choose>
			</div>

		</form:form>
	</div>
</lams:PageLearner>
