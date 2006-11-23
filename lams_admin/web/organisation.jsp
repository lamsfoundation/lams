<%@ include file="/taglibs.jsp"%>

<html-el:form action="/orgsave.do" method="post">
<html-el:hidden property="orgId" />
<html-el:hidden property="parentId" />
<html-el:hidden property="typeId" />
<h2>
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	
	<logic:equal name="OrganisationForm" property="typeId" value="3">
		: <a href="orgmanage.do?org=<bean:write name="OrganisationForm" property="parentId"  />"><bean:write name="OrganisationForm" property="parentName"/></a>
	</logic:equal>
	
	<logic:notEmpty name="OrganisationForm" property="orgId">
		: <a href="
			<logic:equal name="OrganisationForm" property="typeId" value="2">org</logic:equal>
			<logic:equal name="OrganisationForm" property="typeId" value="3">user</logic:equal>manage.do?org=<bean:write name="OrganisationForm" property="orgId" />">
				<bean:write name="OrganisationForm" property="name"/>
			</a>
		: <fmt:message key="admin.edit"/>
	</logic:notEmpty>
	<logic:empty name="OrganisationForm" property="orgId">
		<logic:equal name="OrganisationForm" property="typeId" value="2">
			: <fmt:message key="admin.course.add"/>
		</logic:equal>
		<logic:equal name="OrganisationForm" property="typeId" value="3">
			: <fmt:message key="admin.class.add"/>
		</logic:equal>
	</logic:empty>
</h2>

<p>&nbsp;</p>

<div align="center"><html-el:errors/></div>
<table>
	<tr>
		<td><fmt:message key="admin.organisation.name"/>:</td>
		<td><html-el:text property="name" size="40" /> *</td>
	</tr>
	<tr>
	<td><fmt:message key="admin.organisation.code"/>:</td>
		<td><html-el:text property="code" size="20" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.description"/>:</td>
		<td><html-el:textarea property="description" cols="50" rows="3" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.locale"/>:</td>
		<td>
			<html-el:select property="localeId">
				<c:forEach items="${locales}" var="locale">
					<html-el:option value="${locale.localeId}">
						<c:out value="${locale.description}" />
					</html-el:option>
				</c:forEach>	
			</html-el:select>
		</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.status"/>:</td>
		<td>
			<html-el:select property="stateId">
				<c:forEach items="${status}" var="state">
					<html-el:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>
	<logic:equal name="OrganisationForm" property="typeId" value="2">
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanAddNewUsers" />
			<fmt:message key="admin.can.add.user"/>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanBrowseAllUsers" />
			<fmt:message key="admin.can.browse.user"/>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanChangeStatusOfCourse" />
			<fmt:message key="admin.can.change.status"/>
		</td>
	</tr>
	</logic:equal>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td><logic:equal name="OrganisationForm" property="typeId" value="2"><fmt:message key="msg.group.organisation_id"/></logic:equal>
			<logic:equal name="OrganisationForm" property="typeId" value="3"><fmt:message key="msg.subgroup.organisation_id"/></logic:equal>
			<bean:write name="OrganisationForm" property="orgId" />.
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan=2 class="align-right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
<table>
</html-el:form>
