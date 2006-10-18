<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<p class="small-space-top">
	<html:checkbox property="lockOnFinished" styleClass="noBorder"
		styleId="lockOnFinished" disabled="true">
	</html:checkbox>

	<label for="lockOnFinished">
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</p>

<p>
	<html:checkbox property="reflectOnActivity" value="1"
		styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>

	<label for="reflectOnActivity">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>
<p>
	<html:textarea property="reflectInstructions" cols="30" rows="3" />
</p>

<h2>
	<fmt:message key="advanced.selectScribe" />
</h2>

<p>
	<html:radio property="autoSelectScribe" value="true" styleId="autoSelectScribe1">
	</html:radio>
	<label for="autoSelectScribe1">
		<fmt:message key="advanced.firstLearner" />
	</label>
</p>
<p>
	<html:radio property="autoSelectScribe" value="false" styleId="autoSelectScribe2">
	</html:radio>
	<label for="autoSelectScribe2">
		<fmt:message key="advanced.selectInMonitor" />
	</label>
</p>
