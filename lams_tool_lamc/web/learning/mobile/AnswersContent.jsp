<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>
	
	<script language="JavaScript" type="text/JavaScript">
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
			var isOneQuestionPerPage = ${mcGeneralLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'};
			var answersRequiredNumber = (isOneQuestionPerPage) ? 1 : ${fn:length(requestScope.listQuestionCandidateAnswersDto)};
			
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
			
	</script>
</lams:head>

<body class="large-font">
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>	
	</div>

	<div data-role="content">
	<html:form  action="/learning?method=displayMc&validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="toolContentID"/>						
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="httpSessionID"/>			
		<html:hidden property="userID"/>								
		<html:hidden property="userOverPassMark"/>						
		<html:hidden property="passMarkApplicable"/>										
		<html:hidden property="learnerProgress"/>										
		<html:hidden property="learnerProgressUserId"/>										
		<html:hidden property="questionListingMode"/>					
		
		<%@ include file="/common/messages.jsp"%>
		
		<c:choose> 
		  <c:when test="${mcGeneralLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}" > 
				<jsp:include page="/learning/mobile/SingleQuestionAnswersContent.jsp" /> 
		  </c:when> 
		  <c:otherwise>
			  	<jsp:include page="/learning/mobile/CombinedAnswersContent.jsp" /> 
		  </c:otherwise>
		</c:choose> 
	</html:form>
	
</div>

</body>
</lams:html>