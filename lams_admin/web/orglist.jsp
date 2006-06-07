<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<logic:equal name="OrgManageForm" property="type" value="1">
	<h4><fmt:message key="admin.course.manage" /></h4>
	<p>Parent Organisation:<bean:write name="OrgManageForm" property="parentName"/></p>
	<p align="right"><input type="button" value='<fmt:message key="admin.course.add"/>' onclick=javascript:document.location='/organisation.do?method=add&parent=<bean:write name="OrgManageForm" property="parentId"/>' /></p>
</logic:equal>
<logic:equal name="OrgManageForm" property="type" value="2">
	<h4><fmt:message key="admin.class.manage" /></h4>
	<p>Parent Organisation:<bean:write name="parentOrganisaton" property="name"/></p>
	<p align="right"><input type="button" value='<fmt:message key="admin.class.add"/>' onclick=javascript:document.location='/organisation.do?method=add&parent=<bean:write name="OrgManageForm" property="parentId"/>'/></p>
</logic:equal>
<table width=100%>
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
			<bean:write name='orgManageBean' property='localeLanguage'/>
		</td>
		<td>
			<bean:write name='orgManageBean' property='localeCountry'/>
		</td>
		<td>
			<bean:write name='orgManageBean' property='status'/>
		</td>
		<td>
			<logic:equal name='orgManageBean' property='editable' value="true">
				<a href="/organisation.do?method=edit&org=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="/organisation.do?method=remove&org=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.remove"/></a>
				<br/>
				<a href="/usermanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><fmt:message key="admin.user.manage"/></a>
				<logic:equal name="OrgManageForm" property="type" value="1">
					<br/><a href="orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><fmt:message key="admin.class.manage"/></a>
				</logic:equal>
			</logic:equal>
		</td>		
	</tr>
</logic:iterate>
</table>
</form>