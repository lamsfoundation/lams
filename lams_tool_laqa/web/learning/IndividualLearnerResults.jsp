<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

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
	<div id="content">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>			

		<c:if test="${(generalLearnerFlowDTO.lockWhenFinished == 'true') && !generalLearnerFlowDTO.noReeditAllowed && (generalLearnerFlowDTO.showOtherAnswers) }">
			<div class="info space-bottom">
				<fmt:message key="label.responses.locked" />								
			</div>
		</c:if>

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" value="storeAllResults"/>
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />

			<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

				<div class="shading-bg">
					<p>
						<strong>
							<fmt:message key="label.question" /> <c:out value="${questionEntry.key}" escapeXml="false"/>:
						</strong>
						<br>
						<c:out value="${questionEntry.value.question}" escapeXml="false" />
					</p>

					<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswersPresentable}">
						<c:if test="${answerEntry.key == questionEntry.key}">

							<p class="user-answer">
								<strong> 
									<fmt:message key="label.learning.yourAnswer" />
								</strong>

								<br>
								<c:out value="${answerEntry.value}" escapeXml="false" />
							</p>

						</c:if>
					</c:forEach>

					<c:if test="${(questionEntry.value.feedback != '') && (questionEntry.value.feedback != null) }">
						<span class="field-name"> 
							<fmt:message key="label.feedback" />: 
						</span>
						<c:out value="${questionEntry.value.feedback}" escapeXml="false" />
					</c:if>

				</div>

			</c:forEach>

			<div class="last-item">

			</div>

			<div class="space-bottom-top small-space-top">
				<c:if test="${!generalLearnerFlowDTO.noReeditAllowed}">
					<html:button property="redoQuestions" styleClass="button"
							onclick="submitMethod('redoQuestions');">
						<fmt:message key="label.redo" />
					</html:button>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">
					<html:button property="viewAllResults"
						onclick="submitMethod('storeAllResults');" styleClass="button">
						<fmt:message key="label.allResponses" />
					</html:button>
				</c:if>				

				<c:if test="${!generalLearnerFlowDTO.showOtherAnswers}">
					<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					    <div class="space-bottom-top align-right">
						<html:link href="#nogo" property="endLearning" styleId="finishButton"
							onclick="javascript:submitMethod('storeAllResults');return false"
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
					    </div>
					</c:if>

					<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
						<html:button property="forwardtoReflection"
							onclick="javascript:submitMethod('storeAllResults');"
							styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:if>
				</c:if>
			
			</div>

		</html:form>
	</div>

	<div id="footer"></div>

</body>
</lams:html>
