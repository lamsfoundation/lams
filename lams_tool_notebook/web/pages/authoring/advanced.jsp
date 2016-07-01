<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->


<lams:SimplePanel titleKey="label.editing.options">
<div class="checkbox">
	<label for="allowRichEditor">
		<html:checkbox property="allowRichEditor" styleId="allowRichEditor" value="1" />
		<fmt:message key="advanced.allowRichEditor" />
	</label>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lockOnFinished">
		<html:checkbox property="lockOnFinished" styleId="lockOnFinished" value="1" />
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>
</lams:SimplePanel>
