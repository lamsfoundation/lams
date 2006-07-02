<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
		<tbody>
			<tr>
				<th colspan="2">
					${session.sessionName}
				</th>
			</tr>

			<tr>
				<td class="field-name" width="30%">
					<fmt:message>heading.totalLearners</fmt:message>
				</td>
				<td>
					${session.numberOfLearners}
				</td>
			</tr>

			<tr>
				<td class="field-name" width="30%">
					<fmt:message>heading.totalMessages</fmt:message>
				</td>
				<td>
					${session.numberOfPosts}
				</td>
			</tr>

			<tr>
				<th>
					<fmt:message>Learner</fmt:message>
				</th>
				<th>
					<fmt:message>No of Posts</fmt:message>
				</th>
			</tr>

			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						${user.jabberNickname}
					</td>
					<td>
						${user.postCount}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:forEach>
