<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/groupDisplay.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery.validate.js"></script>



<script type="text/javascript">
	$.validator.addMethod("pwcheck", function(value) {
		return /[a-z]/.test(value) // has a lowercase letter
		<c:if test="${mustHaveUppercase}"> && /[A-Z]/.test(value) // has uppercase letters 
		</c:if>
		<c:if test="${mustHaveNumerics}"> && /\d/.test(value) // has a digit
		</c:if>
		<c:if test="${mustHaveSymbols}">
				&& /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value) //has symbols
		</c:if>
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
	$.validator
			.addMethod(
					"emailCheck",
					function(value) {
						return /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
								.test(value)

					});

	$(function() {
		// Setup form validation 

		$("#SignupForm")
				.validate(
						{
							debug : true,
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
									required : true,
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
									required : "<fmt:message key='error.password.empty'/>",
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
							},
							submitHandler : function(form) {
								form.submit();
							}
						});

	});
</script>





<div>
	<html:form styleId="SignupForm" action="/signup/signup.do"
		method="post">
		<html:hidden property="method" value="register" />
		<html:hidden property="submitted" value="1" />
		<html:hidden property="context" value="${signupOrganisation.context}" />
		<html:hidden property="selectedTab" value="0" />

		<table>

			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.username" />:</td>
				<td width="30%"><html:text property="username" size="40"
						maxlength="255" /></td>
				<td><html:errors property="username" /> <span
					style="display: none;'" class="msg error"> <fmt:message
							key="error.username.invalid.characters" /></span></td>
			</tr>

			<tr>

				<td class="table-row-caption"><fmt:message
						key='label.password.must.contain' />: <lams:Alert type="info"
						id="passwordConditions" close="false"></td>
				<td width="30%">
					<ul class="list-unstyled" style="line-height: 1.2">
						<li><span class="fa fa-check"></span> <fmt:message
								key='label.password.min.length'>
								<fmt:param value='${minNumChars}' />
							</fmt:message></li>

						<c:if test="${mustHaveUppercase}">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.must.ucase' /></li>
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
				</td>
				</lams:Alert>

			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.password" />:</td>
				<td><html:password property="password" size="40"
						styleClass="form-control" maxlength="25" /></td>
				<td><html:errors property="password" /></td>

			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.confirm.password" />:</td>
				<td><html:password property="confirmPassword" size="40"
						maxlength="25" /></td>
				<td><span style="display: none;'" class="confirmPassword error"><fmt:message
							key="error.passwords.unequal" /></span></td>
			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.first.name" />:</td>
				<td><html:text property="firstName" size="40" maxlength="255" /></td>
				<td><html:errors property="firstName" /> <span
					style="display: none;'" class="first error"><fmt:message
							key="error.firstname.invalid.characters" /></span></td>
			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.last.name" />:</td>
				<td><html:text property="lastName" size="40" maxlength="255" /></td>
				<td><html:errors property="lastName" /> <span
					style="display: none;'" class="last error"><fmt:message
							key="error.lastname.invalid.characters" /></span></td>
			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message key="signup.email" />:
				</td>
				<td><html:text property="email" size="40" maxlength="255" /></td>
				<td><html:errors property="email" /><span
					style="display: none;'" class="email error"><fmt:message
							key="error.email.invalid.format" /></span></td>
			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.confirm.email" />:</td>
				<td><html:text property="confirmEmail" size="40"
						maxlength="255" /></td>
				<td><span style="display: none;'" class="confirmEmail error"><fmt:message
							key="error.emails.unequal" /></span></td>
			</tr>
			<tr>
				<td class="table-row-caption"><fmt:message
						key="signup.course.key" />:</td>
				<td><html:password property="courseKey" size="40"
						maxlength="255" /></td>
				<td><html:errors property="courseKey" /></td>
			</tr>
			<tr>
				<td></td>
				<td class="align-right"><html:submit>
						<fmt:message key="signup.submit" />
					</html:submit></td>
				<td></td>
			</tr>
		</table>

	</html:form>
</div>
