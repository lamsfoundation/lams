<%@ include file="/taglibs.jsp"%>

<html-el:form action="/userrolessave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h4 align="left">
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	<logic:notEmpty name="pOrgId">
		: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />"><c:out value="${parentName}" /></a>
		: <a href="usermanage.do?org=<bean:write name="UserRolesForm" property="orgId" />"><c:out value="${orgName}" /></a>
	</logic:notEmpty>
	<logic:empty name="pOrgId">
		<logic:notEqual name="UserRolesForm" property="orgId" value="1">
			: <a href="orgmanage.do?org=<bean:write name="UserRolesForm" property="orgId" />"><c:out value="${orgName}" /></a>
		</logic:notEqual>
		<logic:equal name="UserRolesForm" property="orgId" value="1">
			: <a href="usermanage.do?org=<bean:write name="UserRolesForm" property="orgId" />"><fmt:message key="admin.global.roles.manage" /></a>
		</logic:equal>
	</logic:empty>
</h5>

<h1><fmt:message key="admin.user.assign.roles"/></h1>

<p><fmt:message key="msg.roles.mandatory"/></p>

<div align="center"><html-el:errors/><html-el:messages id="roles" message="true"><bean:write name="roles" /></html-el:messages></div>
<table>
    <tr>
		<td align="right"><fmt:message key="admin.user.login"/>:</td>
		<td><bean:write name="login" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="admin.user.name"/>:</td>
		<td><bean:write name="fullName" /></td>
	</tr>
	<tr>
	    <td align="right"><fmt:message key="admin.user.roles"/>:</td>
	    <td>
            <c:forEach items="${rolelist}" var="role">
                <html-el:multibox name="UserRolesForm" property="roles" value="${role.roleId}"/>
                <fmt:message>role.<lams:role role="${role.name}" /></fmt:message><br/>
            </c:forEach>
	    </td>
	</tr>
	<tr>
		<td colspan=2 class="align-right">
			<html-el:submit styleClass="button"><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset styleClass="button"><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel styleClass="button"><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
</table>
</html-el:form>
