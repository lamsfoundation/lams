<%@ include file="/taglibs.jsp"%>

<c:if test="${empty monitors}">
	<p>This group has no monitors!  Please add some via the Add/Remove users screen.</p>
</c:if>

<c:if test="${not empty monitors}">
	<p>Check the box of each monitor to add as staff to each of the new lessons.</p>

	<table class="alternative-color">
		<tr>
			<th></th>
			<th>Username</th>
			<th>Name</th>
			<th>Email</th>
		</tr>
		<c:forEach items="${monitors}" var="user">
			<tr>
				<td>
					<input id="staff" name="staff" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" />
				</td>
				<td>
					<c:out value="${user.login}" />
				</td>
				<td>
					<c:out value="${user.title}" /> <c:out value="${user.firstName}" /> <c:out value="${user.lastName}" />
				</td>
				<td>
					<c:out value="${user.email}" />
				</td>
			</tr>
		</c:forEach>
	</table>
</c:if>