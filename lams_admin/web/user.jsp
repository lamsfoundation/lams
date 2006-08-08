<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html-el:form action="/usersave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h2 align="left">
    <logic:notEmpty name="pOrgId">
        <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a> ::
    </logic:notEmpty>
    <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="orgId" />">
      <bean:write name="orgName"/></a> ::
	<logic:notEmpty name="UserForm" property="userId">
		<fmt:message key="admin.edit"/> User
	</logic:notEmpty>
	<logic:empty name="UserForm" property="userId">
		<fmt:message key="admin.create"/> User
	</logic:empty>
</h2>

<div align="center"><html-el:errors/></div>
<table>
  <col align="right" /><col align="left" />
  <logic:equal name="canEdit" value="true">
	<tr>
		<td><fmt:message key="admin.user.login"/> *:</td>
		<td><html-el:text property="login" size="20" maxlength="20" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.password"/> *:</td>
		<td><html-el:password property="password" size="20" maxlength="50" /></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.user.password.confirm"/> *:</td>
		<td><html-el:password property="password2" size="20" maxlength="50" /></td>
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
			<html-el:select property="localeId">
				<c:forEach items="${locales}" var="locale">
					<html-el:option value="${locale.localeId}">
						<c:out value="${locale.description}" />
					</html-el:option>
				</c:forEach>	
			</html-el:select>
		</td>
	</tr>
  </logic:equal>
  <logic:notEqual name="canEdit" value="true">
    <tr>
		<td><fmt:message key="admin.user.login"/>:</td>
		<td><bean:write name="UserForm" property="login" /></td>
	</tr>
	<tr>
	<td>Name:</td>
		<td><bean:write name="UserForm" property="title" /> <bean:write name="UserForm" property="firstName" /> <bean:write name="UserForm" property="lastName" /></td>
	</tr>
	<html-el:hidden property="login" />
	<html-el:hidden property="password" />
	<html-el:hidden property="password2" />
	<html-el:hidden property="title" />
	<html-el:hidden property="firstName" />
	<html-el:hidden property="lastName" />
	<html-el:hidden property="addressLine1" />
	<html-el:hidden property="addressLine2" />
	<html-el:hidden property="addressLine3" />
	<html-el:hidden property="city" />
	<html-el:hidden property="state" />
	<html-el:hidden property="country" />
	<html-el:hidden property="dayPhone" />
	<html-el:hidden property="eveningPhone" />
	<html-el:hidden property="mobilePhone" />
	<html-el:hidden property="fax" />
	<html-el:hidden property="email" />
	<html-el:hidden property="localeId" />
  </logic:notEqual>
	<tr>
	    <td><fmt:message key="admin.user.roles"/>:</td>
	    <td>
            <c:forEach items="${rolelist}" var="role">
                <html-el:multibox name="UserForm" property="roles" value="${role.roleId}"/>
                <c:out value="${role.name}"/><br/>
            </c:forEach>
	    </td>
	</tr>
	<tr>
		<td colspan=2 align="right">
			<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
			<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
			<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
</table>
</html-el:form>
