<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<lams:SimplePanel titleKey="label.chat.options">
<div class="checkbox">
	<label for="filteringEnabled">
	<form:checkbox path="filteringEnabled" value="1" id="filteringEnabled"/>
	<fmt:message key="advanced.filteringEnabled" />
	</label>
</div>
<div class="form-group">
	<textarea name="filterKeywords" rows="3" class="form-control">${authoringForm.filterKeywords}</textarea>
</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lockOnFinished">
	<form:checkbox path="lockOnFinished" value="1" id="lockOnFinished"/>
	<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>
</lams:SimplePanel>

