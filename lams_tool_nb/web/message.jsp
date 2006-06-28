<%@ include file="/includes/taglibs.jsp"%>

<table cellpadding="0">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<tr>
				<td>
					<bean:write name="message" />
				</td>
			</tr>
		</html:messages>
	</logic:messagesPresent>

	<tr>
		<td align="right">
			<html:button property="closeWindow" onclick="window.close()" styleClass="button">
				<fmt:message key="button.next" />
			</html:button>
		</td>
	</tr>
</table>
