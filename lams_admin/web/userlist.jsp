<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<h2 align="left">
  <logic:notEmpty name="pOrgId">
    <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a> ::
  </logic:notEmpty>
  <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="UserManageForm" property="orgId" />">
    <bean:write name="UserManageForm" property="orgName"/></a> ::
  <fmt:message key="admin.user.manage" />
</h2>
<p align="right">
	<logic:equal name="UserManageForm" property="courseAdminCanAddNewUsers" value="true">
		<input type="button" value='<fmt:message key="admin.user.create"/>' onclick=javascript:document.location='user.do?method=edit&orgId=<bean:write name="UserManageForm" property="orgId"/>' />
	</logic:equal>
	<logic:notEqual name="UserManageForm" property="orgId" value="1">
		<input type="button" value='<fmt:message key="admin.user.add"/>' onclick=javascript:document.location='userorg.do?orgId=<bean:write name="UserManageForm" property="orgId"/>' />
	</logic:notEqual>
</p>
<table class="alternative-color" width=100%>
<tr>
	<th></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.title"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th><fmt:message key="admin.user.roles"/></th>
	<th></th>
</tr>
<logic:iterate id="userManageBean" name="UserManageForm" property="userManageBeans">
	<tr>
		<td>
			<bean:write name="userManageBean" property="userId" />
		</td>
		<td>
			<bean:write name="userManageBean" property="login" />
		</td>
		<td>
			<bean:write name="userManageBean" property="title" />
		</td>
		<td>
			<bean:write name="userManageBean" property="firstName" />
		</td>
		<td>
			<bean:write name="userManageBean" property="lastName" />
		</td>
		<td>
		    <small>
		    <logic:iterate id="role" name="userManageBean" property="roles">
		        <c:out value="${role.name}" />&nbsp;
		    </logic:iterate>
		    </small>
		</td>
		<td>
				<a href="user.do?method=edit&userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.user.delete"/></a>
				<br/>
		</td>		
	</tr>
</logic:iterate>
</table>
</form>