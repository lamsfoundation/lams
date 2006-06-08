<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html:form action="/orgsave.do" method="post">
<html:hidden property="orgId" />
<html:hidden property="parentId" />
<html:hidden property="typeId" />
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
</h4>
<p align="left">
	<fmt:message key="admin.in"/>&nbsp;
	<logic:equal name="OrganisationForm" property="typeId" value="2">
		<fmt:message key="admin.organisation"/>
	</logic:equal>
	<logic:equal name="OrganisationForm" property="typeId" value="3">
		<fmt:message key="admin.course"/>
	</logic:equal>
	:<bean:write name="OrganisationForm" property="parentName"/>
</p>
<div align="left"><font color="red"><html:errors/></font></div>
<table>
	<tr>
		<td><fmt:message key="admin.organisation.name"/>:</td>
		<td><html:text property="name" size="20" /> *</td>
	</tr>
	<tr>
	<td><fmt:message key="admin.organisation.code"/>:</td>
		<td><html:text property="code" size="20" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.description"/>:</td>
		<td><html:textarea property="description" cols="50" rows="3" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.country"/>:</td>
		<td>
			<html:select property="localeCountry">
				<logic:iterate id="country" name="countries">
					<html:option value="<bean:write name='country' property='isoCode'/>"><fmt:message key="locale.country.${country.isoCode}"/></html:option>
				</logic:iterate>
			</html:select>
		</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.language"/>:</td>
		<td>
			<html:select property="localeLanguage">
				<logic:iterate id="language" name="languages">
					<html:option value="<bean:write name='language' property='isoCode'/>"><fmt:message key="locale.language.${language.isoCode}"/></html:option>
				</logic:iterate>	
			</html:select>
		</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.status"/>:</td>
		<td>
			<html:select property="stateId">
				<logic:iterate id="state" name="status">
					<html:option value="<bean:write name='state' property='organisationStateId'/>"><fmt:message key="organisation.state.${state.description}"/></html:option>
				</logic:iterate>
			</html:select>
		</td>
	</tr>
<table>
<p>
<html:submit><fmt:message key="admin.save"/></html:submit>
<html:cancel><fmt:message key="admin.cancel"/></html:cancel>
</p>
</html:form>
