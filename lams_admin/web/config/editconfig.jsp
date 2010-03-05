<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ include file="/taglibs.jsp"%>

<script src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js" type="text/javascript"></script>
<script type="text/javascript">
<!--
	/**
	 * Processes change event on InternalSMTPServer selectbox
	 */
	jQuery(document).ready(function(){
		
		function updateDisableAttributes() {
			var internalSMTPServer = $("input[type='hidden'][value='InternalSMTPServer']").next();
			var isDisabled  = (internalSMTPServer.val() == "true");
			$("input[type='hidden'][value='SMTPPassword']").attr('disabled', isDisabled);
			$("input[type='hidden'][value='SMTPPassword']").next().attr('disabled', isDisabled);
			$("input[type='hidden'][value='SMTPServer']").attr('disabled', isDisabled);
			$("input[type='hidden'][value='SMTPServer']").next().attr('disabled', isDisabled);
			$("input[type='hidden'][value='SMTPUser']").attr('disabled', isDisabled);	
			$("input[type='hidden'][value='SMTPUser']").next().attr('disabled', isDisabled);			
		}
		
		$("input[type='hidden'][value='InternalSMTPServer']").next().change(function() {
			updateDisableAttributes();
		});
		updateDisableAttributes();
		
	});
//-->
</script>


<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="<%= Configuration.CONFIGURATION_HELP_PAGE %>"/>
<h1><fmt:message key="sysadmin.config.settings.edit" /></h1>

<div align="center">
<c:if test="${not empty error}">
	<p class='warning'><c:out value="${error}"/></p>
</c:if>
</div>

<html:form action="/config" method="post">
	<html:hidden property="method" value="save"/>
	
	<tiles:insert attribute="items" />
	
	<p align="center">
		<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
		<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
		<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>