<%@ include file="/common/taglibs.jsp"%>

<div class="grade-slider form-group">
	<div class="col-sm-1 greyed-out-label">
		<fmt:message key="label.authoring.basic.option.grade"/>
	</div>
    <div class="col-sm-5">
       	<div class="slider"></div>
       	<input type="hidden" name="optionMaxMark${status.index}" id="optionMaxMark${status.index}" value="${option.maxMark}">
    </div>
</div>
