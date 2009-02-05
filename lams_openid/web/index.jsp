<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="config.jsp" %>
<%@ page import="org.verisign.joid.consumer.OpenIdFilter" %>
<%@ page import="org.verisign.joid.util.UrlUtils" %>
<%@ page import="org.lamsfoundation.lams.util.HashUtil" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.net.URLEncoder" %>
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
				<div style="padding-left:40px">
					<h1><img src="<%=LAMS_SERVER_URL %>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" /></h1>
					<br/>
					<h1>Login Using OpenID</h1>
					
					<%
						if(request.getParameter("signin") != null && blacklisted == true && error == false){
					%>
					
					<p class="warning">
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
						<p class="warning">
							<%=errorMessage %>
						</p>
					
					<%
						}
					%>			
					<p>
						<br/>
						Login here using your personal OpenID URL. For example: <i><tt>someone.yahoo.com</tt></i>
						<br/>
						<br/>
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
					        <b>Login with your OpenID URL:</b> <input type="text" size="30"name="openid_url"/>
					        <input type="submit" value="Login"/><br/>
					    </form>
					</div>
				</div>
			
				<div class="clear"></div>
			</div>  <!--closes login-content-->
	
			<div id="footer">
				<p>&copy; 2002-2009 LAMS Foundation.</p>
	  		</div><!--closes footer-->
	</body>
</html>
