<%@ page language="java" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
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
<html xmlns="http://www.w3.org/1999/xhtml">
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
	
	<!--[if IE]>
	
	  <style type="text/css">
	  
	    @import url(css/ie-styles.css);
		
	  </style>
	  
	<![endif]-->
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
	  
			  <div id="login-left-col" class="row"><h1><img src="<lams:LAMSURL/>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" /></h1>
			 <h2><img src="<lams:LAMSURL/>/images/css/lams_news.gif" alt="LAMS News and Latest Updates" class="news" /></h2>
			 
			  <ul>
			  	<li>
					<h3>LAMS 2.0 Beta1 Released!</h3> 
					Finally, LAMS 2.0 Beta is ready for testing. Have a look at the <a href="http://wiki.lamsfoundation.org/display/lams/LAMS+2.0+Feature+List" target="_new">cool new features</a> packaged inside.<br>
Found a bug? Report it in the <a href="http://lamscommunity.org/dotlrn/clubs/technicalcommunity/forums/" target="_new">LAMS Community</a>.<br>
LAMS is currently being translated to <a href="http://lamscommunity.org/i18n" target="_new">17 languages</a> by more than 25 volunteers. Want to <a href="http://lamscommunity.org/i18n" target="_new">help out</a>? 
				</li>
			  </ul>
			  </div>
				<!--closes left col-->
				
				<div id="login-right-col" class="row">
				 <h2><fmt:message key="button.login"/></h2>
				 <form action="j_security_check" method="post" name="loginForm" id="loginForm">
					<c:if test="${!empty param.failed}">
						<p class="warning">
							<fmt:message key="error.login"/>
						</p>
					</c:if>	
				 <p class="first"><fmt:message key="label.username"/>: 
				   <input name="j_username" type="text" size="16" />
				  </p>
				 <p><fmt:message key="label.password"/>: 
				   <input name="j_password" type="password" size="16" autocomplete="off"/>
				  </p>
					
					 <p><a href="javascript:submitForm()" class="button"><fmt:message key="button.login"/></a></p>
				</form>	 
				</div><!--closes right col-->
	 
	  <div class="clear"></div><!-- forces the CSS to display the columns-->
	
	  </div>  <!--closes content-->
	   
		
		
		
		
		<div id="footer">
		</div><!--closes footer-->
		
	</div><!--closes page-->

</body>


</html>