<%@ include file="/taglibs.jsp"%>

<c:if test="${empty learners}">
	<p>This group has no learners!  Please add some via the Add/Remove users screen.</p>
</c:if>

<c:if test="${not empty learners}">
	<p>Check the box of each learner to add to each of the new lessons.</p>

	<table class="alternative-color">
		<tr>
			<th></th>
			<th>Username</th>
			<th>Name</th>
			<th>Email</th>
		</tr>
		<c:forEach items="${learners}" var="user">
			<tr>
				<td>
					<input id="learners" name="learners" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" />
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