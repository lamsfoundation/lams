<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) {
			if (actionMethod == 'endLearning') {
				document.getElementById("finishButton").disabled = true;
			}
			document.QaLearningForm.method.value = actionMethod;
			document.QaLearningForm.submit();
		}

		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
		}
	</script>
</lams:head>

<body class="stripes">

	<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
	<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">

		<lams:Page type="learner" title="${generalLearnerFlowDTO.activityTitle}">

		<!--  Announcements and advanced settings -->

		<c:if test="${generalLearnerFlowDTO.noReeditAllowed}">
			<lams:Alert type="danger" id="noRedosAllowed" close="false">
				<fmt:message key="label.noredo.enabled" />
			</lams:Alert>
		</c:if>

		<c:if test="${generalLearnerFlowDTO.lockWhenFinished && not generalLearnerFlowDTO.noReeditAllowed}">
			<lams:Alert type="danger" id="lockWhenFinished" close="false">
				<fmt:message key="label.responses.locked" />
			</lams:Alert>
		</c:if>

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert type="danger" id="submissionDeadline" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
		<!-- End announcements -->

			<html:hidden property="method" value="storeAllResults" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />

			<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

				<div class="row no-gutter">
					<div class="col-xs-12">
						<div class="panel panel-default">
							<div class="panel-heading panel-title">
								<strong> <fmt:message key="label.question" /> <c:out value="${questionEntry.key}" escapeXml="false" />:
								</strong> <br>
								<c:out value="${questionEntry.value.question}" escapeXml="false" />
							</div>
							<div class="panel-body">

								<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswersPresentable}">
									<c:if test="${answerEntry.key == questionEntry.key}">

										<h5>
											<fmt:message key="label.learning.yourAnswer" />
										</h5>

										<div class="panel" id="answer${questionEntry.key}">
											<c:out value="${answerEntry.value}" escapeXml="false" />
										</div>


									</c:if>
								</c:forEach>

								<c:if test="${(questionEntry.value.feedback != '') && (questionEntry.value.feedback != null) }">
									<!-- Feedback -->

									<div class="row no-gutter">
										<div class="col-xs-12">
											<div class="panel panel-default voffset5" id="feedback${questionEntry.key}">
												<div class="panel-heading panel-heading-sm panel-title">
													<fmt:message key="label.feedback" />
												</div>
												<div class="panel-body panel-body-sm">
													<c:out value="${questionEntry.value.feedback}" escapeXml="false" />
												</div>
											</div>
										</div>
									</div>

									<!-- End feedback -->
								</c:if>



							</div>
							<!-- End panel body -->
						</div>
					</div>
				</div>
				<div class="shading-bg">
					<p></p>



				</div>

			</c:forEach>

			<hr class="msg-hr" />

			<div class="voffset10">
				<c:if test="${!generalLearnerFlowDTO.noReeditAllowed}">
					<html:button property="redoQuestions" styleClass="btn btn-default pull-left"
						onclick="submitMethod('redoQuestions');">
						<fmt:message key="label.redo" />
					</html:button>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.showOtherAnswers == 'true'}">
					<html:button property="viewAllResults" onclick="submitMethod('storeAllResults');"
						styleClass="btn btn-default pull-right">
						<fmt:message key="label.allResponses" />
					</html:button>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.showOtherAnswers != 'true'}">
					<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
						<div class="space-bottom-top align-right">
							<html:link href="#nogo" property="endLearning" styleId="finishButton"
								onclick="javascript:submitMethod('storeAllResults');return false" styleClass="btn btn-primary pull-right na">
								<c:choose>
									<c:when test="${sessionMap.activityPosition.last}">
										<fmt:message key="button.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.endLearning" />
									</c:otherwise>
								</c:choose>

							</html:link>
						</div>
					</c:if>

					<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
						<html:button property="forwardtoReflection" onclick="javascript:submitMethod('storeAllResults');"
							styleClass="btn btn-primary pull-right">
							<fmt:message key="label.continue" />
						</html:button>
					</c:if>
				</c:if>

			</div>


		<div id="footer"></div>

		</lams:Page>

	</html:form>
	<!-- end form -->

</body>
</lams:html>
