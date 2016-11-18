<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
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
	<script language="JavaScript" type="text/JavaScript">

        //autoSaveAnswers if hasEditRight
        if (${hasEditRight}) {
          var interval = "30000"; // = 30 seconds
          window.setInterval(
            function(){
              //ajax form submit
              $('#learningForm').ajaxSubmit({
                url: "<c:url value='/learning.do?method=autoSaveAnswers&date='/>" + new Date().getTime(),
                success: function() {
                  $.growlUI('<i class="fa fa-lg fa-floppy-o"></i> <fmt:message key="label.learning.draft.autosaved" />');
                }
              });
            }, interval
          );
        }

        function submitNextQuestionSelected() {
          if (verifyAllQuestionsAnswered()) {
            ++document.McLearningForm.questionIndex.value;
            document.McLearningForm.nextQuestionSelected.value = 1;
            disableContinueButton();
            document.McLearningForm.submit();
          }
        }

        function submitAllAnswers() {
          document.McLearningForm.continueOptionsCombined.value = 1;			
          doSubmit();
        }

        function doSubmit() {
          if (verifyAllQuestionsAnswered()) {
            disableContinueButton();
            document.McLearningForm.submit();
          }
        }

        function verifyAllQuestionsAnswered() {
          // in case oneQuestionPerPage option is ON user has to select 1 answer, and all answers otherwise
          var isOneQuestionPerPage = ${sessionMap.content.questionsSequenced};
          var answersRequiredNumber = (isOneQuestionPerPage) ? 1 : ${fn:length(requestScope.learnerAnswersDTOList)};

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
            url: '<c:url value="/learning.do"/>',
            data: 'method=checkLeaderProgress&toolSessionID=' + $("#tool-session-id").val(),
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

	<lams:Page type="learner" title="${mcGeneralLearnerFlowDTO.activityTitle}">

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

		<div class="form-group">
			<html:form styleId="learningForm" action="/learning?method=displayMc&validate=false" enctype="multipart/form-data"
				method="POST" target="_self">
				<html:hidden property="toolContentID" />
				<html:hidden property="toolSessionID" styleId="tool-session-id" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="userID" />
				<html:hidden property="userOverPassMark" />
				<html:hidden property="passMarkApplicable" />

				<%@ include file="/common/messages.jsp"%>

				<c:if test="${isLeadershipEnabled}">
					<h4>
						<fmt:message key="label.group.leader">
							<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
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
			</html:form>
		</div>
	</lams:Page>
</body>
</lams:html>

