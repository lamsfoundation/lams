<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%
String key = request.getParameter("key");
%>

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
			var pass = document.getElementById("newPassword").value;
			var passConfirm = document.getElementById("confirmNewPassword").value;

			if (pass == null || pass == "" || passConfirm == null
					|| passConfirm == "") {
				alert("<fmt:message key="error.forgot.password.fields" />");
			} else if (pass != passConfirm) {
				alert("<fmt:message key="error.newpassword.mismatch" />");
			} else {
				document.changePass.submit();
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


<body class="stripes">
	<div id="page">
		<!--main box 'page'-->
		<form action="<lams:LAMSURL/>/ForgotPasswordRequest" method="post" name="changePass">
			<input type="hidden" name="method" id="method" value="requestPasswordChange" /> <input type="hidden" name="key" id="key"
				value="<%=key %>" />

			<div id="header-no-tabs"></div>
			<div id="content">
				<h1 class="no-tabs-below">
					<fmt:message key="label.forgot.password" />
				</h1>

				<table class="body">
					<tr>
						<td class="align-right"><fmt:message key="label.password.new.password" />:</td>
						<td class="align-left"><input type="password" id="newPassword" name="newPassword" size="50" maxlength="50"
							onKeyPress="return submitenter(this,event)" /></td>
					</tr>
					<tr>
						<td class="align-right"><fmt:message key="label.password.confirm.new.password" />:</td>
						<td class="align-left"><input type="password" id="confirmNewPassword" name="confirmNewPassword" size="50"
							maxlength="50" onKeyPress="return submitenter(this,event)" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>

						<td><html:button property="save" styleClass="button" onclick="javascript:validateForm();">
								<fmt:message key="button.save" />
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
