<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>

<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
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
	$.validator.addMethod("charactersNotAllowed", function(value) {
		return /^[^<>^!#&()/\\|\"?,:{}= ~`*%$]*$/.test(value)

	});

	$.validator.addMethod("charactersNotAllowedName", function(value) {
		return /^[^<>^*@%$]*$/.test(value)

	});
	$.validator.addMethod("emailCheck", function(value) {
		return /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
			.test(value)
	});
	$.validator.addMethod("notEqualTo", function(value, element, param) {
		return this.optional(element) || value != param;
	}, "Please specify a different (non-default) value");

	$(function() {
		// Setup form validation 
		$("#SignupForm").validate({
							errorClass : 'help-block',
							//  validation rules
							rules : {
								password : {
									required : true,
									minlength : <c:out value="${minNumChars}"/>,
									maxlength : 25,
									charactersAllowed : true,
									pwcheck : true
								},
								confirmPassword : {
									equalTo : $('form input[name="password"]')
								},

								username : {
									required : true,
									maxlength : 255,
									charactersNotAllowed : true
								},
								lastName : {
									required : true,
									maxlength : 255,
									charactersNotAllowedName : true
								},
								firstName : {
									required : true,
									maxlength : 255,
									charactersNotAllowedName : true
								},
								email : {
									required : true,
									maxlength : 255,
									emailCheck : true
								},
								confirmEmail : {
									required : true,
									maxlength : 255,
									equalTo : $('form input[name="email"]')
								},
								country : {
									required: true,
									notEqualTo: "0"
								}

							},

							// Specify the validation error messages
							messages : {
								password : {
									required : "<fmt:message key='error.password.empty'/>",
									minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
									maxlength : "<fmt:message key='label.password.max.length'/>",
									charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
									pwcheck : "<fmt:message key='label.password.restrictions'/>"
								},
								confirmPassword : {
									equalTo : "<fmt:message key='error.passwords.unequal'/>"
								},
								username : {
									charactersNotAllowed : "<fmt:message key='error.username.invalid.characters'/>"
								},
								lastName : {
									charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
								},
								firstName : {
									charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
								},
								email : {
									emailCheck : "<fmt:message key='error.email.invalid.format'/>"
								},
								confirmEmail : {
									equalTo : "<fmt:message key='error.emails.unequal'/>"
								},
								country: {
									required: "<fmt:message key='error.country.required'/>",
									notEqualTo: "<fmt:message key='error.country.required'/>"
								}
							},
							submitHandler : function(form) {
								form.submit();
							},
							invalidHandler : function(){
								$('#submitButton').button('reset');
							}
						});

	});
</script>
<div>
	<form:form modelAttribute="SignupForm" id="SignupForm" name="SignupForm" method="post" action="/lams/signup/signup.do" novalidate="novalidate">
		<form:hidden path="submitted" value="1" />
		<form:hidden path="context" value="${signupOrganisation.context}" />
		<div class="container">
			<div class="row vertical-center-row">
				<div
					class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
					<div class="panel">
						<div class="panel-body">

							<div class="form-group">
								<label for="username"><fmt:message key="signup.username" /></label>:
								<input id="username" name="username" class="form-control" type="text" value="" size="40" maxlength="255" autocomplete="username">
								<lams:errors path="username"/>
								<span style="display: none;'" class="msg error"> <fmt:message
										key="error.username.invalid.characters" /></span>
							</div>

							<div class="col-xs-12">
								<lams:Alert type="info" id="passwordConditions" close="false">
									<strong><fmt:message key='label.password.must.contain' />:</strong>
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
								<label for="password"><fmt:message key="signup.password" /></label>:

								<input id="password" name="password" class="form-control" type="password" value="" size="40" maxlength="25" autocomplete="new-password">
								<lams:errors path="password"/>
							</div>

							<div class="form-group">
								<label for="confirmPassword"><fmt:message key="signup.confirm.password" /></label>:

								<input id="confirmPassword" name="confirmPassword" class="form-control" type="password" value="" size="40" maxlength="25" autocomplete="new-password">
								<span style="display: none;'" class="confirmPassword error"><fmt:message
										key="error.passwords.unequal" /></span>
							</div>
							<div class="form-group">
								<label for="firstName"><fmt:message key="signup.first.name" /></label>:

								<input id="firstName" name="firstName" class="form-control valid" type="text" value="" size="40" maxlength="255" autocomplete="given-name">
								<lams:errors path="firstName"/>
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>
							</div>

							<div class="form-group">
								<label for="lastName"><fmt:message key="signup.last.name" /></label>:

								<input id="lastName" name="lastName" class="form-control valid" type="text" value="" size="40" maxlength="255" autocomplete="family-name">
								<lams:errors path="lastName"/>
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							<div class="form-group">
								<label for="email"><fmt:message key="signup.email" /></label>:

								<input id="email" name="email" class="form-control" type="text" value="" size="40" maxlength="255" autocomplete="email">
								<lams:errors path="email"/>
								<span style="display: none;'" class="email error"><fmt:message
										key="error.email.invalid.format" /></span>
							</div>

							<div class="form-group">
								<label for="confirmEmail"><fmt:message key="signup.confirm.email" /></label>:

								<input id="confirmEmail" name="confirmEmail" class="form-control" type="text" value="" size="40" maxlength="255" autocomplete="email">
								<span style="display: none;'" class="confirmEmail error"><fmt:message
										key="error.emails.unequal" /></span>
							</div>

							<div class="form-group">
								<label for="country">
									<fmt:message key="label.country" />
								</label>:

								<form:select path="country" cssClass="form-control">
									<form:option value="0">
										<fmt:message key="label.select.country" />
									</form:option>
									
									<c:forEach items="${countryCodes}" var="countryCode">
										<form:option value="${countryCode.key}">
											${countryCode.value}
										</form:option>
									</c:forEach>
								</form:select>
								<lams:errors path="country"/>
							</div>
							
							<div class="form-group">
								<label for="courseKey"><fmt:message key="signup.course.key" /></label>:

								<input id="courseKey" name="courseKey" class="form-control" type="text" value="" size="40" maxlength="255" autocomplete="one-time-code">
								<lams:errors path="courseKey"/>
							</div>

							<div class="form-group" align="right">
								<button id="submitButton"
										class="btn btn-sm btn-default voffset5"
									    data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='login.submit' /></span>"
									    onClick="javascript:$(this).button('loading');$('#SignupForm').submit()"
								>
									<fmt:message key="login.submit" />
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</div>
