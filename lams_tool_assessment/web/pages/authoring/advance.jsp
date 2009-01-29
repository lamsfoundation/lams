<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->
<p class="small-space-top">
	<html:text property="assessment.timeLimit" size="3" styleId="timeLimit"></html:text>
	<label for="timeLimit">
		<fmt:message key="label.authoring.advance.time.limit" />
	</label>
</p>

<p>
	<html:select property="assessment.questionsPerPage">
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
	<fmt:message key="label.authoring.advance.questions.per.page" />
</p>

<p>
	<html:checkbox property="assessment.shuffled" styleClass="noBorder" styleId="shuffled">
	</html:checkbox>
	<label for="shuffled">
		<fmt:message key="label.authoring.advance.shuffle.questions" />
	</label>
</p>

<p>
	<html:select property="assessment.attemptsAllowed" >
		<html:option value="0"><fmt:message key="label.authoring.advance.unlimited" /></html:option>
		<html:option value="6">6</html:option>
		<html:option value="5">5</html:option>
		<html:option value="4">4</html:option>
		<html:option value="3">3</html:option>
		<html:option value="2">2</html:option>
		<html:option value="1">1</html:option>
	</html:select>
	<fmt:message key="label.authoring.advance.attempts.allowed" />
</p>

<p>
	<html:checkbox property="assessment.allowOverallFeedbackAfterQuestion" styleClass="noBorder" styleId="allowOverallFeedbackAfterQuestion">
	</html:checkbox>
	<label for="allowOverallFeedbackAfterQuestion">
		<fmt:message key="label.authoring.advance.allow.students.overall.feedback" />
	</label>
</p>

<p>
	<html:checkbox property="assessment.allowQuestionFeedback" styleClass="noBorder" styleId="allowQuestionFeedback">
	</html:checkbox>
	<label for="allowQuestionFeedback">
		<fmt:message key="label.authoring.advance.allow.students.question.feedback" />
	</label>
</p>

<p>
	<html:checkbox property="assessment.allowRightWrongAnswersAfterQuestion" styleClass="noBorder" styleId="allowRightWrongAnswersAfterQuestion">
	</html:checkbox>
	<label for="allowRightWrongAnswersAfterQuestion">
		<fmt:message key="label.authoring.advance.allow.students.right.wrong.answers" />
	</label>
</p>

<p>
	<html:checkbox property="assessment.allowGradesAfterAttempt" styleClass="noBorder" styleId="allowGradesAfterAttempt">
	</html:checkbox>
	<label for="allowGradesAfterAttempt">
		<fmt:message key="label.authoring.advance.allow.students.grades" />
	</label>
</p>

<p>
	<html:checkbox property="assessment.notifyTeachersOnAttemptCompletion"
		styleClass="noBorder" styleId="notifyTeachersOnAttemptCompletion">
	</html:checkbox>
	<label for="notifyTeachersOnAttemptCompletion">
		<fmt:message key="label.authoring.advanced.notify.on.attempt.completion" />
	</label>
</p>

<!-- Overall feedback -->
<input type="hidden" name="overallFeedbackList" id="overallFeedbackList" />
<p>
	<iframe 
		id="advancedInputArea" name="advancedInputArea"
		style="width:650px;height:100px;border:0px;display:block;" frameborder="no" scrolling="no" src="<c:url value='/authoring/initOverallFeedback.do'/>?sessionMapID=${formBean.sessionMapID}">
	</iframe>
</p>
