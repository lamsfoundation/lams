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
					<select name="question${questionIndex}_${option.uid}" class="mark-hedging-select" data-question-index="${questionIndex}"
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
