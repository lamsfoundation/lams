<%@ page import="org.lamsfoundation.lams.signup.model.SignupOrganisation" %>
<%@ include file="/taglibs.jsp"%>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>

<lams:help style="no-tabs" page="<%= SignupOrganisation.SIGNUP_HELP_PAGE %>"/>

<h1><fmt:message key="admin.list.signup.pages"/></h1>

<c:if test="${not empty error}">
	<p class="warning"><c:out value="${error}" /></p>
</c:if>

<table border="0" cellspacing="1" cellpadding="1" class="alternative-color">
	<tr>
		<th><fmt:message key="admin.group" /></th>
		<th><fmt:message key="admin.group.code" /></th>
		<th><fmt:message key="admin.lessons" /></th>
		<th><fmt:message key="admin.staff" /></th>
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

<p>
	<html:link styleClass="button" page="/signupManagement.do?method=add"><fmt:message key="admin.add.new.signup.page"/></html:link>
</p>