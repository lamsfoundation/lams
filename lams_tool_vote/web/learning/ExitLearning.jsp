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
	<link type="text/css" href="${lams}/css/chart.css" rel="stylesheet" />

	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>

	<script type="text/javascript">
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.VoteLearningForm.dispatch.value = actionMethod;
			document.VoteLearningForm.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<c:set var="formBean" value="<%=request
							.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<c:set var="isUserLeader" value="${formBean.userLeader}" />
		<c:set var="isLeadershipEnabled" value="${formBean.useSelectLeaderToolOuput}" />
		<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

		<html:hidden property="dispatch" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="revisitingUser" />
		<html:hidden property="previewOnly" />
		<html:hidden property="maxNominationCount" />
		<html:hidden property="minNominationCount" />
		<html:hidden property="allowTextEntry" />
		<html:hidden property="lockOnFinish" />
		<html:hidden property="reportViewOnly" />
		<html:hidden property="userEntry" />
		<html:hidden property="showResults" />
		<html:hidden property="userLeader" />
		<html:hidden property="groupLeaderName" />
		<html:hidden property="useSelectLeaderToolOuput" />

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">

			<c:if test="${VoteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<lams:Alert id="lockedActivity" type="info" close="false">
					<fmt:message key="message.activityLocked" />
				</lams:Alert>
			</c:if>

			<c:if test="${isLeadershipEnabled}">
				<lams:Alert id="leaderEnabled" type="info" close="false">
					<fmt:message key="label.group.leader">
						<fmt:param>${formBean.groupLeaderName}</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<c:if test="${VoteLearningForm.showResults == 'true'}">
				<jsp:include page="/learning/RevisitedDisplay.jsp" />
			</c:if>

			<c:if test="${VoteLearningForm.showResults != 'true'}">
				<jsp:include page="/learning/RevisitedNoDisplay.jsp" />
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.reflection}">
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

						<c:if
							test="${voteGeneralLearnerFlowDTO.lockOnFinish == 'false' && hasEditRight && voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
							<html:button property="forwardtoReflection" styleClass="btn btn-sm btn-default voffset10 pull-left"
								onclick="submitMethod('forwardtoReflection');">
								<fmt:message key="label.edit" />
							</html:button>
						</c:if>

					</div>
				</div>
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<html:link href="#nogo" property="learnerFinished" styleClass="btn btn-primary voffset10 pull-right na"
					styleId="finishButton" onclick="submitMethod('learnerFinished');return false">
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
			<div id="footer"></div>

		</lams:Page>
	</html:form>



</body>
</lams:html>
