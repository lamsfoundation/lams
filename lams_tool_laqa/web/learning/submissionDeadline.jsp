<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />
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
			document.QaLearningForm.method.value = actionMethod;
			document.QaLearningForm.submit();
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

		<html:form action="/learning?validate=false" method="POST" styleId="form">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />


			<lams:Alert type="danger" id="submission-deadline" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>

			<div class="right-buttons">

				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<html:link href="#nogo" property="endLearning" styleId="finishButton"
						onclick="javascript:submitMethod('endLearning');return false" styleClass="btn btn-primary pull-right">
						<span class="na"> <c:choose>
								<c:when test="${sessionMap.activityPosition.last}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
						</span>
					</html:link>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<html:button property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</html:button>
				</c:if>

			</div>
		</html:form>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
