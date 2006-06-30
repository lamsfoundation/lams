<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html-el:form action="/orgsave.do" method="post">
<html-el:hidden property="orgId" />
<html-el:hidden property="parentId" />
<html-el:hidden property="typeId" />
<h2>
	<logic:notEmpty name="OrganisationForm" property="orgId">
		<fmt:message key="admin.edit"/>
	</logic:notEmpty>
	<logic:empty name="OrganisationForm" property="orgId">
		<fmt:message key="admin.create"/>
	</logic:empty>
	&nbsp;
	<logic:equal name="OrganisationForm" property="typeId" value="2">
		<fmt:message key="admin.course"/>
	</logic:equal>
	<logic:equal name="OrganisationForm" property="typeId" value="3">
		<fmt:message key="admin.class"/>
	</logic:equal>
	&nbsp;
	<fmt:message key="admin.in"/>&nbsp;
	<logic:equal name="OrganisationForm" property="typeId" value="2">
		<fmt:message key="admin.organisation"/>
	</logic:equal>
	<logic:equal name="OrganisationForm" property="typeId" value="3">
		<fmt:message key="admin.course"/>
	</logic:equal>
	: <bean:write name="OrganisationForm" property="parentName"/>
</h2>

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
		<td><fmt:message key="admin.organisation.language"/>:</td>
		<td>
			<html-el:select property="localeId">
				<c:forEach items="${locales}" var="locale">
					<html-el:option value="${locale.localeId}">
						<fmt:message key="locale.language.${locale.languageIsoCode}"/>
						<c:if test="${!empty locale.countryIsoCode}">
							&nbsp;(<fmt:message key="locale.country.${locale.countryIsoCode}"/>)
						</c:if>
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
	<tr>
		<td colspan=2 align="right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
<table>
</html-el:form>
