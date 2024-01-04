<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.PasswordChangeActionForm"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
<c:set var="passwordHistoryLimit"><%=Configuration.get(ConfigurationKeys.PASSWORD_HISTORY_LIMIT)%></c:set>
<c:set var="lams"><lams:LAMSURL/></c:set>
	
<lams:html>
<lams:head>
	<link rel="stylesheet" href="${lams}css/components.css">
    <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/profile.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript">
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
			
		$.validator.addMethod("charactersAllowed", function(value) {
			return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/.test(value)
		});
	
		$(document).ready(function () {
		  // Setup form validation 
		  $("#change-password").validate({
			  validClass: "is-valid",
		  	  errorClass: 'is-invalid',
		      //  validation rules
		      rules: {
		          oldPassword: "required",
		          password: {
		              required: true,
		              minlength: <c:out value="${minNumChars}"/>,
		              maxlength: 50,
		              charactersAllowed: true,
		              pwcheck: true              
		          },
		          passwordConfirm: {
		        	  required: true,
		        	  equalTo: "#password"
		          }
		      },
		      
		      // Specify the validation error messages
			  messages: {
				oldPassword: {
					required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.password.old.must.entered'/></spring:escapeBody>"
				},
				password: {
					required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.password.empty'/></spring:escapeBody>",
					minlength: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message></spring:escapeBody>",
					maxlength: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.password.max.length'/></spring:escapeBody>",
					charactersAllowed: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.password.symbols.allowed'/></spring:escapeBody>",
					pwcheck: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.password.restrictions'/></spring:escapeBody>"
				},
				passwordConfirm: {
					required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.password.empty'/></spring:escapeBody>",
					equalTo: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.newpassword.mismatch'/></spring:escapeBody>"
				}
			},
		      
		    submitHandler: function(form) {
		    	form.submit();
		    }
		});
		
		
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.password.change.screen" />', '550');
		});	
	</script>
</lams:head>

<body>
	<form:form modelAttribute="passwordChangeActionForm" id="change-password" method="post" action="/lams/passwordChanged.do">
		<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" />' />
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="passwordExpired" />
		<form:hidden path="login" />
	
		<div class="container">
			<c:if test="${passwordChangeActionForm.passwordExpired}">
				<lams:Alert5 type="warning" id="error-messages">
					<fmt:message key="label.password.expired" />
				</lams:Alert5>
			</c:if>
			
			<div class="col-12 col-sm-8 col-sm-offset-2 mt-3 mx-auto">
				<lams:errors5/>
				<div class="mb-3">
					<!--<label for="oldPassword" class="form-label">
						<fmt:message key="label.password.old.password" />
					</label>-->
	 				<input class="form-control" type="password" maxlength="50" placeholder="<fmt:message key="label.password.old.password" />" name="oldPassword" id="oldPassword" autocomplete="current-password"/>
	 				<lams:errors5 path="oldPassword"/>				
				</div>
							
				<lams:Alert5 type="info"  id="passwordConditions">
					<strong><fmt:message key='label.password.must.contain'/>:</strong> 
								 
					<ul class="list-unstyled mb-0" style="line-height: 1.2">
						<li>
							<span class="fa fa-check"></span> 
							<fmt:message key='label.password.min.length'>
								<fmt:param value='${minNumChars}'/>
							</fmt:message>
						</li>
						<c:if test="${mustHaveUppercase}">
							<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.ucase'/></li>
						</c:if>
						<c:if test="${mustHaveLowercase}">
						    <li><span class="fa fa-check"></span> <fmt:message key='label.password.must.lcase' /></li>
						</c:if>
						<c:if test="${mustHaveNumerics}">
							<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.number'/></li>
						</c:if>	
						<c:if test="${mustHaveSymbols}">
							<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.symbol'/></li>
						</c:if>
						<li><span class="fa fa-check"></span>
							<fmt:message key='label.password.user.details' />
						</li>
						<li><span class="fa fa-check"></span>
							<fmt:message key='label.password.common' />
						</li>
						<c:if test="${passwordHistoryLimit > 0}">
							<li><span class="fa fa-check"></span>
								<fmt:message key='label.password.history'>
									<fmt:param value="${passwordHistoryLimit}" />
								</fmt:message>
							</li>
						</c:if>
					</ul>
				</lams:Alert5> 
							
 				<div class="input-group mb-3">
					<span class="input-group-text"><i class="fa fa-lock"></i></span>
					<input class="form-control" type="password"  autocomplete="new-password" 
							placeholder="<fmt:message key='label.password.new.password' />" id="password" name="password" maxlength="50"/>
					<lams:errors5 path="password"/>	 			
				</div>
				<div class="input-group mb-3">
					<span class="input-group-text"><i class="fa fa-lock"></i></span>
					<input class="form-control" type="password" id="passwordConfirm" name="passwordConfirm" autocomplete="new-password" 
							placeholder="<fmt:message key='label.password.confirm.new.password' />" maxlength="50"/>
				</div>
				
				<div class="float-end mb-4 mt-3">
					<c:if test="${not passwordChangeActionForm.passwordExpired}">
						<button type="button" id="cancelButton" class="btn btn-sm btn-secondary me-2" onclick="history.go(-1);">
							<i class="fa-solid fa-ban me-1"></i>
							<fmt:message key="button.cancel"/>
						</button>
					</c:if>
					
					<button id="saveButton" type="submit" class="btn btn-sm btn-primary">
						<i class="fa-regular fa-floppy-disk me-1"></i> 
						<fmt:message key="button.save" />
					</button>
				</div>
			</div>
		</div>
	</form:form>
</body>
</lams:html>
