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
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.VoteLearningForm.dispatch.value = actionMethod;
			document.VoteLearningForm.submit();
		}
	</script>

</lams:head>

<body class="stripes">

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">

		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">

			<html:hidden property="dispatch" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userLeader" />
			<html:hidden property="groupLeaderName" />
			<html:hidden property="useSelectLeaderToolOuput" />

			<div class="panel">
				<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</div>

			<html:textarea rows="5" property="entryText" styleClass="form-control" styleId="focused"></html:textarea>
			<button type="submit" onclick="javascript:submitMethod('submitReflection')"
				class="btn btn-primary voffset10 pull-right na" id="finishButton">
				<fmt:message key="button.endLearning" />
			</button>

		</lams:Page>

		</html:form>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>
	
</body>
</lams:html>








