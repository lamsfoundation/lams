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
			document.VoteLearningForm.action += "&dispatch=" + actionMethod;
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
		<html:hidden property="groupLeaderUserId" />
		<html:hidden property="useSelectLeaderToolOuput" />

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">

			<c:if test="${VoteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
				<lams:Alert id="lockWhenFinished" type="info" close="false">
					<fmt:message key="message.warnLockOnFinish" />
				</lams:Alert>
			</c:if>

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${formBean.groupLeaderName}" userId="${formBean.groupLeaderUserId}"/>
			</c:if>

			<div class="row">
				<div class="col-xs-12">
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

							<c:if test="${not empty VoteLearningForm.userEntry}">
								<div class="media">
									<div class="media-left">
										<i class="fa fa-xs fa-check text-success"></i>
									</div>
									<div class="media-body">
										<c:out value="${VoteLearningForm.userEntry}" escapeXml="true" />
									</div>
								</div>
							</c:if>

							<c:if test="${hasEditRight}">
								<html:submit property="redoQuestions" styleClass="btn btn-sm btn-default pull-left voffset10"
									onclick="submitMethod('redoQuestions');">
									<fmt:message key="label.retake" />?
							</html:submit>
							</c:if>

						</div>
					</div>

				</div>
			</div>


			<c:choose>
				<c:when test="${VoteLearningForm.showResults=='true'}">
					<html:submit property="viewAllResults" styleClass="btn btn-primary pull-right"
						onclick="submitMethod('viewAllResults');">
						<fmt:message key="label.overAllResults" />
					</html:submit>
				</c:when>

				<c:when test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
					<html:submit property="learnerFinished" styleId="finishButton" onclick="javascript:submitMethod('learnerFinished')"
						styleClass="btn btn-primary voffset10 pull-right n	a">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submitActivity" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.endLearning" />
							</c:otherwise>
						</c:choose>
					</html:submit>
				</c:when>

				<c:otherwise>
					<html:submit property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:otherwise>
			</c:choose>

		</lams:Page>
	</html:form>

	</div>
	<div id="footer"></div>
</body>
</lams:html>
