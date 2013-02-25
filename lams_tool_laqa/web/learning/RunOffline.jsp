<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
		function submitLearningMethod(actionMethod) 
		{
			if (actionMethod == 'endLearning') {
				document.getElementById("finishButton").disabled = true;
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
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
			<c:choose>
				<c:when test="${empty sessionMap.submissionDeadline}">
					<p>
						<fmt:message key="label.learning.forceOfflineMessage" />
					</p>
				</c:when>
				<c:otherwise>
					<div class="warning">
						<fmt:message key="authoring.info.teacher.set.restriction" >
							<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
						</fmt:message>
					</div>
				</c:otherwise>
			</c:choose>

			<div class="space-bottom-top align-right">

				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<html:link href="#nogo" property="endLearning" styleId="finishButton"
						onclick="javascript:submitMethod('endLearning');return false"
						styleClass="button">
						<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
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








