<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.grade}</fmt:param>
	</fmt:message>
</div>

<table class="question-table">
	<c:forEach var="option" items="${question.options}">
		<tr>
			<c:if test="${finishedLock || question.responseSubmitted}">
				<td class="complete-item-gif">
				
					<c:if test="${assessment.allowRightAnswersAfterQuestion && option.correct}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && !option.correct}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">
					</c:if>
						
				</td>
			</c:if>
			
			<td class="question-option">
				<c:out value="${option.optionString}" escapeXml="false" />
			</td>
			
			<td class="has-radio-button">
			
				<select name="question${questionIndex}_${option.sequenceId}" class="mark-hedging-select" data-question-index="${questionIndex}"
					<c:if test="${isEditingDisabled || question.responseSubmitted}">disabled="disabled"</c:if>				
				>
					
					<c:forEach var="i" begin="0" end="${question.grade}">
						<option
							<c:if test="${option.answerInt == i}">selected="selected"</c:if>						
						>${i}</option>
					</c:forEach>
					
				</select>
			</td>
			
			<c:if test="${(finishedLock || question.responseSubmitted) && (option.answerInt > 0) && assessment.allowQuestionFeedback}">

				<c:choose>
                	<c:when test="${option.correct}">
                    	<c:set var="color" scope="page" value="red" />
        			</c:when>
					<c:otherwise>
                    	<c:set var="color" scope="page" value="blue" />
        			</c:otherwise>
        		</c:choose>

				<td style="padding:5px 10px 2px; font-style: italic; color:${color}; width=30%;">
					<c:out value="${option.feedback}" escapeXml="false" />
				</td>
			</c:if>
			
		</tr>
	</c:forEach>
</table>

<c:if test="${!finishedLock && !question.responseSubmitted && isLeadershipEnabled && isUserLeader}">
	<div>
		<html:button property="submit-hedging-question${questionIndex}" onclick="return submitSingleMarkHedgingQuestion(${question.uid}, ${questionIndex});" 
				styleClass="button">
			<fmt:message key="label.learning.submit" />
		</html:button>
	</div>
</c:if>

<c:if test="${finishedLock || question.responseSubmitted}">
	<c:if test="${assessment.allowQuestionFeedback}">
		<div class="question-feedback">
			<c:choose>
				<c:when test="${question.mark == question.grade}">
					<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
				</c:when>
				<c:when test="${question.mark > 0}">
					<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
				</c:when>
				<c:when test="${question.mark <= 0}">
					<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
				</c:when>		
			</c:choose>
		</div>
	</c:if>

	<c:if test="${assessment.allowQuestionFeedback && (question.generalFeedback != null)}">
		<div class="question-feedback">
			<c:out value="${question.generalFeedback}" escapeXml="false" />
		</div>
	</c:if>
	
	<div class="question-feedback" style="padding-bottom: 10px;">
		<fmt:message key="label.learning.marks" >
			<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
			<fmt:param>${question.grade}</fmt:param>
		</fmt:message>
		<c:if test="${(question.mark != question.grade) && (fn:length(question.questionResults) > 1)}">
			<fmt:message key="label.learning.penalty" >
				<fmt:param><fmt:formatNumber value="${question.penalty}" maxFractionDigits="2"/></fmt:param>
			</fmt:message>
		</c:if>
	</div>
</c:if>
