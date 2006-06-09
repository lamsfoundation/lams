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
					<b> <fmt:message key="message.session.name" /> : </b>
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
					<td>
						<b><fmt:message key="message.monitoring.summary.no.users" /></b>
					</td>
				</tr>
			</c:if>

			<tr>
				<td colspan="3">
					<hr />
				</td>
			</tr>
			<tr>
				<td align="right">
					<html:form action="/learning/viewForum.do" target="_blank">
						<html:hidden property="mode" value="${toolAccessMode}" />
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="viewForum" style="width:120px" styleClass="buttonStyle">
							<fmt:message key="label.monitoring.summary.view.forum" />
						</html:submit>
					</html:form>
				</td>

				<td align="center">
					<html:form action="/monitoring/viewAllMarks" target="_blank">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="Mark" style="width:120px"  styleClass="buttonStyle">
							<fmt:message key="lable.topic.title.mark" />
						</html:submit>
					</html:form>
				</td>
				<td align="left">
					<html:form action="/monitoring/downloadMarks">
						<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
						<html:submit property="downloadMarks"  style="width:120px" styleClass="buttonStyle">
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
