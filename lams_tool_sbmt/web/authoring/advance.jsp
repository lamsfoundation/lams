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

<div class="checkbox">
	<label for="lockOnFinished">
		<html:checkbox property="lockOnFinished" styleId="lockOnFinished" />
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</div>

<div class="checkbox">
	<label for="limitUpload">
		<html:checkbox property="limitUpload" styleId="limitUpload" />
		<fmt:message key="label.limit.number.upload" />
	</label>
</div>

<html:select property="limitUploadNumber" styleId="limitUploadNumber" styleClass="roffset5 form-control">
	<html:option value="1">1</html:option>
	<html:option value="2">2</html:option>
	<html:option value="3">3</html:option>
	<html:option value="4">4</html:option>
	<html:option value="5">5</html:option>
</html:select>

<div class="checkbox">
	<label for="notifyLearnersOnMarkRelease">
		<html:checkbox property="notifyLearnersOnMarkRelease" styleId="notifyLearnersOnMarkRelease" />
		<fmt:message key="label.authoring.advanced.notify.mark.release" />
	</label>
</div>

<div class="checkbox">
	<label for="notifyTeachersOnFileSubmit">
		<html:checkbox property="notifyTeachersOnFileSubmit" styleId="notifyTeachersOnFileSubmit" />
		<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
	</label>
</div>

<div class="checkbox">
	<label for="notifyLearnersOnMarkRelease">
		<html:checkbox property="notifyLearnersOnMarkRelease" styleId="notifyLearnersOnMarkRelease" />
		<fmt:message key="label.authoring.advanced.notify.mark.release" />
	</label>
</div>

<div class="checkbox">
	<label for="reflectOnActivity">
		<html:checkbox property="reflectOnActivity" styleId="reflectOnActivity" />
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="reflectInstructions" styleId="reflectInstructions" styleClass="form-control" cols="50" rows="4" />
</div>