<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<div class="checkbox">
	<label for="lockOnFinished">
		<html:checkbox property="lockOnFinished" styleId="lockOnFinished" />
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>

<div class="checkbox">
	<label for="allowRichEditor">
		<html:checkbox property="allowRichEditor" styleId="allowRichEditor" />
		<fmt:message key="advanced.allowRichEditor" />
	</label>
</div>
