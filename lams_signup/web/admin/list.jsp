<%@ include file="/taglibs.jsp"%>

<h1>List of signup pages</h1>

<c:if test="${not empty error}">
	<p class="warning"><c:out value="${error}" /></p>
</c:if>

<table border="0" cellspacing="1" cellpadding="1" class="alternative-color">
	<tr>
		<th>Group name</th>
		<th>Group code</th>
		<th>Add to lessons?</th>
		<th>Add as staff?</th>
		<th>Create date</th>
		<th>Disabled?</th>
		<th>Context</th>
		<th>Actions</th>
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
				<html:link page="/admin.do?method=edit&soid=${signupOrganisation.signupOrganisationId}">Edit</html:link>
				&nbsp;&nbsp;
				<html:link page="/admin.do?method=delete&soid=${signupOrganisation.signupOrganisationId}">Delete</html:link>
			</td>
		</tr>
	</c:forEach>
</table>

<p>
	<html:link styleClass="button" page="/admin.do?method=add">Add a new signup page</html:link>
</p>