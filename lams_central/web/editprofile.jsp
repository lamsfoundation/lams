<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<h2><fmt:message key="title.profile.edit.screen"/></h2>

<html:form action="/saveprofile.do" method="post">
<html:hidden property="userId" />
<html:hidden property="login" />
<html:hidden property="password" />

<logic:messagesPresent> 
	<p class="warning"><html:errors/></p>
</logic:messagesPresent>
<table>
    <tr>
		<td align="right"><fmt:message key="label.username"/>:</td>
		<td><bean:write name="UserForm" property="login" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.title"/>:</td>
		<td><html:text property="title" size="20" maxlength="32" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.first_name"/> *:</td>
		<td><html:text property="firstName" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.last_name"/> *:</td>
		<td><html:text property="lastName" size="20" maxlength="128" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.email"/> *:</td>
		<td><html:text property="email" size="20" maxlength="128" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.address_line_1"/>:</td>
		<td><html:text property="addressLine1" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.address_line_2"/>:</td>
		<td><html:text property="addressLine2" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.address_line_3"/>:</td>
		<td><html:text property="addressLine3" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.city"/>:</td>
		<td><html:text property="city" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.state"/>:</td>
		<td><html:text property="state" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.postcode"/>:</td>
		<td><html:text property="postcode" size="20" maxlength="10" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.country"/>:</td>
		<td><html:text property="country" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.day_phone"/>:</td>
		<td><html:text property="dayPhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.evening_phone"/>:</td>
		<td><html:text property="eveningPhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.mobile_phone"/>:</td>
		<td><html:text property="mobilePhone" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.fax"/>:</td>
		<td><html:text property="fax" size="20" maxlength="64" /></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.language"/>:</td>
		<td>
			<html:select property="localeId">
				<c:forEach items="${locales}" var="locale">
					<html:option value="${locale.localeId}">
						<c:out value="${locale.description}" />
					</html:option>
				</c:forEach>	
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan=2 align="right">
			<html:submit><fmt:message key="button.save"/></html:submit>
			<html:reset><fmt:message key="button.reset"/></html:reset>
			<html:cancel><fmt:message key="button.cancel"/></html:cancel>
		</td>
	</tr>
</table>
</html:form>