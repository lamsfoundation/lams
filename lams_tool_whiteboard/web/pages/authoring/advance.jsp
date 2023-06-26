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

		// time limit various options functionality
		$('input[name="timeLimit"]').change(function(){
			let timeLimitTypeNone = $('#timeLimitNone'),
					timeLimitTypeRelative = $('#timeLimitRelative'),
					timeLimitTypeRelativeValue = $('#timeLimitRelativeValue'),
					timeLimitTypeAbsolute = $('#timeLimitAbsolute'),
					timeLimitTypeAbsoluteValue = $('#timeLimitAbsoluteValue');

			if (timeLimitTypeNone.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', true).val(0);
				timeLimitTypeAbsoluteValue.prop('disabled', true).val(0);
				return;
			}
			if (timeLimitTypeRelative.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', false);
				timeLimitTypeAbsoluteValue.prop('disabled', true).val(0);
				return;
			}
			if (timeLimitTypeAbsolute.prop('checked')) {
				timeLimitTypeRelativeValue.prop('disabled', true).val(0);
				timeLimitTypeAbsoluteValue.prop('disabled', false);
				return;
			}
		}).first().change();

		$('[data-toggle="tooltip"]').bootstrapTooltip();
	});
</script>

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="whiteboard.useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.resource.options">

	<fmt:message key="label.authoring.advance.time.limit" />

	<div class="loffset20">
		<div class="radio form-inline">
			<label for="timeLimitNone">
				<input type="radio" name="timeLimit" value="none" id="timeLimitNone"
					${authoringForm.whiteboard.relativeTimeLimit eq 0 and authoringForm.whiteboard.absoluteTimeLimit eq 0 ? 'checked' : ''}
				/>
				<fmt:message key="label.authoring.advance.time.limit.none" />
			</label>
		</div>

		<div class="radio form-inline">
			<label for="timeLimitRelative">
				<input type="radio" id="timeLimitRelative" name="timeLimit" value="relative"
					${authoringForm.whiteboard.relativeTimeLimit > 0 ? 'checked' : ''} />
				<fmt:message key="label.authoring.advance.time.limit.relative" />&nbsp;
			</label>
			<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right"
			   title='<fmt:message key="label.authoring.advance.time.limit.relative.tooltip" />'></i>&nbsp;
			<form:input path="whiteboard.relativeTimeLimit" type="number" min="0" max="999" size="3"
						id="timeLimitRelativeValue" cssClass="form-control input-sm"/>

		</div>

		<div class="radio form-inline">
			<label for="timeLimitAbsolute">
				<input type="radio" id="timeLimitAbsolute" name="timeLimit" value="absolute"
					${authoringForm.whiteboard.absoluteTimeLimit > 0 ? 'checked' : ''} />
				<fmt:message key="label.authoring.advance.time.limit.absolute" />&nbsp;
			</label>
			<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right"
			   title='<fmt:message key="label.authoring.advance.time.limit.absolute.tooltip" />'></i>&nbsp;
			<form:input path="whiteboard.absoluteTimeLimit" type="number" min="0" max="999" size="3"
						id="timeLimitAbsoluteValue" cssClass="form-control input-sm"/>

		</div>
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
			<form:checkbox path="whiteboard.galleryWalkEnabled" id="gallery-walk-enabled" />
			<fmt:message key="label.authoring.advance.gallery.walk.enabled" />
		</label>
	</div>

	<div id="gallery-walk-options" class="loffset10">
		<div class="checkbox">
			<label for="gallery-walk-read-only">
				<form:checkbox path="whiteboard.galleryWalkReadOnly" id="gallery-walk-read-only" />
				<fmt:message key="label.authoring.advance.gallery.walk.read.only" />
			</label>
			<i class="fa fa-question-circle"
			   data-toggle="tooltip" title="<fmt:message key='label.authoring.advance.gallery.walk.read.only.tooltip' />"></i>
		</div>

		<div class="form-group">
			<fmt:message key='label.authoring.advance.gallery.walk.instructions' var="galleryWalkInstructions"/>
			<form:textarea path="whiteboard.galleryWalkInstructions" cssClass="form-control" rows="3"
						   placeholder="${galleryWalkInstructions}" />
		</div>

	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.whiteboard.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
			<form:checkbox path="whiteboard.lockWhenFinished" id="lockWhenFinished" />
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>

	<div class="checkbox">
		<label for="reflect-on">
			<form:checkbox path="whiteboard.reflectOnActivity" id="reflect-on"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>

	<div class="form-group">
		<form:textarea path="whiteboard.reflectInstructions" cssClass="form-control" id="reflect-instructions" rows="3" />
	</div>

</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	$('#reflect-instructions').keyup(function(){
		$('#reflect-on').prop('checked', !isEmpty($(this).val()));
	});
</script>