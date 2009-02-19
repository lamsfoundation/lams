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
		<fmt:message key="advanced.allowLearnerVideoVisibility" />
	</label>
</p>

<p>
	<html:checkbox property="allowComments" value="1"
		styleClass="noBorder" styleId="allowComments"></html:checkbox>
	<label for="allowComments">
		<fmt:message key="advanced.allowComments" />
	</label>
</p>

<p>
	<html:checkbox property="allowRatings" value="1"
		styleClass="noBorder" styleId="allowRatings"></html:checkbox>
	<label for="allowRatings">
		<fmt:message key="advanced.allowRatings" />
	</label>
</p>

<p>
	<html:checkbox property="exportOffline" value="1"
		styleClass="noBorder" styleId="exportOffline"></html:checkbox>
	<label for="exportOffline">
		<fmt:message key="advanced.exportOffline" />
	</label>
</p>

<p>
	<html:checkbox property="exportAll" value="1"
		styleClass="noBorder" styleId="exportAll"></html:checkbox>
	<label for="exportAll">
		<fmt:message key="advanced.exportAll" />
	</label>
</p>