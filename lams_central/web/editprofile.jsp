<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<html:form action="/saveprofile.do" method="post">
<html:hidden property="userId" />
<html:hidden property="login" />
<html:hidden property="password" />

<logic:messagesPresent> 
	<p class="warning"><html:errors/></p>
</logic:messagesPresent>

<c:set var="profileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_EDIT_ENABLE)%></c:set>
<c:set var="partialProfileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)%></c:set>

<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="title.profile.edit.screen"/></h2>

<div class="shading-bg">

<table>

<c:set var="authenticationMethodId"><lams:user property="authenticationMethodId"/></c:set>
<c:set var="dbId"><%= AuthenticationMethod.DB %></c:set>
<c:set var="lamsCommunityToken"><bean:write name="UserForm" property="lamsCommunityToken" /></c:set>
<c:set var="lamsCommunityUsername"><bean:write name="UserForm" property="lamsCommunityUsername" /></c:set>
<c:if test="${authenticationMethodId eq dbId}">	

    <tr>
		<td class="align-right" width="50%"><fmt:message key="label.username"/>:</td>
		<td width="50%"><bean:write name="UserForm" property="login" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.title"/>:</td>
		<td><html:text property="title" size="32" maxlength="32" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.first_name"/> *:</td>
		<td><html:text property="firstName" size="50" maxlength="128" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.last_name"/> *:</td>
		<td><html:text property="lastName" size="50" maxlength="128" disabled="${!profileEditEnabled}" /></td>
	</tr>
	
	<%-- 
		Disabled properties are not submitted, but first and last name are required.
		These hidden fields deliver required data to ProfileSaveAction even if only Partial Profile Editing is available.
	--%>
	<c:if test="${!profileEditEnabled}">
		<html:hidden property="firstName" />
		<html:hidden property="lastName" />
	</c:if>
	
	<tr>
		<td class="align-right"><fmt:message key="label.email"/> *:</td>
		<td><html:text property="email" size="50" maxlength="128" disabled="${!profileEditEnabled and !partialProfileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_1"/>:</td>
		<td><html:text property="addressLine1" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_2"/>:</td>
		<td><html:text property="addressLine2" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_3"/>:</td>
		<td><html:text property="addressLine3" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.city"/>:</td>
		<td><html:text property="city" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.state"/>:</td>
		<td><html:text property="state" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.postcode"/>:</td>
		<td><html:text property="postcode" size="10" maxlength="10" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.country"/>:</td>
		<td><html:text property="country" size="50" maxlength="64" disabled="${!profileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.day_phone"/>:</td>
		<td><html:text property="dayPhone" size="50" maxlength="64" disabled="${!profileEditEnabled and !partialProfileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.evening_phone"/>:</td>
		<td><html:text property="eveningPhone" size="50" maxlength="64" disabled="${!profileEditEnabled and !partialProfileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.mobile_phone"/>:</td>
		<td><html:text property="mobilePhone" size="50" maxlength="64" disabled="${!profileEditEnabled and !partialProfileEditEnabled}" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.fax"/>:</td>
		<td><html:text property="fax" size="50" maxlength="64" disabled="${!profileEditEnabled and !partialProfileEditEnabled}" /></td>
	</tr>
		
	<tr>
		<td class="align-right"><fmt:message key="label.language"/>:</td>
		<td>
			<html:select property="localeId" disabled="${!profileEditEnabled}" >
				<c:forEach items="${locales}" var="locale">
					<html:option value="${locale.localeId}">
						<c:out value="${locale.description}" />
					</html:option>
				</c:forEach>	
			</html:select>
		</td>
	</tr>
		<tr>
		<td class="align-right"><fmt:message key="label.timezone.title"/>:</td>
		<td>
			<html:select property="timeZone" disabled="${!profileEditEnabled}" >
				<c:forEach items="${timezoneDtos}" var="timezoneDto">
					<html:option value="${timezoneDto.timeZoneId}">
						${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
					</html:option>
				</c:forEach>	
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.tutorial.enabled"/>:</td>
		<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
		<td><html:select property="tutorialsDisabled" disabled="${!profileEditEnabled}" >
			<html:option value="false"><fmt:message key="label.yes"/></html:option>
			<html:option value="true"><fmt:message key="label.no"/></html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.theme"/>:</td>
		<td>
		<html:select property="userTheme" disabled="${!profileEditEnabled}" >
			<c:forEach items="${themes}" var="theme">	
				<html:option value="${theme.themeId}">${theme.name}</html:option>
			</c:forEach>
		</html:select>
		</td>
	</tr>
	
	<c:if test="${not empty sharedSecret}">
		<tr>
			<td class="align-right"><fmt:message key="label.2FA.shared.secret"/>:</td>
			<td>
				${sharedSecret}
			</td>
		</tr>
	</c:if>	
	
	<c:if test="${not empty lamsCommunityToken}">
		<tr>
			<td class="align-right">
				<fmt:message key="label.lamscommunity.changeuser">
					<fmt:param value="${lamsCommunityUsername}" />
				</fmt:message>
			:</td> 
			<td>
				<html:checkbox property="disableLamsCommunityUsername" disabled="${!profileEditEnabled}" ></html:checkbox>
			</td>
		</tr>
	</c:if>
</c:if>
<br />
<c:if test="${authenticationMethodId != dbId}">	

	<html:hidden property="title" />
	<html:hidden property="firstName" />
	<html:hidden property="lastName" />
	<html:hidden property="email" />
	<html:hidden property="addressLine1" />
	<html:hidden property="addressLine2" />
	<html:hidden property="addressLine3" />
	<html:hidden property="city" />
	<html:hidden property="state" />
	<html:hidden property="postcode" />
	<html:hidden property="country" />
	<html:hidden property="dayPhone" />
	<html:hidden property="eveningPhone" />
	<html:hidden property="mobilePhone" />
	<html:hidden property="fax" />

    <tr>
		<td class="align-right"><fmt:message key="label.username"/>:</td>
		<td><bean:write name="UserForm" property="login" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.title"/>:</td>
		<td><bean:write name="UserForm" property="title" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.first_name"/> *:</td>
		<td><bean:write name="UserForm" property="firstName" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.last_name"/> *:</td>
		<td><bean:write name="UserForm" property="lastName" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.email"/> *:</td>
		<td><bean:write name="UserForm" property="email" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_1"/>:</td>
		<td><bean:write name="UserForm" property="addressLine1" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_2"/>:</td>
		<td><bean:write name="UserForm" property="addressLine2" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.address_line_3"/>:</td>
		<td><bean:write name="UserForm" property="addressLine3" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.city"/>:</td>
		<td><bean:write name="UserForm" property="city" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.state"/>:</td>
		<td><bean:write name="UserForm" property="state" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.postcode"/>:</td>
		<td><bean:write name="UserForm" property="postcode" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.country"/>:</td>
		<td><bean:write name="UserForm" property="country" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.day_phone"/>:</td>
		<td><bean:write name="UserForm" property="dayPhone" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.evening_phone"/>:</td>
		<td><bean:write name="UserForm" property="eveningPhone" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.mobile_phone"/>:</td>
		<td><bean:write name="UserForm" property="mobilePhone" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.fax"/>:</td>
		<td><bean:write name="UserForm" property="fax" /></td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.theme"/>:</td>
		<td>
		<html:select property="userTheme" disabled="${!profileEditEnabled}" >
			<c:forEach items="${themes}" var="theme">	
				<html:option value="${theme.themeId}">${theme.name}</html:option>
			</c:forEach>
		</html:select>
		</td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.language"/>:</td>
		<td>
			<c:set var="localeId"><bean:write name="UserForm" property="localeId" /></c:set>
			<c:forEach items="${locales}" var="locale">
				<c:if test="${locale.localeId eq localeId}">
					<c:out value="${locale.description}" />
				</c:if>
			</c:forEach>
		</td>
	</tr>

		<tr>
		<td class="align-right"><fmt:message key="label.timezone.title"/>:</td>
		<td>
			<c:set var="timeZone"><bean:write name="UserForm" property="timeZone" /></c:set>
			${timeZone}"
		</td>
	</tr>
	<tr>
		<td class="align-right"><fmt:message key="label.tutorial.enabled"/>:<bean:write name="UserForm" property="tutorialsDisabled" /></td>
		<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
		<td><html:select property="tutorialsDisabled" disabled="${!profileEditEnabled}" >
			<html:option value="false"><fmt:message key="label.yes"/></html:option>
			<html:option value="true"><fmt:message key="label.no"/></html:option>
			</html:select>
		</td>
	</tr>
</c:if>
	
	</table>
			</div>
			
			
			<div class="space-top" align="right">
			<html:cancel styleClass="button"><fmt:message key="button.cancel"/></html:cancel>
			<c:if test="${profileEditEnabled or partialProfileEditEnabled}">
			<html:submit styleClass="button"><fmt:message key="button.save"/></html:submit>
			</c:if>
			
			</div>




</html:form>