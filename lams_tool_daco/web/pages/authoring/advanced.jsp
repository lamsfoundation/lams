<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.authoring.advanced.record">
<div class="form-group form-inline">
<!-- Minimum number of records learner must provide -->
<label for="daco.minRecords"><fmt:message key="label.common.min" /></label> 
<form:select path="daco.minRecords" cssClass="roffset5 form-control">
	<form:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</form:option>
	<c:forEach begin="1" end="50" var="index">
		<form:option value="${index}">${index}</form:option>
	</c:forEach>
</form:select>
<label for="maxRecords" class="loffset10"> <fmt:message key="label.common.max" /> </label> 
<!-- Maximum number of records learner must provide -->
<form:select
	path="daco.maxRecords" cssClass="form-control">
	<form:option value="0">
		<fmt:message key="label.authoring.advanced.record.nolimit" />
	</form:option>
	<c:forEach begin="1" end="50" var="index">
		<form:option value="${index}">${index}</form:option>
	</c:forEach>
</form:select>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">
<div class="checkbox">
<label for="notifyTeachersOnLearnerEntry"><form:checkbox path="daco.notifyTeachersOnLearnerEntry" id="notifyTeachersOnLearnerEntry" />
<fmt:message key="label.authoring.advanced.notify.onlearnerentry" /></label>
</div>

<div class="checkbox">
<label for="notifyTeachersOnRecordSubmit"><form:checkbox path="daco.notifyTeachersOnRecordSumbit" id="notifyTeachersOnRecordSubmit"/> 
<fmt:message key="label.authoring.advanced.notify.onrecordsubmit" /> </label>
</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.daco.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
<label for="lockOnFinished"><form:checkbox path="daco.lockOnFinished" id="lockOnFinished" />
<fmt:message key="label.authoring.advanced.lock.on.finished" /></label>
</div>
</lams:SimplePanel>


