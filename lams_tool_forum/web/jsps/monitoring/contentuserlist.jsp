<%@ include file="/includes/taglibs.jsp"%>
<html:errors />

<div class="datatablecontainer">
	<c:forEach var="element" items="${sessionUserMap}">
		<c:set var="toolSessionDto" value="${element.key}" />
		<c:set var="userlist" value="${element.value}" />
		<c:set var="toolAccessMode" value="${mode}" />

		<br />

		<table class="forms">
			<tr>
				<th style="border-bottom:1px #000 solid;" colspan="3">
					<b>
						<fmt:message key="message.session.name" />
						:
					</b>
					<c:out value="${toolSessionDto.sessionName}" />
				</th>

			</tr>

			<c:forEach var="user" items="${userlist}">
				<tr>

					<td>
						<c:out value="${user.firstName}" />
						<c:out value="${user.lastName}" />
					</td>
					<td>
						<c:out value="${user.loginName}" />
					</td>
					<td class="formcontrol">
						<html:form action="/monitoring/viewUserMark" target="_blank">
							<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
							<html:hidden property="userID" value="${user.uid}" />
							<html:submit property="Mark">
								<fmt:message key="lable.topic.title.mark" />
							</html:submit>
						</html:form>
					</td>
				</tr>
			</c:forEach>

			<c:if test="${empty userlist}">
				<tr>
					<td>
						<fmt:message key="message.monitoring.summary.no.users" />
					</td>
				</tr>
			</c:if>

			<tr>
				<td colspan="3">
					&nbsp;
				</td>

			</tr>

			<tr>

				<td class="formcontrol">

					<c:url value="/learning/viewForum.do" var="url">
						<c:param name="mode" value="${toolAccessMode}" />
						<c:param name="toolSessionID" value="${toolSessionDto.sessionID}" />
					</c:url>
					
					<c:set var="viewForumUrl">
						<html:rewrite href="${url}" />
					</c:set>
					
					<html:link href="${viewForumUrl}" target="_blank">
						<fmt:message key="label.monitoring.summary.view.forum" />
					</html:link>

				</td>

				<td class="formcontrol">
					<html:form action="/monitoring/viewAllMarks" target="_blank">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="Mark">
							<fmt:message key="lable.topic.title.mark" />
						</html:submit>
					</html:form>
				</td>
				<td class="formcontrol">
					<html:form action="/monitoring/downloadMarks">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="downloadMarks">
							<fmt:message key="message.download.marks" />
						</html:submit>
					</html:form>
				</td>


			</tr>
		</table>

		<br />
	</c:forEach>
	<c:if test="${empty sessionUserMap}">
		<fmt:message key="message.monitoring.summary.no.session" />
	</c:if>
</div>
