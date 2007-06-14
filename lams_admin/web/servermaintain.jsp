<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :
	<a href="serverlist.do"><fmt:message key="sysadmin.maintain.external.servers" /></a> :
	<fmt:message key="sysadmin.maintain.server.edit"/>
</h2>
<lams:help style="no-tabs" page="LAMS+v2.0+Integration+Setup+Step-by-Step+Guide"/>
<br />
<html:errors/>
<br />
<html:form action="serversave.do" method="post">
<html:hidden property="sid" />
<table>
	<tr>
		<td><fmt:message key="sysadmin.serverid" />:</td>
		<td><html:text property="serverid" size="20"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.serverkey" />:</td>
		<td><html:text property="serverkey" size="30"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.servername" />:</td>
		<td><html:text property="servername" size="30"/> *</td>
	</tr>
	<tr>
		<td valign="top"><fmt:message key="sysadmin.serverdesc" />:</td>
		<td><html:textarea property="serverdesc" cols="40" rows="3"/> </td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.prefix" />:</td>
		<td><html:text property="prefix" size="10"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.disabled" />:</td>
		<td><html:checkbox property="disabled" /></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.organisation" />:</td>
		<td>
			<html:select property="orgId" >
				<html:options collection="orgs" property="organisationId" labelProperty="name"/> 
			</html:select> &nbsp; 
			<fmt:message key="label.or" /> &nbsp;
			<html:checkbox property="newOrg" onclick="changeStatus(this)" /> 
			<fmt:message key="sysadmin.organisation.create" />
			<html:text property="orgName" disabled="true" size="20"/> *
		</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.userinfoUrl" />:</td>
		<td><html:text property="userinfoUrl" size="70"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.timeoutUrl" />:</td>
		<td><html:text property="timeoutUrl" size="70"/> *</td>
	</tr>
</table>
<div align="center">
	<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
</div>
</html:form>
<script type="text/javascript" language="javascript">
<!--
	function changeStatus(obj){
		document.forms[0].orgId.disabled = obj.checked;
		document.forms[0].orgName.disabled = !obj.checked;
	}
//->
</script>