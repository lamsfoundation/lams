<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<table width="100%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="3" align="center" valign="middle">
			<img width="10" height="7" src="images/spacer.gif"/>
		</td>
	</tr>
	<tr>
		<td align="center" valign="middle"> 
			<p><img height="300" src="images/customer_logo.gif" width="300" /></p>
		</td>
		<td width="100%">
			&nbsp;
		</td>
		<td width="200" style="{border-left: solid #CCCCCC 1px;}">
			<form action="<%= response.encodeURL("j_security_check") %>" method="post" name="loginForm" id="loginForm">
				<%String failed = request.getParameter("failed");
				if ( failed != null ){%>
					<span class="error">
						Sorry, that username or password is not known. Please try again.
					</span>
				<%}%>
				<table width="150" height="87" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" class="smallText">
							Username
						</td>
						<td colspan="2" align="right">
							<input name="j_username" type="text" class="textField" size="15" />
						</td>
					</tr>
					<tr>
						<td align="left" class="smallText">
							Password
						</td>
						<td colspan="2" align="right">
							<input name="j_password" type="password" class="textField" size="15" />
						</td>
					</tr>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="Login" class="button" onmouseover="changeStyle(this,'buttonover')" onmouseout="changeStyle(this,'button')" />
						</td>
					</tr>
					<tr>
						<td colspan="3" align="center">
							<br><br><br>
						</td>
					</tr>
					<!-- TODO: show "webauth login" only if webauth is enabled -->
					<tr>
						<td colspan="3" align="left" class="smallText">
							<a href="https://array00.melcoe.mq.edu.au/lams/webauth">WebAuth Users: click here</a> 
						</td>
					</tr>
							
				</table>
			</form>
			<img height="1" src="images/spacer.gif" width="200" />
		</td>
	</tr>
	<tr valign="bottom" class="lightNote">
		<td>
			<a href="javascript:alert('LAMS&#8482; &copy; 2002-2005 LAMS Foundation. 
				\nAll rights reserved.
				\n\nLAMS is a trademark of LAMS Foundation.
				\nDistribution of this software is prohibited.');" class="lightNoteLink">&copy; 2002-2005 LAMS Foundation.
			</a>
		</td>
		<td align="center">
			This copy of LAMS&#8482; is authorised for use by the registered users only.
		</td>
		<td align="right"> 
			Version 1.1
		</td>
	</tr>
</table>
