<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.apache.struts.action.ActionMessages"
	import="org.lamsfoundation.lams.web.PasswordChangeActionForm"%>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="css/defaultHTML_learner.css" type="text/css" />

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/groupDisplay.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript">
		function checkPasswordField(field) {
			if (/^\s*$/.test(field.value)) {
				alert("Password cannot be empty");
			}
		};
		
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.password.change.screen" />', '430');
		});	
	</script>
</lams:head>

<body>
<html:form action="/passwordChanged" method="post">

	<div style="clear: both"></div>

	<div class="container">
		<div class="row vertical-center-row">
			<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel voffset20">
					<logic:messagesPresent message="true">
						<p class="warning">
							<html:messages message="true" id="errMsg">
								<bean:write name="errMsg" />
								<br>
							</html:messages>
						</p>
					</logic:messagesPresent>
					<div class="panel-body">
						<form class="form-horizontal">
							<html:hidden name="<%=PasswordChangeActionForm.formName%>"
								property="login" />
							<div class="form-group">
								<b><label><fmt:message
											key="label.password.old.password" />:</label></b>
								<html:password name="<%=PasswordChangeActionForm.formName%>"
									property="oldPassword" size="50" maxlength="50"
									onblur="checkPasswordField(this)" styleClass="form-control" />
							</div>

							<div class="form-group">
								<b><label><fmt:message
											key="label.password.new.password" />:</label></b>
								<html:password name="<%=PasswordChangeActionForm.formName%>"
									property="password" size="50" maxlength="50"
									onblur="checkPasswordField(this)" styleClass="form-control" />
							</div>
							<div class="form-group">
								<b><label><fmt:message
											key="label.password.confirm.new.password" />:</label></b>
								<html:password name="<%=PasswordChangeActionForm.formName%>"
									property="passwordConfirm" size="50" maxlength="50"
									onblur="checkPasswordField(this)" styleClass="form-control" />
							</div>

							<div class="form-group" align="right">
								<html:cancel styleClass="btn btn-sm btn-default voffset5">
									<fmt:message key="button.cancel" />
								</html:cancel>
								&nbsp;&nbsp;
								<html:submit styleClass="btn btn-sm btn-default voffset5">
									<fmt:message key="button.save" />
								</html:submit>

							</div>

						</form>
					</div>

				</div>
			</div>
		</div>
	</div>
</html:form>
</body>
</lams:html>
