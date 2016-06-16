<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>

<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> :
	<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>
	<c:if test="${org.organisationType.organisationTypeId eq classTypeId}">
		<a href="orgmanage.do?org=<c:out value="${org.parentOrganisation.organisationId}" />" class="btn btn-default">
			<c:out value="${org.parentOrganisation.name}" />
		</a> :
	</c:if>
	<a href="orgmanage.do?org=<c:out value="${org.organisationId}" />" class="btn btn-default">
		<c:out value="${org.name}" />
	</a>
</p>