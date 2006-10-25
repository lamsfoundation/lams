<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<form>
<h2 align="left">
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	<logic:equal name="orgType" value="1">
		: <fmt:message key="admin.user.manage" />
	</logic:equal>
	<logic:equal name="orgType" value="2">
		: <a href="orgmanage.do?org=<bean:write name="UserManageForm" property="orgId" />"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
	<logic:equal name="orgType" value="3">
		: <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a>
		: <a href="usermanage.do?org=<bean:write name="UserManageForm" property="orgId" />"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
	: <fmt:message key="admin.user.manage" />
</h2>

<p>&nbsp;</p>

<p align="right">
	<logic:equal name="UserManageForm" property="courseAdminCanAddNewUsers" value="true">
		<input type="button" value='<fmt:message key="admin.user.create"/>' onclick=javascript:document.location='user.do?method=edit&orgId=<bean:write name="UserManageForm" property="orgId"/>' />
	</logic:equal>
	<logic:notEqual name="UserManageForm" property="orgId" value="1">
		<input type="button" value='<fmt:message key="admin.user.add"/>' onclick=javascript:document.location='userorg.do?orgId=<bean:write name="UserManageForm" property="orgId"/>' />
	</logic:notEqual>
	<input type="button" value='<fmt:message key="admin.edit" /> <bean:write name="UserManageForm" property="orgName"/>' onclick=javascript:document.location='organisation.do?method=edit&orgId=<c:out value="${UserManageForm.orgId}"/>' />
</p>
<table class="alternative-color" width=100%>
<tr>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.name"/></th>
	<th><fmt:message key="admin.user.roles"/></th>
	<th><fmt:message key="admin.user.actions"/></th>
</tr>
<logic:iterate id="userManageBean" name="UserManageForm" property="userManageBeans">
	<tr>
		<td>
			<bean:write name="userManageBean" property="login" />
		</td>
		<td>
			<bean:write name="userManageBean" property="title" /> <bean:write name="userManageBean" property="firstName" /> <bean:write name="userManageBean" property="lastName" />
		</td>
		<td>
		    <small>
		    <logic:iterate id="role" name="userManageBean" property="roles">
		        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message>&nbsp;
		    </logic:iterate>
		    </small>
		</td>
		<td>
				<a href="userroles.do?userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.user.assign.roles"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.user.delete"/></a>
				<br/>
		</td>		
	</tr>
</logic:iterate>
</table>
</form>