<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<script language="javascript" type="text/JavaScript">
function toggleCheckboxes(roleIndex, object){
	<logic-el:iterate id="userBean" name="UserOrgRoleForm" property="userBeans" indexId="beanIndex" >
	document.UserOrgRoleForm.elements[roleIndex+1+<c:out value="${numroles}" />*(<c:out value="${beanIndex}" />+1)].checked=object.checked;
	</logic-el:iterate>
}
</script>

<h2>Assign Roles</h2>

<div align="center"><html-el:errors/></div>

<html-el:form action="/userorgrolesave.do" method="post">
<html-el:hidden property="orgId" />
<table class="alternative-color" width=100%>
<tr>
	<th><fmt:message key="admin.user.login"/></th>
	<logic-el:iterate id="role" name="roles" indexId="roleIndex">
		<th><input type="checkbox" 
					name="<c:out value="${roleIndex}" />" 
					onclick="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" 
					onkeyup="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" />
			<c:out value="${role.name}" /></th>
	</logic-el:iterate>
</tr>
<logic-el:iterate id="userBean" name="UserOrgRoleForm" property="userBeans" indexId="beanIndex">
	<tr>
		<td>
			<c:out value="${userBean.login}" /><br />
		</td>
		<logic-el:iterate id="role" name="roles">
			<td>
				<html-el:multibox property="userBeans[${beanIndex}].roleIds" value="${role.roleId}" />&nbsp;
			</td>
		</logic-el:iterate>
	</tr>
</logic-el:iterate>
<tr>
	<td></td>
	<td colspan=<c:out value="${numroles}" /> align="right">
		<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
		<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
		<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
	</td>
</tr>

</table>
</html-el:form>
