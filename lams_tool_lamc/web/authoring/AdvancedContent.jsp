<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput" onclick="clickSelectLeaderToolOuputHandler();"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.question.options">
	<div class="checkbox">
		<label for="prefixAnswersWithLetters">
			<form:checkbox path="prefixAnswersWithLetters" value="1" id="prefixAnswersWithLetters"/>
			<fmt:message key="label.prefix.sequential.letters.for.each.answer" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="questionsSequenced">
			<form:checkbox path="questionsSequenced" value="1" id="questionsSequenced"/>
			<fmt:message key="radiobox.onepq" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="showMarks">
			<form:checkbox path="showMarks" value="1" id="showMarks"/>
			<fmt:message key="label.showMarks" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="randomize">
			<form:checkbox path="randomize" value="1" id="randomize"/>
			<fmt:message key="label.randomize" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="retries">
			<form:checkbox path="retries" value="1" id="retries"/>
			<fmt:message key="radiobox.retries" />
		</label>
	</div>
	
	<div class="form-inline loffset20">
		<div id="passmark-container" class="form-group"
				<c:if test="${mcAuthoringForm.retries != 1}">style="display:none;"</c:if>>
			<select id="passmark" name="passmark"  class="form-control input-sm">
				<c:if test="${mcAuthoringForm.passmark != 0}">
					<option value="${mcAuthoringForm.passmark}" SELECTED>
						${mcAuthoringForm.passmark}
					</option>
				</c:if>
			</select>
			<fmt:message key="radiobox.passmark" />
		</div>
	</div>

	<div class="checkbox">
		<label for="enable-confidence-levels">
			<form:checkbox path="enableConfidenceLevels" value="true" id="enable-confidence-levels"/>
			<fmt:message key="label.enable.confidence.levels" />
		</label>
	</div>

	<lams:SimplePanel panelBodyClass="panel-body-sm" titleKey="label.feedback.simple">
	<div class="radio">
		<label for="displayAnswers">
			<form:radiobutton path="displayAnswersFeedback" value="answers" id="displayAnswers"/>
			<fmt:message key="label.displayAnswers" />		
		</label>
	</div>

	<div class="radio form-inline">
		<label for="displayFeedback">
			<form:radiobutton path="displayAnswersFeedback" value="feedback" id="displayFeedback"/>
			<fmt:message key="label.displayFeedbackOnly" />
		</label>
	</div>

	<div class="radio form-inline">
		<label for="displayNoAnswersOrFeedback">
			<form:radiobutton path="displayAnswersFeedback" value="none" id="displayNoAnswersOrFeedback"/>
			<fmt:message key="label.displayNoAnswersOrFeedback" />
		</label>
	</div>
	</lams:SimplePanel>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />
