<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page session="true" %>
<%@ page import="com.lamsinternational.lams.util.JspRedirectStrategy" %>
<%@ page import="com.lamsinternational.lams.security.Global" %>
<%	

	//FIXME: temporary solution
	if (Global.getServletContext() == null) 
		Global.setServletContext( request.getSession().getServletContext() );

	String failed = request.getParameter("failed");
	if (failed == null)
	{
		if (JspRedirectStrategy.loginPageRedirected(request,response))
			return;
	}
	else
	{
		if (JspRedirectStrategy.errorPageRedirected(request,response))
			return;	
	}
	
	String webAuthUser = (String) session.getAttribute("WEBAUTH_USER");
	if (webAuthUser != null)
		{
		response.sendRedirect("j_security_check?j_username=" + webAuthUser + "&j_password=Dummy");	
		}
			
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<!--
flash is searching for this string, so leave it!:
j_security_login_page
-->
<head>
	<title>Login - LAMS :: Learning Activity Management System</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link href="style.css" rel="stylesheet" type="text/css" />
	<script language="JavaScript" type="text/JavaScript">
	<!--
		function pviiClassNew(obj, new_style) { //v2.7 by PVII
			obj.className=new_style;
		}
	//-->
	</script>
</head>

<body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="95%" height="95%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr> 
					<td>
						<img src="images/spacer.gif"> 
					</td>
				</tr>
				<tr> 
					<td>
						<img width="100%" height="53" src="images/learner_header_right.gif">
					</td>
				</tr>
				<tr bgcolor="#1C8AC6"> 
					<td align="right" >
						<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">Welcome</font>
						<img width="10" src="images/spacer.gif">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr> 
		<td bgcolor="#FFFFFF" width="100%" height="100%">
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
							<%if ( failed != null ){%>
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
										<input type="submit" value="Login" class="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" />
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
		</td>
	</tr>
	<tr> 
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr bgcolor="#282871"> 
					<td width="100%" height="10"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>

</html>
