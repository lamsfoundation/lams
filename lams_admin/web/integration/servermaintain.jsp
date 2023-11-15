<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<lams:html>
	<lams:head>
		<c:set var="title"><fmt:message key="appadmin.maintain.server.edit"/></c:set>
		<title>${title}</title>
		<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />


		<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#ttl-login-request-enabled").click(function(){
					$('#ttl-login-request-wrap').toggle('slow');
					if ($("#ttl-login-request-enabled").is(':checked')) {
						$('#ttl-login-request').prop("value", 80);
					}
				});

				$('#learnerPresenceAvailable').change(function(){
					$('#learnerImAvailable').prop('disabled', !$(this).is(':checked'));
				}).change();

				// validate signup form on keyup and submit
				var validator = $("#extServerForm").validate({
					errorClass: 'text-danger form-text font-italic',
					rules: {
						serverid: "required",
						serverkey: "required",
						servername: "required",
						prefix: "required",
						timeToLiveLoginRequest: {
							required: true,
							min: 1
						}
					},
					messages: {
						serverid: "<c:set var="namev"><fmt:message key='sysadmin.serverid' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						serverkey: "<c:set var="namev"><fmt:message key='sysadmin.serverkey' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						servername: "<c:set var="namev"><fmt:message key='sysadmin.servername' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						prefix: "<c:set var="namev"><fmt:message key='sysadmin.prefix' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
						timeToLiveLoginRequest: {
							required: "<c:set var="namev"><fmt:message key='sysadmin.login.request.ttl' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
							min: "<fmt:message key="error.login.request.ttl.negative" />"
						}
					}
				});
			});
		</script>

	</lams:head>

	<body class="component pb-4 pt-2 px-2 px-sm-4">

	<c:set var="help"><fmt:message key="Integrations"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
		<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbChild1"><lams:LAMSURL/>admin/extserver/serverlist.do| <fmt:message key="appadmin.maintain.external.servers" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="appadmin.maintain.server.edit"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbChild1},${breadcrumbActive}"/>


	<lams:Page5 type="admin" title="${title}" titleHelpURL="${help}" formID="extServerForm" breadcrumbItems="${breadcrumbItems}">


		<lams:errors/>
		<form:form action="serversave.do" id="extServerForm" modelAttribute="extServerForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="sid" />

			<div class="row">
				<div class="col-6 offset-3">
					<div class="mb-3">
						<label for="serverid" class="form-label"><fmt:message key="sysadmin.serverid" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="serverid" name="serverid" value="${extServerForm.serverid}" class="form-control" maxlength="20" required/>
					</div>
					<div class="mb-3">
						<label for="serverkey" class="form-label"><fmt:message key="sysadmin.serverkey" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="serverkey" name="serverkey" value="${extServerForm.serverkey} " class="form-control" maxlength="32" required/>
					</div>
					<div class="mb-3">
						<label for="servername" class="form-label"><fmt:message key="sysadmin.servername" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="servername" name="servername" value="${extServerForm.servername}"  class="form-control" maxlength="32" required/>
					</div>
					<div class="mb-3">
						<label for="serverdesc" class="form-label"><fmt:message key="sysadmin.serverdesc" /></label>
						<form:input path="serverdesc" cols="40" rows="3" cssClass="form-control"/>
					</div>
					<div class="mb-3">
						<label for="prefix" class="form-label"><fmt:message key="sysadmin.prefix" /></label>&nbsp;<span class="text-danger">*</span>
						<input id="prefix" name="prefix" class="form-control" value="${extServerForm.prefix}" required maxlength="10"/>
					</div>
					<div class="form-check mb-2">
						<form:checkbox id="disabled" path="disabled" name="disabled" cssClass="form-check-input"/>
						<label class="form-check-label" for="disabled">
							<fmt:message key="sysadmin.disabled" />
						</label>
					</div>
					<div class="form-check mb-2">
						<form:checkbox path="timeToLiveLoginRequestEnabled" id="ttl-login-request-enabled" cssClass="form-check-input"/>
						<label class="form-check-label" for="ttl-login-request-enabled">
							<fmt:message key="sysadmin.login.request.ttl.enable" />
						</label>
					</div>
					<div class="mb-3" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if> >
						<label for="ttl-login-request" class="form-label"><fmt:message key="sysadmin.login.request.ttl" /></label>
						<form:input path="timeToLiveLoginRequest" size="10" id="ttl-login-request" maxlength="120" cssClass="form-control"/>
					</div>
					<div class="form-check mb-2">
						<form:checkbox path="addStaffToAllLessons" cssClass="form-check-input"/>
						<label class="form-check-label" for="addStaffToAllLessons">
							<fmt:message key="sysadmin.add.staff.to.all.lessons" />
						</label>
					</div>
					<div class="mb-3">
						<label for="userinfoUrl" class="form-label"><fmt:message key="sysadmin.userinfoUrl" /></label>
						<form:input path="userinfoUrl" size="70" cssClass="form-control"/>
					</div>
					<div class="mb-3">
						<label for="lessonFinishUrl" class="form-label"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
						<form:input path="lessonFinishUrl" size="70" cssClass="form-control"/>
					</div>
					<div class="mb-3">
						<label for="extGroupsUrl" class="form-label"><fmt:message key="sysadmin.extGroupsUrl" /></label>
						<form:input path="extGroupsUrl" size="70" cssClass="form-control"/>
					</div>
					<div class="mb-3">
						<label for="logoutUrl" class="form-label"><fmt:message key="sysadmin.logoutUrl" /></label>
						<form:input path="logoutUrl" size="70" cssClass="form-control"/>
					</div>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-6 offset-3">
					<%@ include file="extLessonForm.jsp"%>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-6 offset-3 text-end">
					<a href="<lams:LAMSURL/>admin/extserver/serverlist.do" class="btn btn-secondary"><fmt:message key="admin.cancel"/></a>
					<input type="submit" name="submitbutton" class="btn btn-primary" value="<fmt:message key="admin.save" />" />
				</div>
			</div>

		</form:form>
	</lams:Page5>

	</body>
</lams:html>