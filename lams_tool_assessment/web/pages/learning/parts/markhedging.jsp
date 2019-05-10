<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.maxMark}</fmt:param>
	</fmt:message>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td>
					<c:out value="${option.name}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
					<select name="question${questionIndex}_${option.displayOrder}" class="mark-hedging-select" data-question-index="${questionIndex}"
						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>				
					>
						
						<c:forEach var="i" begin="0" end="${question.maxMark}">
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
				<td colspan="2">
					<c:if test="${hasEditRight}">
						<div>
							<fmt:message key="label.justify.hedging.marks" />
						</div>
					</c:if>
					<lams:textarea id="justification-question${questionIndex}" name="question${questionIndex}" class="mark-hedging-select" disabled="${!hasEditRight}" 
								    data-question-index="${questionIndex}" rows="4" cols="60">
						<c:out value="${question.answerString}" />
					</lams:textarea>
				</td>
			</tr>
		</c:if>
	</table>
</div>

<c:if test="${isLeadershipEnabled && isUserLeader}">
	<div class="float-right">
		<button type="button" name="submit-hedging-question${questionIndex}" onclick="return submitSingleMarkHedgingQuestion(${question.uid}, ${questionIndex});" 
				class="btn pull-right">
			<fmt:message key="label.learning.submit" />
		</button>
	</div>
</c:if>
