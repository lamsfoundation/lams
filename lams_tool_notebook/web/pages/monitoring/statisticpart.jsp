<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty statisticList}">
	<fmt:message key="label.no.user.available" />
</c:if>

<c:forEach var="statistic" items="${statisticList}">

	<c:if test="${isGroupedActivity}">
		<h1><c:out value="${statistic.sessionName}" /></h1>
	</c:if>

	<table  class="alternative-color">
		<tr>
			<td>
				<fmt:message key="heading.totalLearnersInGroup" />
			</td>
			<td>
				<c:out value="${statistic.numLearners}" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="heading.totalFinishedLearnersInGroup" />
			</td>
			<td>
				<c:out value="${statistic.numLearnersFinished}" />
			</td>
		</tr>
	</table>
</c:forEach>
