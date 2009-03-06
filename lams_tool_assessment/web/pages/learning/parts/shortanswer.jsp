<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<tr>
		<c:if test="${finishedLock && assessment.allowRightWrongAnswersAfterQuestion}">
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				<c:choose>
					<c:when	test="${question.mark > 0}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">
					</c:when>
					<c:otherwise>
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">
					</c:otherwise>
				</c:choose>				
			</td>		
		</c:if>	
		<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; ">
			<input type="text" name="question${status.index}" value="${question.answerString}" styleClass="noBorder" size="70"
				<c:if test="${finishedLock}">disabled="disabled"</c:if>					 
			/>	
		</td>
	</tr>
</table>	

<c:if test="${finishedLock && assessment.allowQuestionFeedback && (question.questionFeedback != null)}">
	<div style="padding: 10px 15px 0px; font-style: italic; color:#47bc23;">
		<c:out value="${question.questionFeedback}" escapeXml="false" />
	</div>
</c:if>	

<%@ include file="markandpenaltyarea.jsp"%>