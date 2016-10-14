<%@page import="org.lamsfoundation.lams.web.PasswordChangeActionForm"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<script type="text/javascript"
	src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript"
	src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
<link rel="stylesheet" href="css/defaultHTML_learner.css"
	type="text/css" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script type="text/javascript">
	function checkPasswordField(field) {
		if (/^\s*$/.test(field.value)) {
			alert("Password cannot be empty");
		}
	};
</script>


<html:form action="/passwordChanged" method="post">


	<div style="clear: both"></div>

	<div class="container">
		<div class="row vertical-center-row">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<fmt:message key="title.password.change.screen" />
						</div>
					</div>
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



