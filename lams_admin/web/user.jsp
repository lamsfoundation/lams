<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html-el:form action="/usersave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h4 align="left">
	<logic:notEmpty name="UserForm" property="userId">
		<fmt:message key="admin.edit"/> User
	</logic:notEmpty>
	<logic:empty name="UserForm" property="userId">
		<fmt:message key="admin.create"/> User
	</logic:empty>
</h4>

<div align="center"><html-el:errors/></div>
<table>
    <col align="right" /><col align="left" />
	<tr>
		<td><fmt:message key="admin.user.login"/> *:</td>
		<td><html-el:text property="login" size="20" maxlength="20" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.password"/> *:</td>
		<td><html-el:text property="password" size="20" maxlength="50" /></td>
	</tr>
	<tr>
	<td><fmt:message key="admin.user.title"/>:</td>
		<td><html-el:text property="title" size="20" maxlength="32" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.first_name"/>:</td>
		<td><html-el:text property="firstName" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.last_name"/>:</td>
		<td><html-el:text property="lastName" size="20" maxlength="128" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.address_line_1"/>:</td>
		<td><html-el:text property="addressLine1" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.address_line_2"/>:</td>
		<td><html-el:text property="addressLine2" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.address_line_3"/>:</td>
		<td><html-el:text property="addressLine3" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.city"/>:</td>
		<td><html-el:text property="city" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.state"/>:</td>
		<td><html-el:text property="state" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.country"/>:</td>
		<td><html-el:text property="country" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.day_phone"/>:</td>
		<td><html-el:text property="dayPhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.evening_phone"/>:</td>
		<td><html-el:text property="eveningPhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.mobile_phone"/>:</td>
		<td><html-el:text property="mobilePhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.fax"/>:</td>
		<td><html-el:text property="fax" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.email"/>:</td>
		<td><html-el:text property="email" size="20" maxlength="128" /></td>
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
		<td><fmt:message key="admin.organisation.country"/>:</td>
		<td>
			<html-el:select property="localeCountry">
				<c:forEach items="${countries}" var="country">
					<html-el:option value="${country.isoCode}"><fmt:message key="locale.country.${country.isoCode}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>
	<logic:notEmpty name="UserForm" property="userId">
	    <tr>
	        <td><fmt:message key="admin.user.roles"/>:</td>
	        <td>
                <input type="checkbox" name="learner" <c:if test="${userRoles.learner}">checked</c:if> /><fmt:message key="admin.user.learner" /><br />
                <input type="checkbox" name="author" <c:if test="${userRoles.author}">checked</c:if> /><fmt:message key="admin.user.author" /><br />
                <input type="checkbox" name="staff" <c:if test="${userRoles.staff}">checked</c:if> /><fmt:message key="admin.user.staff" /><br />
                <input type="checkbox" name="admin" <c:if test="${userRoles.admin}">checked</c:if> /><fmt:message key="admin.user.admin" /><br />
                <input type="checkbox" name="manager" <c:if test="${userRoles.manager}">checked</c:if> /><fmt:message key="admin.user.manager" />
	        </td>
	    </tr>
	</logic:notEmpty>
	<tr>
		<td colspan=2 align="right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
<table>
</html-el:form>
