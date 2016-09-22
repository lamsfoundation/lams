<%@ include file="/taglibs.jsp"%>
<style media="screen,projection" type="text/css">
	table td {
  		padding-bottom: 7px;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<h4>
	<a href="sysadminstart.do">
		<fmt:message key="sysadmin.maintain" />
	</a> :
	<a href="serverlist.do">
		<fmt:message key="sysadmin.maintain.external.servers" />
	</a>
</h4>
<lams:help style="no-tabs" page="Integrations"/>
<h1>
	<fmt:message key="sysadmin.maintain.server.edit"/>
</h1>

<html:errors/>
<br />
<html:form action="serversave.do" styleId="ext-server-form" method="post">

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<html:hidden property="sid" />
<input type="hidden" name="serverTypeId" value="1">
	
<table>
	<tr>
		<td><fmt:message key="sysadmin.serverid" />:</td>
		<td><html:text property="serverid" size="20"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.serverkey" />:</td>
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
		<td><fmt:message key="sysadmin.login.request.ttl.enable" />:</td>
		<td>
			<html:checkbox property="timeToLiveLoginRequestEnabled" styleId="ttl-login-request-enabled"/>
			<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
			<input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">
		</td>
	</tr>
	
	<tr id="ttl-login-request-wrap" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if>>
		<td><fmt:message key="sysadmin.login.request.ttl" />:</td>
		<td><html:text property="timeToLiveLoginRequest" size="10" styleId="ttl-login-request"/></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.userinfoUrl" />:</td>
		<td><html:text property="userinfoUrl" size="70"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.serverUrl" />:</td>
		<td><html:text property="serverUrl" size="70"/></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.timeoutUrl" />:</td>
		<td><html:text property="timeoutUrl" size="70"/> *</td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.lessonFinishUrl" />:</td>
		<td><html:text property="lessonFinishUrl" size="70"/></td>
	</tr>
		<tr>
		<td><fmt:message key="sysadmin.extGroupsUrl" />:</td>
		<td><html:text property="extGroupsUrl" size="70"/></td>
	</tr>
</table>

<div align="center">
	<html:submit property="submitbutton" styleClass="button">
		<fmt:message key="admin.save" />
	</html:submit>
	<html:reset styleClass="button cancel">
		<fmt:message key="admin.reset" />
	</html:reset>
	<html:cancel styleClass="button cancel">
		<fmt:message key="admin.cancel" />
	</html:cancel>
</div>
</html:form>

<script type="text/javascript">	
	$(document).ready(function(){
		$("#ttl-login-request-enabled").click(function(){
			$('#ttl-login-request-wrap').toggle('slow');
			if ($("#ttl-login-request-enabled").is(':checked')) {
				$('#ttl-login-request').prop("value", 80);
			}
		});

		// validate signup form on keyup and submit
		var validator = $("#ext-server-form").validate({
			rules: {
				serverid: "required", 
				serverkey: "required",
				servername: "required",
				prefix: "required",
				userinfoUrl: "required",
				timeoutUrl: "required",
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
				timeoutUrl: "<c:set var="namev"><fmt:message key='sysadmin.timeoutUrl' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
				timeToLiveLoginRequest: {
					required: "<c:set var="namev"><fmt:message key='sysadmin.login.request.ttl' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
					min: "<fmt:message key="error.login.request.ttl.negative" />"
				}
			}
		});
	});
</script>
