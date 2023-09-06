<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader">
		<fmt:message key="label.learning.short.answer.answer" />
	</div>
	
	<div class="table-responsive mb-3 d-flex align-items-center">
		<c:if test="${assessment.allowRightAnswersAfterQuestion || assessment.allowWrongAnswersAfterQuestion}">
			<span class="complete-item-gif">
				<c:if test="${assessment.allowRightAnswersAfterQuestion && question.answerBoolean}">
					<i class="fa fa-check text-success"></i>	
				</c:if>			
				<c:if test="${assessment.allowWrongAnswersAfterQuestion && !question.answerBoolean}">
					<i class="fa fa-times text-danger"></i>	
				</c:if>
			</span>
		</c:if>		
				
		<c:out value='${question.answer}' />		
	</div>		
</c:if>

<c:if test="${assessment.allowQuestionFeedback && (question.questionFeedback != null)}">
	<div class="feedback">
		<c:out value="${question.questionFeedback}" escapeXml="false" />
	</div>
</c:if>	
