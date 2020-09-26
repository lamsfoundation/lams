<%@ include file="/common/taglibs.jsp"%>

<div class="col-12 p-0">
	<lams:OutcomeAuthor5 toolContentId="${assessmentForm.assessment.contentId}" />
	
	<lams:Switch name="assessment.reflectOnActivity" id="reflectOnActivity"
		useSpringForm="true"
		labelKey="advanced.reflectOnActivity"
		tooltipKey="advanced.reflectOnActivity.tooltip"
		tooltipDescriptionKey="advanced.reflectOnActivity.tooltip.description" />

	<div class="form-group row">
		<div class="col-sm-12">
			<form:textarea path="assessment.reflectInstructions" rows="3" id="reflectInstructions" cssClass="form-control" />
		</div>
	</div>
</div>