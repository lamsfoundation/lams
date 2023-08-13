<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${qaLearningForm.toolSessionID}', '', function(){
			$('#qaLearningForm').submit();
		});
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>

<body class="stripes">

<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
<form:form action="submitReflection.do" modelAttribute="qaLearningForm" id="qaLearningForm" onsubmit="disableFinishButton()">
<lams:Page type="learner" title="${generalLearnerFlowDTO.activityTitle}">

	<form:hidden path="toolSessionID" />
	<form:hidden path="userID" />
	<form:hidden path="sessionMapID" />
	<form:hidden path="totalQuestionCount" />

	<p>
		<lams:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
	</p>

	<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control"></form:textarea>

	<div class="activity-bottom-buttons">
		<button id="finishButton"  class="btn btn-primary na">
			<span class="nextActivity">
				<c:choose>
					<c:when test="${sessionMap.isLastActivity}">
						<fmt:message key="button.submit" />
					</c:when>
					<c:otherwise>
		 				<fmt:message key="button.endLearning" />
					</c:otherwise>
				</c:choose>
			</span>
		</button>
	</div>
</lams:Page>
</form:form>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</body>
</lams:html>
