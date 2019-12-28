<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.signup.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.add.edit.signup.page"/></c:set>
	<lams:Page type="admin" title="${title}" formID="signupForm">
	
		<div>
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
			<a href="<lams:LAMSURL/>admin/signupManagement/start.do" class="btn btn-default loffset5"><fmt:message key="admin.signup.title" /></a>
		</div>
		<c:set var="csrfToken"><csrf:token/></c:set>	
		<form:form action="add.do?${csrfToken}" modelAttribute="signupForm" id="signupForm" method="post">
				<form:hidden path="signupOrganisationId" />
				
				<table class="table table-condensed table-no-border">
					<tr>
						<td style="width: 250px;"><fmt:message key="admin.group" />:</td>
						<td>
							<form:select path="organisationId" cssClass="form-control">
								<c:forEach items="${organisations}" var="organisation">
									<form:option value="${organisation.organisationId}"><c:out value="${organisation.name}" /></form:option>
								</c:forEach>
							</form:select>
						</td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.lessons" />:</td>
						<td><form:checkbox path="addToLessons" /></td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.staff" />:</td>
						<td><form:checkbox path="addAsStaff" /></td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.email.verify" />:</td>
						<td colspan="2">
							<form:checkbox path="emailVerify" />&nbsp;&nbsp;
							<span class="signupFieldDescription">
								<fmt:message key="admin.email.verify.desc" />
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<lams:errors path="courseKey"/>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="admin.course.key" />:</td>
						<td><form:input path="courseKey" size="40" maxlength="255" cssClass="form-control"/></td>
					<tr>
						<td><fmt:message key="admin.confirm.course.key" />:</td>
						<td><form:input path="confirmCourseKey" size="40" maxlength="255" cssClass="form-control"/></td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.description.txt" />:</td>
						<td>
						  <lams:CKEditor id="blurb" 
						     value="${signupForm.blurb}" 
						     contentFolderID="../public/signups">
						  </lams:CKEditor>
						</td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.disable.option" />:</td>
						<td><form:checkbox path="disabled" /></td>
						<td></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.login.tab" />:</td>
						<td><form:checkbox path="loginTabActive" /></td>
						<td></td>
					</tr>		
					<tr>
						<td colspan="2">
						<lams:errors path="context"/>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="admin.context.path" />:</td>
						<td style="vertical-align: middle;"><lams:LAMSURL/>signup/<form:input path="context" /></td>
					</tr>
				</table>
				
				<div class="pull-right">
					<a href="<lams:LAMSURL/>admin/signupManagement/start.do" class="btn btn-default loffset5"><fmt:message key="admin.cancel" /></a>
					<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
				</div>
			
		</form:form>
	</lams:Page>
</body>
</lams:html>

