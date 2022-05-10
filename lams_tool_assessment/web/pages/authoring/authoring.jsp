<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" scope="request" />
<c:set var="mode" value="${sessionMap.mode}" scope="request" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" scope="request" />
<c:set var="lamsUrl" scope="request"><lams:LAMSURL/></c:set>

<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title><fmt:message key="label.author.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap4.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components-responsive.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>includes/css/assessment.css">

	<script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>	
	<script src="<lams:LAMSURL/>includes/javascript/bootstrap4.bundle.min.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.pager.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/common.js"></script> 
	
	<script>
		// jQuery UI overrides Bootstrap's tooltip, so store it under another name
		$.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
			
	    function validateForm() {
	        //check with a teacher whether he forgot to add questions to the question bank
	        var referenceCount = $("#referencesTable tr").length - 1;
			if ((referenceCount == 0) && !confirm("<fmt:message key="label.no.questions.in.question.bank"/>")) {
				return false;
			}
	
			var timeLimit = $('#relativeTimeLimit').val();
			if (!timeLimit || timeLimit < 0) {
				$('#relativeTimeLimit').val(0);
			}
	
			//serialize overallFeedbackForm
	        $("#overallFeedbackList").val($('#advancedInputArea').contents().find('#overallFeedbackForm').serialize(true));
	        	
	        //enable checkbox to allow its value been submitted
	        $("#display-summary").removeAttr("disabled", "disabled");
	
	    	return true;
	    }
	    
		$(document).ready(function(){
			$('[data-toggle="tooltip"]').bootstrapTooltip();
			  
			$("#attemptsAllowedRadio").change(function() {
				$("#passingMark").val("0");
				$("#passingMark").prop("disabled", true);
				$("#attemptsAllowed").prop("disabled", false);
			});
			
			$("#passingMarkRadio").change(function() {
				$("#attemptsAllowed").val("0");
				$("#attemptsAllowed").prop("disabled", true);
				$("#passingMark").prop("disabled", false);
			});
			
			$("#display-summary").change(function(){
				$('#display-summary-area').toggle('slow');
				$('#allowQuestionFeedback').prop("checked", false);
				$('#allowDiscloseAnswers').prop("checked", false);
				$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').prop("checked", false).prop('disabled', false);
				$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').parent().removeClass('text-muted');
				$('#allowHistoryResponsesAfterAttempt').prop("checked", false);
			});
	
			$('#allowDiscloseAnswers').change(function(){
				if ($(this).prop('checked')) {
					$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').prop('checked', false).prop('disabled', true);
				} else {
					$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').prop('disabled', false);
				}
				$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').parent().toggleClass('text-muted');
			});
			
			$("#useSelectLeaderToolOuput").change(function() {
				if ($(this).prop('checked')) {
					$("#display-summary").prop("checked", true).prop("disabled", true);
					$('#display-summary-area').show('slow');
					$('#questionEtherpadEnabled').closest('.checkbox').show('slow');
					$('#allowDiscloseAnswers').prop('disabled', false);
				} else {
					$("#display-summary").prop("disabled", false);
					$('#questionEtherpadEnabled').prop("checked", false).closest('.checkbox').hide('slow');
					$('#allowDiscloseAnswers').prop("checked", false).prop('disabled', true).change();
				}		
			}).change();
	
			$("#enable-confidence-levels").change(function(){
				$('#confidence-levels-type-area').toggle('slow');
			});
			
			<c:if test="${assessmentForm.assessment.passingMark == 0}">$("#passingMark").prop("disabled", true);</c:if>
			<c:if test="${assessmentForm.assessment.passingMark > 0}">$("#attemptsAllowed").prop("disabled", true);</c:if>
			<c:if test="${assessmentForm.assessment.useSelectLeaderToolOuput}">$("#display-summary").prop("disabled", true);</c:if>
			<c:if test="${assessmentForm.assessment.allowDiscloseAnswers}">
				$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').prop('disabled', true);
				$('#allowRightAnswersAfterQuestion, #allowWrongAnswersAfterQuestion').parent().addClass('text-muted');
			</c:if>
			<c:if test="${!assessmentForm.assessment.enableConfidenceLevels}">
				$('#confidence-levels-type-area').css('display', 'none');
			</c:if>
			

			$('#reflectInstructions').keyup(function(){
				$('#reflectOnActivity').prop('checked', !isEmpty($(this).val().trim()));
			});
		});
	</script>
</head>

<body class="component">
<lams:PageComponent titleKey="label.author.title">
	<c:if test="${isAuthoringRestricted}">
		<lams:Alert id="edit-in-monitor-while-assessment-already-attempted" type="error" close="true">
			<fmt:message key="label.edit.in.monitor.warning"/>
		</lams:Alert>
	</c:if>
	
	<form:form action="updateContent.do" method="post" modelAttribute="assessmentForm" id="authoringForm" onsubmit="return validateForm()">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="mode" value="${mode}">
		
		<form:hidden path="assessment.contentId" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />

        <div id="content">
			<lams:Panel id="basic" collapsible="false" iconClass="fa-file-o" colorClass="green">
				<jsp:include page="basic.jsp"/>
			</lams:Panel>
				
           <lams:Panel id="questions" titleKey="label.authoring.basic.question.list.title" iconClass="fa-question-circle-o" colorClass="yellow" expanded="true">
				<jsp:include page="questions.jsp"/>
			</lams:Panel>
			
			<lams:Panel id="advanced" titleKey="label.authoring.heading.advance" iconClass="fa-gear" colorClass="purple" expanded="false">
				<jsp:include page="advance.jsp"/>
			</lams:Panel>
			
			<lams:Panel id="leader-selection" titleKey="label.select.leader" iconClass="fa-star-o" colorClass="yellow" expanded="false">
				<jsp:include page="leader.jsp"/>
			</lams:Panel>
			
			<lams:Panel id="feedback" titleKey="label.authoring.basic.general.feedback" iconClass="fa-comment-o" colorClass="blue" expanded="false">
				<jsp:include page="feedback.jsp"/>
			</lams:Panel>
							
			<lams:Panel id="reflection" titleKey="label.activity.completion"  icon="${lamsUrl}images/components/assess-icon7.svg"
						colorClass="green2" expanded="false">
				<jsp:include page="reflection.jsp"/>
			</lams:Panel>
			
			<!-- Button Row -->
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do" toolSignature="<%=AssessmentConstants.TOOL_SIGNATURE%>"
				toolContentID="${assessmentForm.assessment.contentId}"
				accessMode="${mode}" defineLater="${mode=='teacher'}"
				contentFolderID="${assessmentForm.contentFolderID}" />
		</div>
	</form:form>
</lams:PageComponent>
</body>
</lams:html>