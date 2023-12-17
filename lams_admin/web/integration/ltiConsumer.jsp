<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<c:set var="title"><fmt:message key="label.edit.tool.consumer"/></c:set>
		<title>${title}</title>
		<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

		<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>

		<script type="text/javascript">
			$(document).ready(function(){
				// validate signup form on keyup and submit
				var validator = $("#ltiConsumerForm").validate({
					errorClass: 'text-danger form-text font-italic',
					rules: {
						serverid: "required",
						serverkey: "required",
						servername: "required",
						prefix: "required"
					},
					messages: {
						serverid: "<c:set var="namev"><fmt:message key='sysadmin.serverid' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						serverkey: "<c:set var="namev"><fmt:message key='sysadmin.serverkey' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						servername: "<c:set var="namev"><fmt:message key='sysadmin.servername' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						prefix: "<c:set var="namev"><fmt:message key='sysadmin.prefix' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>"
					}
				});
			});
		</script>
	</lams:head>

	<body class="component pb-4 pt-2 px-2 px-sm-4">
		<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbChild1"><lams:LAMSURL/>admin/ltiConsumerManagement/start.do | <fmt:message key="label.manage.tool.consumers" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="appadmin.maintain.server.edit"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbChild1},${breadcrumbActive}"/>


	<lams:Page5 type="admin" title="${title}" formID="ltiConsumerForm" breadcrumbItems="${breadcrumbItems}">
		<lams:errors path="*"/>
		<form:form action="save.do" id="ltiConsumerForm" modelAttribute="ltiConsumerForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="sid" />

			<div class="row">
				<div class="col-6 offset-3">
					<div class="mb-3">
						<label for="serverid" class="form-label"><fmt:message key="sysadmin.serverkey" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="serverid" maxlength="20" name="serverid" value="${ltiConsumerForm.serverid}" class="form-control" required/>
						<lams:errors path="serverid"/>
					</div>
					<div class="mb-3">
						<label for="serverkey" class="form-label"><fmt:message key="sysadmin.serversecret" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="serverkey" maxlength="32" name="serverkey" value="${ltiConsumerForm.serverkey}" class="form-control" required/>
						<lams:errors path="serverkey"/>
					</div>
					<div class="mb-3">
						<label for="servername" class="form-label"><fmt:message key="sysadmin.servername" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="servername" maxlength="32" name="servername" value="${ltiConsumerForm.servername}" class="form-control" required/>
						<lams:errors path="servername"/>
					</div>
					<div class="mb-3">
						<label for="serverdesc" class="form-label"><fmt:message key="sysadmin.serverdesc" /></label>
						<form:input path="serverdesc" cssClass="form-control"/>
						<lams:errors path="serverdesc"/>
					</div>
					<div class="mb-3">
						<label for="prefix" class="form-label"><fmt:message key="sysadmin.prefix" /></label>&nbsp;<span class="text-danger">*</span>
						<form:input path="prefix" cssClass="form-control" maxlength="20" />
						<lams:errors path="prefix"/>
					</div>
					<div class="mb-3">
						<label for="ltiToolConsumerMonitorRoles" class="form-label"><fmt:message key="sysadmin.lti.consumer.monitor.roles" /></label>:
						<form:input id="ltiToolConsumerMonitorRoles" path="ltiToolConsumerMonitorRoles" cssClass="form-control" />
					</div>
					<div class="mb-3">
						<label for="userIdParameterName" class="form-label"><fmt:message key="sysadmin.user.id.name" /></label>:
						<form:input id="userIdParameterName" path="userIdParameterName" cssClass="form-control" />
					</div>
					<div class="mb-3">
						<label for="lessonFinishUrl" class="form-label"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
						<form:input id="lessonFinishUrl" path="lessonFinishUrl" size="70" cssClass="form-control"/>
					</div>

					<div class="mb-3 mt-3">
						<div class="form-check mb-2">
							<form:checkbox path="disabled" cssClass="form-check-input"/>
							<label class="form-check-label" for="disabled">
								<fmt:message key="sysadmin.disabled" />
							</label>
						</div>
					</div>
					<c:if test="${ltiAdvantageEnabled}">
						<h4 class="mt-3"><fmt:message key="sysadmin.lti.advantage" /></h4>

						<div class="mb-3">
							<label for="defaultCountry" class="form-label"><fmt:message key="admin.user.country" /></label>
							<form:select path="defaultCountry" cssClass="form-select">
								<form:option value="0"><fmt:message key="label.select.country" /></form:option>
								<c:forEach items="${countryCodes}" var="countryCode">
									<form:option value="${countryCode.key}">
										${countryCode.value}
									</form:option>
								</c:forEach>
							</form:select>
							<lams:errors path="defaultCountry"/>
						</div>
						<div class="mb-3">
							<label for="defaultLocaleId"><fmt:message key="admin.organisation.locale" /></label>
							<form:select path="defaultLocaleId" cssClass="form-select">
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>
							</form:select>
							<lams:errors path="defaultLocaleId"/>
						</div>
						<div class="mb-3">
							<label for="defaultTimeZone" class="form-label"><fmt:message key="admin.user.time.zone" /></label>
							<form:select path="defaultTimeZone" cssClass="form-select">
								<c:forEach items="${timezones}" var="timezone">
									<form:option value="${timezone}"><c:out value="${timezone}" /></form:option>
								</c:forEach>
							</form:select>
							<lams:errors path="defaultTimeZone"/>
						</div>
						<div class="form-check mb-2">
							<form:checkbox path="useCoursePrefix" cssClass="form-check-input"/>
							<label class="form-check-label" for="useCoursePrefix">
								<fmt:message key="sysadmin.use.course.prefix" />
							</label>
						</div>
						<div class="form-check mb-2">
							<form:checkbox path="userRegistrationEnabled" cssClass="form-check-input"/>
							<label class="form-check-label" for="userRegistrationEnabled">
								<fmt:message key="sysadmin.user.registration.enabled" />
							</label>
						</div>
						<div class="form-check mb-2">
							<form:checkbox path="userNameLowerCase" cssClass="form-check-input"/>
							<label class="form-check-label" for="userNameLowerCase">
								<fmt:message key="sysadmin.user.name.lower.case" />
							</label>
						</div>
						<div class="form-check mb-2">
							<form:checkbox path="enforceStateCookie" cssClass="form-check-input"/>
							<label class="form-check-label" for="enforceStateCookie">
								<fmt:message key="sysadmin.lti.advantage.enforce.state.cookie" />
							</label>
						</div>
						<div class="form-check mb-2">
							<form:checkbox path="toolReregistrationEnabled" cssClass="form-check-input"/>
							<label class="form-check-label" for="toolReregistrationEnabled">
								<fmt:message key="sysadmin.lti.advantage.tool.reregistration.enabled" />
							</label>
						</div>
						<div class="mb-3">
							<label for="issuer" class="form-label"><fmt:message key="sysadmin.lti.advantage.platform.issuer" /></label>
							<form:input path="issuer" cssClass="form-control"/>
							<lams:errors path="issuer"/>
						</div>
						<div class="mb-3">
							<label for="platformKeySetUrl" class="form-label"><fmt:message key="sysadmin.lti.advantage.platform.keyset.url" /></label>
							<form:input path="platformKeySetUrl" cssClass="form-control"/>
							<lams:errors path="platformKeySetUrl"/>
						</div>
						<div class="mb-3">
							<label for="oidcAuthUrl" class="form-label"><fmt:message key="sysadmin.lti.advantage.platform.oidc.url" /></label>
							<form:input path="oidcAuthUrl" cssClass="form-control"/>
							<lams:errors path="oidcAuthUrl"/>
						</div>
						<div class="mb-3">
							<label for="accessTokenUrl" class="form-label"><fmt:message key="sysadmin.lti.advantage.platform.access.token.url" /></label>
							<form:input path="accessTokenUrl" cssClass="form-control"/>
							<lams:errors path="accessTokenUrl"/>
						</div>
						<div class="mb-3">
							<label for="toolName" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.name" /></label>
							<form:input path="toolName" cssClass="form-control"/>
							<lams:errors path="toolName"/>
						</div>
						<div class="mb-3">
							<label for="toolDescription" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.description" /></label>
							<form:input path="toolDescription" cssClass="form-control"/>
							<lams:errors path="toolDescription"/>
						</div>
						<div class="mb-3">
							<label for="clientId" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.client.id" /></label>
							<form:input path="clientId" cssClass="form-control"/>
							<lams:errors path="clientId"/>
						</div>
						<div class="mb-3">
							<label for="toolKeySetUrl" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.keyset.url" /></label>
							<form:input path="toolKeySetUrl" cssClass="form-control"/>
							<lams:errors path="toolKeySetUrl"/>
						</div>
						<div class="mb-3">
							<label for="toolKeyId" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.key.id" /></label>
							<form:input path="toolKeyId" cssClass="form-control"/>
							<lams:errors path="toolKeyId"/>
						</div>
						<div class="mb-3">
							<label for="publicKey" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.public.key" /></label>
							<form:input path="publicKey" cssClass="form-control"/>
							<lams:errors path="publicKey"/>
						</div>
						<div class="mb-3">
							<label for="privateKey" class="form-label"><fmt:message key="sysadmin.lti.advantage.tool.private.key" /></label>
							<form:input path="privateKey" cssClass="form-control"/>
							<lams:errors path="privateKey"/>
						</div>
					</c:if>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-6 offset-3">
					<%@ include file="extLessonForm.jsp"%>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-6 offset-3 text-end">
					<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do" class="btn btn-secondary"><fmt:message key="admin.cancel"/></a>
					<input type="submit" name="submitbutton" class="btn btn-primary" value="<fmt:message key="admin.save" />" />
				</div>
			</div>
		</form:form>
	</lams:Page5>
	</body>
</lams:html>