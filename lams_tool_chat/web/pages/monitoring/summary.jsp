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
					TODO
				</p>
			</td>
		</tr>

		<tr>
			<td class="formlabel">
				<fmt:message>heading.recentMessages</fmt:message>
			</td>
		
			<td>
				<div style="background-color: white; padding: 8px;">
				<c:choose>
					<c:when test="${empty session.chatMessages}">
						<fmt:message>message.noChatMessages</fmt:message>
					</c:when>
					<c:otherwise>

						<c:forEach var="message" items="${session.chatMessages}">
							<div>
								<span style="font-weight: bold"> <c:out value="${message.from}" /></span>
								<c:out value="${message.body}" />
								<br />
							</div>
						</c:forEach>

					</c:otherwise>
				</c:choose>
				</div>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<html:form action="/monitoring" method="post" target="_blank" style="float:left; margin-right: 4px">
					<html:hidden property="dispatch" value="openChatHistory" />
					<html:hidden property="toolSessionID" value="${session.sessionID}" />
					<html:submit>
						<fmt:message>summary.editMessages</fmt:message>
					</html:submit>
				</html:form>
				
				<html:form action="/monitoring" method="post" target="_blank" >
					<html:hidden property="dispatch" value="openChatClient" />
					<html:hidden property="toolSessionID" value="${session.sessionID}" />
					<html:submit>
						<fmt:message>summary.openChat</fmt:message>
					</html:submit>
					<html:checkbox property="teacherVisible">
						<fmt:message>Visible</fmt:message>
					</html:checkbox>
				</html:form>
							
			</td>
		</tr>
	</table>
</c:forEach>
