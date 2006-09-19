<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td align="right">
				<html:checkbox property="reflectOnActivity" value="1"
					styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>
			</td>
			<td>
				<label for="reflectOnActivity">
					<fmt:message key="advanced.reflectOnActivity" />
				</label>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:textarea property="reflectInstructions" cols="30" rows="3" />
			</td>
		</tr>
	</tbody>
</table>