<html:form>
<logic:equal name="type" value="2">
	<h4><bean:message key="admin.course.manage" /></h4>
	<p>Parent Organisation:<bean:write name="parentOrganisaton" property="name"/></p>
	<p align="right"><html:button titleKey="admin.course.add" onClick="javascript:document.location=/organisation.do?method=add&type=<bean:write name='organisationType' property='name'/>&parent=<bean:write name='parentOrganisation' property='organisationId'>"/></p>
</logic:equal>
<logic:equal name="type" value="3">
	<h4><bean:message key="admin.class.manage" /></h4>
	<p>Parent Organisation:<bean:write name="parentOrganisaton" property="name"/></p>
	<p align="right"><html:button titleKey="admin.class.add" onClick="javascript:document.location=/organisation.do?method=add&type=<bean:write name='organisationType' property='name'/>&parent=<bean:write name='parentOrganisation' property='organisationId'>"/></p>
</logic:equal>
<table width=80%>
<tr>
	<th>Name</th>
	<th>Code</th>
	<th>Description</th>
	<th>Language</th>
	<th>Country</th>
	<th>Status</th>
	<th></th>
</tr>
<logic:iterate id="orgManageBean" name="OrgManageForm" property="orgManageBeans">
	<tr>
		<td>
			<bean:write name="orgManageBean" property="name" />
		</td>
		<td>
			<bean:write name="orgManageBean" property="code" />
		</td>
		<td>
			<bean:write name="orgManageBean" property="description" />
		</td>
		<td>
			<fmt:message key="locale.language.<bean:write name='orgManageBean' property='localeLanguage'/>"/>
		</td>
		<td>
			<fmt:message key="locale.country.<bean:write name='orgManageBean' property='localeCountry'/>"/>
		</td>
		<td>
			<fmt:message key="organisation.state.<bean:write name='orgManageBean' property='status'/>"/>
		</td>
		<td>
			<logic:equal name='orgManageBean' property='editable' value="true">
				<a href="/organisation.do?method=edit&org=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="/organisation.do?method=remove&org=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.remove"/></a>
				<br/>
				<a href="/organisation.do?method=manageUsers?org=<bean:write name='orgManageBean' property='organisationId'/>"><fmt:message key="admin.user.manage"/></a>
				<logic:equal name="type" value="2">
					<a href="/orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><fmt:message key="admin.class.manage"/></a>
				</logic:equal>
			</logic:equal>
		</td>		
	</tr>
</logic:iterate>
</table>
</html:form>