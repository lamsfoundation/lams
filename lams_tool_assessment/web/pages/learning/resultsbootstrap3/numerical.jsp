<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="question-type">
		<fmt:message key="label.learning.short.answer.answer" />
	</div>
	
	<div class="table-responsive">
		<table class="table table-hover table-condensed">
			<tr>
				<td class="complete-item-gif">
					<c:if test="${assessment.allowRightAnswersAfterQuestion && question.answerBoolean}">
						<i class="fa fa-check text-success"></i>	
					</c:if>			
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && !question.answerBoolean}">
						<i class="fa fa-times text-danger"></i>	
					</c:if>
				</td>
				
				<td>
					<input type="text" autocomplete="off" name="question${status.index}" value="<c:out value='${question.answer}' />" class="form-control"
						disabled="disabled"
					/>	
				</td>
			</tr>
		</table>
	</div>		
</c:if>

<c:if test="${assessment.allowQuestionFeedback && (question.questionFeedback != null)}">
	<div class="feedback">
		<c:out value="${question.questionFeedback}" escapeXml="false" />
	</div>
</c:if>	
