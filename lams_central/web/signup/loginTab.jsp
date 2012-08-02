	<div>
		<html:form action="/signup/signup.do" method="post">
			<html:hidden property="method" value="login" />
			<html:hidden property="submitted" value="1" />
			<html:hidden property="context" value="${signupOrganisation.context}" />
			<html:hidden property="selectedTab" value="1" />
			
			<table>
				<tr>
					<td class="table-row-caption"><fmt:message key="login.username"/>:	</td>
					<td width="30%"><html:text property="usernameTab2" size="40" maxlength="255" /></td>
					<td><html:errors property="usernameTab2" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="login.password"/>:	</td>
					<td><input name="passwordTab2" type="password" size="40" maxlength="255" autocomplete="off"/></td>
					<td><html:errors property="passwordTab2" /></td>
				</tr>
				<tr>
					<td class="table-row-caption"><fmt:message key="login.course.key"/>:	</td>
					<td><html:password property="courseKeyTab2" size="40" maxlength="255"/></td>
					<td><html:errors property="courseKeyTab2" /></td>
				</tr>
				<tr>
					<td></td>
					<td class="align-right"><html:submit styleClass="button"><fmt:message key="login.submit"/></html:submit></td>
					<td></td>
				</tr>
			</table>
		
		</html:form>
	</div>