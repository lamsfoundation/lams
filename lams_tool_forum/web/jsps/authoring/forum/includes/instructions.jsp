
<table width="80%" cellspacing="8" align="CENTER">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="forum.label.onlineInstructions"/></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:textarea rows="10" cols="60" tabindex="3"  property="forum.onlineInstructions" /> <html:errors property="forum.onlineInstructions"/></td>
	</tr>
    <tr>
    		<td valign="MIDDLE"><b><bean:message key="forum.label.onlineFile"/></b></td>
    		<td valign="MIDDLE">&nbsp;&nbsp;<html:file property="onlineFile"/> <html:errors property="onlineFile" /> (<bean:message key="message.msg.maxFileSize"/>)</td>
            <%--<td> <html:submit onclick='<html:rewrite page="/authoring/forum/upload.do"/>'><bean:message key="button.upload"/></html:submit> </td> --%>
    </tr>

    <tr>
		<td valign="MIDDLE"><b><bean:message key="forum.label.offlineInstructions"/></b></td>
		<td valign="MIDDLE">&nbsp;&nbsp;<html:textarea rows="10" cols="60" tabindex="4"  property="forum.offlineInstructions" /> <html:errors property="forum.offlineInstructions"/></td>
	</tr>
    <tr>
    		<td valign="MIDDLE"><b><bean:message key="forum.label.offlineFile"/></b></td>
    		<td valign="MIDDLE">&nbsp;&nbsp;<html:file property="offlineFile"/> <html:errors property="offlineFile" /> (<bean:message key="message.msg.maxFileSize"/>)</td>
            <%--<td> <html:submit onclick='<html:rewrite page="/authoring/forum/upload.do"/>'><bean:message key="button.upload"/></html:submit> </td>--%>
    </tr>
   

    
	<td>&nbsp;&nbsp;<html:submit ><bean:message key="button.done"/></html:submit></td>
   </tr>
  </table>

