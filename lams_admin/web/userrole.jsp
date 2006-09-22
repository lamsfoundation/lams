<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html-el:form action="/userrolessave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h2 align="left"><fmt:message key="admin.user.assign.roles"/>: <bean:write name="orgName" /></h2>

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
                <c:out value="${role.name}"/><br/>
            </c:forEach>
	    </td>
	</tr>
	<tr>
		<td colspan=2 align="right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
</table>
</html-el:form>
