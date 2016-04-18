<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script type="text/javascript">
	function turnOnReflect(){
			document.getElementById("reflectOn").checked = !isEmpty(document.getElementById("reflectInstructions").value);
	}
</script>

<!-- Advance Tab Content -->
<div class="form-group  voffset10">
<label><fmt:message key="label.authoring.advanced.record" /></label><BR/>
<!-- Minimum number of records learner must provide -->
<label for="minRecords"><fmt:message key="label.common.min" /></label> 
<html:select property="daco.minRecords" styleClass="roffset5">
	<html:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</html:option>
	<c:forEach begin="1" end="50" var="index">
		<html:option value="${index}">${index}</html:option>
	</c:forEach>
</html:select>
<label for="maxRecords" style="space-left"> <fmt:message key="label.common.max" /> </label> 
<!-- Maximum number of records learner must provide -->
<html:select
	property="daco.maxRecords" styleClass="noBorder">
	<html:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</html:option>
	<c:forEach begin="1" end="50" var="index">
		<html:option value="${index}">${index}</html:option>
	</c:forEach>
</html:select>
</div>

<div class="form-group">
<html:checkbox property="daco.notifyTeachersOnLearnerEntry"
	styleId="notifyTeachersOnLearnerEntry">
</html:checkbox> <label for="notifyTeachersOnLearnerEntry">&nbsp;<fmt:message key="label.authoring.advanced.notify.onlearnerentry" /> </label></p>
</div>

<div class="form-group">
<html:checkbox property="daco.notifyTeachersOnRecordSumbit"
	styleId="notifyTeachersOnRecordSubmit">
</html:checkbox> <label for="notifyTeachersOnRecordSubmit">&nbsp;<fmt:message key="label.authoring.advanced.notify.onrecordsubmit" /> </label></p>
</div>

<div class="form-group">
<html:checkbox property="daco.lockOnFinished" 
	styleId="lockOnFinished">
</html:checkbox> <label for="lockOnFinished">&nbsp;<fmt:message key="label.authoring.advanced.lock.on.finished" /> </label>
</div>

<div class="form-group">
<html:checkbox property="daco.reflectOnActivity" styleId="reflectOn">
</html:checkbox> <label for="reflectOn">&nbsp;<fmt:message key="label.authoring.advanced.reflectOnActivity" /> </label>
<BR/>
<div class="form-group">
<html:textarea property="daco.reflectInstructions" styleId="reflectInstructions" cols="50" rows="3"
	onkeyup="javascript:turnOnReflect()" />
</div>