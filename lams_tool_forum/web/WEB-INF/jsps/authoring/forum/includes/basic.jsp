
<table width="80%" cellspacing="8" align="CENTER">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="forum.label.title"/></b></td>
		<td valign="MIDDLE"><b class="required">*</b> <html:text property="forum.title" size="30" tabindex="1" /> <html:errors property="forum.title" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="forum.label.instructions"/></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:textarea rows="10" cols="60" tabindex="2" property="forum.instructions"  /> <html:errors property="forum.instructions"/></td>
	</tr>
    <tr>
        <td valign="MIDDLE" colspan="2"> <b> <html:link page="/authoring/forum/newTopic.do" styleClass="nav"><b><bean:message key="forum.link.createTopic"/></b></html:link> <td>
	</tr>

    <tr>
    <td>&nbsp;&nbsp;<html:submit ><bean:message key="button.submit"/></html:submit></td>
   </tr>
</table>


