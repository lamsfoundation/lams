<%@ include file="/taglibs.jsp"%>

<form>
<logic:equal name="OrgManageForm" property="type" value="1">
	<h2>
		<a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><fmt:message key="admin.course.manage" /></a>
	</h2>
	<p>&nbsp;</p>
	<p align="right">
	<logic:equal name="createOrEditGroup" value="true">
		<c:url var="editaction" value="organisation.do">
			<c:param name="method" value="edit" />
			<c:param name="typeId" value="2" />
			<c:param name="parentId" value="${OrgManageForm.parentId}" />
		</c:url>
		<input class="button" type="button" value='<fmt:message key="admin.course.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' />
	</logic:equal>
	<logic:equal name="manageGlobalRoles" value="true">
		<input class="button" type="button" value='<fmt:message key="admin.global.roles.manage" />' onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
	</logic:equal>
	</p>
</logic:equal>
<logic:equal name="OrgManageForm" property="type" value="2">
	<h2>
		<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
		: <a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><bean:write name="OrgManageForm" property="parentName"/></a>
		: <fmt:message key="admin.class.manage" />
	</h2>
	<p>&nbsp;</p>
	<c:url var="editaction" value="organisation.do">
		<c:param name="method" value="edit" />
		<c:param name="typeId" value="3" />
		<c:param name="parentId" value="${OrgManageForm.parentId}" />
	</c:url>
	<p align="right">
		<input class="button" type="button" value='<fmt:message key="admin.class.add"/>' onclick=javascript:document.location='<c:out value="${editaction}"/>' />
		<input class="button" type="button" value='<fmt:message key="admin.user.manage" />' onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
		<logic:equal name="createOrEditGroup" value="true">
			<input class="button" type="button" value='<fmt:message key="admin.edit" /> <bean:write name="OrgManageForm" property="parentName"/>' onclick=javascript:document.location='organisation.do?method=edit&orgId=<c:out value="${OrgManageForm.parentId}"/>' />
		</logic:equal>
	</p>
</logic:equal>
<table class=alternative-color width=100%>
<tr>
	<th>Id</th>
	<th><fmt:message key="admin.organisation.name"/></th>
	<th><fmt:message key="admin.organisation.code"/></th>
	<th><fmt:message key="admin.organisation.description"/></th>
	<th><fmt:message key="admin.organisation.locale"/></th>
	<th><fmt:message key="admin.organisation.status"/></th>
</tr>
<logic:iterate id="orgManageBean" name="OrgManageForm" property="orgManageBeans" indexId="idx">
	<tr>
		<td><bean:write name="orgManageBean" property="organisationId" /></td>
		<td>
			<logic:equal name="OrgManageForm" property="type" value="1">
				<a href="orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><bean:write name="orgManageBean" property="name" /></a>
			</logic:equal>
			<logic:equal name="OrgManageForm" property="type" value="2">
				<a href="usermanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><bean:write name="orgManageBean" property="name" /></a>
			</logic:equal>
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
</logic:iterate>
</table>
</form>