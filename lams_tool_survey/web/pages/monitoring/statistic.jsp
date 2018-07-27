<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<c:choose>
<c:when test="${empty sessionMap.statistic}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:when>
<c:otherwise>	
	<table class="table table-condensed table-no-border">
		<tr>
			<th class="first">
				<fmt:message key="label.session.name"/>
			</th>
			<th class="first">
				<fmt:message key="label.number.learners"/>
			</th>
		</tr>
		<c:forEach var="entry" items="${sessionMap.statistic}">
			<c:set var="session" value="${entry.key}"/>
			<c:set var="usersInSession" value="${entry.value}"/>
			<tr>
				<td>${session.sessionName}</td>
				<td>${usersInSession}</td>
			</tr>
		</c:forEach>
	</table>
</c:otherwise>
</c:choose>