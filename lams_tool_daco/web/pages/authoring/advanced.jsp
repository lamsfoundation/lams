<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script type="text/javascript">
	function turnOnReflect(){
			document.getElementById("reflectOn").checked = !isEmpty(document.getElementById("reflectInstructions").value);
	}
</script>

<!-- Advance Tab Content -->

<p class="small-space-top"><fmt:message key="label.authoring.advanced.record" /></p>
<p>
<!-- Minimum number of records learner must provide -->
<label for="minRecords">
<fmt:message key="label.common.min" /></label> <html:select property="daco.minRecords"
	styleClass="noBorder">
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

</p>

<p class="small-space-top"><html:checkbox property="daco.notifyTeachersOnLearnerEntry" styleClass="noBorder"
	styleId="notifyTeachersOnLearnerEntry">
</html:checkbox> <label for="notifyTeachersOnLearnerEntry"> <fmt:message key="label.authoring.advanced.notify.onlearnerentry" /> </label></p>

<p><html:checkbox property="daco.notifyTeachersOnRecordSumbit" styleClass="noBorder"
	styleId="notifyTeachersOnRecordSubmit">
</html:checkbox> <label for="notifyTeachersOnRecordSubmit"> <fmt:message key="label.authoring.advanced.notify.onrecordsubmit" /> </label></p>

<p><html:checkbox property="daco.lockOnFinished" styleClass="noBorder"
	styleId="lockOnFinished">
</html:checkbox> <label for="lockOnFinished"> <fmt:message key="label.authoring.advanced.lock.on.finished" /> </label></p>

<p><html:checkbox property="daco.reflectOnActivity" styleClass="noBorder" styleId="reflectOn">
</html:checkbox> <label for="reflectOn"> <fmt:message key="label.authoring.advanced.reflectOnActivity" /> </label></p>

<p><html:textarea property="daco.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3"
	onkeyup="javascript:turnOnReflect()" /></p>