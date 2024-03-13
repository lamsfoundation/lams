<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty voteStatsDTO}">
	<lams:Alert5 type="info" id="no-session-summary" close="false">
		<fmt:message key="error.noLearnerActivity"/>
	</lams:Alert5>
</c:if>
 
 <div class="lcard p-2" >
<c:forEach var="stats" items="${voteStatsDTO}">
			<h5>
				${stats.sessionName}
			</h5>

	<div><fmt:message key="label.total.completed.students"/>&nbsp;${stats.countUsersComplete}</div>
	<p><fmt:message key="label.total.students"/>&nbsp;${stats.countAllUsers}</p>
</c:forEach>
</div>

