<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty statisticList}">
	<fmt:message key="label.no.user.available" />
</c:if>

<c:forEach var="statistic" items="${statisticList}">

	<c:if test="${isGroupedActivity}">
		<h1>
			<fmt:message key="label.session.name" />: 
			<c:out value="${statistic.sessionName}" />
		</h1>
	</c:if>

	<table  class="alternative-color">
		<tr>
			<td>
				<fmt:message key="monitoring.statistic.marked" />
			</td>
			<td>
				<c:out value="${statistic.markedCount}" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="monitoring.statistic.not.marked" />
			</td>
			<td>
				<c:out value="${statistic.notMarkedCount}" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="monitoring.statistic.total.uploaded.file" />
			</td>
			<td>
				<c:out value="${statistic.totalUploadedFiles}" />
			</td>
		</tr>
	</table>
</c:forEach>