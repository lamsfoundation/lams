<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.signup.model.SignupOrganisation" %>
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
	<c:set var="help"><fmt:message key="LAMS+Signup"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
	
		<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

		<c:if test="${not empty error}">
			<lams:Alert type="warn" id="errorMessage" close="false">	
				<c:out value="${error}" />
			</lams:Alert>
		</c:if>
		
		<table class="table table-striped table-condensed" >
			<tr>
				<th><fmt:message key="admin.group" /></th>
				<th><fmt:message key="admin.group.code" /></th>
				<th><fmt:message key="admin.lessons" /></th>
				<th><fmt:message key="admin.staff" /></th>
				<th><fmt:message key="admin.email.verify" /></th>
				<th><fmt:message key="admin.added.on"/></th>
				<th><fmt:message key="admin.disable.option"/></th>
				<th><fmt:message key="admin.context.path"/></th>
				<th><fmt:message key="admin.actions"/></th>
			</tr>
			<c:forEach items="${signupOrganisations}" var="signupOrganisation">
				<tr>
					<td>
						<c:out value="${signupOrganisation.organisation.name}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.organisation.code}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.addToLessons}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.addAsStaff}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.emailVerify}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.createDate}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.disabled}" />
					</td>
					<td>
						<c:out value="${signupOrganisation.context}" />
					</td>
					<td>
						<a id="edit" class="btn btn-default btn-xs" href="<lams:LAMSURL/>admin/signupManagement/edit.do?soid=${signupOrganisation.signupOrganisationId}"><fmt:message key="admin.edit"/></a>
						&nbsp;
                        <csrf:form style="display: inline-block;" id="delete_${signupOrganisation.signupOrganisationId}" method="post" action="/lams/admin/signupManagement/delete.do"><input type="hidden" name="soid" value="${signupOrganisation.signupOrganisationId}"/><input type="submit" class="btn btn-danger btn-xs" value="<fmt:message key="admin.delete" />"/></csrf:form>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<a href="<lams:LAMSURL/>admin/signupManagement/add.do" class="btn btn-primary pull-right"><fmt:message key="admin.add.new.signup.page"/></a>

	</lams:Page>

</body>
</lams:html>
