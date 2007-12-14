<%@ include file="/taglibs.jsp"%>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<h1><fmt:message key="admin.list.disabled.users"/></h1>

<table class="alternative-color" width=100% cellspacing="0">
<tr>
	<th></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.title"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th></th>
</tr>
<logic:iterate id="user" name="users">
	<tr>
		<td>
			<bean:write name="user" property="userId" />
		</td>
		<td>
			<bean:write name="user" property="login" />
		</td>
		<td>
			<bean:write name="user" property="title" />
		</td>
		<td>
			<bean:write name="user" property="firstName" />
		</td>
		<td>
			<bean:write name="user" property="lastName" />
		</td>
		<td>
			<a href="user.do?method=enable&userId=<bean:write name='user' property='userId' />"><fmt:message key="admin.enable"/></a>
		</td>		
	</tr>
</logic:iterate>
</table>
