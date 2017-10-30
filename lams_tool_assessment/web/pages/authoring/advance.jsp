<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script lang="javascript">
	$(document).ready(function(){
		$("#attemptsAllowedRadio").click(function() {
			$("#passingMark").val("0");
			$("#passingMark").prop("disabled", true);
			$("#attemptsAllowed").prop("disabled", false);
		});
		
		$("#passingMarkRadio").click(function() {
			$("#attemptsAllowed").val("0");
			$("#attemptsAllowed").prop("disabled", true);
			$("#passingMark").prop("disabled", false);
		});
		
		$("#display-summary").click(function(){
			$('#display-summary-area').toggle('slow');
			$('#allowQuestionFeedback').prop("checked", false);
			$('#allowRightAnswersAfterQuestion').prop("checked", false);
			$('#allowWrongAnswersAfterQuestion').prop("checked", false);
			$('#allowHistoryResponsesAfterAttempt').prop("checked", false);
		});
		
		$("#useSelectLeaderToolOuput").click(function() {
			if ($("#useSelectLeaderToolOuput").is(':checked')) {
				$("#display-summary").prop("checked", true).prop("disabled", true);
				$('#display-summary-area').show('slow');
				
			} else {
				$("#display-summary").prop("disabled", false);
			}		
		});
		
		<c:if test="${formBean.assessment.passingMark == 0}">$("#passingMark").prop("disabled", true);</c:if>
		<c:if test="${formBean.assessment.passingMark > 0}">$("#attemptsAllowed").prop("disabled", true);</c:if>
		<c:if test="${formBean.assessment.useSelectLeaderToolOuput}">$("#display-summary").prop("disabled", true);</c:if>
	});
</script>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.select.leader">
<div class="checkbox">
	<label for="useSelectLeaderToolOuput">
		<html:checkbox property="assessment.useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput"/>
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.question.options">

<lams:SimplePanel panelBodyClass="panel-body-sm">

<div class="form-inline">
	<label for="assessment.questionsPerPage">
		<fmt:message key="label.authoring.advance.questions.per.page" />&nbsp;
	<html:select property="assessment.questionsPerPage" styleClass="form-control input-sm">
		<html:option value="0"><fmt:message key="label.authoring.advance.all.in.one.page" /></html:option>
		<html:option value="10">10</html:option>
		<html:option value="9">9</html:option>
		<html:option value="8">8</html:option>
		<html:option value="7">7</html:option>
		<html:option value="6">6</html:option>
		<html:option value="5">5</html:option>
		<html:option value="4">4</html:option>
		<html:option value="3">3</html:option>
		<html:option value="2">2</html:option>
		<html:option value="1">1</html:option>
	</html:select>
	</label>
</div>

<div class="checkbox">
	<label for="shuffled">
		<html:checkbox property="assessment.shuffled" styleId="shuffled"/>
		<fmt:message key="label.authoring.advance.shuffle.questions" />
	</label>
</div>

<div class="checkbox">
	<label for="questions-numbering">
		<html:checkbox property="assessment.numbered" styleId="questions-numbering"/>
		<fmt:message key="label.authoring.advance.numbered.questions" />
	</label>
</div>

</lams:SimplePanel>

<lams:SimplePanel panelBodyClass="panel-body-sm">

<div class="form-inline">
	<label for="timeLimit">
		<fmt:message key="label.authoring.advance.time.limit" />&nbsp;
		<html:text property="assessment.timeLimit" size="3" styleId="timeLimit" styleClass="form-control input-sm"/>
	</label>
</div>

<fmt:message key="label.authoring.advance.choose.restriction" />
	
<div class="loffset20">
	<div class="radio form-inline">
		<label for="attemptsAllowedRadio">
			<input type="radio" name="isAttemptsChosen" value="${true}" id="attemptsAllowedRadio"
					<c:if test="${formBean.assessment.passingMark == 0}">checked="checked"</c:if> />
					
			<fmt:message key="label.authoring.advance.attempts.allowed" />&nbsp;
			
			<html:select property="assessment.attemptsAllowed" styleId="attemptsAllowed" styleClass="form-control input-sm">
				<html:option value="0"><fmt:message key="label.authoring.advance.unlimited" /></html:option>
				<html:option value="6">6</html:option>
				<html:option value="5">5</html:option>
				<html:option value="4">4</html:option>
				<html:option value="3">3</html:option>
				<html:option value="2">2</html:option>
				<html:option value="1">1</html:option>
			</html:select>
		</label>
	</div>

	<div class="radio form-inline">
		<label for="passingMarkRadio">
			<input type="radio" name="isAttemptsChosen" value="${false}" id="passingMarkRadio"	
					<c:if test="${formBean.assessment.passingMark > 0}">checked="checked"</c:if> />
					
			<fmt:message key="label.authoring.advance.passing.mark" />&nbsp;
			
			<html:select property="assessment.passingMark" styleId="passingMark" styleClass="form-control input-sm"/>
		</label>
	</div>
	
</div>

</lams:SimplePanel>

<lams:SimplePanel panelBodyClass="panel-body-sm">

<div class="checkbox">
	<label for="display-summary">
		<html:checkbox property="assessment.displaySummary" styleId="display-summary"/>
		<fmt:message key="label.authoring.advance.display.summary" />
	</label>
</div>

<div id="display-summary-area" class="loffset20" 
		<c:if test="${!formBean.assessment.displaySummary}">style="display:none;"</c:if>>

	<div class="checkbox">
		<label for="allowQuestionFeedback">
			<html:checkbox property="assessment.allowQuestionFeedback" styleId="allowQuestionFeedback"/>
			<fmt:message key="label.authoring.advance.allow.students.question.feedback" />
		</label>
	</div>

	<div class="checkbox">
		<label for="allowRightAnswersAfterQuestion">
			<html:checkbox property="assessment.allowRightAnswersAfterQuestion" styleId="allowRightAnswersAfterQuestion"/>
			<fmt:message key="label.authoring.advance.allow.students.right.answers" />
		</label>
	</div>

	<div class="checkbox">
		<label for="allowWrongAnswersAfterQuestion">
			<html:checkbox property="assessment.allowWrongAnswersAfterQuestion" styleId="allowWrongAnswersAfterQuestion"/>
			<fmt:message key="label.authoring.advance.allow.students.wrong.answers" />
		</label>
	</div>

	<div class="checkbox">
		<label for="allowHistoryResponsesAfterAttempt">
			<html:checkbox property="assessment.allowHistoryResponses" styleId="allowHistoryResponsesAfterAttempt"/>
			<fmt:message key="label.authoring.advance.allow.students.history.responses" />
		</label>
	</div>
</div>

<div class="checkbox">
	<label for="allowOverallFeedbackAfterQuestion">
		<html:checkbox property="assessment.allowOverallFeedbackAfterQuestion" styleId="allowOverallFeedbackAfterQuestion"/>
		<fmt:message key="label.authoring.advance.allow.students.overall.feedback" />
	</label>
</div>

<div class="checkbox">
	<label for="allowGradesAfterAttempt">
		<html:checkbox property="assessment.allowGradesAfterAttempt" styleId="allowGradesAfterAttempt"/>
		<fmt:message key="label.authoring.advance.allow.students.grades" />
	</label>
</div>

<div class="checkbox">
	<label for="enable-confidence-levels">
		<html:checkbox property="assessment.enableConfidenceLevels" styleId="enable-confidence-levels"/>
		<fmt:message key="label.enable.confidence.levels" />
	</label>
</div>
</lams:SimplePanel>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
<div class="checkbox">
	<label for="notifyTeachersOnAttemptCompletion">
		<html:checkbox property="assessment.notifyTeachersOnAttemptCompletion" styleId="notifyTeachersOnAttemptCompletion"/>
		<fmt:message key="label.authoring.advanced.notify.on.attempt.completion" />
	</label>
</div>
</lams:SimplePanel>

<!-- Overall feedback -->
<lams:SimplePanel titleKey="label.authoring.advance.overall.feedback">

<input type="hidden" name="overallFeedbackList" id="overallFeedbackList" />
<iframe id="advancedInputArea" name="advancedInputArea" style="width:650px;height:100%;border:0px;display:block;" 
		frameborder="no" scrolling="no" src="<c:url value='/authoring/initOverallFeedback.do'/>?sessionMapID=${formBean.sessionMapID}">
</iframe>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="reflectOnActivity">
		<html:checkbox property="assessment.reflectOnActivity" styleId="reflectOnActivity"/>
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="assessment.reflectInstructions" rows="3" styleId="reflectInstructions" styleClass="form-control"/>
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

