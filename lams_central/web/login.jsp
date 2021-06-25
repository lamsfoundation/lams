<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="org.springframework.web.context.request.SessionScope"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.dto.UserDTO"%>

<c:if test="${empty requestScope.login}">
	<c:set var="login" value="${sessionScope.login}" />
	<c:set var="password" value="${sessionScope.password}" />
</c:if>
<c:set var="isForgotYourPasswordEnabled"><%=Configuration.get(ConfigurationKeys.FORGOT_YOUR_PASSWORD_LINK_ENABLE)%></c:set>

<lams:html>

<%-- If login param is empty, this is a regular, manual login page.
	 Otherwise it is a just an almost empty redirect page for integrations and LoginAs authentication.
 --%>
<c:choose>
	<c:when test="${empty login}">
	
		<%-- Optional Module Placeholder - do not remove --%>
		
		<lams:head>
			<title><fmt:message key="title.login.window" /></title>
			<lams:css/>
			<link rel="icon" href="/lams/favicon.ico" type="image/x-icon" />
			<link rel="shortcut icon" href="/lams/favicon.ico" type="image/x-icon" />
			<script type="text/javascript" src="/lams/includes/javascript/browser_detect.js"></script>
			<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
			<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
			<script type="text/javascript">
				function submitForm() {
					$('#loginButton').addClass('disabled');
					$('#loginForm').submit();
				}

				function onEnter(event) {
					intKeyCode = event.keyCode;
					if (intKeyCode == 13) {
						submitForm();
					}
				}
				
				function isBrowserCompatible() {
					return Modernizr.atobbtoa && Modernizr.checked && Modernizr.cookies && Modernizr.nthchild && Modernizr.opacity &&
						   Modernizr.svg && Modernizr.todataurlpng && Modernizr.websockets && Modernizr.xhrresponsetypetext && Modernizr.svgforeignobject;
					// Modernizr.datauri - should be included, it's a async test though
					// Modernizr.time - should be included, fails in Chrome for an unknown reason (reported)
					// Modernizr.xhrresponsetypejson - should be included, fails in IE 11 for an unknown reason (reported)
				}

				$(document).ready(function() {
					$('html').addClass('login-body');
					if (!isBrowserCompatible()) {
						$('#browserNotCompatible').show();
					}
					$('#j_username').focus();
					$('#news').load('/lams/www/news.html');

					//make a POST call to ForgotPasswordRequest
					$("#forgot-password-link").click(function() {
						var $form=$(document.createElement('form'))
							.css({display:'none'})
							.attr("method","POST")
							.attr("action","<lams:LAMSURL/>ForgotPasswordRequest?method=showForgotYourPasswordPage");
						$("body").append($form);
						$form.submit();
					});
				});
			</script>
		</lams:head>
		<body class="login-body">
		<div class="login-content">
		
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-login">
		<div class="container">
	        <div class="navbar-header">
	          <a class="navbar-brand navbar-brand-login" href="#"><%=Configuration.get(ConfigurationKeys.SITE_NAME)%></a>
	        </div>
			<div class="navbar-collapse collapse navbar-right">
				<div class="pull-right login-logo" title="LAMS - Learning Activity Management System"></div>
			</div>
		</div>	
    </nav>
		<!-- Close navbar -->

		<!-- Start content  -->
		<div class="container no-gutter">
			<div id="news" class="col-sm-8 col-md-9 hidden-xs"></div>
			<div id="login-panel" class="col-sm-4 col-md-3">
      <div class="panel panel-default" >
				<div class="panel-heading">
				    <div class="panel-title"> <fmt:message key="button.login" /></div>
				</div>     

        <div class="panel-body" >
            <div id="browserNotCompatible" class="panel panel-danger" style="display: none">
                 <div class="panel-heading"><fmt:message key="msg.browser.compat"/></div>
            </div>
						<c:if test="${!empty param.failed}">
						<div class="panel panel-danger">
							<div class="panel-heading">
								<fmt:message key="error.login" />
							</div>
						</div>
						</c:if>
						<c:if test="${!empty param.lockedOut}">
						<div class="panel panel-danger">
							<div class="panel-heading">
								<fmt:message key="error.lockedout" />
							</div>
						</div>
						</c:if>

						<form action="/lams/j_security_check" method="POST" name="loginForm" role="form" class="form-horizontal" id="loginForm">
							<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" 
						  		 escapeXml="true" />' />
             				 <div class="input-group">
								<span class="input-group-addon"><i class="fa fa-user"></i></span>
								<input id="j_username" type="text" class="form-control" autocapitalize="off" name="j_username" value="" placeholder="<fmt:message key='label.username' />" onkeypress="onEnter(event)" tabindex="1" autocomplete="username">
              				</div>

							<div class="input-group voffset5">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span>
								<input id="j_password" type="password" class="form-control" name="j_password" placeholder="<fmt:message key='label.password' />" onkeypress="onEnter(event)" tabindex="2" autocomplete="current-password">
							</div>
							<div class="form-group voffset5" style="margin-bottom: 5px;">
								<c:if test="${isForgotYourPasswordEnabled}">
							   		<div class="col-md-12 control" style="font-size:75%">
										<a id="forgot-password-link" href="#nogo"> <fmt:message key="label.forgot.password" /></a>
							    	</div>
							    </c:if>
							    
								<!-- Button -->
								<div class="col-sm-12 controls voffset5">
									<a id="loginButton" href="javascript:submitForm()" class="btn btn-primary btn-block" tabindex="3"><fmt:message key="button.login" /></a>
								</div>
							</div>
						</form>     
				</div>
			</div>
		</div>
	</div>
	<!--closes content-->

	<div class="login-footer">
		<p class="text-muted text-center">
			<a href="/lams/www/copyright.jsp" target='copyright' onClick="openCopyRight()"> &copy; <fmt:message key="msg.LAMS.copyright.short" /></a>
		</p>
	</div>

		<!--closes page-->
		</div> <!--  close login-content -->
		</body>
	</c:when>

	<%-- This is version for integrations and LoginAs authentication. --%>

	<c:otherwise>
		<lams:head>
			<lams:css />
			<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
			<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
		</lams:head>
		<body class="stripes">
			<!-- A bit of content so the page is not completely blank -->
			<lams:Page type="admin">

				<div class="text-center" style="margin-top: 20px; margin-bottom: 20px;">
					<i class="fa fa-2x fa-refresh fa-spin text-primary"></i>
					<h4>
						<fmt:message key="msg.loading" />
					</h4>
				</div>

				<form style="display: none" method="POST" action="j_security_check">
					<input type="hidden" name="j_username" value="${login}" /> <input type="hidden" name="j_password"
						value="${password}" /> <input type="hidden" name="redirectURL"
						value='<c:out value="${empty param.redirectURL ? redirectURL : param.redirectURL}" escapeXml="true" />' />
				</form>
				<div id="footer"></div>
			</lams:Page>
			<%
				// invalidate session so a new user can be logged in
				HttpSession hs = SessionManager.getSession();
				if (hs != null) {
					UserDTO userDTO = (UserDTO) hs.getAttribute("user");
					if (userDTO != null && !userDTO.getLogin().equals(request.getAttribute("login"))) {
					    // remove session from mapping
					    SessionManager.removeSessionByLogin(userDTO.getLogin(), true);
					}
				}
			%>
			<script type="text/javascript">
				// submit the hidden form
				document.forms[0].submit();
			</script>
		</body>
	</c:otherwise>
</c:choose>

</lams:html>
