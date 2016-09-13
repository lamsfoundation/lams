<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
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
	<html:form action="/learning?method=displayMc&validate=false" method="POST" target="_self"
		onsubmit="disableFinishButton();" styleId="Form1">

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}">

		<c:choose>
			<c:when test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
				<h4>
					<fmt:message key="label.individual.results.withRetries" />
				</h4>
			</c:when>

			<c:when test="${mcGeneralLearnerFlowDTO.retries != 'true'}">
				<h4>
					<fmt:message key="label.individual.results.withoutRetries" />
				</h4>
			</c:when>
		</c:choose>

		<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') or (mcGeneralLearnerFlowDTO.displayAnswers == 'true')}">
			<p>
				<strong><fmt:message key="label.mark" /></strong>
				<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
				<fmt:message key="label.outof" />
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
			</p>
		</c:if>

		<p>
			<c:if
				test="${mcGeneralLearnerFlowDTO.retries == 'true'
                              && mcGeneralLearnerFlowDTO.userOverPassMark != 'true'
                              && mcGeneralLearnerFlowDTO.passMarkApplicable == 'true'}">

				<fmt:message key="label.notEnoughMarks">
					<fmt:param>
						<c:if test="${mcGeneralLearnerFlowDTO.passMark != mcGeneralLearnerFlowDTO.totalMarksPossible}">
							<fmt:message key="label.atleast" />
						</c:if>

						<c:out value="${mcGeneralLearnerFlowDTO.passMark}" />
						<fmt:message key="label.outof" />
						<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
					</fmt:param>
				</fmt:message>
			</c:if>
		</p>
		<h4>
			<fmt:message key="label.yourAnswers" />
		</h4>

		<c:forEach var="dto" varStatus="status" items="${answerDtos}">

			<div class="panel panel-default">

				<div class="panel-heading">
					<table>
						<tr>
							<td>${dto.displayOrder})</td>
							<td width="100%" style="padding-left: 5px"><c:out value="${dto.question}" escapeXml="false" /></td>
						</tr>
						<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
							<tr>
								<td width="100%" colspan="2"><strong><fmt:message key="label.mark" /></strong> <c:out value="${dto.mark}" /></td>
							</tr>
						</c:if>
					</table>
				</div>
				<div class="panel-body">

					<table class="table table-condensed">
						<tr>
							<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
								<!-- show right/wrong -->
								<c:choose>
									<c:when test="${dto.attemptCorrect}">
										<td class="bg-success" style="vertical-align: top;"><i class="fa fa-check"
											style="color: green; font-size: 22px"></i></td>
									</c:when>
									<c:otherwise>
										<td class="bg-danger" style="vertical-align: top;"><i class="fa fa-times"
											style="color: red; font-size: 22px"></i></td>
									</c:otherwise>
								</c:choose>
							</c:if>
							<td width="100%"><c:out value="${dto.answerOption.mcQueOptionText}" escapeXml="false" /></td>
						</tr>
					</table>

					<c:if test="${(dto.feedback != null) && (dto.feedback != '')}">
						<!-- answer feedback -->
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<fmt:message key="label.feedback.simple" />
								</h4>
							</div>
							<div class="panel-body">
								<c:out value="${dto.feedback}" escapeXml="true" />
							</div>
						</div>
						<!-- end answer feedback -->
					</c:if>
				</div>
			</div>
		</c:forEach>

		<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
			<h4>
				<fmt:message key="label.group.results" />
			</h4>

			<table class="table table-condensed" cellspacing="0">
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


			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />
			<html:hidden property="learnerProgress" />
			<html:hidden property="learnerProgressUserId" />

			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
				<p>
					<fmt:message key="label.learner.bestMark" />
					<c:out value="${mcGeneralLearnerFlowDTO.latestAttemptMark}" />
					<fmt:message key="label.outof" />
					<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
				</p>

				<html:submit property="redoQuestions" styleClass="btn btn-sm btn-primary buttons_column pull-left">
					<fmt:message key="label.redo.questions" />
				</html:submit>
			</c:if>

			<c:if
				test="${(mcGeneralLearnerFlowDTO.retries != 'true')
                              || (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') 
                              && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">


				<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
					<html:hidden property="learnerFinished" value="Finished" />

					<html:link href="#" styleClass="btn btn-primary pull-right na" styleId="finishButton"
						onclick="submitForm('finish');return false">
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

				<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
					<html:submit property="forwardtoReflection" styleClass="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:if>


			</c:if>

	</lams:Page>
	
	</html:form>
	
</body>
</lams:html>

