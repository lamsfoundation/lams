<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.edit.tool.consumer"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
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
					
				<c:set var="errorKey" value="serverid" /> 
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						             <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>
						
				<c:set var="errorKey" value="serverkey" /> 		
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						            <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>
						
				<c:set var="errorKey" value="servername" /> 		
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						            <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>
						
				<c:set var="errorKey" value="prefix" /> 		
						<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="error" items="${errorMap[errorKey]}"> 
						            <c:out value="${error}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>				
				<br />
				
				<form:form action="save.do" id="ltiConsumerForm" modelAttribute="ltiConsumerForm" method="post">
					<form:hidden path="sid" />
					<input type="hidden" name="serverTypeId" value="2">
					<input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">
						
					<table class="table table-no-border">
						<tr>
							<td><fmt:message key="sysadmin.serverkey" />&nbsp;*</td>
							<td><form:input path="serverid" size="20" cssClass="form-control"/></td>
						</tr>
						<tr>
							<td><fmt:message key="sysadmin.serversecret" />&nbsp;*</td>
							<td><form:input path="serverkey" size="30" cssClass="form-control"/></td>
						</tr>
						<tr>
							<td><fmt:message key="sysadmin.servername" />&nbsp;*</td>
							<td><form:input path="servername" size="30" cssClass="form-control"/></td>
						</tr>
						<tr>
							<td valign="top"><fmt:message key="sysadmin.serverdesc" />:</td>
							<td><form:input path="serverdesc" cols="40" rows="3" cssClass="form-control"/></td>
						</tr>
						<tr>
							<td><fmt:message key="sysadmin.prefix" />&nbsp;*</td>
							<td><form:input path="prefix" size="10" cssClass="form-control"/></td>
						</tr>
						
						<tr>
							<td><fmt:message key="sysadmin.disabled" />:</td>
							<td><form:checkbox path="disabled" /></td>
						</tr>
						<tr>
							<td><fmt:message key="sysadmin.lessonFinishUrl" />:</td>
							<td>${lessonFinishUrl}</td>
						</tr>
						<tr>
							<td><fmt:message key="sysadmin.lti.consumer.monitor.roles" />:</td>
							<td><form:input path="ltiToolConsumerMonitorRoles" size="30"/></td>
						</tr>
					</table>
					
					<div class="pull-right">
						<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/start.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
						<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
					</div>
				</form:form>
			</lams:Page>
		
</body>
</lams:html>


