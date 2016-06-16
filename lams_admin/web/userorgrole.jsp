<%@ include file="/taglibs.jsp"%>

<script language="javascript" type="text/JavaScript">
function toggleCheckboxes(roleIndex, object){
	<logic:iterate id="userBean" name="UserOrgRoleForm" property="userBeans" indexId="beanIndex" >
	document.UserOrgRoleForm.elements[roleIndex+1+<c:out value="${numroles}" />*(<c:out value="${beanIndex}" />+1)].checked=object.checked;
	</logic:iterate>
}
</script>

<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
    <logic:notEmpty name="pOrgId">
        : <a href="orgmanage.do?org=<bean:write name="pOrgId" />" class="btn btn-default"><bean:write name="pOrgName"/></a>
    </logic:notEmpty>
    <logic:notEqual name="UserOrgRoleForm" property="orgId" value="1">
		: <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="UserOrgRoleForm" property="orgId" />" class="btn btn-default">
		<bean:write name="orgName"/></a>
	</logic:notEqual>
	<logic:equal name="UserOrgRoleForm" property="orgId" value="1">
		: <a href="usermanage.do?org=<bean:write name="UserOrgRoleForm" property="orgId" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
	</logic:equal>
</h4>

<h1><fmt:message key="admin.user.assign.roles" /></h1>

<p><fmt:message key="msg.roles.mandatory.users"/></p>

<html-el:form action="/userorgrolesave.do" method="post">
<html-el:hidden property="orgId" />

<table class="table table-condensed table-no-border">
<tr>
	<th><fmt:message key="admin.user.login"/></th>
	<logic:iterate id="role" name="roles" indexId="roleIndex">
		<th><input type="checkbox" 
					name="<c:out value="${roleIndex}" />" 
					onclick="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" 
					onkeyup="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" />&nbsp;
			<fmt:message>role.<lams:role role="${role.name}" /></fmt:message></th>
	</logic:iterate>
</tr>
<logic:iterate id="userBean" name="UserOrgRoleForm" property="userBeans" indexId="beanIndex">
	<tr>
		<td>
			<c:out value="${userBean.login}" /><c:if test="${!userBean.memberOfParent}"> *<c:set var="parentFlag" value="true" /></c:if>
		</td>
		<logic:iterate id="role" name="roles">
			<td>
				<html-el:multibox styleId="${userBean.login}Role${role.roleId}" property="userBeans[${beanIndex}].roleIds" value="${role.roleId}" />&nbsp;
			</td>
		</logic:iterate>
	</tr>
</logic:iterate>
</table>
<c:if test="${parentFlag}">
<p><fmt:message key="msg.user.add.to.parent.group" /></p>
</c:if>

<div class="pull-right">
	<html-el:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
	<html-el:submit styleId="saveButton"   styleClass="btn btn-primary loffset5"><fmt:message key="admin.save"/></html-el:submit>
</div>
</html-el:form>
