<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty questionForOrdering}">
	<c:set var="question" value="${questionForOrdering}" />
	<c:set var="assessment" value="${sessionMap.assessment}" />
	<c:set var="result" value="${sessionMap.assessmentResult}" />	
</c:if>

<div id="orderingArea${question.uid}">
	<div style="padding: 10px 15px 7px; font-style: italic">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
	
	<table cellspacing="0" style="padding-bottom: 10px;">
		<c:forEach var="option" items="${question.questionOptions}" varStatus="ordStatus">
			<tr>
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; ">
					<input type="hidden" name="question${status.index}_${option.sequenceId}" value="${option.sequenceId}" />
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<c:if test="${(mode != 'teacher') || !hasEditRight}">
					<td width="20px" style="padding:5px 0px 2px 15px; vertical-align:middle; text-align: center; background:none; border-bottom:0px;">
						<c:if test="${not ordStatus.first and !finishedLock}">
							<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>"
								onclick="upOption(${question.uid},${ordStatus.index})">
							<c:if test="${ordStatus.last}">
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.authoring.basic.down"/>">
							</c:if>
						</c:if>
			
						<c:if test="${not ordStatus.last and !finishedLock}">
							<c:if test="${ordStatus.first}">
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
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
		<div style="padding: 10px 15px 0px; font-style: italic; color:#47bc23;">
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