<%@ include file="/includes/taglibs.jsp"%>
<html:errors />


<c:forEach var="element" items="${sessionUserMap}">
	<c:set var="toolSessionDto" value="${element.key}" />
	<c:set var="userlist" value="${element.value}" />
	<c:set var="toolAccessMode" value="${mode}" />

	<table cellpadding="0">
		<tr>
			<th colspan="3">
				<fmt:message key="message.session.name" />
				:
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
				<td>
					<c:url value="/monitoring/viewUserMark.do" var="viewuserurl">
						<c:param name="userID" value="${user.uid}" />
						<c:param name="toolSessionID" value="${toolSessionDto.sessionID}" />
					</c:url>
					<html:link href="javascript:launchPopup('${viewuserurl}')" styleClass="button">
						<fmt:message key="lable.topic.title.mark" />
					</html:link>
				</td>
			</tr>
		</c:forEach>

		<c:if test="${empty userlist}">
			<tr>
				<td colspan="3">
					<b><fmt:message key="message.monitoring.summary.no.users" /></b>
				</td>
			</tr>
		</c:if>

		<tr>
			<td align="right">
				<html:form action="/learning/viewForum.do" target="_blank">
					<html:hidden property="mode" value="${toolAccessMode}" />
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="viewForum" styleClass="button">
						<fmt:message key="label.monitoring.summary.view.forum" />
					</html:submit>
				</html:form>
			</td>

			<td align="center">
				<html:form action="/monitoring/viewAllMarks" target="_blank">
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="Mark" styleClass="button">
						<fmt:message key="lable.topic.title.mark" />
					</html:submit>
				</html:form>
			</td>
			<td align="left">
				<html:form action="/monitoring/downloadMarks">
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="downloadMarks" styleClass="button">
						<fmt:message key="message.download.marks" />
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>
</c:forEach>

<c:if test="${empty sessionUserMap}">
	<p>
		<fmt:message key="message.monitoring.summary.no.session" />
	</p>
</c:if>

