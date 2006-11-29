<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.util.IUserImportFileParser" %>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="admin.user.import" />
</h2>

<lams:help page="<%= IUserImportFileParser.IMPORT_HELP_PAGE %>"/>

<p>&nbsp;</p>

<p><fmt:message key="msg.import.intro"/></p>
<p>
<fmt:message key="label.notes"/>:<br/>
<ul>
<li><fmt:message key="msg.import.1"/></li>
<li><fmt:message key="msg.import.2"/></li>
</ul>
</p>
<p><fmt:message key="msg.import.conclusion"/></p>

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
</table>
<p align="center">
<html:submit styleClass="button"><fmt:message key="label.import"/></html:submit> &nbsp; 	
<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>