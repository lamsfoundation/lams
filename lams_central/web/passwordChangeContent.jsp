<%@page import="org.lamsfoundation.lams.web.PasswordChangeActionForm" %>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form name="PasswordChangeActionForm" id="change-password" method="post" action="/lams/passwordChanged.do">

<div style="clear:both"></div>


<h2 class="small-space-top"><fmt:message key="title.password.change.screen"/></h2>

	
			
<div class="shading-bg">


			<logic:messagesPresent message="true"> 
				<p class="warning">
				<html:messages message="true" id="errMsg" >
					<bean:write name="errMsg"/><br>
				</html:messages>
				</p>
			</logic:messagesPresent>
			
		
			<table class="body">
				<tr>
					<td class="align-right" width="50%">
						<fmt:message key="label.username"/>:
					</td>
					<td class="align-left" width="50%">
						<html:hidden name="<%=PasswordChangeActionForm.formName%>" property="login"/>
						<bean:write name="<%=PasswordChangeActionForm.formName%>" property="login"/>
					</td>
				</tr>
				<tr>
					<td class="align-right">
						<fmt:message key="label.password.old.password"/>:
					</td>
					<td class="align-left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="oldPassword" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="align-right">
						<fmt:message key="label.password.new.password"/>:
					</td>
					<td class="align-left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="password" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="align-right">
						<fmt:message key="label.password.confirm.new.password"/>:
					</td>
					<td class="align-left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="passwordConfirm" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				
					<td>
						<html:submit styleClass="button"><fmt:message key="button.save"/></html:submit>  	
						<html:cancel  styleClass="button"><fmt:message key="button.cancel"/></html:cancel>
					</td>
				</tr>
			</table>

</form>
			