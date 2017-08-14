<%@ include file="/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<html:errors/>

<html:form action="serversave.do" styleId="ext-server-form" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="sid" />
	
<table class="table table-no-border">
	<tr>
		<td width="25%"><fmt:message key="sysadmin.serverid" />&nbsp;*</td>
		<td><html:text property="serverid" size="20" styleClass="form-control"/></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.serverkey" />&nbsp;*</td>
		<td><html:text property="serverkey" size="30" styleClass="form-control"/></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.servername" />&nbsp;*</td>
		<td><html:text property="servername" size="30" styleClass="form-control"/></td>
	</tr>
	<tr>
		<td valign="top"><fmt:message key="sysadmin.serverdesc" /></td>
		<td><html:textarea property="serverdesc" cols="40" rows="3" styleClass="form-control"/> </td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.prefix" />&nbsp;*</td>
		<td><html:text property="prefix" size="10" styleClass="form-control"/></td>
	</tr>
	
	<tr>
		<td><fmt:message key="sysadmin.disabled" /></td>
		<td><html:checkbox property="disabled" /></td>
	</tr>
	
	<tr>
		<td><fmt:message key="sysadmin.login.request.ttl.enable" /></td>
		<td>
			<html:checkbox property="timeToLiveLoginRequestEnabled" styleId="ttl-login-request-enabled"/>
			<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
			<input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">
		</td>
	</tr>
	
	<tr id="ttl-login-request-wrap" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if>>
		<td><fmt:message key="sysadmin.login.request.ttl" /></td>
		<td><html:text property="timeToLiveLoginRequest" size="10" styleId="ttl-login-request" styleClass="form-control"/></td>
	</tr>

	<tr>
		<td><fmt:message key="sysadmin.userinfoUrl" />&nbsp;*</td>
		<td><html:text property="userinfoUrl" size="70" styleClass="form-control"/></td>
	</tr>
	<tr>
		<td><fmt:message key="sysadmin.lessonFinishUrl" /></td>
		<td><html:text property="lessonFinishUrl" size="70" styleClass="form-control"/></td>
	</tr>
		<tr>
		<td><fmt:message key="sysadmin.extGroupsUrl" /></td>
		<td><html:text property="extGroupsUrl" size="70" styleClass="form-control"/></td>
	</tr>
</table>
<div class="pull-right">
	<html:reset styleClass="btn btn-default"><fmt:message key="admin.reset" /></html:reset>
	<html:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel" /></html:cancel>
	<html:submit property="submitbutton" styleClass="btn btn-primary loffset5"><fmt:message key="admin.save" /></html:submit>
</div>
</html:form>

<a href="sysadminstart.do" class="btn btn-default pull-left"><fmt:message key="sysadmin.maintain" /></a>

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


