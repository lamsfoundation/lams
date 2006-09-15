<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td align="right">
				<html:checkbox property="lockOnFinished" styleClass="noBorder"
					styleId="lockOnFinished" disabled="true">
				</html:checkbox>
			</td>
			<td>
				<label for="lockOnFinished">
					<fmt:message key="advanced.lockOnFinished" />
				</label>
			</td>
		</tr>
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

		<tr>
			<td colspan="2">
				<div class="field-name" style="text-align: left;">
					<fmt:message key="advanced.selectScribe"/>
				</div>
			</td>
		</tr>
		<tr>
			<td align="right">
				<html:radio property="autoSelectScribe" value="true"></html:radio>
			</td>
			<td>
				<fmt:message key="advanced.firstLearner"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<html:radio property="autoSelectScribe" value="false"></html:radio>
			</td>
			<td>
				<fmt:message key="advanced.selectInMonitor"/>
			</td>
		</tr>
	</tbody>
</table>
