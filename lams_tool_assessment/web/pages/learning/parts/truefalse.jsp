<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
		<tr>
			<c:if test="${finishedLock}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				
					<c:if test="${(question.answerString != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
						<c:choose>
							<c:when test="${question.correctAnswer}">
								<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
							</c:when>
							<c:otherwise>
								<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">	
							</c:otherwise>		
						</c:choose>					
					
					</c:if>
						
				</td>		
			</c:if>			
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; width: 5px; border-bottom:0px; ">
						<input type="radio" name="question${status.index}" value="${true}" styleClass="noBorder"
	 						<c:if test="${question.answerBoolean}">checked="checked"</c:if>
							<c:if test="${finishedLock || !hasEditRight}">disabled="disabled"</c:if>					 
						/>
			</td>
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;">
				<fmt:message key="label.learning.true.false.true" />
			</td>
		</tr>
		<tr>
			<c:if test="${finishedLock}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				
					<c:if test="${(question.answerString != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
						<c:choose>
							<c:when test="${!question.correctAnswer}">
								<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
							</c:when>
							<c:otherwise>
								<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">	
							</c:otherwise>		
						</c:choose>					
					</c:if>				
						
				</td>		
			</c:if>			
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; width: 5px; border-bottom:0px; ">
						<input type="radio" name="question${status.index}" value="${false}" styleClass="noBorder"
	 						<c:if test="${(!question.answerBoolean) and (question.answerString != null)}">checked="checked"</c:if>
							<c:if test="${finishedLock || !hasEditRight}">disabled="disabled"</c:if>					 
						/>
			</td>
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;">
				<fmt:message key="label.learning.true.false.false" />
			</td>
		</tr>		

</table>	

<c:if test="${finishedLock && assessment.allowQuestionFeedback}">
	<c:choose>
		<c:when test="${question.answerBoolean and (question.answerString != null)}">
			<div style="padding: 15px 15px 0px; font-style: italic; color:#47bc23;">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${!question.answerBoolean and (question.answerString != null)}">
			<div style="padding: 15px 15px 0px; font-style: italic; color:#47bc23;">
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</div>
		</c:when>		
	</c:choose>
</c:if>

<%@ include file="markandpenaltyarea.jsp"%>