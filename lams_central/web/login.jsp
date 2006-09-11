<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%	
	if (JspRedirectStrategy.loginPageRedirected(request,response))
	{
		return;
	}		

	/*String webAuthUser = (String) session.getAttribute("WEBAUTH_USER");
	if (webAuthUser != null)
	{
		response.sendRedirect("j_security_check?j_username=" + webAuthUser + "&j_password=Dummy");	
	}*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<!--
flash is searching for this string, so leave it!:
j_security_login_page
-->
<head>
	<title><fmt:message key="title.login.window"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<lams:css/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<script language="JavaScript" type="text/javascript" src="includes/javascript/sha1.js"></script>
	<script>
		function submitForm(){
			  var password=document.loginForm.j_password.value;	 
			  document.loginForm.j_password.value=hex_sha1(password);
			  document.loginForm.submit();
		}
	</script>
</head>

<body>
	<div id="login-page"><!--main box 'page'-->
	
		<h1 class="no-tabs-below">&nbsp;</h1>
		
		<div id="login-header">
		
		</div><!--closes header-->
	
	
	
	  <div id="login-content">	
	  
			  <div id="login-left-col"><h1><img src="<lams:LAMSURL/>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" /></h1>
			  	<c:set var="url"><lams:LAMSURL/>www/news.html</c:set>
		  		<c:import url="${url}" charEncoding="utf-8" />
			  </div>
				<!--closes left col-->
				
				<div id="login-right-col">
				<p class="version"><fmt:message key="msg.LAMS.version"/> <%= Configuration.get(ConfigurationKeys.VERSION) %></p>
				 <h2><fmt:message key="button.login"/></h2>
				 <form action="j_security_check" method="post" name="loginForm" id="loginForm">
					<c:if test="${!empty param.failed}">
						<div class="warning-login">
							<fmt:message key="error.login"/>
						</div>
					</c:if>	
				 <p class="first"><fmt:message key="label.username"/>: 
				   <input name="j_username" type="text" size="16" style="width:125px" tabindex="1"/>
				  </p>
				 <p><fmt:message key="label.password"/>: 
				   <input name="j_password" type="password" size="16" style="width:125px" autocomplete="off" tabindex="2"/>
				  </p>
					<p class="login-button">
					 <input type="submit" class="button" value="<fmt:message key="button.login"/>" onClick="javascript:submitForm()" tabindex="3"/>
					 </p>
				</form>	 
				</div><!--closes right col-->
	 
	  <div class="clear"></div><!-- forces the CSS to display the columns-->
	
	  </div>  <!--closes content-->
	   
		
		
		
		
		<div id="footer">
		
		<p>&copy; <fmt:message key="msg.LAMS.copyright.short"/></p>
		
	  </div><!--closes footer-->
		
	</div><!--closes page-->


</body>


</lams:html>