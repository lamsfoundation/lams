<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL /></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
<c:set var="isUserLeader" value="${voteLearningForm.userLeader}" />
<c:set var="isLeadershipEnabled" value="${voteLearningForm.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:PageLearner title="${voteGeneralLearnerFlowDTO.activityTitle}" toolSessionID="${voteLearningForm.toolSessionID}">
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet" />

	<!--<lams:JSImport src="includes/javascript/common.js" />-->
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${voteLearningForm.toolSessionID}', '', function() {
			submitMethod('learnerFinished');
		});
		
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.voteLearningForm.action = actionMethod + ".do";
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

			<c:if test="${isLeadershipEnabled}">
				<lams:LeaderDisplay idName="leaderEnabled" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
			</c:if>
			
			<div id="nominations">
				<div class="card lcard">
					<div class="card-header">
						<c:choose>
							<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
								<fmt:message key="label.learner.nominations" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.learner.nomination" />
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="card-body">
						<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
							<div class="d-flex">
								<div class="flex-shrink-0">
									<i class="fa fa-check text-success"></i>
								</div>
								<div class="flex-grow-1 ms-3">
									<c:out value="${entry}" escapeXml="false" />
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
					</div>
				</div>
			</div>

			<!--present  a mini summary table here -->
			<div class="card lcard">
				<div class="card-header">
					<fmt:message key="label.progressiveResults" />
				</div>

				<div class="card-body">
					<div class="ltable table-hover">
						<div class="row">
							<div class="col"><fmt:message key="label.nomination" /></div>
							<div class="col-2 text-center"><fmt:message key="label.total.votes" /></div>
						</div>
		
						<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
							<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
							<div class="row">
								<div class="col text-left">
									<c:out value="${currentNomination.value}" escapeXml="false" />
								</div>
		
								<div class="col-2 text-center"><c:forEach var="currentUserCount"
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
									</c:forEach>
								</div>
							</div>
						</c:forEach>
					</div>
					
					<div id="textEntries">
						<c:if test="${voteLearningForm.allowTextEntry}">
							<div class="card-subheader">
								<fmt:message key="label.open.votes" />
							</div>
		
							<c:forEach var="vote" items="${requestScope.listUserEntriesContent}">
								<c:if test="${vote.userEntry != ''}">
									<div class="d-flex">
										<div class="flex-shrink-0">
											<i class="fa fa-check text-success"></i>
										</div>
										<div class="flex-grow-1 ms-3">
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
		
					<div class="float-end">
						<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${voteLearningForm.toolSessionID}" />
						<button type="button" title="<fmt:message key='label.tip.displayPieChart'/>"
							class="fa fa-pie-chart text-primary btn btn-light"
							onclick="javascript:drawChart('pie', 'chartDiv', '${chartURL}')"></button> 
						<button type="button" title="<fmt:message key='label.tip.displayBarChart'/>"
							class="fa fa-bar-chart text-primary btn btn-light"
							onclick="javascript:drawChart('bar', 'chartDiv', '${chartURL}')"></button>
					</div>
					
					<div id="chartDiv" style="height: 220px; display: none;" class="mb-4"></div>
				</div>
			</div>

			<c:if test="${voteGeneralLearnerFlowDTO.notebookEntry != null && voteGeneralLearnerFlowDTO.notebookEntry != ''}">			
				<lams:NotebookReedit
					reflectInstructions="${voteGeneralLearnerFlowDTO.reflectionSubject}"
					reflectEntry="${voteGeneralLearnerFlowDTO.notebookEntry}"
					isEditButtonEnabled="false"
					notebookHeaderLabelKey="label.notebook.entries"/>
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }">
				<div class="activity-bottom-buttons">
					<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
						<button type="button" class="btn btn-primary na" id="finishButton">
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
	
					<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true' && hasEditRight}">
						<button type="button" onclick="javascript:submitMethod('forwardtoReflection');" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:if>
	
					<c:if test="${voteLearningForm.lockOnFinish != 'true' && hasEditRight}">
						<button type="button" class="btn btn-secondary btn-icon-return me-2" onclick="submitMethod('redoQuestionsOk');">
							<fmt:message key="label.retake" />
						</button>
					</c:if>
					
					<button type="button" class="btn btn-secondary btn-icon-refresh me-2" onclick="submitMethod('viewAllResults');">
						<fmt:message key="label.refresh" />
					</button>
				</div>
			</c:if>
			
		</form:form>
	</div>
</lams:PageLearner>
