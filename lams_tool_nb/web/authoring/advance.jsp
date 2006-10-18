<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tr>
		<td>
			<html:checkbox property="reflectOnActivity" value="1"
				styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>
			<label for="reflectOnActivity">
				<fmt:message key="advanced.reflectOnActivity" />
			</label>
		</td>
	</tr>
	<tr>
		<td>
			<html:textarea property="reflectInstructions" cols="30" rows="3" />
		</td>
	</tr>
</table>
