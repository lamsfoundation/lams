<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script type="text/javascript">
	function turnOnReflect(){
			document.getElementById("reflectOn").checked = !isEmpty(document.getElementById("reflectInstructions").value);
	}
</script>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.authoring.advanced.record">
<div class="form-group form-inline">
<!-- Minimum number of records learner must provide -->
<label for="daco.minRecords"><fmt:message key="label.common.min" /></label> 
<html:select property="daco.minRecords" styleClass="roffset5 form-control">
	<html:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</html:option>
	<c:forEach begin="1" end="50" var="index">
		<html:option value="${index}">${index}</html:option>
	</c:forEach>
</html:select>
<label for="maxRecords" class="loffset10"> <fmt:message key="label.common.max" /> </label> 
<!-- Maximum number of records learner must provide -->
<html:select
	property="daco.maxRecords" styleClass="form-control">
	<html:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</html:option>
	<c:forEach begin="1" end="50" var="index">
		<html:option value="${index}">${index}</html:option>
	</c:forEach>
</html:select>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
<div class="checkbox">
<label for="notifyTeachersOnLearnerEntry"><html:checkbox property="daco.notifyTeachersOnLearnerEntry" styleId="notifyTeachersOnLearnerEntry" />
<fmt:message key="label.authoring.advanced.notify.onlearnerentry" /></label>
</div>

<div class="checkbox">
<label for="notifyTeachersOnRecordSubmit"><html:checkbox property="daco.notifyTeachersOnRecordSumbit" styleId="notifyTeachersOnRecordSubmit"/> 
<fmt:message key="label.authoring.advanced.notify.onrecordsubmit" /> </label>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
<label for="lockOnFinished"><html:checkbox property="daco.lockOnFinished" styleId="lockOnFinished" />
<fmt:message key="label.authoring.advanced.lock.on.finished" /></label>
</div>

<div class="checkbox">
<label for="reflectOn"><html:checkbox property="daco.reflectOnActivity" styleId="reflectOn"/>
<fmt:message key="label.authoring.advanced.reflectOnActivity" /> </label>
<div class="form-group">
<html:textarea property="daco.reflectInstructions" styleId="reflectInstructions"  styleClass="form-control" cols="50" rows="3"
	onkeyup="javascript:turnOnReflect()"/>
</div>
</lams:SimplePanel>


