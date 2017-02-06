<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty questionForOrdering}">
	<c:set var="question" value="${questionForOrdering}" />
	<c:set var="assessment" value="${sessionMap.assessment}" />
	<c:set var="result" value="${sessionMap.assessmentResult}" />	
</c:if>

<div id="orderingArea${question.uid}">
	<div class="question-type">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
	
	<div class="table-responsive">
		<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.options}" varStatus="ordStatus">
			<tr>

				<c:if test="${finishedLock}">
					<td class="complete-item-gif">
						<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.sequenceId == ordStatus.index)}">
							<i class="fa fa-check"></i>	
						</c:if>
						<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.sequenceId != ordStatus.index)}">
							<i class="fa fa-times"></i>	
						</c:if>
							
					</td>		
				</c:if>													
			
				<td class="ordering-option">
					<input type="hidden" name="question${status.index}_${option.sequenceId}" value="${option.sequenceId}" />
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
								
				<c:if test="${(mode != 'teacher') || !hasEditRight}">
					<td class="arrows">
						<c:if test="${not ordStatus.first and !finishedLock}">
							<c:set var="up"><fmt:message key='label.authoring.basic.up'/></c:set>
							<lams:Arrow state="up" title="${up}" onclick="upOption(${question.uid},${ordStatus.index})"/>
						</c:if>
			
						<c:if test="${not ordStatus.last and !finishedLock}">
							<c:set var="down"><fmt:message key='label.authoring.basic.down'/></c:set>
							<lams:Arrow state="down" title="${down}" onclick="downOption(${question.uid},${ordStatus.index})"/>
						</c:if>
					</td>
				</c:if>			
			</tr>
		</c:forEach>
		</table>
	</div>
	
	<c:if test="${finishedLock && assessment.allowQuestionFeedback}">
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

	<%@ include file="markandpenaltyarea.jsp"%>
</div>
