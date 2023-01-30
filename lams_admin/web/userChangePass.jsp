<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:css/>

<%-- javascript --%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
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
		return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
				.test(value)
	});

	$(function() {
		// Setup form validation 
		$("#userForm").validate({
							errorClass : 'help-block',
							//  validation rules
							rules : {
								login : {
									required: true,
									maxlength : 50
								},
								password : {
									required: true,
									minlength : <c:out value="${minNumChars}"/>,
									maxlength : 50,
									charactersAllowed : true,
									pwcheck : true
									 
								},
								password2 : {
									equalTo : "#password"
								}
								
							},

							// Specify the validation error messages
							messages : {
								login : {
									required: "<fmt:message key='error.login.required'/>"
								},
								password : {
									required : "<fmt:message key='error.password.empty'/>",
									minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
									maxlength : "<fmt:message key='label.password.max.length'/>",
									charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
									pwcheck : "<fmt:message key='label.password.restrictions'/>"
								},
								password2: {
									equalTo : "<fmt:message key='error.password.mismatch'/>"
								}
								
							},

							submitHandler : function(form) {
								form.submit();
							}
		});

	});
</script>


<body class="stripes">
	<form id="userForm" modelAttribute="userForm" action="usersave/changePass.do" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="userId" value='<c:out value="${param.userId}" />' />
		<div class="panel panel-default">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel voffset20">

					<div class="panel panel-default">

						<div class="panel-heading">
							<div class="panel-title">
								<fmt:message key="admin.user.edit" />
							</div>
						</div>
					</div>
					<div class="panel-body">
						<lams:Alert type="info" id="passwordConditions" close="false">
							<fmt:message key='label.password.must.contain' />:
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
								
								<li><span class="fa fa-check"></span>
									<fmt:message key='label.password.user.details' />
								</li>
								<li><span class="fa fa-check"></span>
									<fmt:message key='label.password.common' />
								</li>
							</ul>
						</lams:Alert>
						
						<lams:errors path="password"/>
						
						<div>
							<label for="login"><fmt:message key="admin.user.login" />:</label>
							<span><c:out value="${param.login}" /></span>
						</div>
						<div class="form-group">
							<label for="password"><fmt:message
									key="admin.user.password" />:</label> <input type="password"
								name="password" maxlength="50" id="password"
								class="form-control" />
						</div>
						<div class="form-group">
							<label for="password2"><fmt:message
									key="admin.user.password.confirm" />:</label> <input type="password"
								name="password2" maxlength="50" id="password2"
								class="form-control" />
						</div>
						<div class="pull-right">
							<a href="javascript:history.back();" class="btn btn-default">
								<fmt:message key="admin.cancel" />
							</a> <input type="submit" id="saveButton" class="btn btn-primary"
								value="<fmt:message key="admin.save" />" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
