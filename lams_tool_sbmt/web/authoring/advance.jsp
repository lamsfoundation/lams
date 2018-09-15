<%@include file="/common/taglibs.jsp"%>


<!-- ========== Advanced Tab ========== -->

<script type="text/javascript">
	$(document).ready(function(){
		$('#limitUpload').change(function(){
			var enabled = $(this).is(':checked');
			$('#limitUploadNumber').prop('disabled', !enabled);
		}).change();
		
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
	<label for="limitUpload">
		<form:checkbox path="limitUpload" id="limitUpload" />
		<fmt:message key="label.limit.number.upload" />
	</label>

	<form:select path="limitUploadNumber" id="limitUploadNumber" cssClass="loffset5 form-control form-control-inline">
		<form:option value="1">1</form:option>
		<form:option value="2">2</form:option>
		<form:option value="3">3</form:option>
		<form:option value="4">4</form:option>
		<form:option value="5">5</form:option>
	</form:select>
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

