<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="config.jsp" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="org.verisign.joid.consumer.OpenIdFilter" %>
<%@ page import="org.verisign.joid.util.UrlUtils" %>
<%@ page import="org.lamsfoundation.lams.util.HashUtil" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.net.URLEncoder" %>
<c:set var="encrypt"><%= Configuration.getAsBoolean(ConfigurationKeys.LDAP_ENCRYPT_PASSWORD_FROM_BROWSER) %></c:set>
<%
	boolean blacklisted = true;
	boolean error = false;
	String errorMessage = "";
	String ret = request.getParameter("return");
	String userID = request.getParameter("openid_url");
	String loggedInAs = OpenIdFilter.getCurrentUser(session);
	String timestamp = "" + new Date().getTime();
	String method = "none";
	String plaintext = timestamp + loggedInAs + method + LAMS_SERVER_ID + LAMS_SERVER_KEY;
	String hash = HashUtil.sha1(plaintext);
	String courseID = (ADD_TO_GROUP) ? GROUP_ID : "";
	String courseName = (ADD_TO_GROUP) ? GROUP_NAME : "";
	
	String returnTo = UrlUtils.getBaseUrl(request) + "?return=yes";
	try {
		if (request.getParameter("signin") != null) {
		
			if (!userID.startsWith("http:")) {
			    userID = "http://" + userID;
			}
			
			String provider = userID.substring(userID.indexOf(".") +1,  userID.length());
			for(int i=0; i<TRUSTED_OPENID_PROVIDERS.length; i++)
			{
			    if(provider.equals(TRUSTED_OPENID_PROVIDERS[i]))
				{
					blacklisted = false;
				}
			}
			
			if (!blacklisted){
				String trustRoot = returnTo;
				String s = OpenIdFilter.joid().getAuthUrl(userID, returnTo, trustRoot);
				response.sendRedirect(s);
			}
		}
	} catch (Throwable e) {
		e.printStackTrace();
		error = true;
		errorMessage = "OpenID login attempt failed, the OpenID URL you used may be invalid, please try again.";
	}
		
%>

<html>
	<head>
		<title>OpenID Login - LAMS :: Learning Activity Management System</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<link href="<%=LAMS_SERVER_URL %>/css/defaultHTML.css" rel="stylesheet" type="text/css" />
		<link rel="icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
	
		<script language="javascript" type="text/javascript" src="<%=LAMS_SERVER_URL %>/includes/javascript/AC_OETags.js"></script>
		<script language="javascript" type="text/javascript" src="<%=LAMS_SERVER_URL %>/includes/javascript/browser_detect.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=LAMS_SERVER_URL %>/includes/javascript/sha1.js"></script>
		<script type="text/javascript">
			var provider = {"aol":["http://openid.aol.com/username",22],
				"blogger":["http://username.blogspot.com/",7],
				"wordpress":["http://username.wordpress.com/",7],
				"technorati":["http://technorati.com/people/technorati/username",40],
				"vox":["http://username.vox.com/",7],
				"claimid":["http://claimid.com/username",19],
				"myopenid":["http://username.myopenid.com/",7],
				"vidoop":["http://username.myvidoop.com/",7],
				 "verisign":["http://username.pip.verisignlabs.com/",7]
				}
			function change_provider(prdname){
				var prd = provider[prdname]
			      
				var field = document.getElementById("openid_url");
				field.value = prd[0];
			      
				var start = prd[1];
				var end = prd[1]+8;
				if(field.setSelectionRange) {
			    	field.setSelectionRange(start,end);
			    }
				else if (field.createTextRange) {
					var range = field.createTextRange();
					range.collapse(true);
				 	range.moveEnd('character', end);
				 	range.moveStart('character', start);
					range.select();
				}
			    field.focus();  
			}
			  
			function submitForm(){
				var password=document.loginForm.j_password.value;
				<c:if test="${encrypt eq 'true'}">
				document.loginForm.j_password.value=hex_sha1(password);
				</c:if>
				document.loginForm.submit();
			}
	
			function onEnter(event){
				intKeyCode = event.keyCode;
				if (intKeyCode == 13) {
					submitForm();
				}
			}
		</script>
	</head>
	
	<body class="stripes">
		<br />
		<br />
		<br />
		<div id="login-page"><!--main box 'page'-->
			<h1 class="no-tabs-below">&nbsp;</h1>
		
			<div id="login-header">
			</div><!--closes header-->
	
			<div id="login-content">
				<%
							if(request.getParameter("signin") != null && blacklisted == true && error == false){
				%>
					<p class="warning" style="text-align: left; margin-top: 0px;">
						Sorry, that OpenID provider is not part of the trusted list. Please try one of the following: <br />
						<%
							for(int i=0; i<TRUSTED_OPENID_PROVIDERS.length; i++)
							{
							    out.print("&nbsp;&nbsp;&nbsp;&nbsp;" + TRUSTED_OPENID_PROVIDERS[i] + "<br />");
							}
						%>
					</p>
					
				<%
					} else if (error == true) {
				%>
					<p class="warning" style="text-align: left; margin-top: 0px;">
						<%=errorMessage %>
					</p>
				<%
					}
				%>	
				<div id="login-left-col" style="width:300px";>
				    <h1><img src="<lams:LAMSURL/>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" /></h1>
	 				<%try{%>
					  	<c:set var="url"><lams:LAMSURL/>/www/news.html</c:set>
			  			<c:import url="${url}" charEncoding="utf-8" />
			  		<%}catch(Exception e){}%>
			  		<br />
			  		<br />
			  	</div>
				
					<div id="login-right-col" style="background: url('images/login-bar.jpg') repeat-x 0 0;width:355px;">
					<p class="version">Version <%= Configuration.get(ConfigurationKeys.VERSION) %></p>
					
					<h2>Login</h2>
					
					<div id="loginDiv">
						
						<form action="<%=LAMS_SERVER_URL %>/j_security_check" method="post" name="loginForm" id="loginForm">
							<c:if test="${!empty param.failed}">
								<div class="warning-login">
									Username and/or password incorrect, please try again
								</div>
							</c:if>	
						 	<p class="first">Username: 
						   		<input name="j_username" type="text" size="16" style="width:125px" tabindex="1" onkeypress="onEnter(event)"/>
						  	</p>
						 	<p>Password: 
						   		<input name="j_password" type="password" size="16" style="width:125px" autocomplete="off" tabindex="2" onkeypress="onEnter(event)"/>
						  	</p>
							<p class="login-button">
								<a href="javascript:submitForm()" class="button" tabindex="3"/>Login</a>
						 	</p>
						 	
					 	</form>	
					 	<p class="login-button">
					 		<a href="register.jsp">
						    	Sign-Up
						    </a>
						    &nbsp;
							<a href="<lams:LAMSURL/>forgotPassword.jsp">
						    	Forgot your password?   	
						    </a>
						 	&nbsp;
						 	<a href="<lams:LAMSURL/>/www/help/troubleshoot-<%=Configuration.get(ConfigurationKeys.SERVER_LANGUAGE)%>.pdf">
						 		Help
							</a>
						</p> 
					
					</div>
					<div id="openIdDiv" style="text-align: left;">
						<h3>&nbsp;Or login/sign-up using OpenID</h3>
								
						<p style="text-align:left;">
							Login here using your personal OpenID URL. For example: <i><tt>someone.myopenid.com.</tt></i> 
							If you have not done this before, you will be taken to a registration page after you have been authenticated through your OpenID provider.
							<br />
							<br />
							Don't have an OpenID? <a href="https://pip.verisignlabs.com/" target="_blank">Go</a>
						    <a href="http://www.myopenid.com/" target="_blank">get</a>
						    <a href="https://myvidoop.com/" target="_blank">one</a>.
						</p>
						
						<% 
						    if(loggedInAs != null && ret != null && ret.equals("yes"))
							{
								// We have authentication, construct login request
								String loginRequestURL = LAMS_SERVER_URL + "/openid/LoginUsingOpenId?";
								//loginRequestURL += "&uid=" + URLEncoder.encode(loggedInAs, "UTF8");
								loginRequestURL += "&method=none";
								loginRequestURL += "&ts=" + timestamp;
								loginRequestURL += "&sid=" + LAMS_SERVER_ID;
								loginRequestURL += "&hash=" + hash;
								loginRequestURL += "&courseid=" + URLEncoder.encode(courseID, "UTF8");
								loginRequestURL += "&courseName=" + URLEncoder.encode(courseName, "UTF8");
								loginRequestURL += "&country=" + GROUP_COUNTRY_ISO_CODE;
								loginRequestURL += "&lang=" + GROUP_LANG_ISO_CODE;
								loginRequestURL += "&requestSrc=&notifyCloseURL=&lsid=";
								response.sendRedirect(loginRequestURL);
						    }
						%>
						
						<div style='margin: 1em 0 1em 2em; border-left: 2px solid black; padding-left: 1em;'>
						    <form action="index.jsp" method="post">
						        <input type="hidden" name="signin" value="true"/>
						        <b>Login with your OpenID URL:</b> <input type="text" size="30" name="openid_url" id="openid_url"/>
						        <input type="submit" value="Login"/><br/>
						    </form>
						</div>
						
						<p style="text-align:left;">
						Click your OpenID provider below and/or enter your OpenID in the field above: 
						<p>
						<div style="text-align:center;" >
							<img onclick="change_provider('aol')" height="24" width="70" style="margin-right: 3px;" src="images/openid_logos/aol.jpg"/>
					        <img onclick="change_provider('blogger')" height="24" width="24" style="margin-right: 3px;" src="images/openid_logos/blogger.jpg"/>
					        <img onclick="change_provider('wordpress')" height="24" width="24" style="margin-right: 3px;" src="images/openid_logos/wordpress.jpg"/>
					        <img onclick="change_provider('technorati')" height="24" width="26" style="margin-right: 3px;" src="images/openid_logos/technorati.jpg"/>
					       	<img onclick="change_provider('vox')" height="24" width="55" style="margin-right: 3px;" src="images/openid_logos/vox.jpg"/>
					        <img onclick="change_provider('claimid')" height="24" width="69" style="margin-right: 3px;" src="images/openid_logos/claimid.jpg"/>
					        <img onclick="change_provider('myopenid')" height="24" width="92" style="margin-right: 3px;" src="images/openid_logos/myopenid.jpg"/>
					        <img onclick="change_provider('vidoop')" height="24" width="74" style="margin-right: 3px;" src="images/openid_logos/vidoop.jpg"/>
					       	<img onclick="change_provider('verisign')" height="24" width="57" style="margin-right: 3px;" src="images/openid_logos/verisign.jpg"/>
						</div>					
					</div>
				</div>
			
				<div class="clear"></div>
			</div>  <!--closes login-content-->
	
			<div id="footer">
				<p>&copy; 2002-2009 LAMS Foundation.</p>
	  		</div><!--closes footer-->
	</body>
</html>
