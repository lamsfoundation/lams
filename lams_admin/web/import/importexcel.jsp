<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<script language="javascript" type="text/JavaScript">
	function goToStatus() {
		document.location = '<lams:LAMSURL/>/admin/import/status.jsp';
	}
</script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<p><fmt:message key="msg.import.intro"/></p>
<p>
<ul>
	<li>
		<fmt:message key="msg.import.1"/>
	</li>
	<li>
		<fmt:message key="msg.import.2"/>
		<ul><li><p><a href="file/lams_users_template.xls">lams_users_template.xls</a></p></li></ul>
	</li>
	<li>
		<fmt:message key="msg.import.3"/>
		<ul><li><p><a href="file/lams_roles_template.xls">lams_roles_template.xls</a></p></li></ul>
	</li>
</ul>
</p>
<p><fmt:message key="msg.import.conclusion"/></p>

<html:form action="/importexcelsave.do" method="post" enctype="multipart/form-data" onsubmit="goToStatus();">
<html:hidden property="orgId" />

<table>
	<tr>
		<td align="right"><fmt:message key="label.excel.spreadsheet" />:&nbsp;</td>
		<td><html:file property="file" styleClass="form-control"/></td>
	</tr>
</table>
<div class="pull-right">
<html:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html:cancel>
<html:submit styleId="importButton" styleClass="btn btn-primary loffset5"><fmt:message key="label.import"/></html:submit> &nbsp; 	
</div>

</html:form>