<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.grade}</fmt:param>
	</fmt:message>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				
				<td>
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
					<select name="question${questionIndex}_${option.sequenceId}" class="mark-hedging-select" data-question-index="${questionIndex}"
						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>				
					>
						
						<c:forEach var="i" begin="0" end="${question.grade}">
							<option
								<c:if test="${option.answerInt == i}">selected="selected"</c:if>						
							>${i}</option>
						</c:forEach>
						
					</select>
				</td>				
			</tr>
		</c:forEach>
		
		<c:if test="${question.hedgingJustificationEnabled}">
			<tr>
				<td>
					<c:if test="${hasEditRight}">
						<div>
							<fmt:message key="label.justify.hedging.marks" />
						</div>
					</c:if>
					
					<lams:STRUTS-textarea property="question${questionIndex}" rows="4" cols="60" value="${question.answerString}" 
						disabled="${!hasEditRight}" styleClass="mark-hedging-select"
					/>
				</td>
			</tr>
		</c:if>
	</table>
</div>

<c:if test="${isLeadershipEnabled && isUserLeader}">
	<div class="float-right">
		<html:button property="submit-hedging-question${questionIndex}" onclick="return submitSingleMarkHedgingQuestion(${question.uid}, ${questionIndex});" 
				styleClass="btn pull-right">
			<fmt:message key="label.learning.submit" />
		</html:button>
	</div>
</c:if>
