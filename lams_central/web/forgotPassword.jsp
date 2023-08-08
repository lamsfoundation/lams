<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico"
		type="image/x-icon" />
	<script type="text/javascript">
		function toHome() {
			window.location = "<lams:LAMSURL/>index.do";
		};

		function validateForm() {
			if (document.forgotForm.selectType[0].checked) {
				var login = document.forms.forgotForm.login.value;
				if (login == null || login == "") {
					alert("<fmt:message key="error.forgot.password.username" />");
					return false;
				} else {
					document.forms.forgotForm.submit();
				}
			} else if (document.forms.forgotForm.selectType[1].checked) {
				var email = document.forms.forgotForm.email.value;
				//var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				var filter = /^(.)+\@(.)+\.(.)+/;

				if (email == null || email == "") {
					alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.forgot.password.email' /></spring:escapeBody>");
					return false;
				} else if (!filter.test(email)) {
					alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.valid.email.required' /></spring:escapeBody>");
					return false;
				} else {
					document.forms.forgotForm.submit();
				}
			}
		}

		function radioSelected() {
			if (document.forms.forgotForm.selectType[0].checked) {
				document.forms.forgotForm.login.disabled = false;
				document.forms.forgotForm.email.disabled = true;
			} else if (document.forms.forgotForm.selectType[1].checked) {
				document.forms.forgotForm.login.disabled = true;
				document.forms.forgotForm.email.disabled = false;
			}
		}

		function submitenter(myfield, e) {
			var keycode;
			if (window.event)
				keycode = window.event.keyCode;
			else if (e)
				keycode = e.which;
			else
				return true;

			if (keycode == 13) {
				validateForm();
				return false;
			} else {
				return true;
			}
		}
	</script>
</lams:head>

<c:set var="title">
	<fmt:message key="title.forgot.password" />
</c:set>

<body class="stripes" onload="radioSelected();">

	<lams:Page type="admin" title="${title}">
		<form action="<lams:LAMSURL/>ForgotPasswordRequest" method="post" name="forgotForm">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="method" id="method" value="requestEmail" />

			<h4 class="no-tabs-below">
				<fmt:message key="label.forgot.password" />
			</h4>

			<p>
				<fmt:message key="label.forgot.password.instructions.1" />
			</p>

			<p>
				<fmt:message key="label.forgot.password.instructions.2" />
			</p>

			<div class="row">
				<div class="col-xs-12">
					<div class="panel-body">

						<div class="form-group">
							<input type="radio" id="loginCheck" name="selectType"
								value="radioUsername" onclick="radioSelected();" checked>
							<label for="loginCheck">
								<fmt:message key="label.forgot.password.username" />
							</label>
						</div>
						<div class="form-group">
							<input type="text" name="login" id="login" class="form-control" maxlength="256" tabindex="1"
								onKeyPress="return submitenter(this,event)" />
						</div>

						<hr class="msg-hr" />

						<div class="form-group">
							<input type="radio" id="emailCheck" name="selectType" value="radioEmail" onclick="radioSelected();"> 
							<label for="emailCheck">
								<fmt:message key="label.forgot.password.email" />
							</label>
						</div>
						<div class="form-group">
							<input type="text" name="email" id="email" class="form-control" maxlength="256" tabindex="1"
								onKeyPress="return submitenter(this,event)" />
						</div>

						<div class="form-group">
							<button name="cancel"
								class="btn btn-default roffset10" type="button"
								onclick="javascript:toHome();">
								<fmt:message key="button.cancel" />
							</button>
							<button name="ok" class="btn btn-primary" type="button"
								onclick="javascript:validateForm();">
								<fmt:message key="label.ok" />
							</button>
						</div>

					</div>
				</div>
			</div>

		</form>

	</lams:Page>
</body>

</lams:html>
