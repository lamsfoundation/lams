<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.signup.title"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
	
	<c:set var="title">${title}: <fmt:message key="admin.add.edit.signup.page"/></c:set>
	<lams:Page type="admin" title="${title}" formID="signupForm">
		<div>
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
			<a href="signupManagement.do" class="btn btn-default loffset5"><fmt:message key="admin.signup.title" /></a>
			</div>
			
			<form:form action="add.do" modelAttribute="signupForm" id="signupForm" method="post">
				<input type="hidden" name="signupOrganisationId" />
				
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
						<td colspan="2">
						 <c:set var="errorKey" value="courseKey" />
							   <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
							      <lams:Alert id="error" type="danger" close="false">
							        <c:forEach var="error" items="${errorMap[errorKey]}">
							           <c:out value="${error}" />
							        </c:forEach>
							      </lams:Alert>
							  </c:if>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="admin.course.key" />:</td>
						<td><input type="text" name="courseKey" size="40" maxlength="255"  class="form-control"/></td>
					<tr>
						<td><fmt:message key="admin.confirm.course.key" />:</td>
						<td><input type="text" name="confirmCourseKey" size="40" maxlength="255"  class="form-control"/></td>
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
						<c:set var="errorKey" value="context" />
							   <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
							      <lams:Alert id="error" type="danger" close="false">
							        <c:forEach var="error" items="${errorMap[errorKey]}">
							           <c:out value="${error}" />
							        </c:forEach>
							      </lams:Alert>
							  </c:if>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="admin.context.path" />:</td>
						<td style="vertical-align: middle;"><lams:LAMSURL/>signup/<input type="text" name="context" /></td>
					</tr>
				</table>
				
				<div class="pull-right">
					<input type="submit" name="CANCEL" value="<fmt:message key="admin.cancel"/>" onclick="bCancel=true;" class="btn btn-default">
					<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.delete"/>" />
				</div>
			
			</form:form>
	</lams:Page>

</body>
</lams:html>

