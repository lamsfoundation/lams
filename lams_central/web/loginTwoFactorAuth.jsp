<%@page import="org.springframework.web.context.request.SessionScope"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.dto.UserDTO"%>

<c:if test="${empty requestScope.login}">
	<c:set var="login" value="${sessionScope.login}" />
	<c:set var="password" value="${sessionScope.password}" />
</c:if>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="title.login.window" /></title>
		<lams:css style="core" />
		<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/browser_detect.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript">
			function submitForm() {
				$('#loginForm').submit();
			}

			function onEnter(event) {
				intKeyCode = event.keyCode;
				if (intKeyCode == 13) {
					submitForm();
				}
			}

			$(document).ready(function() {
				$('#verificationCode').focus();
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
							<img src="<lams:LAMSURL/>/www/images/lams_login.gif" alt="LAMS - Learning Activity Management System" />
						</h1>

					</div>
					<!--closes left col-->

					<div id="login-right-col">
						<p class="version">
							<fmt:message key="msg.LAMS.version" />
							<%=Configuration.get(ConfigurationKeys.VERSION)%>
						</p>
					
						<h2>
							<fmt:message key="button.login" />
						</h2>
						
						<form action="/lams/j_security_check" method="POST" name="loginForm" id="loginForm">
							<c:if test="${!empty param.failed}">
								<div class="warning-login">
									<fmt:message key="error.verification.code" />
								</div>
								
							</c:if>
							<input type="hidden" name="j_username" value="${login}" /> 
							<input type="hidden" name="j_password" value="${password}" /> 
							<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" escapeXml="true" />' />

							<p class="first">
								<fmt:message key="label.verification.code" />
								: <input id="verificationCode" name="verificationCode" type="text" size="16" style="width: 125px" tabindex="1"
									onkeypress="onEnter(event)" />
							</p>

							<p class="login-button">
								<a id="loginButton" href="javascript:submitForm()" class="button" tabindex="3" />
									<fmt:message key="button.login" />
								</a>
							</p>
						</form>
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
					<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()"> &copy; <fmt:message
							key="msg.LAMS.copyright.short" />
					</a>
				</p>
			</div>
			<!--closes footer-->
		</div>
		<!--closes page-->
			
		<%
			// remove login and password attributes from the session as they are no longer needed
			HttpSession hs = SessionManager.getSession();
			if (hs != null) {
			    hs.removeAttribute("login");
			    hs.removeAttribute("password");
			}
		%>
	</body>

</lams:html>