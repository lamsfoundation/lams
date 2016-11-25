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

<h4>
	<a href="sysadminstart.do">
		<fmt:message key="sysadmin.maintain" />
	</a> :
	<a href="ltiConsumerManagement.do">
		<fmt:message key="label.manage.tool.consumers" />
	</a>
</h4>
<lams:help style="no-tabs" page="Integrations"/>

<html:form action="ltiConsumerManagement.do?method=save" styleId="ext-server-form" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	
	<h1>
		<logic:notEmpty name="formBean" property="sid">
			<fmt:message key="label.edit.tool.consumer"/>
		</logic:notEmpty>
			
		<logic:empty name="formBean" property="sid">
			<fmt:message key="label.add.tool.consumer"/>
		</logic:empty>
	</h1>
	
	<html:errors/>
	<br />
	
	<html:hidden property="sid" />
	<input type="hidden" name="serverTypeId" value="2">
	<input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">
		
	<table>
		<tr>
			<td><fmt:message key="sysadmin.serverkey" />:</td>
			<td><html:text property="serverid" size="20"/> *</td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.serversecret" />:</td>
			<td><html:text property="serverkey" size="30"/> *</td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.servername" />:</td>
			<td><html:text property="servername" size="30"/> *</td>
		</tr>
		<tr>
			<td valign="top"><fmt:message key="sysadmin.serverdesc" />:</td>
			<td><html:textarea property="serverdesc" cols="40" rows="3"/> </td>
		</tr>
		<tr>
			<td><fmt:message key="sysadmin.prefix" />:</td>
			<td><html:text property="prefix" size="10"/> *</td>
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
	
	<div align="center">
		<html:submit property="submitbutton" styleClass="button">
			<fmt:message key="admin.save" />
		</html:submit>
		<html:cancel styleClass="button cancel">
			<fmt:message key="admin.cancel" />
		</html:cancel>
	</div>
</html:form>
