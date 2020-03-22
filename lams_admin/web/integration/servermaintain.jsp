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
    
<body class="stripes">
	
	<c:set var="help"><fmt:message key="Integrations"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="extServerForm">
	
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/extserver/serverlist.do"><fmt:message key="sysadmin.maintain.external.servers" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="sysadmin.maintain.server.edit"/></li>
		  </ol>
		</nav>
	
	<lams:errors/>
    <form:form action="serversave.do" id="extServerForm" modelAttribute="extServerForm" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="sid" />
		
		<div class="form-group">
		    <label for="serverid"><fmt:message key="sysadmin.serverid" /></label>&nbsp;<span class="text-danger">*</span>
		    <input id="serverid" name="serverid" value="${extServerForm.serverid}" class="form-control form-control-sm" maxlength="20" required/>
		    <label for="serverkey"><fmt:message key="sysadmin.serverkey" /></label>&nbsp;<span class="text-danger">*</span>
		    <input id="serverkey" name="serverkey" value="${extServerForm.serverkey} " class="form-control form-control-sm" maxlength="20" required/>
		</div>
		<div class="form-group">
		    <label for="servername"><fmt:message key="sysadmin.servername" /></label>&nbsp;<span class="text-danger">*</span>
		    <input id="servername" name="servername" value="${extServerForm.servername}"  class="form-control form-control-sm" maxlength="20" required/>
		    <label for="serverdesc"><fmt:message key="sysadmin.serverdesc" /></label>
		    <form:input path="serverdesc" cols="40" rows="3" cssClass="form-control form-control-sm"/>
		</div>
		<div class="form-group">
		    <label for="prefix"><fmt:message key="sysadmin.prefix" /></label>&nbsp;<span class="text-danger">*</span>
		    <input id="prefix" name="prefix" class="form-control form-control-sm" value="${extServerForm.prefix}" required maxlength="10"/>
		</div>
		<div class="form-group">
			<div class="form-check">
				<form:checkbox id="disabled" path="disabled" name="disabled" cssClass="form-check-input"/>
		    	<label class="form-check-label" for="disabled">
		    		<fmt:message key="sysadmin.disabled" />
		    	</label>
		    </div>
			<div class="form-check">
			    <form:checkbox path="timeToLiveLoginRequestEnabled" id="ttl-login-request-enabled" cssClass="form-check-input"/>
			    <label class="form-check-label" for="ttl-login-request-enabled">
			    	<fmt:message key="sysadmin.login.request.ttl.enable" />
			    </label>
			</div>
		</div>
		<div class="form-group" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if> >
		    <label for="ttl-login-request"><fmt:message key="sysadmin.login.request.ttl" /></label>
		    <form:input path="timeToLiveLoginRequest" size="10" id="ttl-login-request" maxlength="120" cssClass="form-control"/>
		</div>
		<div class="form-group">
		    <label for="userinfoUrl"><fmt:message key="sysadmin.userinfoUrl" /></label>
		    <form:input path="userinfoUrl" size="70" cssClass="form-control form-control-sm"/>
		</div> 
		<div class="form-group">
		    <label for="lessonFinishUrl"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
		    <form:input path="lessonFinishUrl" size="70" cssClass="form-control form-control-sm"/>
		</div>
		<div class="form-group">
		    <label for="extGroupsUrl"><fmt:message key="sysadmin.extGroupsUrl" /></label>
		    <form:input path="extGroupsUrl" size="70" cssClass="form-control form-control-sm"/>
		</div>
		<div class="form-group">
		    <label for="logoutUrl"><fmt:message key="sysadmin.logoutUrl" /></label>
		    <form:input path="logoutUrl" size="70" cssClass="form-control form-control-sm"/>
		</div>
		
		<%@ include file="extLessonForm.jsp"%>
		<hr>
		<div class="pull-right">
			<a href="<lams:LAMSURL/>admin/extserver/serverlist.do" class="btn btn-outline-secondary btn-sm"><fmt:message key="admin.cancel"/></a>
			<input type="submit" name="submitbutton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="admin.save" />" />
		</div>
	</form:form>
	
</lams:Page>

</body>
</lams:html>
