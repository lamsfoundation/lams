<%@ include file="/taglibs.jsp"%>

<h1 align="center">
	<img src="<lams:LAMSURL/>/images/css/lams_login.gif" 
		alt="LAMS - Learning Activity Management System" width="186" height="90" ></img>
</h1>

<c:if test="${not empty signupOrganisation}">
	<p>&nbsp;</p>
	<h1 align="center">
		<c:out value="${signupOrganisation.organisation.name}" /> 
		<c:if test="${not empty signupOrganisation.organisation.code}">
			(<c:out value="${signupOrganisation.organisation.code}" />)
		</c:if>
	</h1>
	<c:if test="${not empty signupOrganisation.blurb}">
		<p>&nbsp;</p>
		<p>
			<c:out value="${signupOrganisation.blurb}" />
		</p>
	</c:if>
</c:if>
<p>
	If you already have an account, please proceed to the <a href="<lams:LAMSURL />">login</a> page.
</p>

<c:if test="${not empty error}">
	<p class="warning"><c:out value="${error}" /></p>
</c:if>

<html:form action="/register.do" method="post">
	<html:hidden property="method" value="register" />
	<html:hidden property="submitted" value="1" />
	<html:hidden property="context" value="${signupOrganisation.context}" />
	
	<table border="0" cellspacing="1" cellpadding="1" align="left">
		<tr>
			<td align="right">Username:	</td>
			<td align="left"><html:text property="username" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="username" /></td>
		</tr>
		<tr>
			<td align="right">Password:	</td>
			<td align="left"><html:text property="password" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="password" /></td>
		</tr>
		<tr>
			<td align="right">Confirm password:	</td>
			<td align="left"><html:text property="confirmPassword" size="40" maxlength="255" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">First name:	</td>
			<td align="left"><html:text property="firstName" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="firstName" /></td>
		</tr>
		<tr>
			<td align="right">Last name:	</td>
			<td align="left"><html:text property="lastName" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="lastName" /></td>
		</tr>
		<tr>
			<td align="right">Email:	</td>
			<td align="left"><html:text property="email" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="email" /></td>
		</tr>
		<tr>
			<td align="right">Confirm email:	</td>
			<td align="left"><html:text property="confirmEmail" size="40" maxlength="255" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Course key:	</td>
			<td align="left"><html:password property="courseKey" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="courseKey" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><html:submit styleClass="button">Submit</html:submit></td>
		</tr>
	</table>

</html:form>