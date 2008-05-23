<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<p class="small-space-top">
	<html:checkbox property="lockOnFinished" value="1"
		styleClass="noBorder" styleId="lockOnFinished"></html:checkbox>
	<label for="lockOnFinished">
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</p>

<p>
	<html:checkbox property="allowRichEditor" value="1"
		styleClass="noBorder" styleId="allowRichEditor"></html:checkbox>
	<label for="allowRichEditor">
		<fmt:message key="advanced.allowRichEditor" />
	</label>
</p>

