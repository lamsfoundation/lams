<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<logic:equal name="OrgManageForm" property="type" value="1">
	<h2><fmt:message key="admin.course.manage" /> <fmt:message key="admin.in"/> <fmt:message key="admin.organisation"/>: <bean:write name="OrgManageForm" property="parentName"/></h2>
	<c:url var="editaction" value="organisation.do">
		<c:param name="method" value="edit" />
		<c:param name="typeId" value="2" />
		<c:param name="parentId" value="${OrgManageForm.parentId}" />
	</c:url>
	<p align="right"><input type="button" value='<fmt:message key="admin.course.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' /></p>
</logic:equal>
<logic:equal name="OrgManageForm" property="type" value="2">
	<h2><fmt:message key="admin.class.manage" /> <fmt:message key="admin.in"/> <fmt:message key="admin.course"/>:<bean:write name="OrgManageForm" property="parentName"/></h2>
	<c:url var="editaction" value="organisation.do">
		<c:param name="method" value="edit" />
		<c:param name="typeId" value="3" />
		<c:param name="parentId" value="${OrgManageForm.parentId}" />
	</c:url>
	<p align="right"><input type="button" value='<fmt:message key="admin.class.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' /></p>
</logic:equal>
<table class=alternative-color width=100%>
<tr>
	<th><fmt:message key="admin.organisation.name"/></th>
	<th><fmt:message key="admin.organisation.code"/></th>
	<th><fmt:message key="admin.organisation.description"/></th>
	<th><fmt:message key="admin.organisation.locale"/></th>
	<th><fmt:message key="admin.organisation.status"/></th>
</tr>
<logic:iterate id="orgManageBean" name="OrgManageForm" property="orgManageBeans" indexId="idx">
	<tr>
		<td>
			<bean:write name="orgManageBean" property="name" />
		</td>
		<td>
			<bean:write name="orgManageBean" property="code" />
		</td>
		<td>
			<bean:write name="orgManageBean" property="description" />
		</td>
		<td>
			<c:out value="${orgManageBean.locale.description}"/>
		</td>
		<td>
			<fmt:message key="organisation.state.${orgManageBean.status}"/>
		</td>
	</tr>
	<tr>
		<td colspan="6">
			<logic:equal name='orgManageBean' property='editable' value="true">
				<a href="organisation.do?method=edit&orgId=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<logic:equal name="OrgManageForm" property="type" value="1">
					<a href="orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>" target="_blank"><fmt:message key="admin.class.manage"/></a>
					&nbsp;
				</logic:equal>
			</logic:equal>
			<a href="usermanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>" target="_blank"><fmt:message key="admin.user.manage"/></a>
		</td>
	</tr>
</logic:iterate>
</table>
</form>