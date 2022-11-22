<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.add.edit.signup.page"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4 pt-2">
	<c:set var="title"><fmt:message key="admin.add.edit.signup.page"/></c:set>
	
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbChild1"><lams:LAMSURL/>admin/signupManagement/start.do | <fmt:message key="admin.signup.title"/></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="admin.add.edit.signup.page"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbChild1}, ${breadcrumbActive}"/>

	
	<lams:Page5 type="admin" title="${title}" formID="signupForm" breadcrumbItems="${breadcrumbItems}">
	
		<form:form action="add.do" modelAttribute="signupForm" id="signupForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="signupOrganisationId" />
				
			<div class="row">
				<div class="col-6 offset-3">
					<div class="mb-3">
						<label for="selector" class="form-label"><fmt:message key="admin.group" /></label>:
						<form:select id="selector" path="organisationId" cssClass="form-control">
							<c:forEach items="${organisations}" var="organisation">
								<form:option value="${organisation.organisationId}"><c:out value="${organisation.name}" escapeXml="true" /></form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="form-check mb-2">
						<form:checkbox path="addToLessons" id="addToLessons" cssClass="form-check-input"/>
						<label class="form-check-label" for="addToLessons"><fmt:message key="admin.lessons" /></label>
					</div>	
					<div class="form-check mb-2">
						<form:checkbox id="addAsStaff" path="addAsStaff" cssClass="form-check-input"/>
						<label class="form-check-label" for="addToLessons"><fmt:message key="admin.staff" /></label>
					</div>
					<div class="form-check mb-2">
						<form:checkbox id="emailVerify" path="emailVerify" cssClass="form-check-input" />
						<label class="form-check-label" for="emailVerify"><fmt:message key="admin.email.verify" /></label>
						<small id="passwordHelpBlock" class="form-text text-muted">
									<fmt:message key="admin.email.verify.desc" />
						</small>
					</div>
					<div class="mb-3">
						<label for="courseKey" class="form-label"><fmt:message key="admin.course.key" /></label>
						<input value="${signupForm.courseKey}" required id="courseKey" name="courseKey" maxlength="20" class="form-control"/>
						<small id="passwordHelpBlock" class="form-text text-muted">
								<lams:errors path="courseKey"/>
						</small>	
					</div>
					<div class="mb-3">
						<label for="confirmCourseKey" class="form-label"><fmt:message key="admin.confirm.course.key" /></label>
						<input value="${signupForm.courseKey}" id="confirmCourseKey"  name="confirmCourseKey" required  maxlength="20" class="form-control"/>
					</div>
					<div class="mb-3">
						<label for="blurb" class="form-label"><fmt:message key="admin.description.txt" /></label>
						  <lams:CKEditor id="blurb" 
						     value="${signupForm.blurb}" 
						     contentFolderID="../public/signups">
						  </lams:CKEditor>
					</div>
					<div class="form-check mb-2">
						<form:checkbox path="disabled" cssClass="form-check-input"/>
						<label class="form-check-label" for="disabled"><fmt:message key="admin.disable.option" /></label>
					</div>
					<div class="form-check mb-2">
						<form:checkbox id="loginTabActive" path="loginTabActive" cssClass="form-check-input"/>
						<label class="form-check-label" for="loginTabActive"><fmt:message key="admin.login.tab" /></label>
					</div>
					<div class="mb-3">
						<label for="context" class="form-label"><fmt:message key="admin.context.path" /></label>:</br>
						<lams:LAMSURL/>signup/<input id="context" value="${signupForm.context}" name="context" required class="form-control form-control-inline"/>
						<small id="passwordHelpBlock" class="form-text text-danger">
								<lams:errors path="context"/>
						</small>				
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-6 offset-3 text-end">
					<a href="<lams:LAMSURL/>admin/signupManagement/start.do" class="btn btn-secondary"><fmt:message key="admin.cancel" /></a>
					<input type="submit" id="saveButton" class="btn btn-primary" value="<fmt:message key="admin.save" />" />
				</div>
			</div>
		</form:form>
	</lams:Page5>
</body>
</lams:html>

