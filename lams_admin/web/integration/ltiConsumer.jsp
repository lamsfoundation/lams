<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.edit.tool.consumer"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
		table td {
	  		padding-bottom: 7px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
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
    
<body class="stripes">
	<lams:Page type="admin" title="${title}" formID="ltiConsumerForm">
	
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do"><fmt:message key="label.manage.tool.consumers" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="sysadmin.maintain.server.edit"/></li>
		  </ol>
		</nav>

	
		<lams:errors path="*"/>
		<form:form action="save.do" id="ltiConsumerForm" modelAttribute="ltiConsumerForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="sid" />
						
		<div class="form-group">
		    <label for="serverid"><fmt:message key="sysadmin.serverkey" /></label>&nbsp;<span class="text-danger">*</span>
		    <input id="serverid" maxlength="20" name="serverid" value="${ltiConsumerForm.serverid}" class="form-control form-control-sm" required/>
			<lams:errors path="serverid"/>
			
			<label for="serverkey"><fmt:message key="sysadmin.serversecret" /></label>&nbsp;<span class="text-danger">*</span>
			<input id="serverkey" maxlength="20" name="serverkey" value="${ltiConsumerForm.serverkey}" class="form-control form-control-sm" required/>
			<lams:errors path="serverkey"/>

			<label for="servername"><fmt:message key="sysadmin.servername" /></label>&nbsp;<span class="text-danger">*</span>
			<input id="servername" maxlength="20" name="servername" value="${ltiConsumerForm.servername}" class="form-control form-control-sm" required/>
			<lams:errors path="servername"/>
			
			<label for="serverdesc"><fmt:message key="sysadmin.serverdesc" /></label>&nbsp;<span class="text-danger">*</span>
			<input id="serverdesc" maxlength="20" name="serverdesc" value="${ltiConsumerForm.serverdesc}" class="form-control form-control-sm"/>
			<lams:errors path="servername"/>

			<label for="prefix"><fmt:message key="sysadmin.prefix" /></label>&nbsp;<span class="text-danger">*</span>
			<input id="prefix" maxlength="10" name="prefix" value="${ltiConsumerForm.prefix}" class="form-control form-control-sm"/>
			<lams:errors path="servername"/>
		</div>
		<div class="form-group">
			<div class="form-check">
				<form:checkbox id="disabled" path="disabled" name="disabled" cssClass="form-check-input"/>
		    	<label class="form-check-label" for="disabled">
		    		<fmt:message key="sysadmin.disabled" />
		    	</label>
		    </div>
		</div>
		<div class="form-group">
			<label for="lessonFinishUrl"><fmt:message key="sysadmin.lessonFinishUrl" /></label>:
			<form:hidden id="lessonFinishUrl" path="lessonFinishUrl"/>
			<c:out value="${ltiConsumerForm.lessonFinishUrl}"/>
		</div>

		<div class="form-group">
			<label for="ltiToolConsumerMonitorRoles"><fmt:message key="sysadmin.lti.consumer.monitor.roles" /></label>:
			<form:input id="ltiToolConsumerMonitorRoles" path="ltiToolConsumerMonitorRoles" size="30"/>
		</div>

		<div class="form-group">
			<div class="form-check">
				<form:checkbox id="useAlternativeUseridParameterName" name="useAlternativeUseridParameterName" path="useAlternativeUseridParameterName" cssClass="form-check-input"/>
		    	<label class="form-check-label" for="disabled">
		    		<fmt:message key="sysadmin.alternative.user.id.name" />
		    	</label>
			</div>
		</div>	
				
			<%@ include file="extLessonForm.jsp"%>
			
			<hr>
					
			<div class="pull-right">
				<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do" class="btn btn-outline-secondary btn-sm"><fmt:message key="admin.cancel"/></a>
				<input type="submit" name="submitbutton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="admin.save" />" />
			</div>
			
		</form:form>
	</lams:Page>		
</body>
</lams:html>
