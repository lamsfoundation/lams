<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td>
				<html:checkbox property="lockOnFinished" value="1"
					styleClass="noBorder" styleId="lockOnFinished"></html:checkbox>
				<label for="lockOnFinished">
					<fmt:message key="advanced.lockOnFinished" />
				</label>
			</td>
		</tr>
		<tr>
			<td>
				<html:checkbox property="allowRichEditor" value="1"
					styleClass="noBorder" styleId="allowRichEditor"></html:checkbox>
				<label for="allowRichEditor">
					<fmt:message key="advanced.allowRichEditor" />
				</label>
			</td>
		</tr>
	</tbody>
</table>
