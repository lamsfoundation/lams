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
	<link rel="icon" href="/lams/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="/lams/favicon.ico" type="image/x-icon" />
	<lams:css/>
	<link rel="stylesheet" href="/lams/css/login.css" type="text/css">
	<style>
	html,
body {
  height: 100%;
}

body {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-align: center;
  align-items: center;
  padding-top: 40px;
  padding-bottom: 40px;
  background-color: #f5f5f5;
}

.form-signin {
  width: 100%;
  max-width: 420px;
  padding: 15px;
  margin: auto;
}

.form-label-group {
  position: relative;
  margin-bottom: 1rem;
}

.form-label-group > input,
.form-label-group > label {
  height: 3.125rem;
  padding: .75rem;
}

.form-label-group > label {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  margin-bottom: 0; /* Override default `<label>` margin */
  line-height: 1.5;
  color: #495057;
  pointer-events: none;
  cursor: text; /* Match the input under the label */
  border: 1px solid transparent;
  border-radius: .25rem;
  transition: all .1s ease-in-out;
}

.form-label-group input::-webkit-input-placeholder {
  color: transparent;
}

.form-label-group input:-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-moz-placeholder {
  color: transparent;
}

.form-label-group input::placeholder {
  color: transparent;
}

.form-label-group input:not(:placeholder-shown) {
  padding-top: 1.25rem;
  padding-bottom: .25rem;
}

.form-label-group input:not(:placeholder-shown) ~ label {
  padding-top: .25rem;
  padding-bottom: .25rem;
  font-size: 12px;
  color: #777;
}

/* Fallback for Edge
-------------------------------------------------- */
@supports (-ms-ime-align: auto) {
  .form-label-group > label {
    display: none;
  }
  .form-label-group input::-ms-input-placeholder {
    color: #777;
  }
}

/* Fallback for IE
-------------------------------------------------- */
@media all and (-ms-high-contrast: none), (-ms-high-contrast: active) {
  .form-label-group > label {
    display: none;
  }
  .form-label-group input:-ms-input-placeholder {
    color: #777;
  }
}
	</style>

	<script type="text/javascript" src="/lams/includes/javascript/browser_detect.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap.js"></script>
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
				   Modernizr.svg && Modernizr.todataurlpng && Modernizr.websockets && Modernizr.xhrresponsetypetext;
			// Modernizr.datauri - should be included, it's a async test though
			// Modernizr.time - should be included, fails in Chrome for an unknown reason (reported)
			// Modernizr.xhrresponsetypejson - should be included, fails in IE 11 for an unknown reason (reported)
		}

		$(document).ready(function() {
			//$('html').addClass('login-body');
			if (!isBrowserCompatible()) {
				$('#browserNotCompatible').show();
			}
			//$('#j_username').focus();
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
<body class="d-flex align-items-center min-vh-100">
	<div class="container">
		<div class="row">
			<div class="col-lg-10 col-xl-9 mx-auto">
				<div class="card card-signin flex-row my-5">
					<div class="card-img-left d-none d-md-flex">
						<!-- Background image for card set in CSS! -->
					</div>
          
					<div class="card-body">
						<div class="card-title text-center">
							<div class="login-logo" title="LAMS - Learning Activity Management System"></div>

				          	<div class="voffset20">
				          		<%=Configuration.get(ConfigurationKeys.SITE_NAME)%>
				          	</div>
            
				            <div>
				            	Welcome to LAMS 5.0
				            </div>
          				</div>
          
			            <div id="browserNotCompatible" class="card bg-danger text-white mb-3" style="display: none">
			                 <div class="card-header"><fmt:message key="msg.browser.compat"/></div>
			            </div>
						<c:if test="${!empty param.failed}">
							<div class="card bg-danger text-white mb-3">
								<div class="card-header">
									<fmt:message key="error.login" />
								</div>
							</div>
						</c:if>
						<c:if test="${!empty param.lockedOut}">
							<div class="card bg-danger text-white mb-3">
								<div class="card-header">
									<fmt:message key="error.lockedout" />
								</div>
							</div>
						</c:if>
           
						<form action="/lams/j_security_check" method="POST" name="loginForm" role="form" class="form-signin" id="loginForm">
           					<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" escapeXml="true" />' />
           	 
			            	<div class="form-label-group">
			              		<input id="j_username" type="text" class="form-control" autocapitalize="off" name="j_username" onkeypress="onEnter(event)" tabindex="1" required autofocus placeholder="aaaaa" autocomplete="username">
			                	<label for="j_username">Username</label>
			              	</div>
			
			              	<div class="form-label-group">
			              		<input id="" type="password" class="form-control" name="j_password" onkeypress="onEnter(event)" tabindex="2" required autocomplete="current-password">
			                	<label for="j_password">Password</label>
			              	</div>	
			              
			              	<a id="loginButton" href="javascript:submitForm()" class="btn btn-primary btn-block text-uppercase" tabindex="3">
			              		<fmt:message key="button.login" />
			              	</a>
              
            				<c:if test="${isForgotYourPasswordEnabled}">
								<div class="text-center">
									<a id="forgot-password-link" class="small" href="#nogo"> 
										<fmt:message key="label.forgot.password" />
									</a>
							    </div>
							</c:if>
			
							<div id="login-footer" class="text-center text-muted voffset20 login-footer small">
								<a href="/lams/www/copyright.jsp" target='copyright' onClick="openCopyRight()"> &copy; <fmt:message key="msg.LAMS.copyright.short" /></a>
							</div>              
           				</form>
          			</div>
        		</div>
      		</div>
    	</div>
  	</div>
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
					if (userDTO != null) {
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
