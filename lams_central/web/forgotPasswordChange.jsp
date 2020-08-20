<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.validate.js"></script>
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script type="text/javascript">
		function toHome() {
			window.location = "<lams:LAMSURL/>index.do";
		};

		var mustHaveUppercase = ${mustHaveUppercase},
	    	mustHaveNumerics  = ${mustHaveNumerics},
	    	mustHaveLowercase  = ${mustHaveLowercase},
	    	mustHaveSymbols   = ${mustHaveSymbols};

	     $.validator.addMethod("pwcheck", function(value) {
	     	return (!mustHaveUppercase || /[A-Z]/.test(value)) && // has uppercase letters 
	    	(!mustHaveNumerics || /\d/.test(value)) && // has a digit
	    	(!mustHaveLowercase || /[a-z]/.test(value)) && // has a lower case
	    	(!mustHaveSymbols || /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value)); //has symbols
	     });

		$.validator.addMethod(
			"charactersAllowed",
			function(value) {
				return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
				.test(value)
			}
		);

		$(function() {
			// Setup form validation 
			$("form[name='changePass']").validate({
				errorClass : 'help-block',
				//  validation rules
				rules : {
					newPassword : {
						required : true,
						minlength : <c:out value="${minNumChars}"/>,
						maxlength : 25,
						charactersAllowed : true,
						pwcheck : true
					},
					confirmNewPassword : {
						required : true,
						equalTo : "#newPassword"
					}
				},

				// Specify the validation error messages
				messages : {
					newPassword : {
						required : "<fmt:message key='error.password.empty'/>",
						minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
						maxlength : "<fmt:message key='label.password.max.length'/>",
						charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
						pwcheck : "<fmt:message key='label.password.restrictions'/>"
					},
					confirmNewPassword : {
						required : "<fmt:message key='error.password.empty'/>",
						equalTo : "<fmt:message key='error.newpassword.mismatch'/>"
					},
				},

				submitHandler : function(form) {
					document.changePass.submit();
				}
			});
		});
	</script>
</lams:head>

<c:set var="title">
	<fmt:message key="title.forgot.password" />
</c:set>

<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<form action="<lams:LAMSURL/>ForgotPasswordRequest" method="post" name="changePass">
			<input type="hidden" name="method" id="method"	value="requestPasswordChange" />
			<input type="hidden" name="key"	id="key" value="<c:out value='${param.key}' />" />

			<h4>
				<fmt:message key="label.forgot.password" />
			</h4>
			
			<div class="col-xs-12">
				<lams:Alert type="info" id="passwordConditions" close="false">
					<strong><fmt:message key='label.password.must.contain' />:</strong>
					<c:out value="${mustHaveUppercase}" />
					<ul class="list-unstyled" style="line-height: 1.2">
						<li><span class="fa fa-check"></span> <fmt:message
								key='label.password.min.length'>
								<fmt:param value='${minNumChars}' />
							</fmt:message></li>
						<c:if test="${mustHaveUppercase}">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.must.ucase' /></li>
						</c:if>
						<c:if test="${mustHaveLowercase}">
						<li><span class="fa fa-check"></span> <fmt:message
								    key='label.password.must.lcase' /></li>
					    </c:if>
						<c:if test="${mustHaveNumerics}">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.must.number' /></li>
						</c:if>
						<c:if test="${mustHaveSymbols}">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.must.symbol' /></li>
						</c:if>
					</ul>
				</lams:Alert>
			</div>
			
			<div class="form-group">
				<label for="newPassword"><fmt:message
						key="label.password.new.password" /></label> <input class="form-control"
					type="password" id="newPassword" name="newPassword"
					class="form-control" maxlength="25" />
			</div>
			<div class="form-group">
				<label for="confirmNewPassword"><fmt:message
						key="label.password.confirm.new.password" /></label> <input
					type="password" id="confirmNewPassword" class="form-control"
					name="confirmNewPassword" maxlength="25" />
			</div>
			<div class="form-group" align="right">
				<button class="btn btn-sm btn-primary voffset5">
					<fmt:message key="button.save" />
				</button>
			</div>

		</form>


	</lams:Page>

</body>

</lams:html>
