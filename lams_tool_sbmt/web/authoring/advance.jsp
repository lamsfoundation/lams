<%@include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="lockOnFinished" styleClass="noBorder" styleId="lockOnFinished">
	</html:checkbox>
	<label for="lockOnFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="limitUpload" styleId="limitUpload"
		styleClass="noBorder" onclick="limitUploadChange()">
	</html:checkbox>
	<label for="limitUpload">
		<fmt:message key="label.limit.number.upload" />
	</label>

	<html:select property="limitUploadNumber" styleId="limitUploadNumber">
		<html:option value="1">1</html:option>
		<html:option value="2">2</html:option>
		<html:option value="3">3</html:option>
		<html:option value="4">4</html:option>
		<html:option value="5">5</html:option>
	</html:select>
</p>

<p>
	<html:checkbox property="reflectOnActivity" styleClass="noBorder"
		styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="reflectInstructions"
		styleId="reflectInstructions" cols="30" rows="3" />

</p>
<script type="text/javascript">
	function limitUploadChange(){
		var lu = document.getElementById("limitUpload");
		var lun = document.getElementById("limitUploadNumber");
		if(lu.checked){
			lun.disabled = false;
		}else{
			lun.disabled = true;
		}
	}
	//initial set
	limitUploadChange();
</script>
