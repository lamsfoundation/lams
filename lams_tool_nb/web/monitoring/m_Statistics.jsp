<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div class="voffset10">

	
 <c:if test="${isGroupedActivity}">
	<table class="table table-striped table-condensed">
		<tr><td colspan="${allowComments ? 3 : 2}"><fmt:message key="heading.totalLearnersInGroup" /></td></tr>
	
		<c:forEach var="group" items="${monitoringDTO.groupStatsMap}">
			<tr><td width="40%"><c:out value="${group.key}" /></td> 
				<td><c:out value="${group.value}" /></td>
				<c:if test="${allowComments}">
				<td width="20%">
				<c:url value="/monitoring/viewComments.do" var="commentURL">
					<c:param name="toolSessionID" value="${monitoringDTO.sessionIdMap[group.key]}" />
				</c:url>
				<a href="javascript:launchPopup('${commentURL}')" class="btn btn-default btn-sm pull-right"><fmt:message key="label.view.comments" /></a></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</c:if>

<table class="table table-striped table-condensed">
	<tr>
		<td width="40%"><fmt:message key="heading.totalLearners" /></td>
		<td><c:out value="${monitoringDTO.totalLearners}" /></td>
		<c:if test="${allowComments && !isGroupedActivity}">
			<c:forEach var="group" items="${monitoringDTO.sessionIdMap}">
			<c:url value="/monitoring/viewComments.do" var="commentURL">
					<c:param name="toolSessionID" value="${group.value}" />
			</c:url>
			<td width="20%"><a href="javascript:launchPopup('${commentURL}')" cssClass="btn btn-default btn-sm pull-right"><fmt:message key="label.view.comments" /></a></td>
			</c:forEach>
		</c:if>
	</tr>
</table>
 
</div>
