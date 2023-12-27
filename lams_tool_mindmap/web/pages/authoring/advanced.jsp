<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
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

		$('#multiUserMode').change(function(){
			if ($(this).is(':checked')) {
				$('#gallery-walk-enabled, #gallery-walk-read-only').prop('disabled', false);
			} else {
				$('#gallery-walk-enabled, #gallery-walk-read-only').prop('checked', false).prop('disabled', true);
				$('#gallery-walk-options').slideUp();
			}
		}).change();
		
		$('[data-toggle="tooltip"]').bootstrapTooltip();
	});
</script>

<lams:SimplePanel titleKey="label.mindmap.options">

	<div class="checkbox">
		<label for="multiUserMode">
			<form:checkbox path="multiUserMode" value="1" id="multiUserMode"/>
			<fmt:message key="advanced.multiUserMode" />
		</label>
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
			<form:checkbox path="galleryWalkEnabled" id="gallery-walk-enabled" />
			<fmt:message key="label.authoring.advance.gallery.walk.enabled" />
		</label>
	</div>
	
	<div id="gallery-walk-options" class="loffset10">
		<div class="checkbox">
			<label for="gallery-walk-read-only">
				<form:checkbox path="galleryWalkReadOnly" id="gallery-walk-read-only" />
				<fmt:message key="label.authoring.advance.gallery.walk.read.only" />
			</label>
			<i class="fa fa-question-circle" 
			   data-toggle="tooltip" title="<fmt:message key='label.authoring.advance.gallery.walk.read.only.tooltip' />"></i>
		</div>
		
		<div class="form-group">
			<fmt:message key='label.authoring.advance.gallery.walk.instructions' var="galleryWalkInstructions"/>
			<form:textarea path="galleryWalkInstructions" cssClass="form-control" rows="3" 
						   placeholder="${galleryWalkInstructions}" />
		</div>
		
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lock-when-finished">
			<form:checkbox path="lockOnFinished" id="lock-when-finished"/>
			<fmt:message key="advanced.lockOnFinished" />
		</label>
	</div>
</lams:SimplePanel>