<%@ include file="/taglibs.jsp"%>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<table class="table table-striped table-condensed">
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
			<a href="user.do?method=enable&userId=<bean:write name='user' property='userId' />" class="btn btn-default btn-sm"><fmt:message key="admin.enable"/></a>
		</td>		
	</tr>
</logic:iterate>
</table>
