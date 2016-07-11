<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>

<lams:head>
	<lams:css />
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script type="text/javascript">
		function toHome() {
			window.location = "<lams:LAMSURL/>index.do";
		};

		function validateForm() {
			if (document.forgotForm.selectType[0].checked) {
				var login = document.forgotForm.login.value;
				if (login == null || login == "") {
					alert("<fmt:message key="error.forgot.password.username" />");
					return false;
				} else {
					document.forgotForm.submit();
				}
			} else if (document.forgotForm.selectType[1].checked) {
				var email = document.forgotForm.email.value;
				//var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				var filter = /^(.)+\@(.)+\.(.)+/;

				if (email == null || email == "") {
					alert("<fmt:message key="error.forgot.password.email" />");
					return false;
				} else if (!filter.test(email)) {
					alert("<fmt:message key="error.valid.email.required" />");
					return false;
				} else {
					document.forgotForm.submit();
				}
			}
		}

		function radioSelected() {
			if (document.forgotForm.selectType[0].checked) {
				document.forgotForm.login.disabled = false;
				document.forgotForm.email.disabled = true;
			} else if (document.forgotForm.selectType[1].checked) {
				document.forgotForm.login.disabled = true;
				document.forgotForm.email.disabled = false;
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
			} else
				return true;
		}
	</script>
</lams:head>


<body class="stripes" onload="radioSelected();">

	<div id="page">
		<!--main box 'page'-->
		<form action="<lams:LAMSURL/>/ForgotPasswordRequest" method="post" name="forgotForm">
			<input type="hidden" name="method" id="method" value="requestEmail" />


			<div id="header-no-tabs"></div>
			<div id="content">
				<h1 class="no-tabs-below">
					<fmt:message key="label.forgot.password" />
				</h1>

				<p>
					<fmt:message key="label.forgot.password.instructions.1" />
				</p>

				<p>
					<fmt:message key="label.forgot.password.instructions.2" />
				</p>

				<table border="0">
					<tr>
						<td class="align-right" width="35%"><fmt:message key="label.forgot.password.username" /> &nbsp; <input type="radio"
							name="selectType" value="radioUsername" onclick="radioSelected();" checked>
								<td class="align-left" width="65%"><input type="text" name="login" id="login" size="50" maxlength="50" tabindex="1"
									onKeyPress="return submitenter(this,event)" /></td>
					</tr>
					<tr>
						<td class="align-right" width="35%"><fmt:message key="label.forgot.password.email" /> &nbsp; <input type="radio"
							name="selectType" value="radioEmail" onclick="radioSelected();"></td>
						<td class="align-left" width="65%"><input type="text" name="email" id="email" size="50" maxlength="50" tabindex="1"
							onKeyPress="return submitenter(this,event)" /></td>
					</tr>

					<tr>
						<td>&nbsp;</td>

						<td align="left"><html:button property="ok" styleClass="button" onclick="javascript:validateForm();">
								<fmt:message key="label.ok" />
							</html:button> <html:button property="cancel" styleClass="button" onclick="javascript:toHome();">
								<fmt:message key="button.cancel" />
							</html:button></td>
					</tr>
				</table>
			</div>
			<!--closes content-->

		</form>
		<div id="footer"></div>
		<!--closes footer-->

	</div>
	<!--closes page-->



</body>

</lams:html>
