<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />

<lams:PageLearner title="${generalLearnerFlowDTO.activityTitle}" toolSessionID="${generalLearnerFlowDTO.toolSessionID}" >


	<div class="container-lg">
		<h1>sessionid <c:out value="${generalLearnerFlowDTO.toolSessionID}"></c:out></h1>
<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
	<form:form action="/lams/tool/laqa11/learning/learning.do" method="POST" modelAttribute="qaLearningForm" target="_self">

		<!--  Announcements and advanced settings -->

		<c:if test="${generalLearnerFlowDTO.noReeditAllowed}">
			<lams:Alert5 type="danger" id="noRedosAllowed" close="false">
				<fmt:message key="label.noredo.enabled" />
			</lams:Alert5>
		</c:if>

		<c:if test="${generalLearnerFlowDTO.lockWhenFinished && not generalLearnerFlowDTO.noReeditAllowed}">
			<lams:Alert5 type="danger" id="lockWhenFinished" close="false">
				<fmt:message key="label.responses.locked" />
			</lams:Alert5>
		</c:if>

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert5 type="danger" id="submissionDeadline" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
		<!-- End announcements -->

		<form:hidden path="toolSessionID" />
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="totalQuestionCount" />

		<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">


			<div class="card lcard lcard-no-borders shadow mb-3 my-4" id="questions${questionEntry.key}"> 
				<div class="card-header ">
					<strong>
						<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() != 1}">${questionEntry.key}.&nbsp;</c:if> <c:out value="${questionEntry.value.name}" escapeXml="false" /> 
					</strong> 
				</div>
				
				<div class="card-body">
					<div class="mb-3" >
						<c:out value="${questionEntry.value.description}" escapeXml="false" />
					</div>

					<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswersPresentable}">
						<c:if test="${answerEntry.key == questionEntry.key}">
							<span class="fw-bold my-1">
								<fmt:message key="label.learning.yourAnswer" />
							</span>

							<div class="border border-primary p-3 m-2 border-opacity-50 rounded-2" id="answer${questionEntry.key}">
								<c:out value="${answerEntry.value}" escapeXml="false" />
							</div>
						</c:if>
					</c:forEach>

					<!-- Feedback -->
					<c:if test="${(questionEntry.value.feedback != '') && (questionEntry.value.feedback != null) }">

						<div class="panel panel-default mt-4" id="feedback${questionEntry.key}">
							<div class="panel-heading panel-heading-sm panel-title">
								<fmt:message key="label.feedback" />
							</div>
							<div class="panel-body panel-body-sm">
								<c:out value="${questionEntry.value.feedback}" escapeXml="false" />
							</div>
						</div>

					</c:if>

				</div>
			</div>


		</c:forEach>

		<hr class="msg-hr" />

		<div class="activity-bottom-buttons">

			<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">
				<button name="viewAllResults" type="button" onclick="submitMethod('storeAllResults');"
					class="btn btn-primary mx-2">
					<fmt:message key="label.allResponses" />
				</button>
			</c:if>

			<c:if test="${!generalLearnerFlowDTO.showOtherAnswers}">
				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<div class="space-bottom-top align-right">
						<button type="button" id="finishButton" class="btn btn-primary pull-right na">
							<c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>

						</button>
					</div>
				</c:if>
				

				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<button name="forwardtoReflection" type="button" onclick="javascript:submitMethod('storeAllResults');"
						class="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</button>
				</c:if>
			</c:if>

			<c:if test="${!generalLearnerFlowDTO.noReeditAllowed}">
				<button type="button" name="redoQuestions" class="btn btn-primary mx-2 float-start"
					onclick="submitMethod('redoQuestions');">
					<fmt:message key="label.redo" />
				</button>
			</c:if>
		</div>


	</form:form>
	</div>
	<!-- end form -->

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${qaLearningForm.toolSessionID}', '', function(){
			submitMethod('storeAllResults');
		});

		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.qaLearningForm.action = actionMethod+".do";
			document.forms.qaLearningForm.submit();
		}
	</script>
</lams:PageLearner>
