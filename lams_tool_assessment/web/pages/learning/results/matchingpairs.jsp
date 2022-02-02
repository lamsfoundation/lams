<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="question-type">
		<fmt:message key="label.authoring.basic.type.matching.pairs" />
	</div>
</c:if>

<div class="question-type">
	<fmt:message key="label.learning.matching.pairs.possible" />
	<ul class="${empty toolSessionID ? 'loffset20' : ''}">
		<c:forEach var="possibleOption" items="${question.matchingPairOptions}">
			<li>${possibleOption.name}</li>
		</c:forEach>
	</ul>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<c:if test="${not empty toolSessionID}">
					<td class="complete-item-gif">
						<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.answerInt == option.uid)}">
							<i class="fa fa-check text-success"></i>	
						</c:if>
						<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.answerInt != -1) && (option.answerInt != option.uid)}">
							<i class="fa fa-times text-danger"></i>	
						</c:if>			
					</td>
				</c:if>
						
				<td>
					<c:out value="${option.matchingPair}" escapeXml="false" />
				</td>
				
				<c:if test="${not empty toolSessionID}">
					<td style="width: 100px;">
						<fmt:message key="label.learning.matching.pairs.chosen" /><br>
						<c:forEach var="possibleOption" items="${question.matchingPairOptions}">
							<c:if test="${option.answerInt == possibleOption.uid}">
								${possibleOption.name}
							</c:if>
						</c:forEach>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>
