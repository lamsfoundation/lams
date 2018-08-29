<%@ include file="/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<h4><spam class="label label-danger"><html:errors/></spam></h4>

<html:form action="serversave.do" styleId="ext-server-form" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="sid" />

<div class="form-group">
    <label for="serverid"><strong><fmt:message key="sysadmin.serverid" /> *</strong></label>
    <html:text styleId="serverid" property="serverid" styleClass="form-control"/>
</div>
<div class="form-group">
    <label for="serverkey"><strong><fmt:message key="sysadmin.serverkey" /> *</strong></label>
    <html:text styleId="serverkey" property="serverkey" styleClass="form-control"/>
</div>
<div class="form-group">
    <label for="servername"><strong><fmt:message key="sysadmin.servername" /> *</strong></label>
    <html:text styleId="serverkey" property="servername"  styleClass="form-control"/>
</div>
<div class="form-group">
    <label for="serverdesc"><fmt:message key="sysadmin.serverdesc" /></label>
    <html:textarea styleId="serverdesc" property="serverdesc" cols="40" rows="3" styleClass="form-control"/>
</div>
<div class="form-group">
    <label for="prefix"><strong><fmt:message key="sysadmin.prefix" /> *</strong></label>
    <html:text styleId="prefix" property="prefix" styleClass="form-control"/>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="disabled" /><fmt:message key="sysadmin.disabled" />
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="timeToLiveLoginRequestEnabled" styleId="ttl-login-request-enabled"/><fmt:message key="sysadmin.login.request.ttl.enable" />
    <!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
    <input type="hidden" name="timeToLiveLoginRequestEnabled" value="false">        
    </label>
</div>
<div class="form-group" <c:if test="${!formBean.map.timeToLiveLoginRequestEnabled}">style="display:none;"</c:if> >
    <label for="ttl-login-request"><fmt:message key="sysadmin.login.request.ttl" /></label>
    <html:text property="timeToLiveLoginRequest" styleId="ttl-login-request" styleClass="form-control"/>
</div>
<div class="form-group">
    <label for="userinfoUrl"><fmt:message key="sysadmin.userinfoUrl" /></label>
    <html:text styleId="userinfoUrl" property="userinfoUrl" styleClass="form-control"/>
</div> 
<div class="form-group">
    <label for="lessonFinishUrl"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
    <html:text styleId="lessonFinishUrl" property="lessonFinishUrl" styleClass="form-control"/>
</div> 
<div class="form-group">
    <label for="lessonFinishUrl"><fmt:message key="sysadmin.lessonFinishUrl" /></label>
    <html:text styleId="lessonFinishUrl" property="lessonFinishUrl" styleClass="form-control"/>
</div> 
<div class="form-group">
    <label for="extGroupsUrl"><fmt:message key="sysadmin.extGroupsUrl" /></label>
    <html:text property="extGroupsUrl" styleId="extGroupsUrl" styleClass="form-control"/>
</div> 

<h3><fmt:message key="sysadmin.lesson.default" /></h3>
<div class="checkbox">
    <label>
    <html:checkbox property="gradebookOnComplete" /><fmt:message key="sysadmin.lesson.gradebook.complete" />
    <input type="hidden" name="gradebookOnComplete" value="false">        
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="forceLearnerRestart" /><fmt:message key="sysadmin.lesson.force.restart" />
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="allowLearnerRestart" /><fmt:message key="sysadmin.lesson.allow.restart" />
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="liveEditEnabled" /><fmt:message key="sysadmin.lesson.liveedit" />
    <input type="hidden" name="liveEditEnabled" value="false">        
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="enableLessonNotifications" /><fmt:message key="sysadmin.lesson.notification" />
    <input type="hidden" name="enableLessonNotifications" value="false">        
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="learnerPresenceAvailable" styleId="learnerPresenceAvailable" /><fmt:message key="sysadmin.lesson.presence" />
    </label>
</div>
<div class="checkbox">
    <label>
    <html:checkbox property="learnerImAvailable" styleId="learnerImAvailable" /><fmt:message key="sysadmin.lesson.im" />
    </label>
</div>
<div class="pull-right voffset20">
	<html:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel" /></html:cancel>
	<html:submit property="submitbutton" styleClass="btn btn-primary loffset5"><fmt:message key="admin.save" /></html:submit>
</div>
</html:form>

<a href="sysadminstart.do" class="btn btn-default pull-left voffset20"><fmt:message key="sysadmin.maintain" /></a>

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
		var validator = $("#ext-server-form").validate({
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


