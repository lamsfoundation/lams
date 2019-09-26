<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
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
			document.forms.qaLearningForm.action = actionMethod+".do";
			document.forms.qaLearningForm.submit();
		}

		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
		}
	</script>
</lams:head>

<body class="stripes">
	<c:set scope="request" var="title">
		<fmt:message key="activity.title" />
	</c:set>

	<lams:Page type="learner" title="${generalLearnerFlowDTO.activityTitle}">

		<form:form action="/lams/tool/laqa11/learning/learning.do" method="POST" modelAttribute="qaLearningForm" id="qaLearningForm">
			<form:hidden path="toolSessionID" />
			<form:hidden path="userID" />
			<form:hidden path="sessionMapID" />
			<form:hidden path="totalQuestionCount" />


			<lams:Alert type="danger" id="submission-deadline" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>

			<div class="right-buttons">

				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<a href="#nogo" name="endLearning" id="finishButton"
						onclick="javascript:submitMethod('endLearning');return false" class="btn btn-primary pull-right">
						<span class="na"> <c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
						</span>
					</a>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<button name="forwardtoReflection" type="button" onclick="javascript:submitMethod('forwardtoReflection');"
						class="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</button>
				</c:if>

			</div>
		</form:form>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
