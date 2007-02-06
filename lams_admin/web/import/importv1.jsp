<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.ISpreadsheetService" %>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="admin.importv1.title" />
</h2>

<lams:help page="<%= ISpreadsheetService.IMPORTV1_HELP_PAGE %>"/>

<p>&nbsp;</p>

<p>
<ol>
	<li><fmt:message key="msg.importv1.1"/> <a href="file/lams1_user_org_export.sql">lams1_user_org_export.sql</a>.</li>
	<li><fmt:message key="msg.importv1.2"/>:
		<p>shell> mysql lamsone < lams1_user_org_export.sql -u root -p > lams1_users_orgs.txt</p>
	</li>
	<li><fmt:message key="msg.importv1.3a"/> lams1_users_orgs.txt <fmt:message key="msg.importv1.3b"/>&nbsp;&nbsp;
	<fmt:message key="msg.importv1.4"/></li>
</ol>
</p>

<p>&nbsp;</p>

<html:form action="/importv1save.do" method="post" enctype="multipart/form-data">

<table>
	<tr>
		<td align="right">lams1_users_orgs.txt:&nbsp;</td>
		<td><html:file property="file" /></td>
	</tr>
	<tr>
		<td align="right"><html:checkbox property="integrated">&nbsp;</html:checkbox></td>
		<td><fmt:message key="label.importv1.integrated"/></td>
	</tr>
</table>
<p align="center">
<html:submit styleClass="button"><fmt:message key="label.continue"/></html:submit> &nbsp; 	
<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>