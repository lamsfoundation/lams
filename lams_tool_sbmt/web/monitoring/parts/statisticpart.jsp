<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty statisticList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="label.no.user.available" />
	</lams:Alert>
</c:if>

<c:forEach var="statistic" items="${statisticList}">
	<c:if test="${isGroupedActivity}">
		<h4><c:out value="${statistic.sessionName}" /></h4>
	</c:if>
	
	<table class="table table-condensed table-no-border">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="monitoring.statistic.marked" />
			</td>
			<td>
				<c:out value="${statistic.markedCount}" />
			</td>
			
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="monitoring.statistic.not.marked" />
			</td>
			
			<td>
				<c:out value="${statistic.notMarkedCount}" />
			</td>
		</tr>
		
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="monitoring.statistic.total.uploaded.file" />
			</td>
			
			<td>
				<c:out value="${statistic.totalUploadedFiles}" />
			</td>
		</tr>
	</table>
</c:forEach>