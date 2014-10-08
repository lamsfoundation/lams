<%@ include file="/taglibs.jsp"%>

<h4>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :
	<a href="signupManagement.do"><fmt:message key="admin.signup.title" /></a>
</h4>

<h1><fmt:message key="admin.add.edit.signup.page" /></h1>

<html:form action="/signupManagement.do" method="post">
	<html:hidden property="method" value="add" />
	<html:hidden property="signupOrganisationId" />
	
	<table>
		<tr>
			<td style="width: 250px;"><fmt:message key="admin.group" />:</td>
			<td>
				<html:select property="organisationId">
					<c:forEach items="${organisations}" var="organisation">
						<html:option value="${organisation.organisationId}"><c:out value="${organisation.name}" /></html:option>
					</c:forEach>
				</html:select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.lessons" />:</td>
			<td><html:checkbox property="addToLessons" /></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.staff" />:</td>
			<td><html:checkbox property="addAsStaff" /></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.course.key" />:</td>
			<td><html:text property="courseKey" size="40" maxlength="255" /></td>
			<td><html:errors property="courseKey" /></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.confirm.course.key" />:</td>
			<td><html:text property="confirmCourseKey" size="40" maxlength="255" /></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.description.txt" />:</td>
			<td><html:textarea property="blurb" cols="40" rows="3"/></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.disable.option" />:</td>
			<td><html:checkbox property="disabled" /></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.login.tab" />:</td>
			<td><html:checkbox property="loginTabActive" /></td>
			<td></td>
		</tr>		
		<tr>
			<td><fmt:message key="admin.context.path" />:</td>
			<td style="vertical-align: middle;"><lams:LAMSURL/>signup/<html:text property="context" /></td>
			<td><html:errors property="context" /></td>
		</tr>
		<tr>
			<td></td>
			<td style="padding-left: 80px; padding-top: 15px;">
				<html:cancel styleId="cancelButton" styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
				<html:submit styleId="submitButton" styleClass="button"><fmt:message key="admin.submit" /></html:submit>
			</td>
			<td></td>
		</tr>
	</table>

</html:form>
