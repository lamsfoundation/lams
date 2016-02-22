<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty voteStatsDTO}">
	<fmt:message key="error.noLearnerActivity"/>
</c:if>

<c:forEach var="stats" items="${voteStatsDTO}">

	<c:if test="${isGroupedActivity}">
		<h1>${stats.sessionName}</h1>
	</c:if>

	<p><fmt:message key="label.total.completed.students"/> ${stats.countUsersComplete}<BR/>
	<fmt:message key="label.total.students"/> ${stats.countAllUsers}</p>
	
</c:forEach>

