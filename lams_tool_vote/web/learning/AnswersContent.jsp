<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script language="JavaScript" type="text/JavaScript">
		var noSelected = 0;
		var maxVotes = <c:out value="${VoteLearningForm.maxNominationCount}"/>;
		var minVotes = <c:out value="${VoteLearningForm.minNominationCount}"/>;
		function updateCount(clickedObj) {
			var userEntry = 0;
			<c:if test="${VoteLearningForm.allowTextEntry == true}">
			if (document.forms[0].userEntry.value != "") {
				userEntry = 1;
			}
			</c:if>
			if (clickedObj.checked) {
				noSelected++;
			} else {
				noSelected--;
			}

			if ((noSelected + userEntry) > maxVotes) {
				clickedObj.checked = false;
				noSelected--;
				alertTooManyVotes(maxVotes);
			}

		}

		function validate() {
			var error = "";
			var userEntry = 0;
			<c:if test="${VoteLearningForm.allowTextEntry == true}">
			if (document.forms[0].userEntry.value != "") {
				userEntry = 1;
			}
			</c:if>

			var numberOfVotes = noSelected + userEntry;
			if (numberOfVotes > maxVotes) {
				alertTooManyVotes(maxVotes);
				return false;
			} else if (numberOfVotes < minVotes) {
				var msg = "<fmt:message key="error.minNominationCount.not.reached"/> "
						+ minVotes + " <fmt:message key="label.nominations"/>";
				alert(msg);
				return false;
			} else if (numberOfVotes == 0) {
				alert("<fmt:message key="error.empty.selection"/>");
				return false;
			} else {
				$('.btn').prop('disabled', true);
				return true;
			}
		}

		function alertTooManyVotes(maxVotes) {
			var msg = "<fmt:message key="error.maxNominationCount.reached"/> "
					+ maxVotes + " <fmt:message key="label.nominations"/>";
			alert(msg);
		}

		function checkLeaderProgress() {

			$.ajax({
				async : false,
				url : '<c:url value="/learning.do"/>',
				data : 'dispatch=checkLeaderProgress&toolSessionID='
						+ $('[name="toolSessionID"]').val(),
				dataType : 'json',
				type : 'post',
				success : function(json) {
					if (json.isLeaderResponseFinalized) {
						location.reload();
					}
				}
			});
		}

		$(document)
				.ready(
						function() {

							var mode = "${voteGeneralLearnerFlowDTO.learningMode}";
							var isUserLeader = ($('[name="userLeader"]').val() === "true");
							var isLeadershipEnabled = ($(
									'[name="useSelectLeaderToolOuput"]').val() === "true");
							var hasEditRight = !isLeadershipEnabled
									|| isLeadershipEnabled && isUserLeader;

							if (!hasEditRight && (mode != "teacher")) {
								setInterval("checkLeaderProgress();", 60000);// Auto-Refresh every 1 minute for non-leaders
							}

							if (!hasEditRight) {
								$('[name="userEntry"]').prop('disabled', true);
								$('[name="checkedVotes"]').prop('disabled',
										true);
								$('[name="continueOptionsCombined"]').hide();
							}
						});
	</script>
</lams:head>

<body class="stripes">

	<html:form onsubmit="return validate();" action="/learning?validate=false&dispatch=continueOptionsCombined"
		method="POST" target="_self">
		<c:set var="formBean" value="<%=request
							.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="revisitingUser" />
		<html:hidden property="previewOnly" />
		<html:hidden property="maxNominationCount" />
		<html:hidden property="minNominationCount" />
		<html:hidden property="allowTextEntry" />
		<html:hidden property="lockOnFinish" />
		<html:hidden property="reportViewOnly" />
		<html:hidden property="showResults" />
		<html:hidden property="userLeader" />
		<html:hidden property="groupLeaderName" />
		<html:hidden property="useSelectLeaderToolOuput" />


		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">



			<!-- Announcements and advanced settings -->
			<c:if test="${formBean.useSelectLeaderToolOuput}">
				<lams:Alert id="groupLeader" type="info" close="false">
					<fmt:message key="label.group.leader">
						<fmt:param>${formBean.groupLeaderName}</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<c:if test="${not empty voteGeneralLearnerFlowDTO.submissionDeadline}">
				<lams:Alert id="submissionDeadline" type="warning" close="false">
					<fmt:message key="authoring.info.teacher.set.restriction">
						<fmt:param>
							<lams:Date value="${voteGeneralLearnerFlowDTO.submissionDeadline}" />
						</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.maxNominationCountReached == 'true'}">
				<lams:Alert id="maxNominations" type="info" close="false">
					<fmt:message key="error.maxNominationCount.reached" />
					<c:out value="${voteGeneralLearnerFlowDTO.maxNominationCount}" />
					<fmt:message key="label.nominations" />
				</lams:Alert>
			</c:if>
			<!-- End announcements and advanced settings -->
			<div class="panel">
				<c:out value="${voteGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />
			</div>

			<c:if test="${voteGeneralLearnerFlowDTO.maxNominationCount > 1}">
				<lams:Alert id="maxNominations" type="info" close="false">
					<fmt:message key="label.nominations.available">
						<fmt:param>
							<c:out value="${voteGeneralLearnerFlowDTO.maxNominationCount}" />
						</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<!-- options  -->
			<div class="table-responsive">
				<table class="table table-hover table-condensed">
					<tbody>
						<c:set var="count" value="0" scope="page" />
						<c:forEach var="subEntry" varStatus="status" items="${requestScope.mapQuestionContentLearner}">
							<c:set var="count" value="${count + 1}" scope="page" />

							<tr>
								<td width="20px"><input type="checkbox" id="vote${count}" name="checkedVotes" value="${subEntry.key}"
									onClick="updateCount(this);"></td>
								<td width="100%"><label for="vote${count}"><c:out value="${subEntry.value}" escapeXml="false" /></label></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- End options -->

			<c:if test="${VoteLearningForm.allowTextEntry == 'true'}">
				<fmt:message key="label.other" />:
				<html:text styleClass="form-control" property="userEntry" size="30" maxlength="100" />
			</c:if>

			<html:hidden property="donePreview" />


			<html:submit property="continueOptionsCombined" styleClass="btn btn-primary pull-right voffset10">
				<fmt:message key="label.submit.vote" />
			</html:submit>

			<div id="footer"></div>

		</lams:Page>

	</html:form>

</body>
</lams:html>
