<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.grade}</fmt:param>
	</fmt:message>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td class="complete-item-gif">
					
					<c:if test="${assessment.allowRightAnswersAfterQuestion && option.correct}">
						<i class="fa fa-check"></i>
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && !option.correct}">
						<i class="fa fa-times"></i>
					</c:if>
							
				</td>
				
				<td>
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
				
					<select name="question${questionIndex}_${option.sequenceId}" class="mark-hedging-select" data-question-index="${questionIndex}"
							disabled="disabled">
						
						<c:forEach var="i" begin="0" end="${question.grade}">
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
		<lams:STRUTS-textarea property="question${questionIndex}" rows="4" cols="60" value="${question.answerString}" 
				disabled="true" styleClass="mark-hedging-select margin-top-minus-10"/>
	</c:if>
	
</div>

<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
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
	<div class="feedback">
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
