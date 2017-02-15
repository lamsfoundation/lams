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

	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>

	<script type="text/javascript">
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
		<html:hidden property="useSelectLeaderToolOuput" />

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">



			<c:if test="${isLeadershipEnabled}">
				<lams:Alert type="info" id="leaderEnabled" close="false">
					<fmt:message key="label.group.leader">
						<fmt:param>${formBean.groupLeaderName}</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<h4>
				<fmt:message key="label.progressiveResults" />
			</h4>

			<!--present  a mini summary table here -->

			<table class="table table-condensed table-hover">
				<tr>
					<th><fmt:message key="label.nomination" /></th>
					<th class="text-center"><fmt:message key="label.total.votes" /></th>
				</tr>

				<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
					<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
					<tr>
						<td class="text-left"><c:out value="${currentNomination.value}" escapeXml="false" /></td>

						<td class="text-center"><c:forEach var="currentUserCount"
								items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
								<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}" />
								<c:if test="${currentNominationKey == currentUserKey}">

									<c:if test="${currentUserCount.value != '0' }">
										<c:forEach var="currentQuestionUid" items="${voteGeneralLearnerFlowDTO.mapStandardQuestionUid}">
											<c:set var="currentQuestionUidKey" scope="request" value="${currentQuestionUid.key}" />
											<c:if test="${currentQuestionUidKey == currentUserKey}">

												<c:forEach var="currentSessionUid" items="${voteGeneralLearnerFlowDTO.mapStandardToolSessionUid}">
													<c:set var="currentSessionUidKey" scope="request" value="${currentSessionUid.key}" />
													<c:if test="${currentSessionUidKey == currentQuestionUidKey}">

														<c:if test="${currentNomination.value != 'Open Vote'}">
															<c:set scope="request" var="viewURL">
																<lams:WebAppURL />monitoring.do?method=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${currentSessionUid.value}
																						</c:set>

															<c:out value="${currentUserCount.value}" />

														</c:if>
														<c:if test="${currentNomination.value == 'Open Vote'}">
															<c:out value="${currentUserCount.value}" />
														</c:if>
													</c:if>
												</c:forEach>

											</c:if>
										</c:forEach>
									</c:if>
									<c:if test="${currentUserCount.value == '0' }">
										<c:out value="${currentUserCount.value}" />
									</c:if>

								</c:if>
							</c:forEach> <c:forEach var="currentRate" items="${voteGeneralLearnerFlowDTO.mapStandardRatesContent}">
								<c:set var="currentRateKey" scope="request" value="${currentRate.key}" />
								<c:if test="${currentNominationKey == currentRateKey}"> 				
									&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentRate.value}" />
									<fmt:message key="label.percent" />) 
								</c:if>
							</c:forEach></td>
					</tr>
				</c:forEach>

			</table>


			<div class="pull-right">
				<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${formBean.toolSessionID}" />
				<a title="<fmt:message key='label.tip.displayPieChart'/>"
					class="fa fa-pie-chart text-primary btn btn-md btn-primary"
					onclick="javascript:drawChart('pie', 'chartDiv', '${chartURL}')"></a> <a
					title="<fmt:message key='label.tip.displayBarChart'/>" class="fa fa-bar-chart text-primary btn btn-md btn-primary"
					onclick="javascript:drawChart('bar', 'chartDiv', '${chartURL}')"></a>
			</div>

			<div id="textEntries">
				<c:if test="${VoteLearningForm.allowTextEntry}">
					<strong><fmt:message key="label.open.votes" /> </strong>

					<c:forEach var="vote" items="${requestScope.listUserEntriesContent}">
						<c:if test="${vote.userEntry != ''}">
							<div class="media">
								<div class="media-left">
									<i class="fa fa-xs fa-check text-success"></i>
								</div>
								<div class="media-body">
						<c:choose>
						<c:when test="${vote.visible}">
							<c:out value="${vote.userEntry}" escapeXml="true" />
						</c:when>
						<c:otherwise>
							<em>(<fmt:message key="label.hidden"/>)</em>
						</c:otherwise>
						</c:choose>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</c:if>
			</div>
			<div id="nominations">
				<c:choose>
					<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
						<strong><fmt:message key="label.learner.nominations" /> </strong>
					</c:when>
					<c:otherwise>
						<strong><fmt:message key="label.learner.nomination" /> </strong>
					</c:otherwise>
				</c:choose>
				<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
					<div class="media">
						<div class="media-left">
							<i class="fa fa-xs fa-check text-success"></i>
						</div>
						<div class="media-body">
							<c:out value="${entry}" escapeXml="false" />
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
			</div>

			<c:if test="${voteGeneralLearnerFlowDTO.notebookEntry != null && voteGeneralLearnerFlowDTO.notebookEntry != ''}">
				<div class="panel panel-default">
					<div class="panel-heading panel-title"></div>
					<div class="panel-body">
						<fmt:message key="label.notebook.entries" />
						<div class="panel">
							<lams:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true" />
						</div>
					</div>
				</div>
			</c:if>

			<div id="chartDiv" style="height: 220px; display: none;"></div>


			<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }">
				<button class="btn btn-sm btn-default voffset10 pull-left" onclick="submitMethod('viewAllResults');">
					<fmt:message key="label.refresh" />
				</button>

				<c:if test="${VoteLearningForm.lockOnFinish != 'true' && hasEditRight}">
					<button class="btn btn-sm btn-default voffset10 pull-left " onclick="submitMethod('redoQuestionsOk');">
						<fmt:message key="label.retake" />
					</button>
				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
					<button type="submit" onclick="javascript:submitMethod('learnerFinished');"
						class="btn btn-primary pull-right voffset10 na" id="finishButton">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submitActivity" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.endLearning" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true' && hasEditRight}">
					<button onclick="javascript:submitMethod('forwardtoReflection');" class="btn btn-primary pull-right voffset10">
						<fmt:message key="label.continue" />
					</button>
				</c:if>
			</c:if>
			</div>
			<div id="footer"></div>
		</lams:Page>
	</html:form>

</body>
</lams:html>