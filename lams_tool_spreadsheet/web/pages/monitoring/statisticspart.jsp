<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach var="statistic" items="${statisticList}">
	<table cellpadding="0">
		<tr>
			<th colspan="2">
				<fmt:message key="label.monitoring.statistics.session.name" /> <c:out value="${statistic.sessionName}" />
			</th>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.monitoring.statistics.marked" />
			</td>
			<td>
				<c:out value="${statistic.markedCounter}" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.monitoring.statistics.not.marked" />
			</td>
			<td>
				<c:out value="${statistic.notMarkedCounter}" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.monitoring.statistics.total.spreadsheets.sent.by.learners" />
			</td>
			<td>
				<c:out value="${statistic.totalSpreadsheetsSentByLearners}" />
			</td>
		</tr>
	</table>
</c:forEach>