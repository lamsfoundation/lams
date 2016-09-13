<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
<lams:html>
<lams:head>
	<html:base />
	<lams:css />

	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}

		function submitForm(methodName) {
			var f = document.getElementById('Form1');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}">
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader">
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>

		<h4>
			<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}">
				<fmt:message key="label.viewAnswers" />
			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress == 'true'}">
				<fmt:message key="label.learner.viewAnswers" />
			</c:if>
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
									<ul>
										<c:forEach var="subEntry" items="${mainEntry.value}">
											<li><c:out value="${subEntry.value}" escapeXml="false" /></li>
										</c:forEach>
									</ul>
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
																</c:when>
																<c:otherwise>
																	<td class="bg-danger" style="vertical-align: top;"><i class="fa fa-times"
																		style="color: red; font-size: 22px"></i></td>
																</c:otherwise>
															</c:choose>

														</c:if>
													</c:forEach>
													<!-- end show right/wrong -->
												</c:if>
												<td width="100%">
													<!-- display student selection --> <c:forEach var="attemptEntry"
														items="${mcGeneralLearnerFlowDTO.attemptMap}">
														<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
															<c:out value="${attemptEntry.value.mcOptionsContent.mcQueOptionText}" escapeXml="false" />
														</c:if>
													</c:forEach> <!-- end student selection -->
												</td>
											</tr>
										</table>
									</div>
								</c:if>
							</c:forEach>

							<!-- answer feedback -->
							<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
								<c:forEach var="feedbackEntry" items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
									<c:if
										test="${(requestScope.mainQueIndex == feedbackEntry.key)
                                          && (feedbackEntry.value != null) && (feedbackEntry.value != '')}">

										<div class="panel panel-default">
											<div class="panel-heading">
												<h4 class="panel-title">
													<fmt:message key="label.feedback.simple" />
												</h4>
											</div>
											<div class="panel-body">
												<c:out value="${feedbackEntry.value}" escapeXml="true" />
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


		<c:if test="${mcGeneralLearnerFlowDTO.reflection && hasEditRight}">
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

		<div class="form-group">
			<html:form action="/learning?method=displayMc&validate=false" method="POST" target="_self"
				onsubmit="disableFinishButton();" styleId="Form1">
				<html:hidden property="toolContentID" />
				<html:hidden property="toolSessionID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="userID" />
				<html:hidden property="userOverPassMark" />
				<html:hidden property="passMarkApplicable" />
				<html:hidden property="learnerProgress" />
				<html:hidden property="learnerProgressUserId" />


				<c:if
					test="${(mcGeneralLearnerFlowDTO.learnerProgress != 'true') && (mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight}">
					<html:submit property="redoQuestions" styleClass="btn btn-primary pull-left">
						<fmt:message key="label.redo.questions" />
					</html:submit>
				</c:if>
				
				<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}">
					<c:if
						test="${(mcGeneralLearnerFlowDTO.retries != 'true') || (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">

						<div class="voffset5">
							<c:if test="${(mcGeneralLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
								<html:hidden property="learnerFinished" value="Finished" />

								<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton"
									onclick="submitForm('finish'); return false;">
									<c:choose>
										<c:when test="${activityPosition.last}">
											<fmt:message key="label.submit" />
										</c:when>
										<c:otherwise>
											<fmt:message key="label.finished" />
										</c:otherwise>
									</c:choose>
								</html:link>
							</c:if>

							<c:if test="${(mcGeneralLearnerFlowDTO.reflection == 'true') && hasEditRight}">
								<html:submit property="forwardtoReflection" styleClass="btn btn-primary pull-right">
									<fmt:message key="label.continue" />
								</html:submit>
							</c:if>
						</div>

					</c:if>
				</c:if>

				<p>
					<html:hidden property="doneLearnerProgress" />
				</p>

			</html:form>
		</div>
		</lams:Page>
</body>
</lams:html>
