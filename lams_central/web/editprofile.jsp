<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod"
	import="org.lamsfoundation.lams.util.Configuration"
	import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="css/defaultHTML_learner.css" type="text/css" />
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript">
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.profile.edit.screen" />', '100%');
		});
	</script>
</lams:head>

<body>
<html:form action="/saveprofile.do" method="post">
	<html:hidden property="userId" />
	<html:hidden property="login" />
	<html:hidden property="password" />

	<logic:messagesPresent>
		<p class="warning">
			<html:errors />
		</p>
	</logic:messagesPresent>

	<c:set var="profileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_EDIT_ENABLE)%></c:set>
	<c:set var="partialProfileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)%></c:set>
	<div style="clear: both;"></div>
	<div class="container">

		<div class="row vertical-center-row">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel">
					<div class="panel-body">
						<form class="form-horizontal">
							<c:set var="authenticationMethodId">
								<lams:user property="authenticationMethodId" />
							</c:set>
							<c:set var="dbId"><%=AuthenticationMethod.DB%></c:set>
							<c:set var="lamsCommunityToken">
								<bean:write name="UserForm" property="lamsCommunityToken" />
							</c:set>
							<c:set var="lamsCommunityUsername">
								<bean:write name="UserForm" property="lamsCommunityUsername" />
							</c:set>
							<c:if test="${authenticationMethodId eq dbId}">

								<div class="form-group">
									<b><label><fmt:message key="label.username" /></label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="login" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.title" />:</label></b>
									<html:text property="title" size="32" maxlength="32"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.first_name" /> *:</label></b>
									<html:text property="firstName" size="50" maxlength="128"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.last_name" /> *:</label></b>
									<html:text property="lastName" size="50" maxlength="128"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<c:if test="${!profileEditEnabled}">
									<html:hidden property="firstName" />
									<html:hidden property="lastName" />
								</c:if>
								<div class="form-group">
									<b><label><fmt:message key="label.email" /> *:</label></b>
									<html:text property="email" size="50" maxlength="128"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.address_line_1" />:</label></b>
									<html:text property="addressLine1" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.address_line_2" />:</label></b>
									<html:text property="addressLine2" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.address_line_3" />:</label></b>
									<html:text property="addressLine3" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.city" />:</label></b>
									<html:text property="city" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.state" />:</label></b>
									<html:text property="state" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.postcode" />:</label></b>
									<html:text property="postcode" size="10" maxlength="10"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.country" />:</label></b>
									<html:text property="country" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.day_phone" />:</label></b>
									<html:text property="dayPhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.evening_phone" />:</label></b>
									<html:text property="eveningPhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										styleClass="form-control" />
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.mobile_phone" />:</label></b>
									<html:text property="mobilePhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										styleClass="form-control" />
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.fax" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="fax" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.language" />:</label></b>
									<html:select property="localeId"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<html:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</html:option>
										</c:forEach>
									</html:select>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.timezone.title" />:</label></b>
									<html:select property="timeZone"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<html:option value="${timezoneDto.timeZoneId}">
						${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
					</html:option>
										</c:forEach>
									</html:select>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.tutorial.enabled" />:</label></b>
									<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
									<html:select property="tutorialsDisabled"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<html:option value="false">
											<fmt:message key="label.yes" />
										</html:option>
										<html:option value="true">
											<fmt:message key="label.no" />
										</html:option>
									</html:select>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.theme" />:</label></b>
									<html:select property="userTheme"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<html:option value="${theme.themeId}">${theme.name}</html:option>
										</c:forEach>
									</html:select>
								</div>
								<c:if test="${not empty sharedSecret}">
									<div class="form-group">
										<b><label><fmt:message
													key="label.2FA.shared.secret" />:</label></b> ${sharedSecret}
									</div>
								</c:if>
								<c:if test="${not empty lamsCommunityToken}">
									<div class="form-group">
										<b><label> <fmt:message
													key="label.lamscommunity.changeuser">
													<fmt:param value="${lamsCommunityUsername}" />
												</fmt:message>
										</label></b>
										<html:checkbox
												property="disableLamsCommunityUsername"
												disabled="${!profileEditEnabled}" styleClass="form-control"></html:checkbox>
										
									</div>
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

								<div class="form-group">
									<b><label><fmt:message key="label.username" /></label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="login" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.username" /></label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="login" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.title" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="title" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.first_name" /> *:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="firstName" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.last_name" /> *:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="lastName" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.email" /> *:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="email" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.address_line_1" />:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="addressLine1" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.address_line_2" />:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="addressLine2" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.address_line_3" />:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="addressLine3" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.city" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="city" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.state" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="state" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.postcode" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="postcode" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.country" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="country" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.day_phone" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="dayPhone" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.evening_phone" />:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="eveningPhone" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.mobile_phone" />:</label></b>
									<input type="text" class="form-control"
										value=<bean:write name="UserForm" property="mobilePhone" />>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.fax" />:</label></b> <input
										type="text" class="form-control"
										value=<bean:write name="UserForm" property="fax" />>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.theme" />:</label></b>
									<html:select property="userTheme"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<html:option value="${theme.themeId}">${theme.name}</html:option>
										</c:forEach>
									</html:select>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.language" />:</label></b>
									<html:select property="localeId"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<html:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</html:option>
										</c:forEach>
									</html:select>
								</div>

								<div class="form-group">
									<b><label><fmt:message key="label.timezone.title" />:</label></b>
									<html:select property="timeZone"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<html:option value="${timezoneDto.timeZoneId}">
						${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
					</html:option>
										</c:forEach>
									</html:select>
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.timezone.title" />:</label></b>
									<c:set var="timeZone">
										<input type="text" class="form-control"
											value=<bean:write name="UserForm" property="timeZone" />>
									</c:set>
									${timeZone}
								</div>
								<div class="form-group">
									<b><label><fmt:message key="label.tutorial.enabled" />:<bean:write
												name="UserForm" property="tutorialsDisabled" /></label></b>
									<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
									<html:select property="tutorialsDisabled"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<html:option value="false">
											<fmt:message key="label.yes" />
										</html:option>
										<html:option value="true">
											<fmt:message key="label.no" />
										</html:option>
									</html:select>
								</div>

							</c:if>

							<div class="form-group" align="right">
								<html:cancel styleClass="btn btn-sm btn-default voffset5">
									<fmt:message key="button.cancel" />
								</html:cancel>&nbsp;&nbsp;
								<c:if test="${profileEditEnabled or partialProfileEditEnabled}">
									<html:submit styleClass="btn btn-sm btn-default voffset5">
										<fmt:message key="button.save" />
									</html:submit>
								</c:if>

							</div>


						</form>
					</div>

				</div>
			</div>
		</div>
	</div>
</html:form>
</body>
</lams:html>

