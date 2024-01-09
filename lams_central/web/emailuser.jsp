<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<title><fmt:message key="title.admin.window" /></title>
<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico"	type="image/x-icon" />
<lams:css />
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
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/instantedit.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript">
	var returnUrl = '<c:out value="${param.returnUrl}" />';

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
			if (window.top.$("#dialogEmail").length > 0) {
				//clicking the button is added in order to circumwent the problem with the issue with hiding dialog using window.top.$("#dialogEmail").modal('hide');
				window.top.$("#dialogEmail .close").click();
			} else {
				// if this is a pop up, close it
				close();
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
<body class="stripes">
	<div
		class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
		<div class="panel panel-default">
			<div class="panel-heading">
				<fmt:message key="email.compose.mail" />
			</div>
			<lams:errors/>	
			<div class="panel-body">
				<form id="emailUserForm"
					action="<lams:LAMSURL/>emailUser/send.do" method="post">
					<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
					<c:if test="${not empty user}">
						<input type="hidden" name="userId" value="${user.userId}" />
					</c:if>
					<input type="hidden" name="lessonID" value="${param.lessonID}" />
					<input type="hidden" name="ccEmail" id="cc-email-hidden-input"
						value="" /> <span><label><fmt:message
								key="email.to" />:</label> <c:if test="${not empty user}">
							<lams:Portrait userId="${user.userId}"/> 
							<c:out value="${user.getFullName()}" /> &lt;<c:out
								value="${user.email}" />&gt;
							 </c:if> </span>

					<div class="form-group">
						<input type="checkbox" name="ccEmailEnabled" id="cc-email-enabled" />
						<label for="cc-email-enabled"> <fmt:message
								key="label.email.send.me.a.copy" /></label>
					</div>
					<div id="cc-email-area" class="form-group">
						<span id="cc-email-address"><lams:user property="email" /></span>
						<a href="javascript:;" class="btn btn-sm btn-default"
							id="edit-email-address"> <fmt:message key="label.edit" />
						</a>
					</div>

					<div class="form-group">
						<label><fmt:message key="email.subject" /></label> <input
							type="text" name="subject" class="form-control"
							<c:if test="${sendDisabled}">disabled="disabled"</c:if>>
					</div>

					<div class="form-group">
						<textarea name="body" rows="15" class="form-control"
							<c:if test="${sendDisabled}">disabled="disabled"</c:if>></textarea>
					</div>

					<div class="pull-right">
						<a href="#" onclick="javascript:closeDialog()"
							class="btn btn-sm btn-default"> <fmt:message
								key="button.cancel" />
						</a>
						<c:if test="${not sendDisabled}">
							<a href="#" onclick="javascript:sendEmail()"
								class="btn btn-sm btn-default"> <fmt:message
									key="email.send" />
							</a>
						</c:if>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>