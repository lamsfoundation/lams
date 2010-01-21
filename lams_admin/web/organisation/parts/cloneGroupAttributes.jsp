<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>

<ul>
	<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>
	<c:if test="${org.organisationType.organisationTypeId eq classTypeId}">
		<li>Parent group: <c:out value="${org.parentOrganisation.name}" />
		<input type="hidden" id="parentGroupId" name="parentGroupId" value="<c:out value="${org.parentOrganisation.organisationId}" />">
	</c:if>
	<li>Name: <input id="groupName" name="groupName" type="text" value="<c:out value="${org.name}" />" >
	<li>Code: <input id="groupCode" name="groupCode" type="text" value="<c:out value="${org.code}" />" >
	<li>Description: <input id="groupDescription" name="groupDescription" type="text" value="<c:out value="${org.description}" />" >
</ul>