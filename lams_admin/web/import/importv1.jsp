<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<script language="javascript" type="text/JavaScript">
function loading(){
	document.getElementById('loading').style.display="";
	document.getElementById('instructions').style.display="none";
}
</script>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="<%= IImportService.IMPORTV1_HELP_PAGE %>"/>
<h1><fmt:message key="admin.importv1.title" /></h1>

<div id="loading" style="display:none">
	<h3>Please wait while importing...</h3>
	<p align="center"><img src="<lams:LAMSURL/>/images/loading.gif" alt="loading..." /></p>
</div>

<div id="instructions">

<p>
<ul>
	<li><fmt:message key="msg.importv1.1"/>
		<ul><li><p><a href="file/lams1_user_org_export.sql">lams1_user_org_export.sql</a></p></li></ul>
	</li>
	<li><fmt:message key="msg.importv1.2"/>:
		<ul><li><p>shell> mysql lamsone < lams1_user_org_export.sql -u root -p > lams1_users_orgs.txt</p></li></ul>
	</li>
	<li><p><fmt:message key="msg.importv1.3a"/> lams1_users_orgs.txt <fmt:message key="msg.importv1.3b"/>&nbsp;&nbsp;
	<fmt:message key="msg.importv1.4"/></p></li>
	<li><p><fmt:message key="msg.importv1.5"/></p></li>
</ul>
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
<html:submit styleClass="button" onclick="loading();" onkeydown="loading();"><fmt:message key="label.continue"/></html:submit> &nbsp; 	
<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>

</div>