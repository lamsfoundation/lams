<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="config.jsp" %>
<%@ page import="org.apache.axis.client.Call" %>
<%@ page import="org.apache.axis.client.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.lamsfoundation.lams.util.HashUtil" %>
<%@ page import="org.lamsfoundation.lams.util.Emailer" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>



<% 
	String userID = request.getParameter("openid_url");
	String timestamp = "" + new Date().getTime();
	String method = "register";
	String plaintext = timestamp + userID + method + LAMS_SERVER_ID + LAMS_SERVER_KEY;
	String hash = HashUtil.sha1(plaintext);
	String courseID = (ADD_TO_GROUP) ? GROUP_ID : "";
	String loginRequestURL = LAMS_SERVER_URL + "/LoginRequest";
%>
 
<html lang="en_au">
	<head>
		<title>Register - LAMS :: Learning Activity Management System</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<link href="<%=LAMS_SERVER_URL %>/css/defaultHTML.css" rel="stylesheet" type="text/css" />
		<link rel="icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
		<script src="<%=LAMS_SERVER_URL %>/includes/javascript/jquery-latest.pack.js" type="text/javascript"></script>
		<script src="<%=LAMS_SERVER_URL %>/includes/javascript/jquery.validate.pack.js" type="text/javascript"></script>
		
		<script type="text/javascript">
		<!--
			
			$().ready(function() {
				// validate signup form on keyup and submit
				$("#details").validate({
					rules: {
						username: "required",
						password: "required",
						password2: {
							required: true,
							equalTo: "#password"
						},
						firstname: "required",
						lastname: "required",
						email: {
							required: true,
							email: true
						}
					},
					messages: {
						username: "<font color='red'><i> Username is required</i></font>",
						password: "<font color='red'><i> Please provide a password</i></font>",
						password2: {
							required: "<font color='red'><i> Please provide a password</i></font>",
							equalTo: "<font color='red'><i> Passwords do not match</i></font>"
						},
						firstname: "<font color='red'><i> First Name is required</i></font>",
						lastname: "<font color='red'><i> Last Name is required</i></font>",
						email: "<font color='red'><i> Please enter a valid email address</i></font>"
					}
				});
			});
		//-->
		</script>
	</head>
	<body class = stripes>
		<div id="login-page"><!--main box 'page'-->
			<h1 class="no-tabs-below">&nbsp;</h1>
		
			<div id="login-header">
			</div><!--closes header-->
	
			<div id="login-content">	
				<div style="padding-left:40px">	
					<h1><img src="<%=LAMS_SERVER_URL %>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" /></h1>
					<br />	
					
					<h1>Sign up to <%=LAMS_SERVER_NAME %> using OpenID user</h1>
					<br />
					
					<p>
						Just by completing this form, you can register and test LAMS as Teacher, Monitor and Learner.&nbsp;&nbsp;
						You'll be assigned to a Course where you can create sequences, assign them to your students, monitor their progress 
						and participate in them as well.&nbsp;&nbsp;New to LAMS? No worries, have a look at 
						<a href="http://wiki.lamsfoundation.org/display/lamsdocs/LAMS+2.0+Tutorials" target="_blank">these animations</a>.
					</p>
					<p>
						If you already have an account, please proceed to the <a href="http://demo.lamscommunity.org/lams/">login page</a>.
					</p>
					
					<form name="details" action="<%=loginRequestURL %>" method="get" id="details">
						<input type="hidden" name="ts" value="<%=timestamp %>" />
						<input type="hidden" name="sid" value="<%=LAMS_SERVER_ID %>" />
						<input type="hidden" name="hash" value="<%=hash %>" />
						<input type="hidden" name="courseid" value="<%=courseID %>" />
						<input type="hidden" name="country" value="<%=GROUP_COUNTRY_ISO_CODE %>" />
						<input type="hidden" name="lang" value="<%=GROUP_LANG_ISO_CODE %>" />
						<input type="hidden" name="method" value=<%=method %> />
						<input type="hidden" name="requestSrc" value="" />
						<input type="hidden" name="notifyCloseURL" value="" />
						<input type="hidden" name="lsid" value="" />

						<table border="0" cellspacing="1" cellpadding="1" align="left">
						
							<tr>
								<td align="right" width="40%">OpenID URL:</td>
								<td align="left" width="60%"><input name="uid" readonly id="uid" type="text" maxlength="255" value="<%=userID %>" ></td>
							</tr>
							<tr>
								<td align="right">First name *:</td>
								<td align="left"><input name="firstName" id="firstName" type="text" maxlength="255" /></td>
						    </tr>
							<tr>
								<td align="right">Last name *:</td>
								<td align="left"><input name="lastName" id="lastName" type="text" maxlength="255" /></td>
						    </tr>
							<tr>
						       	<td align="right">Email *:</td>
								<td align="left"><input name="email" type="text" id="email" maxlength="128"/></td>
						    </tr>
							<tr>
								<td colspan="2" align="center"><input type="submit" value="Register and Login"/></td>
							</tr>
						</table>
					</form>

					<br />
				</div>
				<div class="clear"></div>
			</div>  <!--closes login-content-->
	
			<div id="footer">
				<p>&copy; 2002-2007 LAMS Foundation.</p>
	  		</div><!--closes footer-->
		
		</div><!--closes page-->
	</body>
</html>