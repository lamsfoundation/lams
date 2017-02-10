<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table class="question-table">
	<tr>
		<c:if test="${finishedLock}">
			<td class="complete-item-gif">
			
				<c:if test="${assessment.allowRightAnswersAfterQuestion && question.answerBoolean}">
					<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">	
				</c:if>			
				<c:if test="${assessment.allowWrongAnswersAfterQuestion && !question.answerBoolean}">
					<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">
				</c:if>
				
			</td>		
		</c:if>		
		<td class="reg-padding">
			<input type="text" autocomplete="off" name="question${status.index}" value="<c:out value='${question.answerString}' />" styleClass="noBorder" size="70"
				<c:if test="${isEditingDisabled}">disabled="disabled"</c:if>
			/>	
		</td>
	</tr>
</table>		

<c:if test="${finishedLock && assessment.allowQuestionFeedback && (question.questionFeedback != null)}">
	<div class="feedback">
		<c:out value="${question.questionFeedback}" escapeXml="false" />
	</div>
</c:if>	

<%@ include file="markandpenaltyarea.jsp"%>
