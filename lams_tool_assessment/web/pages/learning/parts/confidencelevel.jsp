<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.what.is.your.confidence.level" />
</div>

<div>
	<input name="confidenceLevel${status.index}" class="bootstrap-slider" type="text"
		data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
		data-slider-enabled="${hasEditRight}" data-slider-tooltip="hide"
		<c:if test="${question.confidenceLevel != -1}">data-slider-value="${question.confidenceLevel}"</c:if>
		/>
</div>