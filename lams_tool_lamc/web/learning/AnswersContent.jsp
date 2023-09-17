<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" scope="request" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" scope="request" />
<c:set var="mode" value="${sessionMap.mode}" scope="request" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" scope="request" />
<c:set var="isPrefixAnswersWithLetters" value="${sessionMap.content.prefixAnswersWithLetters}" scope="request" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" scope="request" />

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css />
	<link rel="stylesheet" type="text/css" href="${lams}css/bootstrap-slider.css" />
	<style media="screen,projection" type="text/css">
		div.growlUI h1, div.growlUI h2 {
			color: white;
			margin: 5px 5px 5px 0px;
			text-align: center;
			font-size: 18px;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-slider.js"></script>
	<script type="text/javaScript">

		$(document).ready(function() {
			//initialize bootstrap-sliders if "Enable confidence level" option is ON
			$('.bootstrap-slider').bootstrapSlider();
		});

        //autoSaveAnswers if hasEditRight
        if (${hasEditRight}) {
          var interval = "30000"; // = 30 seconds
          window.setInterval(learnerAutosave, interval);
          
          function learnerAutosave(isCommand){
              // isCommand means that the autosave was triggered by force complete or another command websocket message
			  // in this case do not check multiple tabs open, just autosave
			  if (!isCommand) {
				let shouldAutosave = preventLearnerAutosaveFromMultipleTabs(interval);
				if (!shouldAutosave) {
					return;
				}
			  }
				
              //ajax form submit
              $('#mcLearningForm').ajaxSubmit({
                url: "<c:url value='/learning/autoSaveAnswers.do?date='/>" + new Date().getTime(),
                success: function() {
                  $.growlUI('<i class="fa fa-lg fa-floppy-o"></i> <fmt:message key="label.learning.draft.autosaved" />');
                }
              });
          }
        }

        function submitNextQuestionSelected() {
          if (verifyAllQuestionsAnswered()) {
            ++document.forms.mcLearningForm.questionIndex.value;
            document.forms.mcLearningForm.nextQuestionSelected.value = 1;
            disableContinueButton();
            document.forms.mcLearningForm.submit();
          }
        }

        function submitAllAnswers() {
          document.forms.mcLearningForm.continueOptionsCombined.value = 1;			
          doSubmit();
        }

        function doSubmit() {
          if (verifyAllQuestionsAnswered()) {
            disableContinueButton();
            document.forms.mcLearningForm.submit();
          }
        }

        function verifyAllQuestionsAnswered() {
          // in case oneQuestionPerPage option is ON user has to select 1 answer, and all answers otherwise
          var isOneQuestionPerPage = ${sessionMap.content.questionsSequenced};
          var answersRequiredNumber = (isOneQuestionPerPage) ? 1 : ${fn:length(requestScope.learnerAnswerDtos)};

          //check each question is answered
          if ($(':radio:checked').length == answersRequiredNumber) {
            return true;

          } else {
            var msg = "<fmt:message key="answers.submitted.none"/>";
            alert(msg);
            return false;
          }
        }

        function disableContinueButton() {
          var elem = document.getElementById("continueButton");
          if (elem != null) {
            elem.disabled = true;
          }				
        }

        if (${!hasEditRight && mode != "teacher"}) {
          setInterval("checkLeaderProgress();",60000);// Auto-Refresh every 1 minute for non-leaders
        }

        function checkLeaderProgress() {

          $.ajax({
            async: false,
            url: '<c:url value="/learning/checkLeaderProgress.do"/>',
            data: 'toolSessionID=' + $("#tool-session-id").val(),
            dataType: 'json',
            type: 'post',
            success: function (json) {
	            if (json.isLeaderResponseFinalized) {
	            	location.reload();
	         	}
			}
		  });
        }

      </script>
</lams:head>

<body class="stripes">

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}" formID="mcLearningForm">

		<div class="panel">
			<c:out value="${mcGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />
		</div>

		<!-- Announcements and advanced settings -->
		<c:if test="${not empty submissionDeadline}">
			<lams:Alert close="false" id="submissionDeadline" type="danger">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}">
			<lams:Alert close="true" id="passingMark" type="info">
				<fmt:message key="label.learner.message" /> ( <c:out value="${mcGeneralLearnerFlowDTO.passMark}" /> )
			</lams:Alert>
		</c:if>
		<!-- End announcements and advanced settings -->

		<div class="mb-3">
			<form:form id="mcLearningForm" modelAttribute="mcLearningForm" action="displayMc.do" enctype="multipart/form-data"
				method="POST" target="_self">
				<form:hidden path="toolContentID" />
				<form:hidden path="toolSessionID" id="tool-session-id" />
				<form:hidden path="httpSessionID" />
				<form:hidden path="userID" />
				<form:hidden path="userOverPassMark" />
				<form:hidden path="passMarkApplicable" />

				<lams:errors/>

				<c:if test="${isLeadershipEnabled}">
					<h4>
						<fmt:message key="label.group.leader">
							<fmt:param><c:out value="${sessionMap.groupLeader.fullname}" escapeXml="true"/></fmt:param>
						</fmt:message>
					</h4>
				</c:if>

				<c:choose>
					<c:when test="${sessionMap.content.questionsSequenced && hasEditRight}">
						<jsp:include page="/learning/SingleQuestionAnswersContent.jsp" />
					</c:when>
					<c:otherwise>
						<jsp:include page="/learning/CombinedAnswersContent.jsp" />
					</c:otherwise>
				</c:choose>
			</form:form>
		</div>
	</lams:Page>
</body>
</lams:html>

