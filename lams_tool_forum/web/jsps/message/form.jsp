
<table width="80%" cellspacing="8" align="CENTER" class="forms">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.subject" /></b></td>
		<td valign="MIDDLE"><b class="required">*</b><html:text size="30"
			 tabindex="1" property="message.subject" /> <html:errors
			property="message.subject" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.body" /></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:textarea rows="10" cols="60"
			tabindex="2" property="message.body" /> <html:errors
			property="message.body" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.attachment" /></b></td>
		<td valign="MIDDLE">
			&nbsp;&nbsp;<html:file tabindex="3"
				property="attachmentName" /> <html:errors
				property="message.attachment" />
		</td>
	</tr>
		<td>&nbsp;&nbsp;
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</td>
		<td>
			<html:button property="done" onclick="closeWin()">
				<bean:message key="button.done" />
			</html:button>
		</td>
	</tr>
</table>
