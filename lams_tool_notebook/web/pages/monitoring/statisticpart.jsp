<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty statisticList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>

<c:forEach var="statistic" items="${statisticList}">
	<c:if test="${isGroupedActivity}">
		<h4><c:out value="${statistic.sessionName}" /></h4>
	</c:if>
	
	<table class="table table-condensed table-no-border">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalLearnersInGroup" />
			</td>
			<td>
				<c:out value="${statistic.numLearners}" />
			</td>
			
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalFinishedLearnersInGroup" />
			</td>
			
			<td>
				<c:out value="${statistic.numLearnersFinished}" />
			</td>
		</tr>
	</table>
</c:forEach>