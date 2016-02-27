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

<!DOCTYPE html>
<lams:html>

<lams:head>
	<lams:css />
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script language="javascript" type="text/javascript">
	<!--
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
	//-->
	</script>
</lams:head>

<c:set var="title">
	<fmt:message key="title.forgot.password" />
</c:set>


<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<form action="<lams:LAMSURL/>/ForgotPasswordRequest" method="get" name="changePass">
			<input type="hidden" name="method" id="method" value="requestPasswordChange" /> <input type="hidden" name="key"
				id="key" value="<%=key%>" />

			<h4>
				<fmt:message key="label.forgot.password" />
			</h4>

			<div class="form-group">
				<label for="newPassword"><fmt:message key="label.password.new.password" /></label> <input type="password"
					id="newPassword" name="newPassword" class="form-control" maxlength="50" onKeyPress="return submitenter(this,event)" />
			</div>
			<div class="form-group">
				<label for="confirmNewPassword"><fmt:message key="label.password.confirm.new.password" /></label> <input
					type="password" id="confirmNewPassword" name="confirmNewPassword" maxlength="50"
					onKeyPress="return submitenter(this,event)" />
			</div>
			<div class="form-group">
			<html:button property="save" styleClass="btn btn-primary pull-right" onclick="javascript:validateForm();">
					<fmt:message key="button.save" />
				</html:button>
			</div>

		</form>


	</lams:Page>

</body>

</lams:html>
