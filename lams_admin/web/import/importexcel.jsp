<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

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
		<td colspan="2"><html:submit><fmt:message key="admin.save"/></html:submit> &nbsp; 	
						<html:cancel><fmt:message key="admin.cancel"/></html:cancel></td>
	</tr>
</table>

</html:form>