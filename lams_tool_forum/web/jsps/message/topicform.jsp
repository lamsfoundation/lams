<table width="100%" cellspacing="8" border="0" align="left">
	<tr>
		<td>
			<b><bean:message key="message.label.subject" /></b>
		</td>
		<td align="left">
			<b class="required">*</b>
			<html:text size="30" tabindex="1" property="message.subject" />
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="message.label.body" /></b>
		</td>
		<td>
			<b class="required">*</b>
			<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body" />
			<BR>
			<html:errors property="message.body" />
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="message.label.attachment" /></b>
		</td>
		<td>
			&nbsp;&nbsp;
			<html:file tabindex="3" property="attachmentFile" />
			<html:errors property="message.attachment" />
		</td>
	</tr>
	<td colspan="2" align="right" valign="bottom">
		<html:submit style="width:120px" styleClass="buttonStyle">
			<bean:message key="button.add" />
		</html:submit>
		<html:button property="cancel" onclick="javascript:window.parent.hideMessage()"  styleClass="buttonStyle" style="width:120px" >
			<bean:message key="button.cancel" />
		</html:button>
	</td>
	</tr>
</table>
