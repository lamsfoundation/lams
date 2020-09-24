<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO"%>

<script>
	$(document).ready(function(){
		
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
		});

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
	});
</script>

<div class="col-12 col-xl-6 pr-5">
	<lams:Dropdown name="assessment.questionsPerPage"
		useSpringForm="true"
		labelKey="label.authoring.advance.questions.per.page"
		tooltipKey="label.authoring.advance.questions.per.page.tooltip"
		tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description">
			<option value="0"><fmt:message key="label.authoring.advance.all.in.one.page" /></option>
			<option value="10">10</option>
			<option value="9">9</option>
			<option value="8">8</option>
			<option value="7">7</option>
			<option value="6">6</option>
			<option value="5">5</option>
			<option value="4">4</option>
			<option value="3">3</option>
			<option value="2">2</option>
			<option value="1">1</option>
	</lams:Dropdown>

	<lams:Switch name="assessment.shuffled" id="shuffled"
		useSpringForm="true"
		labelKey="label.authoring.advance.shuffle.questions"
		tooltipKey="label.authoring.advance.shuffle.questions.tooltip"
		tooltipDescriptionKey="label.authoring.advance.shuffle.questions.tooltip.description" />

	<lams:Switch name="assessment.numbered" id="questions-numbering"
		useSpringForm="true"
		labelKey="label.authoring.advance.numbered.questions"
		tooltipKey="label.authoring.advance.numbered.questionss.tooltip"
		tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description" />

	<lams:Input
		id="relativeTimeLimit"
		labelKey="label.authoring.advance.time.limit"
		tooltipKey="label.authoring.advance.time.limit.tooltip"
		tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description">
			<form:input path="assessment.relativeTimeLimit" id="relativeTimeLimit" type="number" min="0" max="999" size="3" cssClass="form-control form-control-select"/>
	</lams:Input>
	
	
	<div class="col-sm-12 form-group row">
		<label class="col-form-label"><fmt:message key="label.authoring.advance.choose.restriction" /></label>
	</div>
	
	<div class="form-group row">
		<label class="col-sm-8 col-form-label" for="attemptsAllowedRadio">
			<input type="radio" name="isAttemptsChosen" id="attemptsAllowedRadio" value="true"
				<c:if test="${assessmentForm.assessment.passingMark == 0}">checked="checked"</c:if>
			/>
			&nbsp;
			<fmt:message key="label.authoring.advance.attempts.allowed" />
		</label>
		
	    <div class="col-sm-4 justify-content-end d-flex">
	    	<form:select path="assessment.attemptsAllowed" id="attemptsAllowed" cssClass="form-control form-control-select">
					<form:option value="0"><fmt:message key="label.authoring.advance.unlimited" /></form:option>
					<form:option value="6">6</form:option>
					<form:option value="5">5</form:option>
					<form:option value="4">4</form:option>
					<form:option value="3">3</form:option>
					<form:option value="2">2</form:option>
					<form:option value="1">1</form:option>
			</form:select>
	    </div>
	</div>
	
	<div class="form-group row">
		<label class="col-sm-8 col-form-label" for="passingMarkRadio">
			<input type="radio" name="isAttemptsChosen" id="passingMarkRadio" value="false"
				<c:if test="${assessmentForm.assessment.passingMark > 0}">checked="checked"</c:if>
			/>
			&nbsp;
			<fmt:message key="label.authoring.advance.passing.mark" />
		</label>
		
	    <div class="col-sm-4 justify-content-end d-flex">
	    	<form:select path="assessment.passingMark" id="passingMark" cssClass="form-control form-control-select" />
	    </div>
	</div>
	
	<lams:Switch name="assessment.enableConfidenceLevels" id="enable-confidence-levels"
		useSpringForm="true"
		labelKey="label.enable.confidence.levels"
		tooltipKey="label.enable.confidence.levels.tooltip"
		tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description" />
		
	<lams:Dropdown name="assessment.confidenceLevelsType"
		useSpringForm="true"
		labelKey="label.scale">
			<option value="${ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_0_TO_100}">
				<fmt:message key="label.0.to.100" />
			</option>
			<option value="${ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_CONFIDENT}">
				<fmt:message key="label.not.confident" />, <fmt:message key="label.confident" />, <fmt:message key="label.very.confident" />
			</option>
			<option value="${ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_SURE}">
				<fmt:message key="label.not.sure" />, <fmt:message key="label.sure" />, <fmt:message key="label.very.sure" />
			</option>
	</lams:Dropdown>
</div>


<div class="col-12 col-xl-6 pr-4">
	<lams:Switch name="assessment.displaySummary" id="display-summary"
		useSpringForm="true"
		labelKey="label.authoring.advance.display.summary"
		tooltipKey="label.authoring.advance.display.summary.tooltip"
		tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description" />
	
	<div id="display-summary-area" class="loffset20" 
		<c:if test="${!assessmentForm.assessment.displaySummary}">style="display:none;"</c:if>
	>
	
		<lams:Switch name="assessment.allowQuestionFeedback" id="allowQuestionFeedback"
			useSpringForm="true"
			labelKey="label.authoring.advance.allow.students.question.feedback"
			tooltipKey="label.authoring.advance.allow.students.question.feedback.tooltip"
			tooltipDescriptionKey="label.authoring.advance.allow.students.question.feedback.tooltip.description" />
		
		<lams:Switch name="assessment.allowDiscloseAnswers" id="allowDiscloseAnswers"
			useSpringForm="true"
			labelKey="label.authoring.advance.disclose.answers"
			tooltipKey="label.authoring.advance.disclose.answers.tooltip"
			tooltipDescriptionKey="label.authoring.advance.disclose.answers.tooltip.description" />
		
		<lams:Switch name="assessment.allowRightAnswersAfterQuestion" id="allowRightAnswersAfterQuestion"
			useSpringForm="true"
			labelKey="label.authoring.advance.allow.students.right.answers"
			tooltipKey="label.authoring.advance.allow.students.right.answers.tooltip"
			tooltipDescriptionKey="label.authoring.advance.allow.students.right.answers.tooltip.description" />
			
		<lams:Switch name="assessment.allowWrongAnswersAfterQuestion" id="allowWrongAnswersAfterQuestion"
			useSpringForm="true"
			labelKey="label.authoring.advance.allow.students.wrong.answers"
			tooltipKey="label.authoring.advance.allow.students.wrong.answers.tooltip"
			tooltipDescriptionKey="label.authoring.advance.allow.students.wrong.answers.tooltip.description" />
			
		<lams:Switch name="assessment.allowWrongAnswersAfterQuestion" id="allowHistoryResponsesAfterAttempt"
			useSpringForm="true"
			labelKey="label.authoring.advance.allow.students.history.responses"
			tooltipKey="label.authoring.advance.allow.students.history.responses.tooltip"
			tooltipDescriptionKey="label.authoring.advance.allow.students.history.responses.tooltip.description" />
	</div>
	
	<lams:Switch name="assessment.allowOverallFeedbackAfterQuestion" id="allowOverallFeedbackAfterQuestion"
			useSpringForm="true"
			labelKey="label.authoring.advance.allow.students.overall.feedback"
			tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
			tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description" />
			
	<lams:Switch name="assessment.allowGradesAfterAttempt" id="allowGradesAfterAttempt"
		useSpringForm="true"
		labelKey="label.authoring.advance.allow.students.grades"
		tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
		tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description" />
		
	<lams:Switch name="assessment.allowAnswerJustification" id="allowAnswerJustification"
		useSpringForm="true"
		labelKey="label.authoring.advance.answer.justification"
		tooltipKey="label.authoring.advance.answer.justification.tooltip"
		tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description" />
</div>