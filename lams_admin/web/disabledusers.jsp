<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<h2>Disabled Users</h2>
<p>&nbsp;</p>
<table class="alternative-color" width=100%>
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
</form>