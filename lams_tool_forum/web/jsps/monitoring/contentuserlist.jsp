<%@ include file="/includes/taglibs.jsp"%>

<html:errors />

<div id="datatablecontainer">
	<c:forEach var="element" items="${sessionUserMap}">
		<c:set var="toolSessionDto" value="${element.key}" />
		<c:set var="userlist" value="${element.value}" />
		<c:set var="toolAccessMode" value="${mode}" />

		<!--  need to find cleaner way to contruct url -->
		<c:url value="/learning/viewForum.do" var="viewForumUrl">
			<c:param name="mode" value="${toolAccessMode}" />
			<c:param name="toolSessionID" value="${toolSessionDto.sessionID}" />
		</c:url>
		<a href='<c:out value="${viewForumUrl}"/>'>
			<fmt:message key="label.monitoring.summary.view.forum" />
		</a> 
		
		<html:link href="${viewForumUrl}" styleClass="button" target="_blank">
			<fmt:message key="label.monitoring.summary.view.forum" />
		</html:link>
		
		T2
		<table class="forms">
			<tr>
				<td style="border-bottom:1px #000 solid;" colspan="4">
					<b />
						<fmt:message key="label.monitoring.session.name" />
						:
						<c:out value="${toolSessionDto.sessionName}" />
				</td>
			</tr>
			<c:forEach var="user" items="${userlist}">
				<tr>
					<html:form action="/monitoring/viewUserMark">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:hidden property="userID" value="${user.uid}" />
						<td>
							<b>
								<c:out value="${user.firstName}" />
								<c:out value="${user.lastName}" />
							</b>
						</td>
						<td>
							<b>
								<c:out value="${user.loginName}" />
							</b>
						</td>
						<td class="formcontrol">
							<b>
								<html:submit property="Mark" value="Mark" />
							</b>
						</td>
					</html:form>
				</tr>
			</c:forEach>
			<c:if test="${empty userlist}">
				<tr>
					<td colspan="3">
						<fmt:message key="message.monitoring.summary.no.users" />
					</td>
				</tr>
			</c:if>

			<tr>
				<td class="formcontrol">
					<html:form action="/monitoring/viewAllMarks">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="viewAllMarks" value="View all marks" />
					</html:form>
				</td>
				<td class="formcontrol">
					<html:form action="/monitoring/downloadMarks">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="downloadMarks" value="Download marks" />
					</html:form>
				</td>
			</tr>
		</table>
		<br />
		<br />
	</c:forEach>
	<c:if test="${empty sessionUserMap}">
		<fmt:message key="message.monitoring.summary.no.session" />
	</c:if>
</div>
