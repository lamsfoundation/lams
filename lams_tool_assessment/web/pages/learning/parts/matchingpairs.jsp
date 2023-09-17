<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader" id="instructions-${questionIndex}">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<div class="fst-italic">
	<fmt:message key="label.learning.matching.pairs.possible" />
	<ul>
		<c:forEach var="possibleOption" items="${question.matchingPairOptions}">
			<li>${possibleOption.name}</li>
		</c:forEach>
	</ul>
</div>

<div class="table table-sm div-hover px-3">
	<c:forEach var="option" items="${question.optionDtos}">
		<div class="row">
			<div class="col" id="option-name-${option.uid}">
				<c:out value="${option.matchingPair}" escapeXml="false" />
			</div>
				
			<div style="width: 250px;">
				<select name="question${questionIndex}_${option.uid}" class="form-select"
					<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
					${question.answerRequired? 'aria-required="true" required="true"' : ''}
					aria-labelledby="option-name-${option.uid} instructions-${questionIndex}"
				>						
					<option value="-1">
						<fmt:message key="label.learning.matching.pairs.choose" />
					</option>
						
					<c:forEach var="selectOption" items="${question.matchingPairOptions}">
						<option value="${selectOption.uid}"
							<c:if test="${option.answerInt == selectOption.uid}">selected="selected"</c:if>
						>
							${selectOption.name}
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</c:forEach>
</div>
