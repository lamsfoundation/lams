<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" />
	</title>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) {
			if (actionMethod == 'endLearning') {
				document.getElementById("finishButton").disabled = true;
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
		}
	</script>
</lams:head>

<body class="stripes">

	<html:form action="/learning?validate=false"
		method="POST" styleId="form">
		<html:hidden property="method" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="totalQuestionCount" />

		<div id="content">

			<h1>
				<fmt:message key="activity.title" />
			</h1>
			
			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>

			<div class="space-bottom-top align-right">

				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<html:link href="#nogo" property="endLearning" styleId="finishButton"
						onclick="javascript:submitMethod('endLearning');return false"
						styleClass="button">
						<span class="nextActivity">
							<c:choose>
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
					<html:button property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:if>

			</div>
		</div>
	</html:form>
	<div id="footer"></div>
</body>
</lams:html>
