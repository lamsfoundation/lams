<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>learning/includes/javascript/gate-check.js"></script>
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${voteLearningForm.toolSessionID}', '', function() {
			submitMethod('learnerFinished');
		});
		
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.forms.voteLearningForm.action = actionMethod + '.do';
			document.forms.voteLearningForm.submit();
		}
	</script>

</lams:head>

<body class="stripes">

		<form:form modelAttribute="voteLearningForm" method="POST">

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">

			<form:hidden path="toolSessionID" />
			<form:hidden path="userID" />
			<form:hidden path="userLeader" />
			<form:hidden path="groupLeaderName" />
			<form:hidden path="groupLeaderUserId" />
			<form:hidden path="useSelectLeaderToolOuput" />

			<div class="panel">
				<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</div>

			<form:textarea rows="5" path="entryText" cssClass="form-control" styleId="focused" />
			<button type="button"
				class="btn btn-primary voffset10 pull-right na" id="finishButton">
				<fmt:message key="button.endLearning" />
			</button>

		</lams:Page>

		</form:form>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>
	
</body>
</lams:html>








