<%@ include file="/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript">
	function resetFields() {
		document.UserSearchForm.sUserId.value = '';
		document.UserSearchForm.sLogin.value = '';
		document.UserSearchForm.sFirstName.value = '';
		document.UserSearchForm.sLastName.value = '';
	}
</script>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> 
	: <fmt:message key="admin.user.find"/>
</h2>

<logic:notEqual name="isSysadmin" value="false">
<html-el:form action="/usersearch.do" method="post">
<html-el:hidden property="searched" />
<div align="center">&nbsp;<html-el:errors /><html-el:messages id="results" message="true"><bean:write name="results" /></html-el:messages></div>
<table class="alternative-color" cellspacing="0">
<tr>
	<th><fmt:message key="admin.user.userid"/></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th><fmt:message key="admin.user.actions"/></th>
</tr>
<tr>
	<td><html-el:text property="sUserId" size="5" /></td>
	<td><html-el:text property="sLogin" size="10" /></td>
	<td><html-el:text property="sFirstName" size="10" /></td>
	<td><html-el:text property="sLastName" size="10" /></td>
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
				<a href="user.do?method=edit&userId=<bean:write name='user' property='userId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<bean:write name='user' property='userId' />"><fmt:message key="admin.user.delete"/></a>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<tr>
	<td colspan=5 align="right">
		<html:checkbox property="showAll"><fmt:message key="label.show.all.users"/></html:checkbox>&nbsp;
		<html-el:submit><fmt:message key="admin.search"/></html-el:submit>
		<input type="button" value='<fmt:message key="admin.reset"/>' onclick="resetFields();" />
	</td>
</tr>
</table>
</html-el:form>
</logic:notEqual>