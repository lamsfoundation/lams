<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader" id="instructions-${questionIndex}">
	<fmt:message key="label.learning.ordering.sort.answers" />
</div>

<div class="list-group ordering-question-type">
	<c:forEach var="option" items="${question.optionDtos}" varStatus="ordStatus">
		<div class="list-group-item ordering-option">
			<input type="hidden" name="question${questionIndex}_${option.uid}"
				value="${ordStatus.index}" 
				aria-labelledby="instructions-${questionIndex} option-name-${option.uid}" />
				
			<span id="option-name-${option.uid}">
				<c:out value="${option.name}" escapeXml="false" />
			</span>
		</div>
	</c:forEach>
</div>
