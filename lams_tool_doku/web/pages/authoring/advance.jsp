<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<script>
	// avoid name clash between bootstrap and jQuery UI
	$.fn.bootstrapTooltip = $.fn.tooltip.noConflict();

	$(document).ready(function() {
		$('#gallery-walk-enabled').change(function(){
			if ($(this).is(':checked')) {
				$('#gallery-walk-options').slideDown();
			} else {
				$('#gallery-walk-options').slideUp()
						.find('#gallery-walk-read-only').prop('checked', false);
			}
		}).change();

		$('[data-toggle="tooltip"]').bootstrapTooltip();
	});
</script>

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="dokumaran.useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>

	<div class="checkbox">
		<label for="allowMultipleLeaders">
			<form:checkbox path="dokumaran.allowMultipleLeaders" value="1" id="allowMultipleLeaders"
						   disabled="${!authoringForm.dokumaran.useSelectLeaderToolOuput}"/>
			<fmt:message key="label.allow.multiple.leaders" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.resource.options">
	<div class="form-inline">
		<label for="maxMark">
			<fmt:message key="label.authoring.advanced.maxMark" />
		</label>
		<form:input path="dokumaran.maxMark" type="number" min="1" max="100" size="2" id="maxMark" cssClass="form-control input-sm"/>
	</div>

	<div class="form-inline voffset10">
		<label for="relativeTimeLimit">
			<fmt:message key="label.time.limit" />&nbsp;
			<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="" data-original-title="<fmt:message key="label.time.limit.tooltip"/>"></i>
		</label>
		<form:input path="dokumaran.relativeTimeLimit" type="number" min="0" size="2" id="relativeTimeLimit" cssClass="form-control input-sm"/>
	</div>

	<div class="checkbox">
		<label for="showChat">
			<form:checkbox path="dokumaran.showChat" id="showChat"/>
			<fmt:message key="label.show.chat" />&nbsp;
			<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="" data-original-title="<fmt:message key="label.show.chat.tooltip  "/>"></i>
		</label>
	</div>

	<div class="checkbox">
		<label for="showLineNumbers">
			<form:checkbox path="dokumaran.showLineNumbers" id="showLineNumbers"/>
			<fmt:message key="label.show.line.numbers" />
		</label>
	</div>

	<div class="checkbox">
		<label for="shared-pad-id-on">
			<input type="checkbox" id="shared-pad-id-on"
				   <c:if test="${not empty authoringForm.dokumaran.sharedPadId}">checked="checked"</c:if>/>
			<fmt:message key="label.shared.pad.id" />
		</label>
	</div>

	<div class="form-group">
		<form:input path="dokumaran.sharedPadId" cssClass="form-control" id="shared-pad-id" />
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.gallery.walk">
	<p>
		<fmt:message key="label.authoring.advance.gallery.walk.info1" />
	</p>
	<p>
		<fmt:message key="label.authoring.advance.gallery.walk.info2" />
	</p>

	<div class="checkbox">
		<label for="gallery-walk-enabled">
			<form:checkbox path="dokumaran.galleryWalkEnabled" id="gallery-walk-enabled" />
			<fmt:message key="label.authoring.advance.gallery.walk.enabled" />
		</label>
	</div>

	<div id="gallery-walk-options" class="loffset10">
		<div class="checkbox">
			<label for="gallery-walk-read-only">
				<form:checkbox path="dokumaran.galleryWalkReadOnly" id="gallery-walk-read-only" />
				<fmt:message key="label.authoring.advance.gallery.walk.read.only" />
			</label>
			<i class="fa fa-question-circle"
			   data-toggle="tooltip" title="<fmt:message key='label.authoring.advance.gallery.walk.read.only.tooltip' />"></i>
		</div>

		<div class="form-inline form-group">
			<label for="galleryWalkClusterSize">
				<fmt:message key="label.authoring.advance.gallery.walk.cluster" />&nbsp;
				<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title=""
				   data-original-title="<fmt:message key="label.authoring.advance.gallery.walk.cluster.tooltip"/>"></i>
			</label>
			<form:input path="dokumaran.galleryWalkClusterSize" type="number" min="0" max="9" size="2" id="galleryWalkClusterSize"
						cssClass="form-control input-sm"/>
			<br><small class="text-muted"><fmt:message key="label.authoring.advance.gallery.walk.cluster.0" /></small>
		</div>

		<div class="form-group">
			<fmt:message key='label.authoring.advance.gallery.walk.instructions' var="galleryWalkInstructions"/>
			<lams:CKEditor id="dokumaran.galleryWalkInstructions" value="${authoringForm.dokumaran.galleryWalkInstructions}"
						   height="160" contentFolderID="${authoringForm.contentFolderID}"/>
		</div>

	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.dokumaran.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
			<form:checkbox path="dokumaran.lockWhenFinished" id="lockWhenFinished" />
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>

	<div class="checkbox">
		<label for="reflect-on">
			<form:checkbox path="dokumaran.reflectOnActivity" id="reflect-on"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>

	<div class="form-group">
		<form:textarea path="dokumaran.reflectInstructions" cssClass="form-control" id="reflect-instructions" rows="3" />
	</div>

</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	$('#reflect-instructions').keyup(function(){
		$('#reflect-on').prop('checked', !isEmpty($(this).val()));
	});

	//automatically turn on shared-pad-id-on option if there are text input in shared-pad-id area
	$('#shared-pad-id').keyup(function(){
		$('#shared-pad-id-on').prop('checked', !isEmpty($(this).val()));
	});

	//automatically turn on shared-pad-id-on option if there are text input in shared-pad-id area
	$('#useSelectLeaderToolOuput').change(function(){
		$('#allowMultipleLeaders').prop('disabled', !$('#allowMultipleLeaders').prop('disabled'));
	});
</script>