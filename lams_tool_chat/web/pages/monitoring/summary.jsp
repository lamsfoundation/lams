<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
        <lams:LAMSURL />
</c:set>
<c:set var="dto" value="${requestScope.monitoringDTO}" />

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<h1>
	<c:out value="${monitoringDTO.title}" escapeXml="true"/>
</h1>

<div class="space-top small-space-bottom instructions">
	<c:out value="${monitoringDTO.instructions}" escapeXml="false"/>
</div>

<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
		<c:if test="${isGroupedActivity}">
			<tr>
				<td colspan="2">
					<h2>
						${session.sessionName}
					</h2>
				</td>
			</tr>
		</c:if>

		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message>heading.totalLearners</fmt:message>
			</td>
			<td>
				${session.numberOfLearners}
			</td>
		</tr>

		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message>heading.totalMessages</fmt:message>
			</td>
			<td>
				${session.numberOfPosts}
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<h4>
					<fmt:message>heading.recentMessages</fmt:message>
				</h4>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<c:choose>
					<c:when test="${empty session.messageDTOs}">
						<fmt:message>message.noChatMessages</fmt:message>
					</c:when>
					<c:otherwise>
						<c:forEach var="message" items="${session.messageDTOs}">
							<div class="message">
								<div class="messageFrom">
									${message.from}
								</div>
								<lams:out escapeHtml="true" value="${message.body}"></lams:out>
							</div>
						</c:forEach>

					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	
	<html:form action="/monitoring" method="get" target="_blank" style="display:inline; padding-left: 30px;">
		<html:hidden property="dispatch" value="openChatHistory" />
		<html:hidden property="toolSessionID"	value="${session.sessionID}" />
		<html:submit styleClass="button">
			<fmt:message>summary.editMessages</fmt:message>
		</html:submit>
	</html:form>
	<html:form action="/learning" method="get" target="_blank" style="display:inline;">
		<html:hidden property="toolSessionID" value="${session.sessionID}" />
		<html:hidden property="mode" value="teacher" />
		<html:submit styleClass="button">
			<fmt:message>summary.openChat</fmt:message>
		</html:submit>
	</html:form>
			
	<c:if test="${dto.reflectOnActivity}">
		<h3>
			<fmt:message>label.reflections</fmt:message>
		</h3>
			
		<table class="alternative-color">
			<th>
				<fmt:message>heading.learner</fmt:message>
			</th>
			<th align="center">
				<fmt:message>heading.numPosts</fmt:message>
			</th>

			<th align="center">
				<c:choose>
					<c:when test="${dto.reflectOnActivity}">
						<fmt:message key="heading.reflection" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
			</th>

			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						<c:out value="${user.nickname}" escapeXml="true"/>
					</td>
					<td align="center">
						${user.postCount}
					</td>
					<c:if test="${dto.reflectOnActivity}">
						<td align="center">
							<c:if test="${user.finishedReflection}">
								<c:url value="monitoring.do" var="openNotebook">
									<c:param name="dispatch" value="openNotebook" />
									<c:param name="uid" value="${user.uid}" />
								</c:url>

								<html:link href="javascript:launchPopup('${fn:escapeXml(openNotebook)}')">
									<fmt:message key="link.view" />
								</html:link>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
        </table>
	</c:if>
	
	<c:if test="${fn:length(dto.sessionDTOs) > 1}">
		<hr width="100%"/>
	</c:if>
</c:forEach>

<%@include file="advanceOptions.jsp"%>
<%@include file="daterestriction.jsp"%>
