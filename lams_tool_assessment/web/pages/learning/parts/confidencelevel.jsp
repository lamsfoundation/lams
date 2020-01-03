<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${assessment.confidenceLevelsType == 2}">
		<c:set var="confidenceLabels">["<fmt:message key="label.not.confident" />", "<fmt:message key="label.confident" />", "<fmt:message key="label.very.confident" />"]</c:set>
	</c:when>
	<c:when test="${assessment.confidenceLevelsType == 3}">
		<c:set var="confidenceLabels">["<fmt:message key="label.not.sure" />", "<fmt:message key="label.sure" />", "<fmt:message key="label.very.sure" />"]</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="confidenceLabels">["0", "50", "100%"]</c:set> 
	</c:otherwise>
</c:choose>

<div class="question-type">
	<fmt:message key="label.what.is.your.confidence.level" />
</div>

<div>
	<input name="confidenceLevel${status.index}" class="bootstrap-slider" type="text"
		data-slider-enabled="${hasEditRight}" data-slider-tooltip="hide"
		<c:if test="${question.confidenceLevel != -1}">data-slider-value="${question.confidenceLevel}"</c:if>
		data-slider-ticks-labels='${confidenceLabels}'
		<c:choose>
			<c:when test="${assessment.confidenceLevelsType == 2}">
				data-slider-ticks="[0, 5, 10]"  data-slider-step="5"
			</c:when>
			<c:when test="${assessment.confidenceLevelsType == 3}">
				data-slider-ticks="[0, 5, 10]" data-slider-step="5"
			</c:when>
			<c:otherwise>
				data-slider-ticks="[0, 5, 10]" 
			</c:otherwise>
		</c:choose>
		/>
</div>