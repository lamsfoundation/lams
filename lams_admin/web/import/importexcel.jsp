<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="admin.user.import" />
</h2>

<p>&nbsp;</p>

<html:form action="/importexcelsave.do" method="post" enctype="multipart/form-data">
<html:hidden property="orgId" />

<table>
	<tr>
		<td colspan="2">
			<fmt:message key="label.download.template" /> <a href="file/lams_users_template.xls"><fmt:message key="label.spreadsheet" /></a>
		</td>
	<tr>
		<td><fmt:message key="label.excel.spreadsheet" />:</td>
		<td><html:file property="file" /></td>
	</tr>
	<tr>
		<td colspan="2"><html:submit styleClass="button"><fmt:message key="admin.save"/></html:submit> &nbsp; 	
						<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel></td>
	</tr>
</table>

</html:form>