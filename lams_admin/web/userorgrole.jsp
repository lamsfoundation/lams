<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<div align="center"><html-el:errors/></div>

<html-el:form action="/userorgrolesave.do" method="post">
<html-el:hidden property="orgId" />
<table class="alternative-color" width=100%>
<tr>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.roles"/></th>
</tr>
<logic:iterate id="userBean" name="UserOrgRoleForm" property="userBeans" indexId="beanIndex">
	<tr>
		<td>
			<c:out value="${userBean.login}" /><br />
		</td>
		<td>
			<logic:iterate id="role" name="roles" indexId="roleIndex">
				<html-el:multibox name="UserOrgRoleForm" property="bean[${beanIndex}].roleIds" value="${role.roleId}" /> <c:out value="${role.name}" />&nbsp;
			</logic:iterate>
		</td>
	</tr>
</logic:iterate>
<tr>
	<td colspan=2 align="right">
		<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
		<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
		<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
	</td>
</tr>

</table>
</html-el:form>
