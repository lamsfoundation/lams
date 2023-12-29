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

<lams:html>
	<lams:head>
		<title><fmt:message key="title.login.window" /></title>
		<lams:css/>
		<style>
			/* hide spinner in input number */
			input[type="number"]::-webkit-outer-spin-button,
			input[type="number"]::-webkit-inner-spin-button {
			    -webkit-appearance: none;
			    margin: 0;
			}
			input[type="number"] {
			    -moz-appearance: textfield;
			}
		</style>
		
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/browser_detect.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript">
			function submitForm() {
				$('#loginButton').addClass('disabled');
				$('#loginForm').submit();
			}

			function onEnter(event) {
				intKeyCode = event.keyCode;
				if (intKeyCode == 13) {
					submitForm();
					event.preventDefault();
				}
			}

		</script>
	</lams:head>
	<body>
	
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
	
	<div class="container">
		<div class="panel panel-default center-block" style="max-width: 350px;">
			<div class="panel-heading">
				<div class="panel-title"> <fmt:message key="label.2FA.login.panel" /></div>
			</div>

			<div class="panel-body" >
				<c:if test="${!empty param.failed}">
					<div class="panel panel-danger">
						<div class="panel-heading">
							<fmt:message key="error.verification.code" />
						</div>
					</div>
				</c:if>

				<form action="/lams/j_security_check" method="POST"  role="form" class="form-horizontal" name="loginForm" id="loginForm">
					<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
					<input type="hidden" name="j_username" value="${login}" /> 
					<input type="hidden" name="j_password" value="${password}" /> 
					<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" escapeXml="true" />' />
		
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-mobile"></i></span>
						<input id="verificationCode"  class="form-control" placeholder="<fmt:message key='label.verification.code' />" 
								name="verificationCode" type="number" autocomplete="off" tabindex="1" onkeypress="onEnter(event)" />
					</div>
		
					<div class="form-group voffset10">
						<div class="col-sm-12 controls">
							<a id="loginButton" href="javascript:submitForm()"  class="btn btn-primary btn-block" tabindex="2" />
								<fmt:message key="button.login" />
							</a>
						</div>	
					</div>
				</form>
			</div>		
		</div>
		<!--closes content-->
	
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
