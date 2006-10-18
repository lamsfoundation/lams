<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<p class="small-space-top">
	<html:checkbox property="lockOnFinished" value="1"
		styleClass="noBorder" styleId="lockOnFinished">
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

<p>
	<html:checkbox property="filteringEnabled" value="1"
		styleClass="noBorder" styleId="filteringEnabled"></html:checkbox>

	<label for="filteringEnabled">
		<fmt:message key="advanced.filteringEnabled" />
	</label>
</p>
<p>
	<html:textarea property="filterKeywords" cols="30" rows="3" />
</p>

