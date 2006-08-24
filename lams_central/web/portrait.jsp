<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<html:form action="/saveportrait.do" method="post" enctype="multipart/form-data">
<html:hidden name="PortraitActionForm" property="portraitUuid" />
<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
	<tr> 
		<td valign="top">
			<H2><fmt:message key="title.portrait.change.screen"/></H2>
			<logic:messagesPresent> 
				<p class="warning"><html:errors /></p>
			</logic:messagesPresent>
			<table>
				<tr>
					<td><fmt:message key="label.portrait.current" />:</td>
					<td>
						<logic:notEqual name="PortraitActionForm" property="portraitUuid" value="0">
							<img src="/lams/download/?uuid=<bean:write name="PortraitActionForm" property="portraitUuid" />&preferDownload=false" />
						</logic:notEqual>
						<logic:equal name="PortraitActionForm" property="portraitUuid" value="0">
							<i><fmt:message key="msg.portrait.none" /></i>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.portrait.upload" />:</td>
					<td><html:file property="file" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<html:submit><fmt:message key="button.save"/></html:submit> &nbsp; 	
						<html:cancel><fmt:message key="button.cancel"/></html:cancel>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<p><small><fmt:message key="msg.portrait.resized" /></small></p>
</html:form>