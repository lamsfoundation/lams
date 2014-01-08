<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<c:forEach var="option" items="${question.questionOptions}">
		<tr>
			<c:if test="${finishedLock}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				
					<c:if test="${assessment.allowRightAnswersAfterQuestion && option.answerBoolean && (option.grade > 0)}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && option.answerBoolean && (option.grade <= 0)}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">	
					</c:if>
						
				</td>		
			</c:if>
			
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; width: 5px; border-bottom:0px; ">
				<c:choose>
					<c:when test="${question.multipleAnswersAllowed}">
						<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}" styleClass="noBorder"
	 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
							<c:if test="${finishedLock || !hasEditRight}">disabled="disabled"</c:if>
						/>			
					</c:when>
					<c:otherwise>
						<input type="radio" name="question${status.index}" value="${option.sequenceId}" styleClass="noBorder"
	 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
	 						<c:if test="${finishedLock || !hasEditRight}">disabled="disabled"</c:if>
						/>
					</c:otherwise>
				</c:choose>
			</td>
			
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;">
				<c:out value="${option.optionString}" escapeXml="false" />
			</td>
			
			<c:if test="${finishedLock && option.answerBoolean && assessment.allowQuestionFeedback}">

				<c:choose>
                	<c:when test="${option.grade <= 0}">
                    	<c:set var="color" scope="page" value="red" />
        			</c:when>
					<c:otherwise>
                    	<c:set var="color" scope="page" value="blue" />
        			</c:otherwise>
        		</c:choose>

				<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px; font-style: italic; color:${color};" width="30%">
					<c:out value="${option.feedback}" escapeXml="false" />
				</td>		
			</c:if>
			
		</tr>
	</c:forEach>
</table>	

<c:if test="${finishedLock && assessment.allowQuestionFeedback}">
	<c:choose>
		<c:when test="${question.mark == question.defaultGrade}">
			<div style="padding: 15px 15px 0px; font-style: italic; color:#47bc23;">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${question.mark > 0}">
			<div style="padding: 15px 15px 0px; font-style: italic; color:#47bc23;">
				<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${question.mark <= 0}">
			<div style="padding: 15px 15px 0px; font-style: italic; color:#47bc23;">
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</div>
		</c:when>		
	</c:choose>
</c:if>

<%@ include file="markandpenaltyarea.jsp"%>
