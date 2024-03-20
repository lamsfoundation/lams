<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${mindmapDTO}" />

<c:if test="${empty dto.sessionDTOs}">
	<lams:Alert5 type="info" id="no-session-summary">
		<fmt:message key="label.nogroups" />
	</lams:Alert5>
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}">
	<c:if test="${isGroupedActivity}">
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" />&nbsp;${session.sessionName}
				</span>
			</div>
		</div>
	</c:if>

	<div class="panel-body">
		<table class="table table-condensed table-striped">
			<tr>
				<td width="40%">
					<fmt:message>heading.totalLearnersInGroup</fmt:message>
				</td>
				<td width="70%">
					${session.numberOfLearners}
				</td>
			</tr>
			<tr>
				<td width="40%">
					<fmt:message>heading.totalFinishedLearnersInGroup</fmt:message>
				</td>
				<td width="70%">
					${session.numberOfFinishedLearners}
				</td>
			</tr>
		</table>
	</div>
</c:forEach>
