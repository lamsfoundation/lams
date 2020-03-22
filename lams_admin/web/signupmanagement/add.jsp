<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.add.edit.signup.page"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<c:set var="title"><fmt:message key="admin.add.edit.signup.page"/></c:set>
	<lams:Page type="admin" title="${title}" formID="signupForm">
	
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/signupManagement/start.do"><fmt:message key="admin.signup.title"/></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="admin.add.edit.signup.page"/></li>
		  </ol>
		</nav>			
	
		<form:form action="add.do" modelAttribute="signupForm" id="signupForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="signupOrganisationId" />
				
			<div class="form-group">
				<label for="selector"><fmt:message key="admin.group" /></label>:
				<form:select id="selector" path="organisationId" cssClass="form-control">
					<c:forEach items="${organisations}" var="organisation">
						<form:option value="${organisation.organisationId}"><c:out value="${organisation.name}" escapeXml="true" /></form:option>
					</c:forEach>
				</form:select>
			</div>
			<div class="form-group">
				<div class="form-check">
					<form:checkbox path="addToLessons" id="addToLessons" cssClass="form-check-input"/>
					<label class="form-check-label" for="addToLessons"><fmt:message key="admin.lessons" /></label>
				</div>	
				<div class="form-check">
					<form:checkbox id="addAsStaff" path="addAsStaff" cssClass="form-check-input"/>
					<label class="form-check-label" for="addToLessons"><fmt:message key="admin.staff" /></label>
				</div>
				<div class="form-check">
					<form:checkbox id="emailVerify" path="emailVerify" cssClass="form-check-input" />
					<label class="form-check-label" for="emailVerify"><fmt:message key="admin.email.verify" /></label>
					<small id="passwordHelpBlock" class="form-text text-muted">
						<fmt:message key="admin.email.verify.desc" />
					</small>
				</div>
			</div>
			<div class="form-group">
				<label for="courseKey"><fmt:message key="admin.course.key" /></label>
				<input value="${signupForm.courseKey}" required id="courseKey" name="courseKey" maxlength="20" class="form-control form-control-sm"/>
				<small id="passwordHelpBlock" class="form-text text-muted">
					<lams:errors path="courseKey"/>
				</small>	
			</div>
			<div class="form-group">
				<label for="confirmCourseKey"><fmt:message key="admin.confirm.course.key" /></label>
				<input value="${signupForm.courseKey}" id="confirmCourseKey"  name="confirmCourseKey" required  maxlength="20" class="form-control form-control-sm"/>
			</div>
			<div class="form-group">
				<label for="blurb"><fmt:message key="admin.description.txt" /></label>
				  <lams:CKEditor id="blurb" 
				     value="${signupForm.blurb}" 
				     contentFolderID="../public/signups">
				  </lams:CKEditor>
			</div>
			<div class="form-group">
				<div class="form-check">
					<form:checkbox id="disabled" path="disabled" cssClass="form-check-input"/>
					<label class="form-check-label" for="disabled"><fmt:message key="admin.disable.option" /></label>
				</div>
				<div class="form-check">
					<form:checkbox id="loginTabActive" path="loginTabActive" cssClass="form-check-input"/>
					<label class="form-check-label" for="loginTabActive"><fmt:message key="admin.login.tab" /></label>
				</div>
			</div>
			<div class="form-group">
				<label for="context"><fmt:message key="admin.context.path" /></label>:</br>
				<lams:LAMSURL/>signup/<input id="context" value="${signupForm.context}" name="context" required class="form-control form-control-sm form-control-inline"/>
				<small id="passwordHelpBlock" class="form-text text-danger">
					<lams:errors path="context"/>
				</small>				
			</div>
				
				<div class="pull-right">
					<a href="<lams:LAMSURL/>admin/signupManagement/start.do" class="btn btn-outline-secondary"><fmt:message key="admin.cancel" /></a>
					<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
				</div>
			
		</form:form>
	</lams:Page>
</body>
</lams:html>

