<%@page import="org.lamsfoundation.lams.web.PasswordChangeActionForm" %>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html:form action="/passwordChanged" method="post">
<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
	<tr> 
		<td valign="top">
			<H2><fmt:message key="title.password.change.screen"/></H2>
			<logic:messagesPresent message="true"> 
				<p class="warning">
				<html:messages message="true" id="errMsg" >
					<bean:write name="errMsg"/><BR>
				</html:messages>
				</p>
			</logic:messagesPresent>
			<table>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.username"/>:
					</td>
					<td class="body" align="left">
						<html:hidden name="<%=PasswordChangeActionForm.formName%>" property="login"/>
						<bean:write name="<%=PasswordChangeActionForm.formName%>" property="login"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.old.password"/>:
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="oldPassword" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.new.password"/>:
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="password" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.confirm.new.password"/>:
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="passwordConfirm" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<html:submit><fmt:message key="button.save"/></html:submit> &nbsp; 	
						<html:cancel><fmt:message key="button.cancel"/></html:cancel>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</html:form>
			