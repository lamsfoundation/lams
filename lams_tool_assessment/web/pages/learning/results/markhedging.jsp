<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.maxMark}</fmt:param>
	</fmt:message>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td class="complete-item-gif">
					
					<c:if test="${assessment.allowRightAnswersAfterQuestion && option.correct}">
						<i class="fa fa-check text-success"></i>
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && !option.correct}">
						<i class="fa fa-times text-danger"></i>
					</c:if>
							
				</td>
				
				<td>
					<c:out value="${option.name}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
				
					<select name="question${questionIndex}_${option.displayOrder}" class="mark-hedging-select" data-question-index="${questionIndex}"
							disabled="disabled">
						
						<c:forEach var="i" begin="0" end="${question.maxMark}">
							<option
								<c:if test="${option.answerInt == i}">selected="selected"</c:if>						
							>${i}</option>
						</c:forEach>
						
					</select>
				</td>
				
				<c:if test="${assessment.allowQuestionFeedback}">
					<td width="30%">
						<c:if test="${option.answerInt > 0}">
							<c:out value="${option.feedback}" escapeXml="false" />
						</c:if>
					</td>
				</c:if>
				
			</tr>
		</c:forEach>
	</table>
		
	<c:if test="${question.hedgingJustificationEnabled}">
		<lams:textarea id="justification-question${questionIndex}" name="question${questionIndex}" class="mark-hedging-select margin-top-minus-10"
						disabled="disabled" rows="4" cols="60">
			<c:out value="${question.answer}" />
		</lams:textarea>
	</c:if>
	
</div>

<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when test="${question.mark == question.maxMark}">
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

<c:if test="${assessment.allowQuestionFeedback && (question.feedback != null)}">
	<div class="feedback">
		<c:out value="${question.feedback}" escapeXml="false" />
	</div>
</c:if>
	
<div class="question-feedback" style="padding-bottom: 10px;">
	<fmt:message key="label.learning.marks" >
		<fmt:param><fmt:formatNumber value="${question.mark}" maxFractionDigits="3"/></fmt:param>
		<fmt:param>${question.maxMark}</fmt:param>
	</fmt:message>
	<c:if test="${(question.mark != question.maxMark) && (fn:length(question.questionResults) > 1)}">
		<fmt:message key="label.learning.penalty" >
			<fmt:param><fmt:formatNumber value="${question.penalty}" maxFractionDigits="2"/></fmt:param>
		</fmt:message>
	</c:if>
</div>
