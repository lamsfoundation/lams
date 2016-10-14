<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<title><fmt:message key="title.admin.window" /></title>
<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico"
	type="image/x-icon" />
<style type="text/css">
#cc-email-area {
	display: none;
}

input[type="checkbox"] {
	height: 30px;
	width: 20px;
	vertical-align: bottom;
}
</style>

<script type="text/javascript">
	//constant for instantedit.js
	var IS_AJAX_CALL_REQUIRED = false;
</script>
<script type="text/javascript" src="includes/javascript/jquery.js"></script>
<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
<script type="text/javascript" src="includes/javascript/instantedit.js"></script>
<link rel="stylesheet" href="css/defaultHTML_learner.css"
	type="text/css" />
<script type="text/javascript"
	src="/lams/includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript">
	var returnUrl = '${param.returnUrl}';

	function sendEmail() {

		//prepare ccEmail parameter if available
		if ($("#cc-email-enabled").prop('checked')) {
			var ccEmail = encodeURI($("#cc-email-address").html());
			$("#cc-email-hidden-input").val(ccEmail);
		}

		$('#emailUserForm').ajaxSubmit({
			'success' : function(result) {
				if (result) {
					$('#errorArea').text(result);
				} else {
					closeDialog();
				}
			}
		});
	}

	function closeDialog() {
		if (returnUrl == '') {
			// if this page is in dialog, close it
			if (parent.closeEmailDialog) {
				parent.closeEmailDialog();
			} else {
				// if this is a pop up, close it
				window.close();
			}
		} else {
			// if it is a main page, navigate away 
			document.location.href = returnUrl;
		}
	}

	$(document).ready(function() {

		//toggle cc email address 
		$('#cc-email-enabled').click(function() {
			$("#cc-email-area").toggle();
		});

		$('#edit-email-address').click(function() {
			editBox(document.getElementById("cc-email-address"));
		});

	});
</script>


<div style="clear: both;"></div>
<div class="container">
	<div class="row vertical-center-row">
		<div
			class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title">
						<fmt:message key="email.compose.mail" />
					</div>
				</div>
				<c:if test="${errorsPresent}">
					<div id="errorArea" class="warning">
						<html:messages id="error">
							<c:out value="${error}" escapeXml="false" />
							<br />
						</html:messages>
					</div>
				</c:if>
				<div class="panel-body">
					<form id="emailUserForm"
						action="<lams:LAMSURL/>emailUser.do?method=send" method="post">
						<c:if test="${not empty user}">
							<input type="hidden" name="userId" value="${user.userId}" />
						</c:if>
						<input type="hidden" name="lessonID" value="${param.lessonID}" />
						<input type="hidden" name="ccEmail" id="cc-email-hidden-input"
							value="" />
						<div class="form-group">
							<b><label><fmt:message key="email.to" />:</label></b>
							<c:if test="${not empty user}">
								<c:out value="${user.firstName} ${user.lastName}" /> &lt;<c:out
									value="${user.email}" />&gt;
						 </c:if>
						</div>



						<div>
							<div class="form-group">
								<input type="checkbox" name="ccEmailEnabled"
									id="cc-email-enabled" /> <b><label for="cc-email-enabled">Send
										me a copy</label></b>
							</div>
							<div id="cc-email-area" class="form-group">
								<span id="cc-email-address"><lams:user property="email" /></span>
								<sup> <a href="javascript:;"
									Class="btn btn-sm btn-default voffset5" id="edit-email-address">
										<fmt:message key="label.edit" />
								</a>
								</sup>&nbsp;&nbsp;


							</div>
						</div>

						<div class="form-group">
							<b><label><fmt:message key="email.subject" /></label></b> <input
								type="text" name="subject" class="form-control"
								<c:if test="${sendDisabled}">disabled="disabled"</c:if>>
						</div>

						<div class="form-group">
							<textarea name="body" rows="16" class="form-control"
								<c:if test="${sendDisabled}">disabled="disabled"</c:if>></textarea>
						</div>

						<div class="form-group" id="buttonsDiv" align="right">
							<a href="#" onclick="javascript:closeDialog()"
								class="btn btn-sm btn-default voffset5"> <fmt:message
									key="button.cancel" />
							</a>
							<c:if test="${not sendDisabled}">
								<a href="#" onclick="javascript:sendEmail()"
									class="btn btn-sm btn-default voffset5"> <fmt:message
										key="email.send" />
								</a>
							</c:if>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>


