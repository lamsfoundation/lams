<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty sessionDTOs}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="label.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>

<c:forEach var="session" items="${sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					${session.sessionName}
				</span>
			</div>
		</div>
	</c:if>
	
	<div class="panel-body">
		<table class="table table-condensed">
			<tr>
				<td width="40%">
					<fmt:message key="heading.totalLearnersInGroup"/>
				</td>
				<td width="70%">
					${session.numberOfLearners}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="heading.totalFinishedLearnersInGroup" />
				</td>
				<td>
					${session.numberOfFinishedLearners}
				</td>
			</tr>
		</table>
	</div>
	
</c:forEach>
