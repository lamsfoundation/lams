<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.server.edit"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<script type="text/javascript">	
		$(document).ready(function(){
			$("#ttl-login-request-enabled").click(function(){
				$('#ttl-login-request-wrap').toggle('slow');
				if ($("#ttl-login-request-enabled").is(':checked')) {
					$('#ttl-login-request').prop("value", 80);
				}
			});
	
			// validate signup form on keyup and submit
			var validator = $("#extServerForm").validate({
				rules: {
					serverid: "required", 
					serverkey: "required",
					servername: "required",
					prefix: "required",
					userinfoUrl: "required",
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
					userinfoUrl: "<c:set var="namev"><fmt:message key='sysadmin.userinfoUrl' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
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
				<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
				
				<c:set var="errorKey" value="${requiredField}" /> 
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						             <fmt:message key="sysadmin.${requiredField}" /> <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>
						
				<c:set var="errorKey" value="${uniqueField}" /> 		
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						            <fmt:message key="sysadmin.${uniqueField}" /> <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>
								
				<form:form action="../serversave.do" id="extServerForm" modelAttribute="extServerForm" method="post">
					<form:hidden path="sid" />
					
				<table class="table table-no-border">
					<tr>
						<td width="25%"><fmt:message key="sysadmin.serverid" />&nbsp;*</td>
						<td><form:input path="serverid" size="20" cssClass="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="sysadmin.serverkey" />&nbsp;*</td>
						<td><form:input path="serverkey" size="30" cssClass="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="sysadmin.servername" />&nbsp;*</td>
						<td><form:input path="servername" size="30" cssClass="form-control"/></td>
					</tr>
					<tr>
						<td valign="top"><fmt:message key="sysadmin.serverdesc" /></td>
						<td><form:input path="serverdesc" cols="40" rows="3" cssClass="form-control"/> </td>
					</tr>
					<tr>
						<td><fmt:message key="sysadmin.prefix" />&nbsp;*</td>
						<td><form:input path="prefix" size="10" cssClass="form-control"/></td>
					</tr>
					
					<tr>
						<td><fmt:message key="sysadmin.disabled" /></td>
						<td><form:checkbox path="disabled" /></td>
					</tr>
					
					<tr>
						<td><fmt:message key="sysadmin.login.request.ttl.enable" /></td>
						<td>
							<form:checkbox path="timeToLiveLoginRequestEnabled" id="ttl-login-request-enabled"/>
							<form:hidden path="timeToLiveLoginRequestEnabled" value="false"/>
						</td>
					</tr>
					<tr id="ttl-login-request-wrap" <c:if test="${!extServerForm.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if>>
						<td><fmt:message key="sysadmin.login.request.ttl" /></td>
						<td><form:input path="timeToLiveLoginRequest" size="10" id="ttl-login-request" cssClass="form-control"/></td>
					</tr>
				
					<tr>
						<td><fmt:message key="sysadmin.userinfoUrl" />&nbsp;*</td>
						<td><form:input path="userinfoUrl" size="70" cssClass="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="sysadmin.lessonFinishUrl" /></td>
						<td><form:input path="lessonFinishUrl" size="70" cssClass="form-control"/></td>
					</tr>
						<tr>
						<td><fmt:message key="sysadmin.extGroupsUrl" /></td>
						<td><form:input path="extGroupsUrl" size="70" cssClass="form-control"/></td>
					</tr>
				</table>
				<div class="pull-right">
					<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
					<a href="<lams:LAMSURL/>admin/serverlist.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
					<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
				</div>
				</form:form>
				
				<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default pull-left"><fmt:message key="sysadmin.maintain" /></a>
			</lams:Page>

</body>
</lams:html>






