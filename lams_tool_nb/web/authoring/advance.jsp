<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->


<p class="small-space-top">
	<html:checkbox property="reflectOnActivity" value="1"
		styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>
	<label for="reflectOnActivity">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>

</p>

<p>
	<html:textarea property="reflectInstructions" cols="30" rows="3" />
</p>

