<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
</c:if>

<div class="div-hover table-sm">
	<c:forEach var="option" items="${question.optionDtos}" varStatus="ordStatus">
		<div class="row">			
			<c:if test="${assessment.allowRightAnswersAfterQuestion || assessment.allowWrongAnswersAfterQuestion}">
				<div class="complete-item-gif">
					<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.displayOrder == ordStatus.index)}">
						<i class="fa fa-check text-success"></i>	
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.displayOrder != ordStatus.index)}">
						<i class="fa fa-times text-danger"></i>	
					</c:if>			
				</div>
			</c:if>										
			
			<div class="col">
				<c:out value="${option.name}" escapeXml="false" />
			</div>		
		</div>
	</c:forEach>
</div>
	
<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when	test="${question.mark >= question.maxMark}">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</c:when>
			<c:otherwise>
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</c:otherwise>
		</c:choose>		
	</div>
</c:if>		
