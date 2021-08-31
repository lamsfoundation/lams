<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->


<lams:SimplePanel titleKey="label.editing.options">
<div class="checkbox">
	<label for="allowRichEditor">
		<form:checkbox path="allowRichEditor" id="allowRichEditor" value="1" />
		<fmt:message key="advanced.allowRichEditor" />
	</label>
</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lockOnFinished">
		<form:checkbox path="lockOnFinished" id="lockOnFinished" value="1" />
		<fmt:message key="advanced.lockOnFinished" />
	</label>
	<lams:Popover>
		<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
	</lams:Popover>
</div>
</lams:SimplePanel>
