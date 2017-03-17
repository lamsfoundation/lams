<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<tr>
			<td class="complete-item-gif">
				
				<c:if test="${(question.answerString != null) 
						&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
						|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
					<c:choose>
						<c:when test="${question.correctAnswer}">
							<i class="fa fa-check"></i>
						</c:when>
						<c:otherwise>
							<i class="fa fa-times"></i>	
						</c:otherwise>		
					</c:choose>					
					
				</c:if>
						
			</td>
					
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${true}"
	 				<c:if test="${question.answerBoolean}">checked="checked"</c:if>
					disabled="disabled"				 
				/>
			</td>
			<td>
				<fmt:message key="label.learning.true.false.true" />
			</td>
		</tr>
		<tr>
			<td class="complete-item-gif">
				
				<c:if test="${(question.answerString != null) 
						&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
						|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
					
					<c:choose>
						<c:when test="${!question.correctAnswer}">
							<i class="fa fa-check"></i>	
						</c:when>
						<c:otherwise>
							<i class="fa fa-times"></i>	
						</c:otherwise>		
					</c:choose>					
				</c:if>				
						
			</td>
						
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${false}"
	 				<c:if test="${(!question.answerBoolean) and (question.answerString != null)}">checked="checked"</c:if>
					disabled="disabled"
				/>
			</td>
			<td>
				<fmt:message key="label.learning.true.false.false" />
			</td>
		</tr>
	</table>
</div>

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

