	<script type='text/javascript'>//<![CDATA[        
		$(function(){
			
			
			$('form input[name="username"]').blur(function () {
			    var username = $(this).val();
				var re = /^[^<>^!#&()/\\|\"?,:{}= ~`*%$]*$/;
				if (re.test(username)) {
				    $('.msg').hide();
				} else {
				    $('.msg').show();
				    $(this).focus();
				    $(this).select();
				}

			});

			$('form input[name="confirmPassword"]').blur(function () {
			    var password = $('form input[name="password"]').val();
			    var confirmPassword = $('form input[name="confirmPassword"]').val();
			    if (password != confirmPassword) {
				    $('.confirmPassword').show();
				    $('form input[name="password"]').focus();
				    $('form input[name="password"]').select();
				} else {
				    $('.confirmPassword').hide();
				}
		
			});	
			
			$('form input[name="lastName"]').blur(function () {
			    var lastName = $(this).val();
				var re = /^[^<>^*@%$]*$/;
				if (re.test(lastName)) {			    
				    $('.last').hide();
				} else {
				    $('.last').show();
				    $(this).focus();
				    $(this).select();
				}
		
			});

			$('form input[name="firstName"]').blur(function () {
			    var firstName = $(this).val();
				var re = /^[^<>^*@%$]*$/;
				if (re.test(firstName)) {
				    $('.first').hide();
				} else {
				    $('.first').show();
				    $(this).focus();
				    $(this).select();
				}
		
			});

			$('form input[name="email"]').blur(function () {
			    var email = $(this).val();
				var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				if (re.test(email)) {
				    $('.email').hide();
				} else {
				    $('.email').show();
				    $(this).focus();
				    $(this).select();
				}
		
			});
			
			$('form input[name="confirmEmail"]').blur(function () {
			    var email = $('form input[name="email"]').val();
			    var confirmEmail = $('form input[name="confirmEmail"]').val();
			    if (email != confirmEmail) {
				    $('.confirmEmail').show();
				    $(this).focus();
				    $(this).select();
				} else {
				    $('.confirmEmail').hide();
				}
		
			});	
			
		});
	//]]>  
	</script>
	<div>	

		<form id="SignupForm" name="SignupForm" method="post" action="/lams/signup/signup.do" novalidate="novalidate"  autocomplete="off">
			<c:set var="org.apache.struts.taglib.html.BEAN"  value="${SignupForm}" />
			<html:hidden property="method" value="register" />
			<html:hidden property="submitted" value="1" />
			<html:hidden property="context" value="${signupOrganisation.context}" />
			<html:hidden property="selectedTab" value="0" />
			
			<table>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.username"/>:	</td>
					<td width="30%"><html:text property="username" size="40" maxlength="255"/></td>
					<td><html:errors property="username" />  <span style="display: none;'" class="msg error"> <fmt:message key="error.username.invalid.characters"/></span></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.password"/>:	</td>
					<td><html:password property="password" size="40" maxlength="255" /></td>
					<td><html:errors property="password" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.confirm.password"/>:	</td>
					<td><html:password property="confirmPassword" size="40" maxlength="255" /></td>
					<td><span style="display: none;'" class="confirmPassword error"><fmt:message key="error.passwords.unequal"/></span></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.first.name"/>:	</td>
					<td><html:text property="firstName" size="40" maxlength="255" /></td>
					<td><html:errors property="firstName" /> <span style="display: none;'" class="first error"><fmt:message key="error.firstname.invalid.characters"/></span></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.last.name"/>:	</td>
					<td><html:text property="lastName" size="40" maxlength="255" /></td>
					<td><html:errors property="lastName" /> <span style="display: none;'" class="last error"><fmt:message key="error.lastname.invalid.characters"/></span> </td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.email"/>:	</td>
					<td><html:text property="email" size="40" maxlength="255" /></td>
					<td><html:errors property="email" /><span style="display: none;'" class="email error"><fmt:message key="error.email.invalid.format"/></span></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.confirm.email"/>:	</td>
					<td><html:text property="confirmEmail" size="40" maxlength="255" /></td>
					<td><span style="display: none;'" class="confirmEmail error"><fmt:message key="error.emails.unequal"/></span></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.course.key"/>:	</td>
					<td><html:password property="courseKey" size="40" maxlength="255" /></td>
					<td><html:errors property="courseKey"/></td>
				</tr>
				<tr>
					<td></td>
					<td class="align-right"><html:submit property="submit"><fmt:message key="signup.submit"/></html:submit></td>
					<td></td>
				</tr>
			</table>
		
		</form>
	</div>
