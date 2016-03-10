<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.options}">
			<tr>
				<c:if test="${finishedLock}">
					<td class="complete-item-gif">
					
						<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.answerInt == option.uid)}">
							<i class="fa fa-check"></i>	
						</c:if>
						<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.answerInt != -1) && (option.answerInt != option.uid)}">
							<i class="fa fa-times"></i>	
						</c:if>
							
					</td>		
				</c:if>			
				<td>
					<c:out value="${option.question}" escapeXml="false" />
				</td>
				<td style="width: 100px;">
					<html:select property="question${status.index}_${option.sequenceId}" value="${option.answerInt}" disabled="${isEditingDisabled}">
						<html:option value="-1"><fmt:message key="label.learning.matching.pairs.choose" /></html:option>
						<c:forEach var="selectOption" items="${question.matchingPairOptions}">
							<html:option value="${selectOption.uid}">${selectOption.optionString}</html:option>
						</c:forEach>
					</html:select>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>		

<%@ include file="markandpenaltyarea.jsp"%>