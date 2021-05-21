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
			<form:checkbox path="whiteboard.useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.resource.options">

	<div class="form-inline">
		<label for="relativeTimeLimit">
			<fmt:message key="label.time.limit" />&nbsp;
            <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="" data-original-title="<fmt:message key="label.time.limit.tooltip"/>"></i>
		</label>
        <form:input path="whiteboard.relativeTimeLimit" type="number" min="0" size="2" id="relativeTimeLimit" cssClass="form-control input-sm"/>        
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