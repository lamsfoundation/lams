<%@ include file="/taglibs.jsp"%>

<html-el:form action="/orgsave.do" method="post">
<html-el:hidden property="orgId" />
<html-el:hidden property="parentId" />
<html-el:hidden property="typeId" />
<html:hidden property="courseAdminCanAddNewUsers" />
<html:hidden property="courseAdminCanBrowseAllUsers" />
<html:hidden property="courseAdminCanChangeStatusOfCourse" />
<logic:equal name="OrganisationForm" property="courseAdminCanChangeStatusOfCourse" value="false">
	<html:hidden property="stateId" />
</logic:equal>
<p>
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> : 
	<a href="orgmanage.do?org=<bean:write name="OrganisationForm" property="orgId" />" class="btn btn-default">
		<bean:write name="OrganisationForm" property="name"/>
	</a>
</p>

<h1>
	<fmt:message key="admin.edit"/> <bean:write name="OrganisationForm" property="name"/>
</h1>

<div align="center"><html-el:errors/></div>
<table  class="table table-no-border">
	<tr>
		<td><fmt:message key="admin.organisation.name"/>:</td>
		<td><html-el:text property="name" size="40" styleClass="form-control"/> *</td>
	</tr>
	<tr>
	<td><fmt:message key="admin.organisation.code"/>:</td>
		<td><html-el:text property="code" size="20"  styleClass="form-control"/></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.description"/>:</td>
		<td><html-el:textarea property="description" cols="50" rows="3"  styleClass="form-control"/></td>
	</tr>
	<logic:equal name="OrganisationForm" property="courseAdminCanChangeStatusOfCourse" value="true">
	<tr>
		<td><fmt:message key="admin.organisation.status"/>:</td>
		<td>
			<html-el:select property="stateId"  styleClass="form-control">
				<c:forEach items="${status}" var="state">
					<html-el:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></html-el:option>
				</c:forEach>
			</html-el:select>
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
</table>

<div class="pull-right">
	<html-el:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
	<html-el:reset styleClass="btn btn-default loffset5"><fmt:message key="admin.reset"/></html-el:reset>
	<html-el:submit styleClass="btn btn-primary loffset5"><fmt:message key="admin.save"/></html-el:submit>
</div>

</html-el:form>
		