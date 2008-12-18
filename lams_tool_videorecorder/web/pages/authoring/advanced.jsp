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
	<html:checkbox property="allowUseVoice" value="1"
		styleClass="noBorder" styleId="allowUseVoice"></html:checkbox>
	<label for="allowUseVoice">
		<fmt:message key="advanced.allowUseVoice" />
	</label>
</p>

<p>
	<html:checkbox property="allowUseCamera" value="1"
		styleClass="noBorder" styleId="allowUseCamera"></html:checkbox>
	<label for="allowUseCamera">
		<fmt:message key="advanced.allowUseCamera" />
	</label>
</p>

<p>
	<html:checkbox property="allowLearnerVideoVisibility" value="1"
		styleClass="noBorder" styleId="allowLearnerVideoVisibility"></html:checkbox>
	<label for="allowLearnerVideoVisibility">
		<fmt:message key="allowLearnerVideoVisibility" />
	</label>
</p>

<p>
	<html:checkbox property="allowLearnerVideoExport" value="1"
		styleClass="noBorder" styleId="allowLearnerVideoExport"></html:checkbox>
	<label for="allowLearnerVideoExport">
		<fmt:message key="allowLearnerVideoExport" />
	</label>
</p>
