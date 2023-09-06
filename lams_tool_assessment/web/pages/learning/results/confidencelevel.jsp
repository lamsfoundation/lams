<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${assessment.confidenceLevelsType == 2}">
		<c:set var="sliderLabels"><fmt:message key="label.not.confident" />;<fmt:message key="label.confident" />;<fmt:message key="label.very.confident" /></c:set>
		<c:set var="sliderStep">5</c:set>
	</c:when>
	<c:when test="${assessment.confidenceLevelsType == 3}">
		<c:set var="sliderLabels"><fmt:message key="label.not.sure" />;<fmt:message key="label.sure" />;<fmt:message key="label.very.sure" /></c:set>
		<c:set var="sliderStep">5</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="sliderLabels">0%;50%;100%</c:set> 
		<c:set var="sliderStep">1</c:set>
	</c:otherwise>
</c:choose>
<c:set var="sliderLabelsArray" value="${fn:split(sliderLabels, ';')}" />

<div class="bootstrap-slider mt-2">
	<div class="card-subheader">
		<label for="confidence-level-${questionIndex}">
			<fmt:message key="label.confidence" />
		</label>
	</div>
	
	<div class="ms-2">
		<input type="range" id="confidence-level-${questionIndex}" 
			list="slider-step-list-${question.uid}" disabled
			min="0" max="10" step="${sliderStep}" 
			<c:if test="${question.confidenceLevel != -1}">value="${question.confidenceLevel}"</c:if>>
		<datalist id="slider-step-list-${question.uid}">
			<option value="0" label="${sliderLabelsArray[0]}"/>
			<option value="5" label="${sliderLabelsArray[1]}"/>
			<option value="10" label="${sliderLabelsArray[2]}"/>
		</datalist>
	</div>
</div>