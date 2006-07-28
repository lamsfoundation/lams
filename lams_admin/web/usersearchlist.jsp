<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<h2>Find Users</h2>

<logic:notEqual name="isSysadmin" value="false">
<html-el:form action="/usersearch.do" method="post">
<html-el:hidden property="searched" />
<div align="center">&nbsp;<html-el:errors /><html-el:messages id="results" message="true"><bean:write name="results" /></html-el:messages></div>
<table class="alternative-color">
<tr>
	<th><fmt:message key="admin.user.userid"/></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th width="15%"></th>
</tr>
<tr>
	<td><html-el:text property="userId" size="5" /></td>
	<td><html-el:text property="login" size="10" /></td>
	<td><html-el:text property="firstName" size="10" /></td>
	<td><html-el:text property="lastName" size="10" /></td>
	<td></td>
</tr>

<logic:notEmpty name="userList">
	<logic:iterate id="user" name="userList">
		<tr>
			<td>
				<bean:write name="user" property="userId" />
			</td>
			<td>
				<bean:write name="user" property="login" />
			</td>
			<td>
				<bean:write name="user" property="firstName" />
			</td>
			<td>
				<bean:write name="user" property="lastName" />
			</td>
			<td>
				<a href="user.do?method=edit&userId=<bean:write name='user' property='userId' />&orgId=1"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<bean:write name='user' property='userId' />&orgId=1"><fmt:message key="admin.user.delete"/></a>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<tr>
	<td colspan=5 align="right">
		<html-el:submit><fmt:message key="admin.search"/></html-el:submit>
		<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
	</td>
</tr>
</table>
</html-el:form>
</logic:notEqual>