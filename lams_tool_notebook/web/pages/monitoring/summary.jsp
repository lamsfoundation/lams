<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
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
			<td class="field-name" width="30%">
				<fmt:message>heading.recentMessages</fmt:message>
			</td>
			<td>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div style="background-color: white; padding: 8px;">
					<c:choose>
						<c:when test="${empty session.messageDTOs}">
							<fmt:message>message.noNotebookMessages</fmt:message>
						</c:when>
						<c:otherwise>

							<c:forEach var="message" items="${session.messageDTOs}">
								<div>
									<span style="font-weight: bold"> ${message.from}</span> ${message.body}
									<br />
								</div>
							</c:forEach>

						</c:otherwise>
					</c:choose>
				</div>
			</td>
		</tr>

	</table>
</c:forEach>
