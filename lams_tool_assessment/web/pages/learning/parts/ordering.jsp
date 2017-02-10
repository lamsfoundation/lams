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
	
	<table class="question-table">
		<c:forEach var="option" items="${question.options}" varStatus="ordStatus">
			<tr>

				<c:if test="${finishedLock}">
					<td class="complete-item-gif">
						<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.sequenceId == ordStatus.index)}">
							<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">	
						</c:if>
						<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.sequenceId != ordStatus.index)}">
							<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">	
						</c:if>
							
					</td>		
				</c:if>													
			
				<td class="reg-padding">
					<input type="hidden" name="question${status.index}_${option.sequenceId}" value="${option.sequenceId}" />
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<c:if test="${(mode != 'teacher') || !hasEditRight}">
					<td style="width text-align: center; width: 20px;" class="reg-padding">
						<c:if test="${not ordStatus.first and !finishedLock}">
							<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>"
								onclick="upOption(${question.uid},${ordStatus.index})">
							<c:if test="${ordStatus.last}">
								<img src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.authoring.basic.down"/>">
							</c:if>
						</c:if>
			
						<c:if test="${not ordStatus.last and !finishedLock}">
							<c:if test="${ordStatus.first}">
								<img src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.authoring.basic.up"/>">
							</c:if>
			
							<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>"
								onclick="downOption(${question.uid},${ordStatus.index})">
						</c:if>
					</td>
				</c:if>			
			</tr>
		</c:forEach>
	</table>	
	
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
