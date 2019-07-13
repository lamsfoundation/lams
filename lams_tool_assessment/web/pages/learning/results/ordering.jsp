<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.ordering.sort.answers" />
</div>
	
<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}" varStatus="ordStatus">
			<tr>
				<td class="complete-item-gif">
					<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.displayOrder == ordStatus.index)}">
						<i class="fa fa-check text-success"></i>	
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.displayOrder != ordStatus.index)}">
						<i class="fa fa-times text-danger"></i>	
					</c:if>			
				</td>											
			
				<td class="ordering-option">
					<c:out value="${option.name}" escapeXml="false" />
				</td>		
			</tr>
		</c:forEach>
	</table>
</div>
	
<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when	test="${question.mark > 0}">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</c:when>
			<c:otherwise>
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</c:otherwise>
		</c:choose>		
	</div>
</c:if>		
