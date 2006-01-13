
<c_rt:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<table width="80%" cellspacing="8" align="CENTER" class="forms">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.subject" /></b></td>
		<td valign="MIDDLE"><b class="required">*</b><html:text size="30"
			 tabindex="1" property="message.subject" /> 
			 <html:errors property="message.subject" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.body" /></b></td>
		<td valign="TOP"><%@include file="bodyarea.jsp"%></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.attachment" /></b></td>
		<td valign="MIDDLE">
			&nbsp;&nbsp;<html:file tabindex="3"
				property="attachmentFile" /> <html:errors
				property="message.attachment" />
		</td>
	</tr>
		<td>&nbsp;&nbsp;
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</td>
	</tr>
</table>
