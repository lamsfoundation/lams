<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table class="forms">
		<tr>
			<th colspan="2">
				<c:out value="${session.sessionName}" />
			</th>
		</tr>

		<tr>
			<td class="formlabel">
				<fmt:message>heading.totalLearners</fmt:message>
			</td>
			<td class="formcontrol">
				<p>
					TODO.
				</p>
			</td>
		</tr>

		<tr>
			<td class="formlabel">
				<fmt:message>heading.totalMessages</fmt:message>
			</td>
			<td class="formcontrol">
				<p>
					<c:out value="${session.postCount}" />
				</p>
			</td>
		</tr>

		<tr>
			<td>
				<fmt:message>Learner</fmt:message>
			</td>
			<td>
				<fmt:message>No of Posts</fmt:message>
			</td>
		</tr>

		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td>
					<c:out value="${user.jabberNickname}" />
				</td>
				<td>
					<c:out value="${user.postCount}" />
				</td>
			</tr>
		</c:forEach>

	</table>
</c:forEach>
