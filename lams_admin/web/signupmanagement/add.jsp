<%@ include file="/taglibs.jsp"%>

<div>
<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
<a href="signupManagement.do" class="btn btn-default loffset5"><fmt:message key="admin.signup.title" /></a>
</div>

<html:form action="/signupManagement.do" method="post">
	<html:hidden property="method" value="add" />
	<html:hidden property="signupOrganisationId" />
	
	<table class="table table-condensed table-no-border">
		<tr>
			<td style="width: 250px;"><fmt:message key="admin.group" />:</td>
			<td>
				<html:select property="organisationId" styleClass="form-control">
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
			<td colspan="2"><html:errors property="courseKey" /></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.course.key" />:</td>
			<td><html:text property="courseKey" size="40" maxlength="255"  styleClass="form-control"/></td>
		<tr>
			<td><fmt:message key="admin.confirm.course.key" />:</td>
			<td><html:text property="confirmCourseKey" size="40" maxlength="255"  styleClass="form-control"/></td>
			<td></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.description.txt" />:</td>
			<td>
			  <lams:CKEditor id="blurb" 
			     value="${signupForm.map.blurb}" 
			     contentFolderID="../public/signups">
			  </lams:CKEditor>
			</td>
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
			<td colspan="2"><html:errors property="context" /></td>
		</tr>
		<tr>
			<td><fmt:message key="admin.context.path" />:</td>
			<td style="vertical-align: middle;"><lams:LAMSURL/>signup/<html:text property="context" /></td>
		</tr>
	</table>
	
	<div class="pull-right">
				<html:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel" /></html:cancel>
				<html:submit styleId="submitButton" styleClass="btn btn-primary loffset5"><fmt:message key="admin.submit" /></html:submit>
	</div>

</html:form>