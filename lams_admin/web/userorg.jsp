<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html-el:form action="/userorgsave.do" method="post">
<html-el:hidden property="orgId" />
<h4 align="left">
    <logic:notEmpty name="pOrgId">
        <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a> ::
    </logic:notEmpty>
    <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="UserOrgForm" property="orgId" />">
      <bean:write name="UserOrgForm" property="orgName"/></a> ::
    <fmt:message key="admin.user.add"/>
</h4>

<div align="center"><html-el:errors/></div>
<table width=100%>
<tr>
	<th></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.title"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th></th>
</tr>

<logic:iterate id="user" name="userlist">
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
            <html-el:multibox name="UserOrgForm" property="userIds" value="${user.userId}"/>
		</td>		
	</tr>
</logic:iterate>

<tr>
	<td colspan=6 align="right">
		<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
		<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
		<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
	</td>
</tr>

</table>
</html-el:form>