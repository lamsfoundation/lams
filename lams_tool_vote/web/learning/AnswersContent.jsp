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
		var maxVotes = <c:out value="${voteLearningForm.maxNominationCount}"/>;
		var minVotes = <c:out value="${voteLearningForm.minNominationCount}"/>;
		function updateCount(clickedObj) {
			var userEntry = 0;
			<c:if test="${voteLearningForm.allowTextEntry == true}">
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
			<c:if test="${voteLearningForm.allowTextEntry == true}">
			if (document.forms[0].userEntry.value != "") {
				userEntry = 1;
			}
			</c:if>

			var numberOfVotes = noSelected + userEntry;
			if (numberOfVotes > maxVotes) {
				alertTooManyVotes(maxVotes);
				return false;
			} else if (numberOfVotes < minVotes) {
				var msg = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.minNominationCount.not.reached'/></spring:escapeBody> "
						+ minVotes + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.nominations'/></spring:escapeBody>";
				alert(msg);
				return false;
			} else if (numberOfVotes == 0) {
				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.empty.selection'/></spring:escapeBody>");
				return false;
			} else {
				$('.btn').prop('disabled', true);
				return true;
			}
		}

		function alertTooManyVotes(maxVotes) {
			var msg = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.maxNominationCount.reached'/></spring:escapeBody> "
					+ maxVotes + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.nominations'/></spring:escapeBody>";
			alert(msg);
		}

		function checkLeaderProgress() {

			$.ajax({
				async : false,
				url : '<c:url value="/learning/checkLeaderProgress.do"/>',
				data : 'toolSessionID=' + $('[name="toolSessionID"]').val(),
				dataType : 'json',
				type : 'post',
				success : function(json) {
					if (json.isLeaderResponseFinalized) {
						location.reload();
					}
				}
			});
		}

		$(document).ready(function() {

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


			<%-- Connect to command websocket only if it is learner UI --%>
			<c:if test="${voteLearningForm.useSelectLeaderToolOuput and voteGeneralLearnerFlowDTO.learningMode == 'learner'}">
				// command websocket stuff for refreshing
				// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
				commandWebsocketHookTrigger = 'vote-leader-change-refresh-${voteLearningForm.toolSessionID}';
				// if the trigger is recognised, the following action occurs
				commandWebsocketHook = function() {
					location.reload();
				};
			</c:if>
		});
	</script>
</lams:head>

<body class="stripes">
	<form:form modelAttribute="voteLearningForm" onsubmit="return validate();" action="continueOptionsCombined.do" method="POST">
		<form:hidden path="toolSessionID" />
		<form:hidden path="userID" />
		<form:hidden path="revisitingUser" />
		<form:hidden path="previewOnly" />
		<form:hidden path="maxNominationCount" />
		<form:hidden path="minNominationCount" />
		<form:hidden path="allowTextEntry" />
		<form:hidden path="lockOnFinish" />
		<form:hidden path="reportViewOnly" />
		<form:hidden path="showResults" />
		<form:hidden path="userLeader" />
		<form:hidden path="groupLeaderName" />
		<form:hidden path="groupLeaderUserId" />
		<form:hidden path="useSelectLeaderToolOuput" />


		<lams:Page type="learner" title="${voteGeneralLearnerFlowDTO.activityTitle}">



			<!-- Announcements and advanced settings -->
			<c:if test="${voteLearningForm.useSelectLeaderToolOuput}">
				<lams:LeaderDisplay idName="groupLeader" username="${voteLearningForm.groupLeaderName}" userId="${voteLearningForm.groupLeaderUserId}"/>
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
				<table class="table table-hover table-sm">
					<tbody>
						<c:set var="count" value="0" scope="page" />
						<c:forEach var="subEntry" varStatus="status" items="${requestScope.mapQuestionContentLearner}">
							<c:set var="count" value="${count + 1}" scope="page" />

							<tr>
								<td width="100%">
									<div class="form-check">
										<input type="checkbox" id="vote${count}" name="checkedVotes" value="${subEntry.key}" class="form-check-input"
											onClick="updateCount(this);">
	
										<label for="vote${count}" class="form-check-label">
											<c:out value="${subEntry.value}" escapeXml="false" />
										</label>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- End options -->

			<c:if test="${voteLearningForm.allowTextEntry == 'true'}">
				<fmt:message key="label.other" />:
				<form:input cssClass="form-control" path="userEntry" size="30" maxlength="100" />
			</c:if>

			<div class="activity-bottom-buttons">
				<form:hidden path="donePreview" />
				<input type="submit" name="continueOptionsCombined" class="btn btn-primary"
					   value='<fmt:message key="label.submit.vote" />' />
			</div>

		</lams:Page>

	</form:form>

</body>
</lams:html>
