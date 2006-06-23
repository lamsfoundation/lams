<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<script>
	function encryptPassword(){
		  var password=document.loginForm.j_password.value;	 
		  document.loginForm.j_password.value=hex_sha1(password);
		  return true;
	}
</script>
<table width="100%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="3" align="center" valign="middle">
			<img width="10" height="7" src="images/spacer.gif" alt="spacer.gif"/>
		</td>
	</tr>
	<tr>
		<td align="center" valign="middle"> 
			<p><img height="300" src="images/customer_logo.gif" width="300" alt="customer_logo.gif"/></p>
		</td>
		<td width="100%">
			&nbsp;
		</td>
		<td width="200" style="{border-left: solid #CCCCCC 1px;}">
			<form action="<%= response.encodeURL("j_security_check") %>" method="post" name="loginForm" id="loginForm" onsubmit="encryptPassword()">
				<%String failed = request.getParameter("failed");
				if ( failed != null ){%>
					<span class="error">
						<fmt:message key="error.login"/>
					</span>
				<%}%>
				<table width="150" height="87" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" class="smallText">
							<fmt:message key="label.username"/>
						</td>
						<td colspan="2" align="right">
							<input name="j_username" type="text" class="textField" size="15" />
						</td>
					</tr>
					<tr>	
						<td align="left" class="smallText">
							<fmt:message key="label.password"/>
						</td>
						<td colspan="2" align="right">
							<input name="j_password" type="password" class="textField" size="15" AUTOCOMPLETE="off"/>
						</td>
					</tr>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<fmt:message key="button.login"/>" class="button" onmouseover="changeStyle(this,'buttonover')" onmouseout="changeStyle(this,'button')" />
						</td>
					</tr>
					<tr>
						<td colspan="3" align="center">
							<br><br><br>
						</td>
					</tr>
					<!-- TODO: show "webauth login" only if webauth is enabled -->
					<!--
					<tr>
						<td colspan="3" align="left" class="smallText">
							<a href="https://array00.melcoe.mq.edu.au/lams/webauth">WebAuth Users: click here</a> 
						</td>
					</tr>
					-->		
				</table>
			</form>
			<img height="1" src="images/spacer.gif" width="200" alt="spacer.gif"/>
		</td>
	</tr>
	<tr valign="bottom" class="lightNote">
		<td>
			<a href="javascript:alert('<fmt:message key="msg.LAMS.copyright.long"/>');" class="lightNoteLink">&copy; <fmt:message key="msg.LAMS.copyright.short"/>
			</a>
		</td>
		<td align="right"> 
			<fmt:message key="msg.LAMS.version"/>
		</td>
	</tr>
</table>
