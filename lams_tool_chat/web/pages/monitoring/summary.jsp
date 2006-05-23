<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<c:forEach var="session" items="${dto.chatSessions}">
	<table class="forms">
		<tr>
			<th colspan="2">
				<c:out value="${session.sessionName}" />
			</th>
		</tr>
		<tr>
			<td>
				<p>
					Session User names
				</p>
			</td>
			<td>
				<p>
					link to marking ?? responses ??
				</p>
			</td>
		<tr>
			<td colspan="2">
				<p>
					View chat history link
				</p>

				<c:url value="/monitoring.do" var="openChatURL">
					<c:param name="dispatch" value="openChatClient" />
					<c:param name="toolSessionID" value="${session.sessionId}" />
				</c:url>
				<html:link href="${openChatURL}" target="_blank">
					*..*Open Chat Client*..*
				</html:link>
			</td>
		</tr>
	</table>
</c:forEach>
