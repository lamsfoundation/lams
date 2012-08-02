	<div>	
		<html:form action="/signup/signup.do" method="post">
			<html:hidden property="method" value="register" />
			<html:hidden property="submitted" value="1" />
			<html:hidden property="context" value="${signupOrganisation.context}" />
			<html:hidden property="selectedTab" value="0" />
			
			<table>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.username"/>:	</td>
					<td width="30%"><html:text property="username" size="40" maxlength="255" /></td>
					<td><html:errors property="username" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.password"/>:	</td>
					<td><html:text property="password" size="40" maxlength="255" /></td>
					<td><html:errors property="password" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.confirm.password"/>:	</td>
					<td><html:text property="confirmPassword" size="40" maxlength="255" /></td>
					<td></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.first.name"/>:	</td>
					<td><html:text property="firstName" size="40" maxlength="255" /></td>
					<td><html:errors property="firstName" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.last.name"/>:	</td>
					<td><html:text property="lastName" size="40" maxlength="255" /></td>
					<td><html:errors property="lastName" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.email"/>:	</td>
					<td><html:text property="email" size="40" maxlength="255" /></td>
					<td><html:errors property="email" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.confirm.email"/>:	</td>
					<td><html:text property="confirmEmail" size="40" maxlength="255" /></td>
					<td></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="signup.course.key"/>:	</td>
					<td><html:password property="courseKey" size="40" maxlength="255" /></td>
					<td><html:errors property="courseKey" /></td>
				</tr>
				<tr>
					<td></td>
					<td class="align-right"><html:submit styleClass="button"><fmt:message key="signup.submit"/></html:submit></td>
					<td></td>
				</tr>
			</table>
		
		</html:form>
	</div>