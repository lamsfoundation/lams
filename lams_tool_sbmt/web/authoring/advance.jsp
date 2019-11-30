<%@include file="/common/taglibs.jsp"%>
<style>
	.slider-div {
		padding: 15px 20px 0;
	}

</style>

<script type="text/javascript">
	$(document).ready(function(){
		//initialize bootstrap-sliders
		$('.bootstrap-slider').bootstrapSlider();
		//force slider to show tick labels
		setTimeout(
			function(){ 
				//$(".bootstrap-slider").each(function() {
					//$(this).bootstrapSlider('relayout');
					//$(".bootstrap-slider").bootstrapSlider("relayout");
				//});

				$(".bootstrap-slider").bootstrapSlider("relayout");
			}, 
			1200
		);
		
		$('#enable-min-limit').change(function(){
			//toggle visibility of the slider
			$('#minLimitUploadNumber').parent().toggleClass('hidden');

			//force slider to show tick labels
			$('#minLimitUploadNumber').bootstrapSlider('relayout');

			//update slider's value on pressing checkbox
			if ($(this).is(':checked')) {
				$('#minLimitUploadNumber').bootstrapSlider("setValue", 1);
			} else {
				$('#minLimitUploadNumber').bootstrapSlider("setValue", '');
				$('#minLimitUploadNumber').val('');
			}
		});

		$('#limitUpload').change(function(){
			//toggle visibility of the slider
			$('#limitUploadNumber').parent().toggleClass('hidden');

			//force slider to show tick labels
			$('#limitUploadNumber').bootstrapSlider('relayout');

			//update slider's value on pressing checkbox
			if ($(this).is(':checked')) {
				$('#limitUploadNumber').bootstrapSlider("setValue", 1);
			} else {
				$('#limitUploadNumber').bootstrapSlider("setValue", 0);
				$('#limitUploadNumber').val(0);
			}
		});
		
		$('#reflectInstructions').keyup(function(){
			var checked = $(this).val() != null && $(this).val().trim() != '';
			$('#reflectOnActivity').prop('checked', checked)
		});
	});
</script>

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="useSelectLeaderToolOuput" value="1" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.file.options">
	<div class="checkbox">
		<label for="enable-min-limit">
			<input type="checkbox" id="enable-min-limit" <c:if test="${authoringForm.minLimitUploadNumber != null}">checked="checked"</c:if>/>
			<fmt:message key="label.enable.min.limit" />
		</label>

		<div class="slider-div <c:if test="${authoringForm.minLimitUploadNumber == null}">hidden</c:if>">
			<input name="minLimitUploadNumber" id="minLimitUploadNumber" type="text"
				class="bootstrap-slider" 
				data-slider-tooltip="hide"
				data-slider-ticks="[1, 2, 3, 4, 5]"
				data-slider-ticks-labels='["1", "2", "3", "4", "5"]'
				data-slider-lock-to-ticks="true"
				data-slider-value="${authoringForm.minLimitUploadNumber}"
			/>
		</div>
	</div>
	
	<div class="checkbox">
		<label for="limitUpload">
			<form:checkbox path="limitUpload" id="limitUpload" />
			<fmt:message key="label.limit.number.upload" />
		</label>

		<div class="slider-div <c:if test="${!authoringForm.limitUpload}">hidden</c:if>">
			<input name="limitUploadNumber" id="limitUploadNumber" class="bootstrap-slider" type="text"
				data-slider-tooltip="hide"
				data-slider-ticks="[1, 2, 3, 4, 5, 100]"
				data-slider-ticks-positions="[0, 20, 40, 60, 80, 100]"
				data-slider-ticks-labels='["1", "2", "3", "4", "5", "100"]'
				data-slider-lock-to-ticks="true"
				<c:if test="${authoringForm.limitUpload}">data-slider-value="${authoringForm.limitUploadNumber}"</c:if>
			/>
		</div>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
	<div class="checkbox">
		<label for="notifyLearnersOnMarkRelease">
			<form:checkbox path="notifyLearnersOnMarkRelease" id="notifyLearnersOnMarkRelease" />
			<fmt:message key="label.authoring.advanced.notify.mark.release" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="notifyTeachersOnFileSubmit">
			<form:checkbox path="notifyTeachersOnFileSubmit" id="notifyTeachersOnFileSubmit" />
			<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
		</label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockOnFinished">
			<form:checkbox path="lockOnFinished" id="lockOnFinished" />
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="reflectOnActivity">
			<form:checkbox path="reflectOnActivity" id="reflectOnActivity" />
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<form:textarea path="reflectInstructions" id="reflectInstructions" cssClass="form-control" cols="50" rows="4"></form:textarea>
	</div>
</lams:SimplePanel>
