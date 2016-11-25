<%@ include file="/taglibs.jsp"%>
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
		var validator = $("#ext-server-form").validate({
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

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
		<fmt:message key="sysadmin.maintain" />
	</a>
	
	<a href="<lams:LAMSURL/>/admin/ltiConsumerManagement.do" class="btn btn-default">
		<fmt:message key="label.manage.tool.consumers" />
	</a>
</p>
	
<html:errors/>
<br />

<html:form action="ltiConsumerManagement.do?method=save" styleId="ext-server-form" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	
	<html:hidden property="sid" />
	<input type="hidden" name="serverTypeId" value="2">
	<input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">
		
	<table class="table table-no-border">
		<tr>
			<td><fmt:message key="sysadmin.serverkey" />&nbsp;*</td>
			<td><html:text property="serverid" size="20" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.serversecret" />&nbsp;*</td>
			<td><html:text property="serverkey" size="30" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.servername" />&nbsp;*</td>
			<td><html:text property="servername" size="30" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td valign="top"><fmt:message key="sysadmin.serverdesc" />:</td>
			<td><html:textarea property="serverdesc" cols="40" rows="3" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.prefix" />&nbsp;*</td>
			<td><html:text property="prefix" size="10" styleClass="form-control"/></td>
		</tr>
		
		<tr>
			<td><fmt:message key="sysadmin.disabled" />:</td>
			<td><html:checkbox property="disabled" /></td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.lessonFinishUrl" />:</td>
			<td>${lessonFinishUrl}</td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.lti.consumer.monitor.roles" />:</td>
			<td><html:text property="ltiToolConsumerMonitorRoles" size="30"/></td>
		</tr>
	</table>
	
	<div class="pull-right">
		<html:cancel styleClass="btn btn-default">
			<fmt:message key="admin.cancel" />
		</html:cancel>
		<html:submit property="submitbutton" styleClass="btn btn-primary loffset5">
			<fmt:message key="admin.save" />
		</html:submit>
	</div>
</html:form>
