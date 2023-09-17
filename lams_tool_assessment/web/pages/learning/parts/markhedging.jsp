<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader" id="instructions-${questionIndex}">
	<fmt:message key="label.assign.hedging.mark">
		<fmt:param>${question.maxMark}</fmt:param>
	</fmt:message>
</div>

<div class="table table-sm div-hover px-3">
	<c:forEach var="option" items="${question.optionDtos}">
		<div class="row">
			<div class="col" id="option-name-${option.uid}">
				<c:out value="${option.name}" escapeXml="false" />
			</div>
				
			<div style="width: 70px;">
				<select name="question${questionIndex}_${option.uid}" class="mark-hedging-select form-select" data-question-index="${questionIndex}"
					<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
					aria-labelledby="instructions-${questionIndex} option-name-${option.uid}"
					${question.answerRequired? 'aria-required="true" required="true"' : ''}			
				>
					<c:forEach var="i" begin="0" end="${question.maxMark}">
						<option
							<c:if test="${option.answerInt == i}">selected="selected"</c:if>						
						>${i}</option>
					</c:forEach>
				</select>
			</div>				
		</div>
	</c:forEach>
</div>

<c:if test="${isLeadershipEnabled && isUserLeader}">
	<div>
		<button type="button" name="submit-hedging-question${questionIndex}" onclick="submitSingleMarkHedgingQuestion(${question.uid}, ${questionIndex});" 
				class="btn btn-sm btn-secondary float-end me-2">
			<fmt:message key="label.learning.submit" />
		</button>
	</div>
</c:if>
