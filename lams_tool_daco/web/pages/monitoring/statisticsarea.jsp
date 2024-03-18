<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:choose>
	<c:when test="${empty sessionSummaries}">
		<lams:Alert5 type="info" id="no-session-stats">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert5>
	</c:when>

	<c:otherwise>
	<table class="table table-striped table-condensed">
		<tr>
			<c:if test="${sessionMap.isGroupedActivity}">
			<th scope="col" width="36%">
				<fmt:message key="label.learning.tableheader.summary.group" />
			</th>
			</c:if>
			<th scope="col" width="22%">
				<fmt:message key="label.monitoring.number.learners" />
			</th>
			<th scope="col" width="22%">
				<fmt:message key="label.learning.tableheader.records" />
			</th>
			<th scope="col">
				<fmt:message key="label.monitoring.average.number.records.heading" />
			</th>
		</tr>
		<c:forEach var="session" items="${sessionSummaries}">
		<tr>
			<c:if test="${sessionMap.isGroupedActivity}">
			<td><c:out value="${session.sessionName}" escapeXml="true"/></td>
			</c:if>
			<td><fmt:formatNumber value="${session.numberLearners}"/></td>
			<td><fmt:formatNumber value="${session.totalRecordCount}"/></td>
			<td><fmt:formatNumber value="${session.averageRecordCount}" maxFractionDigits="0"/></td>
		</tr>
		</c:forEach>
	</table>
	</c:otherwise>
</c:choose>
