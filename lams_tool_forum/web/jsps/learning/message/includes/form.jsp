
<table width="80%" cellspacing="8" align="CENTER">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.subject"/></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:text property="message.subject" size="30" tabindex="1" readonly="true"/> <html:errors property="message.subject" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.body"/></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:textarea rows="10" cols="60" tabindex="2" property="message.body"  /> <html:errors property="message.body"/></td>
	</tr>

	<td>&nbsp;&nbsp;<html:submit ><bean:message key="button.submit"/></html:submit></td>
   </tr>
</table>