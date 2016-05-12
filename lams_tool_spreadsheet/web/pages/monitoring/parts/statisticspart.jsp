<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty statisticList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>
	
<c:forEach var="statistic" items="${statisticList}">

	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${toolSessionDto.sessionID}">
			<span class="panel-title">
				<fmt:message key="monitoring.label.group" />&nbsp;<c:out value="${statistic.sessionName}" />
			</span>
        </div>

        <div class="panel-body">
	</c:if>
	
		<table class="table table-condensed table-striped">
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
		
	<c:if test="${isGroupedActivity}">	
		</div>
		</div>
	</c:if>
	
</c:forEach>