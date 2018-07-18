<%@ page import="org.lamsfoundation.lams.signup.model.SignupOrganisation" %>
<%@ include file="/taglibs.jsp"%>

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
				<html:link page="/signupManagement.do?method=edit&soid=${signupOrganisation.signupOrganisationId}"><fmt:message key="admin.edit"/></html:link>
				&nbsp;&nbsp;
				<html:link page="/signupManagement.do?method=delete&soid=${signupOrganisation.signupOrganisationId}"><fmt:message key="admin.delete"/></html:link>
			</td>
		</tr>
	</c:forEach>
</table>

<html:link styleClass="btn btn-primary pull-right" page="/signupManagement.do?method=add"><fmt:message key="admin.add.new.signup.page"/></html:link>
