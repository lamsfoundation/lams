<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.voteLearningForm.action = actionMethod + '.do';
			document.forms.voteLearningForm.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<form:form modelAttribute="voteLearningForm" method="POST">
		<c:set var="isUserLeader" value="${voteLearningForm.userLeader}" />
		<c:set var="isLeadershipEnabled" value="${voteLearningForm.useSelectLeaderToolOuput}" />
		<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

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

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">

			<c:if test="${voteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<lams:Alert id="lockWhenFinished" type="info" close="false">
					<fmt:message key="message.warnLockOnFinish" />
				</lams:Alert>
			</c:if>

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
			</c:if>

			<div class="row">
				<div class="col-12">
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<fmt:message key="label.learning.reportMessage" />
						</div>
						<div class="panel-body">

							<c:forEach var="entry" items="${requestScope.mapGeneralCheckedOptionsContent}">
								<div class="media">
									<div class="media-left">
										<i class="fa fa-xs fa-check text-success"></i>
									</div>
									<div class="media-body">
										<c:out value="${entry.value}" escapeXml="false" />
									</div>
								</div>
							</c:forEach>

							<c:if test="${not empty voteLearningForm.userEntry}">
								<div class="media">
									<div class="media-left">
										<i class="fa fa-xs fa-check text-success"></i>
									</div>
									<div class="media-body">
										<c:out value="${voteLearningForm.userEntry}" escapeXml="true" />
									</div>
								</div>
							</c:if>

							<c:if test="${hasEditRight}">
								<input type="submit" name="redoQuestions" class="btn btn-sm btn-default pull-left voffset10"
									onclick="submitMethod('redoQuestions');"
									value='<fmt:message key="label.retake" />?' />
							</c:if>

						</div>
					</div>

				</div>
			</div>


			<c:choose>
				<c:when test="${voteLearningForm.showResults=='true'}">
					<input type="submit" name="viewAllResults" class="btn btn-primary pull-right"
						onclick="submitMethod('viewAllResults');"
						value='<fmt:message key="label.overAllResults" />' />
				</c:when>

				<c:when test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
					<button type="submit" name="learnerFinished" id="finishButton" onclick="javascript:submitMethod('learnerFinished')"
						class="btn btn-primary voffset10 pull-right">
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
					<input type="submit" name="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');"
						class="btn btn-primary pull-right"
						value='<fmt:message key="label.continue" />' />
				</c:otherwise>
			</c:choose>

		</lams:Page>
	</form:form>

	</div>
	<div id="footer"></div>
</body>
</lams:html>
