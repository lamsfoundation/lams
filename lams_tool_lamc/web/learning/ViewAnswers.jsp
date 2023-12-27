<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
<c:set var="isPrefixAnswersWithLetters" value="${sessionMap.content.prefixAnswersWithLetters}" scope="request" />

<lams:PageLearner title="${mcGeneralLearnerFlowDTO.activityTitle}" toolSessionID="${mcLearningForm.toolSessionID}">
	<style media="screen,projection" type="text/css">
		div.growlUI h1, div.growlUI h2 {
			color: white;
			margin: 5px 5px 5px 0px;
			text-align: center;
			font-size: 18px;
		}
		table .bg-success, table .bg-danger {
			width: 32px;
		}
		.table-top>tbody>tr>td {
			vertical-align: top;
		}
	</style>	

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${mcLearningForm.toolSessionID}', '', submitForm);
	
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}

		function submitForm() {
			var f = document.getElementById('mcLearningForm');
			f.submit();
		}
	</script>

	<div id="container-main">
	<form:form action="displayMc.do" method="POST" target="_self" onsubmit="disableFinishButton();" modelAttribute="mcLearningForm" id="mcLearningForm">

		<c:if test="${isLeadershipEnabled}">
			<lams:LeaderDisplay idName="leader-info" username="${sessionMap.groupLeader.fullname}" userId="${sessionMap.groupLeader.userId}" />
		</c:if>
		
		<c:if test="${hasEditRight && mcGeneralLearnerFlowDTO.retries == 'true'
				&& mcGeneralLearnerFlowDTO.userOverPassMark != 'true'
				&& mcGeneralLearnerFlowDTO.passMarkApplicable == 'true'}">
			<lams:Alert5 id="learning-notEnoughMarks-info" type="danger" close="false">
				<fmt:message key="label.notEnoughMarks">
					<fmt:param>
						<c:if test="${mcGeneralLearnerFlowDTO.passMark != mcGeneralLearnerFlowDTO.totalMarksPossible}">
							<fmt:message key="label.atleast" />
						</c:if>
                                                                
						<c:out value="${mcGeneralLearnerFlowDTO.passMark}" />
						&nbsp;<fmt:message key="label.outof" />&nbsp;
						<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if> 
		
		<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight || (mcGeneralLearnerFlowDTO.displayAnswers == 'true')}">
			<lams:Alert5 id="learning-mark-info" type="info" close="false">
				<strong><fmt:message key="label.mark" /> </strong>
				<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
				&nbsp;<fmt:message key="label.outof" />&nbsp;
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
			</lams:Alert5>
		</c:if>

		<h4>
			<fmt:message key="label.viewAnswers" />
		</h4>

		<!--  QUESTIONS  -->
		<c:set var="mainQueIndex" scope="request" value="0" />
		<c:forEach var="question" varStatus="status" items="${mcGeneralLearnerFlowDTO.questions}">
			<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}" />

			<div class="card lcard">
				<div class="card-header">
					<div class="row align-items-center">
						<div style="width:50px;">
							${status.count})
						</div>
	
						<div class="col">
							<c:if test="${not sessionMap.hideTitles}">
								<c:out value="${question.name}" escapeXml="false" />
							</c:if>
						</div>
					</div>
				</div>
						
				<div class="card-body">		
					<div class="font-size-init">
						<c:out value="${question.description}" escapeXml="false" />
					</div>

					<!--  CANDIDATE ANSWERS  -->
					<c:set var="queIndex" scope="request" value="0" />
					<c:forEach var="mainEntry" items="${mcGeneralLearnerFlowDTO.mapGeneralOptionsContent}">
						<c:set var="queIndex" scope="request" value="${queIndex +1}" />

						<c:if test="${requestScope.mainQueIndex == requestScope.queIndex}">
							<!-- list of candidate answers -->
							<ul>
								<c:forEach var="subEntry" items="${mainEntry.value}">
									<li>
										<c:if test="${isPrefixAnswersWithLetters}">
											${subEntry.key}
											&nbsp;
										</c:if>
												
										<c:out value="${subEntry.value}" escapeXml="false" />
									</li>
								</c:forEach>
							</ul>

							<!-- display students answers -->
							<div class="card-subheader">
								<fmt:message key="label.yourAnswers" />			
							</div>

							<div class="row">
								<!-- show right/wrong -->
								<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
									<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
										<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
											<c:choose>
												<c:when test="${attemptEntry.value.qbOption.correct}">
													<span class="text-bg-success badge ms-3" style="width: 40px;">
														<i class="fa fa-check" style="font-size: 22px"></i>
													</span>
												</c:when>
												<c:otherwise>
													<span class="text-bg-danger badge ms-3" style="width: 40px;">
														<i class="fa fa-times" style="font-size: 22px"></i>
													</span>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</c:if>
			
								<!-- display student selection --> 
								<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
									<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
										<div class="col">
											<c:out value="${attemptEntry.value.qbOption.name}" escapeXml="false" />
										</div>
									</c:if>
								</c:forEach>
							</div>
											
							<c:if test="${sessionMap.content.enableConfidenceLevels}">
									<div class="bootstrap-slider mt-3">
										<div class="card-subheader">
											<label for="confidenceLevel${queIndex}">
												<fmt:message key="label.what.is.your.confidence.level" />
											</label>
										</div>
										
										<div class="col-12 col-sm-8 col-md-6 col-lg-4">
											<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
												<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
													<input type="range" name="confidenceLevel${queIndex}" id="confidenceLevel${queIndex}" class="form-range"
														list="slider-step-list-${queIndex}" 
														min="0" max="10" step="5" disabled
														<c:if test="${attemptEntry.value.confidenceLevel != -1}">value="${attemptEntry.value.confidenceLevel}"</c:if>
													>
													<datalist id="slider-step-list-${queIndex}">
														<option value="0" label="0%"/>
														<option value="5" label="50%"/>
														<option value="10" label="100%"/>
													</datalist>
												</c:if>
											</c:forEach>
										</div>
									</div>
							</c:if>
						</c:if>
					</c:forEach>

					<!-- answer feedback -->
					<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true' || mcGeneralLearnerFlowDTO.displayFeedbackOnly == 'true'}">
						<c:forEach var="feedbackEntry" items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
							<c:if test="${(requestScope.mainQueIndex == feedbackEntry.key)
                    		        && (feedbackEntry.value != null) && (feedbackEntry.value != '')}">
								<div class="card-subheader">
									<fmt:message key="label.feedback.simple" />
								</div>
								
								<div>
									<c:out value="${feedbackEntry.value}" escapeXml="false" />
								</div>
							</c:if>
						</c:forEach>
					</c:if>
				</div>
			</div>
		</c:forEach>

		<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
			<h4>
				<fmt:message key="label.group.results" />
			</h4>

			<div class="ltable table-sm">
				<div class="row">
					<div class="col table-active"><fmt:message key="label.topMark" /></div>
					<div class="col-10"><c:out value="${mcGeneralLearnerFlowDTO.topMark}" /></div>
				</div>

				<div class="row">
					<div class="table-active col"><fmt:message key="label.avMark" /></div>
					<div class="col-10"><c:out value="${mcGeneralLearnerFlowDTO.averageMark}" /></div>
				</div>
			</div>
		</c:if>

		<!--  now really start the form -->
		<div class="activity-bottom-buttons">
			<form:hidden path="toolContentID" />
			<form:hidden path="toolSessionID" />
			<form:hidden path="httpSessionID" />
			<form:hidden path="userID" />
			<form:hidden path="userOverPassMark" />
			<form:hidden path="passMarkApplicable" />
				
			<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true') 
					|| (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">
				<c:if test="${!hasEditRight}">
					<form:hidden path="learnerFinished" value="Finished" />

					<button type="button" class="btn btn-primary na" id="finishButton">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:if>
			</c:if>

			<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight}">
				<button type="submit" name="redoQuestions" class="btn btn-secondary btn-icon-return me-2">
					<fmt:message key="label.redo.questions" />
				</button>
			</c:if>
		</div>
		
	</form:form>
	</div>
</lams:PageLearner>
