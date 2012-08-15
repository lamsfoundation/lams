<%-- This is for AJAX call to refresh statistic page  --%>
<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<c:forEach var="element" items="${statisticList}">
	<c:set var="sessionName" value="${element.key.sessionName}" />
	<c:set var="statistic" value="${element.value}" />
	<table cellpadding="0">
		<c:if test="${isGroupedActivity}">
			<tr>
				<th colspan="2">
					<fmt:message key="label.session.name" />: <c:out value="${sessionName}" />
				</th>
			</tr>
		</c:if>
		
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