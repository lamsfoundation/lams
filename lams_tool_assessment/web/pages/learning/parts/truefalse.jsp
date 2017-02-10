<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table class="question-table">
		<tr>
			<c:if test="${finishedLock}">
				<td class="complete-item-gif">
				
					<c:if test="${(question.answerString != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
						<c:choose>
							<c:when test="${question.correctAnswer}">
								<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">	
							</c:when>
							<c:otherwise>
								<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">	
							</c:otherwise>		
						</c:choose>					
					
					</c:if>
						
				</td>		
			</c:if>			
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${true}" styleClass="noBorder"
	 				<c:if test="${question.answerBoolean}">checked="checked"</c:if>
					<c:if test="${isEditingDisabled}">disabled="disabled"</c:if>					 
				/>
			</td>
			<td class="question-option">
				<fmt:message key="label.learning.true.false.true" />
			</td>
		</tr>
		<tr>
			<c:if test="${finishedLock}">
				<td class="complete-item-gif">
				
					<c:if test="${(question.answerString != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
						<c:choose>
							<c:when test="${!question.correctAnswer}">
								<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">	
							</c:when>
							<c:otherwise>
								<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">	
							</c:otherwise>		
						</c:choose>					
					</c:if>				
						
				</td>		
			</c:if>			
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${false}" styleClass="noBorder"
	 				<c:if test="${(!question.answerBoolean) and (question.answerString != null)}">checked="checked"</c:if>
					<c:if test="${isEditingDisabled}">disabled="disabled"</c:if>
				/>
			</td>
			<td class="question-option">
				<fmt:message key="label.learning.true.false.false" />
			</td>
		</tr>		

</table>	

<c:if test="${finishedLock && assessment.allowQuestionFeedback && (question.answerString != null)}">
	<c:choose>
		<c:when test="${question.answerBoolean}">
			<div class="feedback">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${!question.answerBoolean}">
			<div class="feedback">
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</div>
		</c:when>		
	</c:choose>
</c:if>

<%@ include file="markandpenaltyarea.jsp"%>
