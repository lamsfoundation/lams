<%@include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<p>
	<html:checkbox property="lockOnFinished" styleClass="noBorder">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</html:checkbox>
</p>

<p>
	<html:checkbox property="limitUpload" styleId="limitUpload"
		styleClass="noBorder" onclick="limitUploadChange()">
		<fmt:message key="label.limit.number.upload" />
	</html:checkbox>

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
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</html:checkbox>
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
