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
		<c:param name="parentName" value="${OrgManageForm.parentName}" />
	</c:url>
	<p align="right"><input type="button" value='<fmt:message key="admin.course.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' /></p>
</logic:equal>
<logic:equal name="OrgManageForm" property="type" value="2">
	<h2><fmt:message key="admin.class.manage" /> <fmt:message key="admin.in"/> <fmt:message key="admin.course"/>:<bean:write name="OrgManageForm" property="parentName"/></h2>
	<c:url var="editaction" value="organisation.do">
		<c:param name="method" value="edit" />
		<c:param name="typeId" value="3" />
		<c:param name="parentId" value="${OrgManageForm.parentId}" />
		<c:param name="parentName" value="${OrgManageForm.parentName}" />
	</c:url>
	<p align="right"><input type="button" value='<fmt:message key="admin.class.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' /></p>
</logic:equal>
<table class=alternative-color width=100%>
<tr>
	<th><fmt:message key="admin.number"/></th>
	<th><fmt:message key="admin.organisation.name"/></th>
	<th><fmt:message key="admin.organisation.code"/></th>
	<th><fmt:message key="admin.organisation.description"/></th>
	<th><fmt:message key="admin.organisation.language"/></th>
	<th><fmt:message key="admin.organisation.country"/></th>
	<th><fmt:message key="admin.organisation.status"/></th>
</tr>
<logic:iterate id="orgManageBean" name="OrgManageForm" property="orgManageBeans" indexId="idx">
	<tr>
		<td>
			<bean:write name="idx"/>
		</td>
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
			<fmt:message key="locale.language.${orgManageBean.localeLanguage}"/>
		</td>
		<td>
			<c:if test="${!empty orgManageBean.localeCountry}">
				<fmt:message key="locale.country.${orgManageBean.localeCountry}"/>
			</c:if>
		</td>
		<td>
			<fmt:message key="organisation.state.${orgManageBean.status}"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="6">
			<logic:equal name='orgManageBean' property='editable' value="true">
				<a href="organisation.do?method=edit&orgId=<bean:write name='orgManageBean' property='organisationId' />"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="organisation.do?method=remove&orgId=<bean:write name='orgManageBean' property='organisationId' />&parentId=<bean:write name='OrgManageForm' property='parentId'/>"><fmt:message key="admin.remove"/></a>
				&nbsp;
				<a href="usermanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>" target="_blank"><fmt:message key="admin.user.manage"/></a>
				&nbsp;
				<logic:equal name="OrgManageForm" property="type" value="1">
					<a href="orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>" target="_blank"><fmt:message key="admin.class.manage"/></a>
				</logic:equal>
			</logic:equal>
		</td>
	</tr>
</logic:iterate>
</table>
</form>