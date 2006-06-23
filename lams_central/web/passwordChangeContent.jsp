<%@page import="org.lamsfoundation.lams.web.PasswordChangeActionForm" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html:form action="/passwordChanged" method="post">
<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
	<tr> 
		<td valign="top">
			<H2><fmt:message key="title.password.change.screen"/></H2>
			<p><html:errors/></p>
			<table>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.username"/>
					</td>
					<td class="body" align="left">
						<html:hidden name="<%=PasswordChangeActionForm.formName%>" property="login"/>
						<bean:write name="<%=PasswordChangeActionForm.formName%>" property="login"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.old.password"/>
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="oldPassword" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.new.password"/>
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="password" size="50" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="body" align="right">
						<fmt:message key="label.password.confirm.new.password"/>
					</td>
					<td class="body" align="left">
						<html:password name="<%=PasswordChangeActionForm.formName%>" property="passwordConfirm" size="50" maxlength="50"/>
					</td>
				</tr>
			</table>
			<p> <html:submit><fmt:message key="button.save"/></html:submit> &nbsp; 	
				<html:cancel><fmt:message key="button.cancel"/></html:cancel></p>
		</td>
	</tr>
</table>
</html:form>
			