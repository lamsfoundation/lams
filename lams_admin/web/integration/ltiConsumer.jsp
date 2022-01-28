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
		<p>
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default">
				<fmt:message key="sysadmin.maintain" />
			</a>
					
			<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do" class="btn btn-default">
				<fmt:message key="label.manage.tool.consumers" />
			</a>
		</p>

		<lams:errors path="*"/>
		<form:form action="save.do" id="ltiConsumerForm" modelAttribute="ltiConsumerForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="sid" />
						
			<table class="table table-no-border">
				<tr>
					<td><fmt:message key="sysadmin.serverkey" />&nbsp;*</td>
					<td>
						<lams:errors path="serverid"/>
						<form:input path="serverid" size="20" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.serversecret" />&nbsp;*</td>
					<td>
						<lams:errors path="serverkey"/>
						<form:input path="serverkey" size="30" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.servername" />&nbsp;*</td>
					<td>
						<lams:errors path="servername"/>
						<form:input path="servername" size="30" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td valign="top"><fmt:message key="sysadmin.serverdesc" />:</td>
					<td>
						<form:input path="serverdesc" cols="40" rows="3" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.prefix" />&nbsp;*</td>
					<td>
						<lams:errors path="prefix"/>
						<form:input path="prefix" size="10" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.disabled" />:</td>
					<td>
						<form:checkbox path="disabled"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lessonFinishUrl" />:</td>
					<td>
						<form:input path="lessonFinishUrl" cssClass="form-control" />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.consumer.monitor.roles" />:</td>
					<td>
						<form:input path="ltiToolConsumerMonitorRoles" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.user.id.name" />:</td>
					<td>
						<form:input path="userIdParameterName" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<h3><fmt:message key="sysadmin.lti.advantage" /></h3>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.use.course.prefix" />:</td>
					<td>
						<form:checkbox path="useCoursePrefix"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.user.registration.enabled" />:</td>
					<td>
						<form:checkbox path="userRegistrationEnabled"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.user.name.lower.case" />:</td>
					<td>
						<form:checkbox path="userNameLowerCase"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.enforce.state.cookie" />:</td>
					<td>
						<form:checkbox path="enforceStateCookie"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.reregistration.enabled" />:</td>
					<td>
						<form:checkbox path="toolReregistrationEnabled"  />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.platform.issuer" />:</td>
					<td>
						<form:input path="issuer" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.platform.keyset.url" />:</td>
					<td>
						<form:input path="platformKeySetUrl" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.platform.oidc.url" />:</td>
					<td>
						<form:input path="oidcAuthUrl" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.platform.access.token.url" />:</td>
					<td>
						<form:input path="accessTokenUrl" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.name" />:</td>
					<td>
						<form:input path="toolName" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.description" />:</td>
					<td>
						<form:input path="toolDescription" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.client.id" />:</td>
					<td>
						<form:input path="clientId" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.keyset.url" />:</td>
					<td>
						<form:input path="toolKeySetUrl" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.key.id" />:</td>
					<td>
						<form:input path="toolKeyId" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.public.key" />:</td>
					<td>
						<form:input path="publicKey" cssClass="form-control"/>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="sysadmin.lti.advantage.tool.private.key" />:</td>
					<td>
						<form:input path="privateKey" cssClass="form-control"/>
					</td>
				</tr>
			</table>
				
			<%@ include file="extLessonForm.jsp"%>
					
			<div class="pull-right">
				<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
				<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
			</div>
			
		</form:form>
	</lams:Page>		
</body>
</lams:html>
