<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td >
				<html:checkbox property="lockOnFinished" value="1" styleClass="noBorder"><fmt:message key="advanced.lockOnFinished" /></html:checkbox>
			</td>
		</tr>
		<tr>
			<td >
				<html:checkbox property="allowRichEditor" value="1" styleClass="noBorder"><fmt:message key="advanced.allowRichEditor" /></html:checkbox>
			</td>
		</tr>
	</tbody>
</table>
