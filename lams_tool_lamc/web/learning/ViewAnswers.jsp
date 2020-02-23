<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
<c:set var="isPrefixAnswersWithLetters" value="${sessionMap.content.prefixAnswersWithLetters}" scope="request" />


<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css />
	<link rel="stylesheet" type="text/css" href="${lams}css/bootstrap-slider.css" />
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

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-slider.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//initialize bootstrap-sliders if "Enable confidence level" option is ON
			$('.bootstrap-slider').bootstrapSlider();
		});
	
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}

		function submitForm(methodName) {
			var f = document.getElementById('mcLearningForm');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">
	<form:form action="displayMc.do" method="POST" target="_self" onsubmit="disableFinishButton();" modelAttribute="mcLearningForm" id="mcLearningForm">

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}" formID="mcLearningForm">
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader">
					<fmt:param><c:out value="${sessionMap.groupLeader.fullname}" escapeXml="true"/></fmt:param>
				</fmt:message>
			</h4>
		</c:if>
		
		<c:if test="${hasEditRight && mcGeneralLearnerFlowDTO.retries == 'true'
				&& mcGeneralLearnerFlowDTO.userOverPassMark != 'true'
				&& mcGeneralLearnerFlowDTO.passMarkApplicable == 'true'}">
			<p>
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
			</p>
			<p>
				<strong><fmt:message key="label.mark" /> </strong>
				<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
				&nbsp;<fmt:message key="label.outof" />&nbsp;
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
			</p>
		</c:if> 

		<h4>
			<fmt:message key="label.viewAnswers" />
		</h4>

		<!--  QUESTIONS  -->
		<c:set var="mainQueIndex" scope="request" value="0" />
		<c:forEach var="questionEntry" varStatus="status" items="${mcGeneralLearnerFlowDTO.mapQuestionsContent}">
			<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}" />

			<div class="row no-gutter">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<table>
								<tr>
									<td>${status.count})</td>
									<td width="100%" style="padding: 5px"><c:out value="${questionEntry.value}" escapeXml="false" /></td>
								</tr>
							</table>
						</div>
						<div class="panel-body">

							<!--  CANDIDATE ANSWERS  -->
							<c:set var="queIndex" scope="request" value="0" />
							<c:forEach var="mainEntry" items="${mcGeneralLearnerFlowDTO.mapGeneralOptionsContent}">
								<c:set var="queIndex" scope="request" value="${queIndex +1}" />

								<c:if test="${requestScope.mainQueIndex == requestScope.queIndex}">
									<!-- list of candidate answers -->
				                     <c:choose>
				                     <c:when test="${isPrefixAnswersWithLetters}">
										<table class="table table-hover table-condensed table-no-border table-top">
										<c:forEach var="subEntry" items="${mainEntry.value}">
										<tr><td>${subEntry.key}
										</td>
										<td width="100%"><c:out value="${subEntry.value}" escapeXml="false" /></td>
										</tr>
										</c:forEach>
										</tbody></table>
									</c:when>
									<c:otherwise>
									<ul>
										<c:forEach var="subEntry" items="${mainEntry.value}">
											<li><c:out value="${subEntry.value}" escapeXml="false" /></li>
										</c:forEach>
									</ul>
									</c:otherwise>
									</c:choose>
									<!-- end list of candidate answers -->

									<!-- display students answers -->
									<div class="table-responsive">
										<table class="table table-condensed">
											<thead>
												<tr>
													<th colspan="2"><fmt:message key="label.yourAnswers" /></th>
												</tr>
											</thead>
											<tr>
												<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
													<!-- show right/wrong -->
													<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
														<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
															<c:choose>
																<c:when test="${attemptEntry.value.mcOptionsContent.correctOption}">
																	<td class="bg-success" style="vertical-align: top;"><i class="fa fa-check"
																		style="color: green; font-size: 22px"></i>
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="bg-danger" style="vertical-align: top;"><i class="fa fa-times"
																		style="color: red; font-size: 22px"></i>
																	</td>
																</c:otherwise>
															</c:choose>
														</c:if>
													</c:forEach>
													<!-- end show right/wrong -->
												</c:if>
												
												<td>
													<!-- display student selection --> 
													<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
														<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
															<c:out value="${attemptEntry.value.mcOptionsContent.mcQueOptionText}" escapeXml="false" />
														</c:if>
													</c:forEach> 
													<!-- end student selection -->
												</td>
											</tr>
											
											<c:if test="${sessionMap.content.enableConfidenceLevels}">
												<tr>
													<td
														<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">colspan="2"</c:if>
													>
														<div class="question-type">
															<fmt:message key="label.what.is.your.confidence.level" />
														</div>
														
														<div>
															<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
																<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
																	<input class="bootstrap-slider" type="text" 
																		data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
																		data-slider-enabled="false" data-slider-tooltip="hide"
																		<c:if test="${attemptEntry.value.confidenceLevel != -1}">data-slider-value="${attemptEntry.value.confidenceLevel}"</c:if>
																	/>
																</c:if>
															</c:forEach>
														</div>
													</td>
												</tr>
											</c:if>
										</table>
									</div>
								</c:if>
							</c:forEach>

							<!-- answer feedback -->
							<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true' || mcGeneralLearnerFlowDTO.displayFeedbackOnly == 'true'}">
								<c:forEach var="feedbackEntry" items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
									<c:if test="${(requestScope.mainQueIndex == feedbackEntry.key)
                                          && (feedbackEntry.value != null) && (feedbackEntry.value != '')}">

										<div class="panel panel-default">
											<div class="panel-heading">
												<h4 class="panel-title">
													<fmt:message key="label.feedback.simple" />
												</h4>
											</div>
											<div class="panel-body">
												<c:out value="${feedbackEntry.value}" escapeXml="false" />
											</div>
										</div>

									</c:if>
								</c:forEach>
							</c:if>
							<!-- end answer feedback -->
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- end page panel -->

		<!-- END QUESTION  -->
		
		<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight || (mcGeneralLearnerFlowDTO.displayAnswers == 'true')}">
			<p>
				<strong><fmt:message key="label.mark" /> </strong>
				<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
				&nbsp;<fmt:message key="label.outof" />&nbsp;
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
			</p>
		</c:if>

		<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
			<h4>
				<fmt:message key="label.group.results" />
			</h4>

			<table class="table table-condensed">
				<tr>
					<td class="active" width="30%"><fmt:message key="label.topMark" /></td>
					<td><c:out value="${mcGeneralLearnerFlowDTO.topMark}" /></td>
				</tr>

				<tr>
					<td class="active"><fmt:message key="label.avMark" /></td>
					<td><c:out value="${mcGeneralLearnerFlowDTO.averageMark}" /></td>
				</tr>
			</table>
		</c:if>

		<c:if test="${mcGeneralLearnerFlowDTO.reflection && (notebookEntry != null) && hasEditRight}">
			<div class="row no-gutter">
				<div class="col-xs-12">
					<h4>
						<fmt:message key="label.reflection" />
					</h4>

					<div class="panel panel-default">
						<div class="panel-heading">
							<h2 class="panel-title">
								<lams:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
							</h2>
						</div>
						<div class="panel-body">

							<c:choose>
								<c:when test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
									<lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true" />
								</c:when>
								<c:otherwise>
									<em><fmt:message key="message.no.reflection.available" /></em>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<!--  now really start the form -->
		<div class="form-group">
				<form:hidden path="toolContentID" />
				<form:hidden path="toolSessionID" />
				<form:hidden path="httpSessionID" />
				<form:hidden path="userID" />
				<form:hidden path="userOverPassMark" />
				<form:hidden path="passMarkApplicable" />

				<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight}">
					<input type="submit" name="redoQuestions" class="btn btn-primary pull-left" value="<fmt:message key="label.redo.questions" />"/>
				</c:if>
				
				<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true') 
						|| (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">
					<div class="voffset5">
						<c:if test="${(mcGeneralLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
							<form:hidden path="learnerFinished" value="Finished" />

							<a href="#nogo" class="btn btn-primary pull-right na" id="finishButton"
									onclick="submitForm('finish'); return false;">
								<c:choose>
									<c:when test="${isLastActivity}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</a>
						</c:if>

						<c:if test="${(mcGeneralLearnerFlowDTO.reflection == 'true') && hasEditRight}">
							<input type="submit" name="forwardtoReflection" class="btn btn-primary pull-right" value="<fmt:message key="label.continue" />"/>
						</c:if>
					</div>
				</c:if>

		</div>
		</lams:Page>
	</form:form>
</body>
</lams:html>
