<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<c:forEach var="option" items="${question.questionOptions}">
		<tr>
			<c:if test="${finishedLock}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				
					<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.answerInt == option.uid)}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
					</c:if>
					<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.answerInt != -1) && (option.answerInt != option.uid)}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">	
					</c:if>
						
				</td>		
			</c:if>			
			<td style="padding:5px 15px 2px; vertical-align:middle; background:none; border-bottom:0px; width: 40%;">
				<c:out value="${option.question}" escapeXml="false" />
			</td>
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px; ">
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