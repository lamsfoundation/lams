<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty voteStatsDTO}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="error.noLearnerActivity"/>
	</lams:Alert>
</c:if>
 
<c:forEach var="stats" items="${voteStatsDTO}">

	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading">
			<span class="panel-title">
				${stats.sessionName}
			</span>
        </div>

        <div class="panel-body">
	</c:if>

	<p><fmt:message key="label.total.completed.students"/>&nbsp;${stats.countUsersComplete}</p>
	<p><fmt:message key="label.total.students"/>&nbsp;${stats.countAllUsers}</p>

	<c:if test="${isGroupedActivity}">	
		</div>
		</div>
	</c:if>	
</c:forEach>

