<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<table class="question-table">
	<c:forEach var="option" items="${question.options}">
		<tr>
			<c:if test="${finishedLock}">
				<td class="complete-item-gif">
				
					<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.answerInt == option.uid)}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>">	
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.answerInt != -1) && (option.answerInt != option.uid)}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>">	
					</c:if>
						
				</td>		
			</c:if>			
			<td style="padding:5px 15px 2px; width: 40%;">
				<c:out value="${option.question}" escapeXml="false" />
			</td>
			<td class="question-option">
				<html:select property="question${status.index}_${option.sequenceId}" value="${option.answerInt}" disabled="${finishedLock || !hasEditRight}">
					<html:option value="-1"><fmt:message key="label.learning.matching.pairs.choose" /></html:option>
					<c:forEach var="selectOption" items="${question.matchingPairOptions}">
						<html:option value="${selectOption.uid}">${selectOption.optionString}</html:option>
					</c:forEach>
				</html:select>
			</td>
		</tr>
	</c:forEach>
</table>		

<%@ include file="markandpenaltyarea.jsp"%>