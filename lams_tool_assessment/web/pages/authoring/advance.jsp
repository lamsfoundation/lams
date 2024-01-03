<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO"%>
<c:set var="sessionMapID" value="${assessmentForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<script lang="javascript">
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

		$('.sectionEntry:last .sectionRemoveButton', sectionsContainer).removeClass('hidden');

		// sections add/remove functionality
		let sectionAddButton = $('#sectionAddButton').click(function(){
			let sectionsContainer = $('#sectionsContainer'),
					sections = $('.sectionEntry', sectionsContainer),
					sectionSuffix = sections.length + 1,
					section = $('#sectionTemplate').clone().attr('id', 'sectionEntry' + sectionSuffix).removeClass('hidden');
			$('.sectionRemoveButton', sections).addClass('hidden');
			section.appendTo(sectionsContainer);
			$('.sectionRemoveButton', section).removeClass('hidden');

			$('input[type="text"]', section).attr('name', 'sectionName' + sectionSuffix);
			$('select', section).attr('name', 'sectionQuestionCount' + sectionSuffix);
		});

		$('input[name="questionDistributionType"]').change(function(){
			let distributionType = $('input[name="questionDistributionType"]:checked').val(),
					questionsPerPageSelect = $('#questionsPerPage');
			if (distributionType === 'all') {
				questionsPerPageSelect.val(0).prop('disabled', true);
				sectionAddButton.prop('disabled', true);
				return;
			}
			if (distributionType === 'page') {
				questionsPerPageSelect.prop('disabled', false);
				sectionAddButton.prop('disabled', true);
				return;
			}
			if (distributionType === 'section') {
				questionsPerPageSelect.val(-1).prop('disabled', true);
				sectionAddButton.prop('disabled', false);
				return;
			}
		}).first().change();


		// time limit various options functionality
		$('input[name="timeLimit"]').change(function(){
			let timeLimitTypeNone = $('#timeLimitNone'),
					timeLimitTypeRelative = $('#timeLimitRelative'),
					timeLimitTypeRelativeValue = $('#timeLimitRelativeValue'),
					timeLimitTypeAbsolute = $('#timeLimitAbsolute'),
					timeLimitTypeAbsoluteValue = $('#timeLimitAbsoluteValue');

			if (timeLimitTypeNone.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', true).val(0);
				timeLimitTypeAbsoluteValue.prop('disabled', true).val(0);
				return;
			}
			if (timeLimitTypeRelative.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', false);
				timeLimitTypeAbsoluteValue.prop('disabled', true).val(0);
				return;
			}
			if (timeLimitTypeAbsolute.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', true).val(0);
				timeLimitTypeAbsoluteValue.prop('disabled', false);
				return;
			}
		}).first().change();
	});

	function removeSection(button){
		$(button).closest('.sectionEntry').remove();
		$('#sectionsContainer .sectionEntry:last .sectionRemoveButton').removeClass('hidden');
	}
</script>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="assessment.useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>

	<c:if test="${sessionMap.isQuestionEtherpadEnabled}">
		<div class="checkbox loffset20"
			 <c:if test="${!assessmentForm.assessment.useSelectLeaderToolOuput}">style="display:none;"</c:if>>
			<label for="questionEtherpadEnabled">
				<form:checkbox path="assessment.questionEtherpadEnabled" id="questionEtherpadEnabled"/>
				<fmt:message key="label.authoring.advance.question.etherpad" />
			</label>
		</div>
	</c:if>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.question.options">

	<lams:SimplePanel panelBodyClass="panel-body-sm">
		<div class="checkbox">
			<label for="shuffled">
				<form:checkbox path="assessment.shuffled" id="shuffled"/>
				<fmt:message key="label.authoring.advance.shuffle.questions" />
			</label>
		</div>

		<div class="checkbox">
			<label for="shuffledAnswers">
				<form:checkbox path="assessment.shuffledAnswers" id="shuffledAnswers"/>
				<fmt:message key="label.authoring.advance.shuffle.answers" />
			</label>
		</div>

		<div class="checkbox">
			<label for="questions-numbering">
				<form:checkbox path="assessment.numbered" id="questions-numbering"/>
				<fmt:message key="label.authoring.advance.numbered.questions" />
			</label>
		</div>

		<fmt:message key="label.authoring.advance.question.distribution" />

		<div class="loffset20">
			<div class="radio form-inline">
				<label for="questionDistributionTypeAll">
					<input type="radio" name="questionDistributionType" value="all" id="questionDistributionTypeAll"
						${assessmentForm.assessment.questionsPerPage eq 0 ? 'checked' : ''}
					/>
					<fmt:message key="label.authoring.advance.all.in.one.page" />
				</label>
			</div>

			<div class="radio form-inline">
				<label for="questionDistributionTypePage">
					<input type="radio" id="questionDistributionTypePage" name="questionDistributionType" value="page"
						${assessmentForm.assessment.questionsPerPage > 0 ? 'checked' : ''} />
					<fmt:message key="label.authoring.advance.questions.per.page" />
					<form:select path="assessment.questionsPerPage" id="questionsPerPage"
								 cssClass="form-control input-sm loffset10">
						<form:option value="-1" cssClass="hidden">-</form:option>
						<form:option value="0">-</form:option>
						<c:forEach var="pageQuestionCountOption" begin="1" end="50">
							<form:option value="${pageQuestionCountOption}">${pageQuestionCountOption}</form:option>
						</c:forEach>
					</form:select>
				</label>
			</div>

			<div class="radio form-inline">
				<label for="questionDistributionTypeSection">
					<input type="radio" id="questionDistributionTypeSection" name="questionDistributionType" value="section"
						${assessmentForm.assessment.questionsPerPage eq -1 and not empty assessmentForm.assessment.sections ? 'checked' : ''} />
					<fmt:message key="label.authoring.advance.sections" />
				</label>
			</div>
			<div class="radio form-inline loffset20" id="sectionsContainer">
				<button type="button" id="sectionAddButton" class="btn btn-default btn-sm">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.advance.section.add" />
				</button>

				<c:forEach var="section" items="${assessmentForm.assessment.sections}" varStatus="sectionStatus">
					<div class="sectionEntry voffset10">
						<fmt:message key="label.authoring.advance.section.name" />
						<input type="text" class="form-control input-sm loffset10" maxlength="100"
							   value="<c:out value='${section.name}' />" name="sectionName${sectionStatus.index + 1}" />
						<fmt:message key="label.authoring.advance.section.questions.count" />
						<select class="form-control input-sm loffset10" name="sectionQuestionCount${sectionStatus.index + 1}">
							<option value="0" ${section.questionCount eq 0 ? 'selected' : ''}>
								<fmt:message key="label.authoring.advance.section.all.remaining.questions" />
							</option>
							<c:forEach var="sectionQuestionCountOption" begin="1" end="50">
								<option value="${sectionQuestionCountOption}"
									${sectionQuestionCountOption eq section.questionCount ? 'selected' : ''}>
										${sectionQuestionCountOption}
								</option>
							</c:forEach>
						</select>
						<button type="button" class="sectionRemoveButton btn btn-default btn-sm loffset20 hidden"
								onClick="javascript:removeSection(this)">
							<i class="fa fa-remove"></i>&nbsp;<fmt:message key="label.authoring.advance.section.remove" />
						</button>
					</div>
				</c:forEach>
			</div>

			<div id="sectionTemplate" class="sectionEntry voffset10 hidden">
				<fmt:message key="label.authoring.advance.section.name" />
				<input type="text" class="form-control input-sm loffset10" maxlength="100" />
				<fmt:message key="label.authoring.advance.section.questions.count" />
				<select class="form-control input-sm loffset10">
					<option value="0" selected>
						<fmt:message key="label.authoring.advance.section.all.remaining.questions" />
					</option>
					<c:forEach var="sectionQuestionCountOption" begin="1" end="50">
						<option value="${sectionQuestionCountOption}">
								${sectionQuestionCountOption}
						</option>
					</c:forEach>
				</select>
				<button type="button" class="sectionRemoveButton btn btn-default btn-sm loffset20 hidden"
						onClick="javascript:removeSection(this)">
					<i class="fa fa-remove"></i>&nbsp;<fmt:message key="label.authoring.advance.section.remove" />
				</button>
			</div>
		</div>

	</lams:SimplePanel>

	<lams:SimplePanel panelBodyClass="panel-body-sm">

		<fmt:message key="label.authoring.advance.time.limit" />

		<div class="loffset20">
			<div class="radio form-inline">
				<label for="timeLimitNone">
					<input type="radio" name="timeLimit" value="none" id="timeLimitNone"
						${assessmentForm.assessment.relativeTimeLimit eq 0 and assessmentForm.assessment.absoluteTimeLimit eq 0 ? 'checked' : ''}
					/>
					<fmt:message key="label.authoring.advance.time.limit.none" />
				</label>
			</div>

			<div class="radio form-inline">
				<label for="timeLimitRelative">
					<input type="radio" id="timeLimitRelative" name="timeLimit" value="relative"
						${assessmentForm.assessment.relativeTimeLimit > 0 ? 'checked' : ''} />
					<fmt:message key="label.authoring.advance.time.limit.relative" />&nbsp;
				</label>
				<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right"
				   title='<fmt:message key="label.authoring.advance.time.limit.relative.tooltip" />'></i>&nbsp;
				<form:input path="assessment.relativeTimeLimit" type="number" min="0" max="999" size="3"
							id="timeLimitRelativeValue" cssClass="form-control input-sm"/>

			</div>

			<div class="radio form-inline">
				<label for="timeLimitAbsolute">
					<input type="radio" id="timeLimitAbsolute" name="timeLimit" value="absolute"
						${assessmentForm.assessment.absoluteTimeLimit > 0 ? 'checked' : ''} />
					<fmt:message key="label.authoring.advance.time.limit.absolute" />&nbsp;
				</label>
				<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right"
				   title='<fmt:message key="label.authoring.advance.time.limit.absolute.tooltip" />'></i>&nbsp;
				<form:input path="assessment.absoluteTimeLimit" type="number" min="0" max="999" size="3"
							id="timeLimitAbsoluteValue" cssClass="form-control input-sm"/>

			</div>
		</div>
	</lams:SimplePanel>


	<lams:SimplePanel panelBodyClass="panel-body-sm">

		<fmt:message key="label.authoring.advance.choose.restriction" />

		<div class="loffset20">
			<div class="radio form-inline">
				<label for="attemptsAllowedRadio">
					<input type="radio" name="isAttemptsChosen" value="${true}" id="attemptsAllowedRadio"
						   <c:if test="${assessmentForm.assessment.passingMark == 0}">checked="checked"</c:if> />

					<fmt:message key="label.authoring.advance.attempts.allowed" />&nbsp;

					<form:select path="assessment.attemptsAllowed" id="attemptsAllowed" cssClass="form-control input-sm">
						<form:option value="0"><fmt:message key="label.authoring.advance.unlimited" /></form:option>
						<form:option value="6">6</form:option>
						<form:option value="5">5</form:option>
						<form:option value="4">4</form:option>
						<form:option value="3">3</form:option>
						<form:option value="2">2</form:option>
						<form:option value="1">1</form:option>
					</form:select>
				</label>
			</div>

			<div class="radio form-inline">
				<label for="passingMarkRadio">
					<input type="radio" name="isAttemptsChosen" value="${false}" id="passingMarkRadio"
						   <c:if test="${assessmentForm.assessment.passingMark > 0}">checked="checked"</c:if> />

					<fmt:message key="label.authoring.advance.passing.mark" />&nbsp;

					<form:select path="assessment.passingMark" id="passingMark" cssClass="form-control input-sm"/>
				</label>
			</div>

		</div>

	</lams:SimplePanel>

	<lams:SimplePanel panelBodyClass="panel-body-sm">

		<div class="checkbox">
			<label for="display-summary">
				<form:checkbox path="assessment.displaySummary" id="display-summary"/>
				<fmt:message key="label.authoring.advance.display.summary" />
			</label>
		</div>

		<div id="display-summary-area" class="loffset20"
			 <c:if test="${!assessmentForm.assessment.displaySummary}">style="display:none;"</c:if>>

			<div class="checkbox">
				<label for="allowQuestionFeedback">
					<form:checkbox path="assessment.allowQuestionFeedback" id="allowQuestionFeedback"/>
					<fmt:message key="label.authoring.advance.allow.students.question.feedback" />
				</label>
			</div>

			<div class="checkbox">
				<label for="allowDiscloseAnswers">
					<form:checkbox path="assessment.allowDiscloseAnswers" id="allowDiscloseAnswers"/>
					<fmt:message key="label.authoring.advance.disclose.answers" />
				</label>
			</div>

			<div class="checkbox">
				<label for="allowRightAnswersAfterQuestion">
					<form:checkbox path="assessment.allowRightAnswersAfterQuestion" id="allowRightAnswersAfterQuestion"/>
					<fmt:message key="label.authoring.advance.allow.students.right.answers" />
				</label>
			</div>

			<div class="checkbox">
				<label for="allowWrongAnswersAfterQuestion">
					<form:checkbox path="assessment.allowWrongAnswersAfterQuestion" id="allowWrongAnswersAfterQuestion"/>
					<fmt:message key="label.authoring.advance.allow.students.wrong.answers" />
				</label>
			</div>

			<div class="checkbox">
				<label for="allowHistoryResponsesAfterAttempt">
					<form:checkbox path="assessment.allowHistoryResponses" id="allowHistoryResponsesAfterAttempt"/>
					<fmt:message key="label.authoring.advance.allow.students.history.responses" />
				</label>
			</div>
		</div>

		<div class="checkbox">
			<label for="displayMaxMark">
				<form:checkbox path="assessment.displayMaxMark" id="displayMaxMark"/>
				<fmt:message key="label.authoring.advance.show.max.mark" />
			</label>
		</div>

		<div class="checkbox">
			<label for="allowOverallFeedbackAfterQuestion">
				<form:checkbox path="assessment.allowOverallFeedbackAfterQuestion" id="allowOverallFeedbackAfterQuestion"/>
				<fmt:message key="label.authoring.advance.allow.students.overall.feedback" />
			</label>
		</div>

		<div class="checkbox">
			<label for="allowGradesAfterAttempt">
				<form:checkbox path="assessment.allowGradesAfterAttempt" id="allowGradesAfterAttempt"/>
				<fmt:message key="label.authoring.advance.allow.students.grades" />
			</label>
		</div>

		<div class="checkbox">
			<label for="allowAnswerJustification">
				<form:checkbox path="assessment.allowAnswerJustification" id="allowAnswerJustification"/>
				<fmt:message key="label.authoring.advance.answer.justification" />
			</label>
		</div>

		<div class="checkbox">
			<label for="allowDiscussionSentiment">
				<form:checkbox path="assessment.allowDiscussionSentiment" id="allowDiscussionSentiment"/>
				<fmt:message key="label.authoring.advance.discussion" />
			</label>
			<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="label.authoring.advance.discussion.tooltip" />"></i>
		</div>

		<div class="checkbox">
			<label for="enable-confidence-levels">
				<form:checkbox path="assessment.enableConfidenceLevels" id="enable-confidence-levels"/>
				<fmt:message key="label.enable.confidence.levels" />
			</label>
			<br>

			<label for="assessment.confidenceLevelsType" id="confidence-levels-type-area" class="radio form-inline">
				<fmt:message key="label.scale" />&nbsp;

				<form:select path="assessment.confidenceLevelsType" cssClass="form-control input-sm">
					<form:option value="<%= ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_0_TO_100 %>">
						<fmt:message key="label.0.to.100" />
					</form:option>
					<form:option value="<%= ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_CONFIDENT %>">
						<fmt:message key="label.not.confident" />, <fmt:message key="label.confident" />, <fmt:message key="label.very.confident" />
					</form:option>
					<form:option value="<%= ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_SURE %>">
						<fmt:message key="label.not.sure" />, <fmt:message key="label.sure" />, <fmt:message key="label.very.sure" />
					</form:option>
				</form:select>
			</label>
		</div>
	</lams:SimplePanel>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
	<div class="checkbox">
		<label for="notifyTeachersOnAttemptCompletion">
			<form:checkbox path="assessment.notifyTeachersOnAttemptCompletion" id="notifyTeachersOnAttemptCompletion"/>
			<fmt:message key="label.authoring.advanced.notify.on.attempt.completion" />
		</label>
	</div>
</lams:SimplePanel>

<!-- Overall feedback -->
<lams:SimplePanel titleKey="label.authoring.advance.overall.feedback">

	<input type="hidden" name="overallFeedbackList" id="overallFeedbackList" />
	<iframe id="advancedInputArea" name="advancedInputArea" style="width:650px;height:100%;border:0px;display:block;"
			frameborder="no" scrolling="no" src="<c:url value='/authoring/initOverallFeedback.do'/>?sessionMapID=${assessmentForm.sessionMapID}">
	</iframe>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${assessmentForm.assessment.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="reflectOnActivity">
			<form:checkbox path="assessment.reflectOnActivity" id="reflectOnActivity"/>
			<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>

	<div class="form-group">
		<form:textarea path="assessment.reflectInstructions" rows="3" id="reflectInstructions" cssClass="form-control"/>
	</div>
</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOnActivity");
	function turnOnRefect(){
		if(isEmpty(ra.value)){
			//turn off	
			rao.checked = false;
		}else{
			//turn on
			rao.checked = true;
		}
	}

	ra.onkeyup=turnOnRefect;
</script>