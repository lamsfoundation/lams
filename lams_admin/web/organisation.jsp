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
<h4 align="left">
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
</h4>

<div align="center"><html-el:errors/></div>
<table>
	<tr>
		<td><fmt:message key="admin.organisation.name"/>:</td>
		<td><html-el:text property="name" size="20" /> *</td>
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
		<td><fmt:message key="admin.organisation.country"/>:</td>
		<td>
			<html-el:select property="localeCountry">
				<c:forEach items="${countries}" var="country">
					<html-el:option value="${country.isoCode}"><fmt:message key="locale.country.${country.isoCode}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.language"/>:</td>
		<td>
			<html-el:select property="localeLanguage">
				<c:forEach items="${languages}" var="language">
					<html-el:option value="${language.isoCode}"><fmt:message key="locale.language.${language.isoCode}"/></html-el:option>
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
		<td colspan=2 align="right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
<table>
</html-el:form>
