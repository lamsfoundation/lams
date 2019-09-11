<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.server.edit"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
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
    
<body class="stripes">
	
	<c:set var="help"><fmt:message key="Integrations"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="extServerForm">
	
	<p>
		<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default">
			<fmt:message key="sysadmin.maintain" />
		</a>
		
		<a href="<lams:LAMSURL/>admin/extserver/serverlist.do" class="btn btn-default">
			<fmt:message key="sysadmin.maintain.external.servers" />
		</a>
	</p>
	
	<lams:errors/>
			
	<form:form action="serversave.do" id="extServerForm" modelAttribute="extServerForm" method="post">
		<form:hidden path="sid" />
		
		<div class="form-group">
		    <label for="serverid"><strong><fmt:message key="sysadmin.serverid" /> *</strong></label>
		    <form:input path="serverid" size="20" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="serverkey"><strong><fmt:message key="sysadmin.serverkey" /> *</strong></label>
		    <form:input path="serverkey" size="30" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="servername"><strong><fmt:message key="sysadmin.servername" /> *</strong></label>
		    <form:input path="servername" size="30" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="serverdesc"><fmt:message key="sysadmin.serverdesc" /></label>
		    <form:input path="serverdesc" cols="40" rows="3" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="prefix"><strong><fmt:message key="sysadmin.prefix" /> *</strong></label>
		  	<form:input path="prefix" size="10" cssClass="form-control"/>
		</div>
		<div class="checkbox">
		    <label>
		    <form:checkbox path="disabled" /><fmt:message key="sysadmin.disabled" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		    <form:checkbox path="timeToLiveLoginRequestEnabled" id="ttl-login-request-enabled"/><fmt:message key="sysadmin.login.request.ttl.enable" />
		    </label>
		</div>
		<div class="form-group" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if> >
		    <label for="ttl-login-request"><fmt:message key="sysadmin.login.request.ttl" /></label>
		    <form:input path="timeToLiveLoginRequest" size="10" id="ttl-login-request" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="userinfoUrl"><fmt:message key="sysadmin.userinfoUrl" /></label>
		    <form:input path="userinfoUrl" size="70" cssClass="form-control"/>
		</div> 
		<div class="form-group">
		    <label for="lessonFinishUrl"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
		    <form:input path="lessonFinishUrl" size="70" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="extGroupsUrl"><fmt:message key="sysadmin.extGroupsUrl" /></label>
		    <form:input path="extGroupsUrl" size="70" cssClass="form-control"/>
		</div> 
		
		<%@ include file="extLessonForm.jsp"%>

		<div class="pull-right">
			<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
			<a href="<lams:LAMSURL/>admin/extserver/serverlist.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
			<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
		</div>
	</form:form>
</lams:Page>

</body>
</lams:html>