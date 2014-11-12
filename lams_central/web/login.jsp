<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<%-- If you change this file, remember to update the copy made for CNG-21 --%>

<%
    if (JspRedirectStrategy.loginPageRedirected(request, response)) {
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<!--
flash is searching for this string, so leave it!:
j_security_login_page
-->

<c:set var="encrypt"><%= Configuration.getAsBoolean(ConfigurationKeys.LDAP_ENCRYPT_PASSWORD_FROM_BROWSER) %></c:set>
<lams:head>
	<title><fmt:message key="title.login.window" /></title>
	<lams:css style="core" />
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script type="text/javascript" src="includes/javascript/browser_detect.js"></script>
	<script type="text/javascript" src="includes/javascript/sha1.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		var password = '${param.password}',
			// if password came as a parameter, it is already encrypted
			encrypt = password ? false : ${encrypt};

		function submitForm() {
			var password = $('#j_password').val();
			if (encrypt) {
				$('#j_password').val(hex_sha1(password));
			}
			$('#loginForm').submit();
		}

		function onEnter(event) {
			intKeyCode = event.keyCode;
			if (intKeyCode == 13) {
				submitForm();
			}
		}
		
		$(document).ready(function(){
			$('#j_username').focus();
			$('#news').load('<lams:LAMSURL/>www/news.html');
		});
	</script>
</lams:head>

<body class="stripes">
	<div id="login-page">
		<!--main box 'page'-->
		<h1 class="no-tabs-below">&nbsp;</h1>

		<div id="login-header"></div>
		<!--closes header-->

		<div id="login-content">
			<div id="login-left-col">
				<h1>
					<img src="<lams:LAMSURL/>/www/images/lams_login.gif"
						 alt="LAMS - Learning Activity Management System" />
				</h1>
				
				<!--Test if the browsers meets requirements-->
				<script type="text/javascript">
					if(isBrowserNotCompatible()) {
						// incompatible browser - show warning message
						document.write('<div class=\"warning\"><fmt:message key="msg.browser.compat"/> <a href=\"http://getfirefox.com\" target=\"_blank\"><img src=\"images/firefox_logo.png\"></a>');
						document.write('<br></div>');
					}
					
					function isBrowserNotCompatible() {
						if(ie7 || ie8 || ie9 || ie10 ) return false; // IE8-IE10
						else if(saf4) return false; //Safari4
						else if(chrome) return false; //Chrome
						else if(ie5xwin || ie5 || ie4 || ie5mac || ie5x || ie6) return true;
						else if(saf) return true; // Safari
						else if(op) return true;	// Opera
						else if(moz) 
							if((moz_brow.indexOf("Firefox") != -1) && (moz_brow_nu >= 1.5)) return false;	// Firefox 1.5+
						else return true;
					}
				</script>
				
				<!-- Placeholder for customised login page part -->
				<div id="news"></div>
			</div>
			<!--closes left col-->

			<div id="login-right-col">
				<p class="version">
					<fmt:message key="msg.LAMS.version" />
					<%=Configuration.get(ConfigurationKeys.VERSION)%></p>
				<h2>
					<fmt:message key="button.login" />
				</h2>
				<form action="j_security_check" method="post" name="loginForm" id="loginForm">
					<c:if test="${!empty param.failed}">
						<div class="warning-login">
							<fmt:message key="error.login" />
						</div>
					</c:if>
					
					<input type="hidden" name="redirectURL" value='${param.redirectURL}'/>
					
					<p class="first">
						<fmt:message key="label.username" />
						: <input id="j_username" name="j_username" type="text" size="16"
							style="width: 125px" tabindex="1"
							value="${param.login}"
							onkeypress="onEnter(event)" />
					</p>
					
					<p>
						<fmt:message key="label.password" />
						: <input id="j_password" name="j_password" type="password" size="16"
							style="width: 125px" autocomplete="off" tabindex="2"
							value="${param.password}"
							onkeypress="onEnter(event)" />
					</p>
					
					<p class="login-button">
						<a id="loginButton" href="javascript:submitForm()" class="button"
							tabindex="3" />
						<fmt:message key="button.login" />
						</a>
					</p>
				</form>
				<p class="login-button">
					<a href="<lams:LAMSURL/>forgotPassword.jsp"> <fmt:message
							key="label.forgot.password" />
					</a> <br /> <a
						href="<lams:LAMSURL/>/www/help/troubleshoot-<%=Configuration.get(ConfigurationKeys.SERVER_LANGUAGE)%>.pdf">
						<fmt:message key="label.help" />
					</a>
				</p>
			</div>
			<!--closes right col-->

			<div class="clear"></div>
			<!-- forces the CSS to display the columns-->
		</div>
		<!--closes content-->

		<div id="footer">
			<p>
				<fmt:message key="msg.LAMS.version" />
				<%=Configuration.get(ConfigurationKeys.VERSION)%>
				<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()">
					&copy; <fmt:message key="msg.LAMS.copyright.short" />
				</a>
			</p>
		</div>
		<!--closes footer-->
	</div>
	<!--closes page-->
	
	<script type="text/javascript">
		// if login was set as a parameter, do auto-submit right away
		if ('${param.login}') {
			submitForm();
		}
	</script>
</body>
</lams:html>