<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader">
		<fmt:message key="label.authoring.basic.type.matching.pairs" />
	</div>
</c:if>

<div class="fst-italic">
	<fmt:message key="label.learning.matching.pairs.possible" />
	<ul class="${empty toolSessionID ? 'ms-4' : ''}">
		<c:forEach var="possibleOption" items="${question.matchingPairOptions}">
			<li>${possibleOption.name}</li>
		</c:forEach>
	</ul>
</div>

<div class="table-responsive">
	<div class="table div-hover table-sm">
		<c:forEach var="option" items="${question.optionDtos}">
			<div class="row">
				<c:if test="${not empty toolSessionID && (assessment.allowRightAnswersAfterQuestion || assessment.allowWrongAnswersAfterQuestion)}">
					<div class="complete-item-gif">
						<c:if test="${assessment.allowRightAnswersAfterQuestion && (option.answerInt == option.uid)}">
							<i class="fa fa-check text-success"></i>	
						</c:if>
						<c:if test="${assessment.allowWrongAnswersAfterQuestion && (option.answerInt != -1) && (option.answerInt != option.uid)}">
							<i class="fa fa-times text-danger"></i>	
						</c:if>			
					</div>
				</c:if>

				<div class="col">
					<c:out value="${option.matchingPair}" escapeXml="false" />
				</div>
				
				<c:if test="${not empty toolSessionID}">
					<div style="width: 250px;">
						<fmt:message key="label.learning.matching.pairs.chosen" /><br>
						<c:forEach var="possibleOption" items="${question.matchingPairOptions}">
							<c:if test="${option.answerInt == possibleOption.uid}">
								${possibleOption.name}
							</c:if>
						</c:forEach>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
</div>
