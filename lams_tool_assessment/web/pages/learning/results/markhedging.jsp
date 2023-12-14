<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader" id="instructions-${questionIndex}">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.maxMark}</fmt:param>
	</fmt:message>
</div>

<div class="table table-sm div-hover px-3">
	<c:forEach var="option" items="${question.optionDtos}">
		<div class="row">
			<c:if test="${assessment.allowRightAnswersAfterQuestion || assessment.allowWrongAnswersAfterQuestion}">
				<div class="complete-item-gif">
					<c:if test="${assessment.allowRightAnswersAfterQuestion && option.correct}">
						<i class="fa fa-check text-success"></i>
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && !option.correct}">
						<i class="fa fa-times text-danger"></i>
					</c:if>
				</div>
			</c:if>
				
			<div class="col" id="option-name-${option.uid}">
				<c:out value="${option.name}" escapeXml="false" />
			</div>
				
			<c:if test="${assessment.allowQuestionFeedback}">
				<div style="width:30%;">
					<c:if test="${option.answerInt > 0}">
						<c:out value="${option.feedback}" escapeXml="false" />
					</c:if>
				</div>
			</c:if>
				
			<div style="width:70px;">
				<c:if test="${not empty toolSessionID}">
					<select name="question${questionIndex}_${option.displayOrder}" class="mark-hedging-select form-select" data-question-index="${questionIndex}"
							aria-labelledby="instructions-${questionIndex} option-name-${option.uid}"
							disabled="disabled">
							
						<c:forEach var="i" begin="0" end="${question.maxMark}">
							<option
								<c:if test="${option.answerInt == i}">selected="selected"</c:if>						
							>${i}</option>
						</c:forEach>
							
					</select>
				</c:if>
			</div>
		</div>
	</c:forEach>
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

<c:if test="${not empty toolSessionID and assessment.allowRightAnswersAfterQuestion}">
	<div class="feedback">
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
</c:if>