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
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet" />

	<title><fmt:message key="activity.title" /></title>

	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	
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

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
			</c:if>

			<div class="row">
				<div class="col-xs12">
					<div id="revisitedContent" class="panel-body">


						<c:if test="${voteLearningForm.showResults == 'true'}">
							<jsp:include page="/learning/RevisitedDisplay.jsp" />
						</c:if>

						<c:if test="${voteLearningForm.showResults != 'true'}">
							<jsp:include page="/learning/RevisitedNoDisplay.jsp" />
						</c:if>

						<c:if test="${hasEditRight}">
							<input type="submit" name="redoQuestionsOk" class="btn btn-sm btn-default voffset10 pull-left"
								onclick="submitMethod('redoQuestionsOk');"
								value='<fmt:message key="label.retake" />' />
						</c:if>
					</div>
				</div>
			</div>

			<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}">
			<hr class="msg-hr">
				<div class="panel panel-default">
					<div class="panel-heading panel-title">
						<fmt:message key="label.reflection" />
					</div>
					<div class="panel-body">

						<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />

						<div class="panel-body voffset5 bg-warning">
							<lams:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true" />
						</div>

						<c:if test="${voteGeneralLearnerFlowDTO.lockOnFinish == 'false' && hasEditRight}">
							<form:button path="forwardtoReflection" cssClass="btn btn-sm btn-default voffset10 pull-left"
								onclick="submitMethod('forwardtoReflection');">
								<fmt:message key="label.edit" />
							</form:button>
						</c:if>

					</div>
				</div>
			</c:if>


			<button type="button" id="finishButton"
				class="btn btn-primary voffset10 pull-right na">
				<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="button.submitActivity" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.endLearning" />
						</c:otherwise>
				</c:choose>
			</button>
			<div id="footer"></div>

		</lams:Page>
	</form:form>



</body>
</lams:html>