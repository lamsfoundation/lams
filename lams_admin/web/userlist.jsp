<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<h4 align="left"><fmt:message key="admin.user.manage" /></h4>
<p align="left"><fmt:message key="admin.in"/> <fmt:message key="admin.organisation"/> : <bean:write name="UserManageForm" property="orgName"/></p>
<p align="right"><input type="button" value='<fmt:message key="admin.user.add"/>' onclick=javascript:document.location='user.do?method=add&orgId=<bean:write name="UserManageForm" property="orgId"/>' /></p>
<table width=100%>
<tr>
	<th></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.title"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
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
				<a href="user.do?method=edit&userId=<bean:write name='userManageBean' property='userId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<bean:write name='userManageBean' property='userId' />"><fmt:message key="admin.user.remove"/></a>
				<br/>
		</td>		
	</tr>
</logic:iterate>
</table>
</form>