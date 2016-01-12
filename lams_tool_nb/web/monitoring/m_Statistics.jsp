<%@ include file="/includes/taglibs.jsp"%>

<c:if test="${isGroupedActivity}">
	<table>
		<tr><td colspan="2"><h4><fmt:message key="heading.totalLearnersInGroup" /></h4></td></tr>
	
		<c:forEach var="group" items="${formBean.groupStatsMap}">
			<tr><td width="40%"><c:out value="${group.key}" /></td> 
				<td><c:out value="${group.value}" /></td>
				<c:if test="${allowComments}">
				<td width="20%">
				<c:url value="monitoring.do" var="commentURL">
					<c:param name="method" value="viewComments" />
					<c:param name="toolSessionID" value="${formBean.sessionIdMap[group.key]}" />
				</c:url>
				<html:link href="javascript:launchPopup('${commentURL}')"><fmt:message key="label.view.comments" /></html:link></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</c:if>

<table>
	<tr>
		<td width="40%"><h4><fmt:message key="heading.totalLearners" /></h4></td>
		<td><c:out value="${formBean.totalLearners}" /></td>
		<c:if test="${allowComments && !isGroupedActivity}">
			<c:forEach var="group" items="${formBean.sessionIdMap}">
			<c:url value="monitoring.do" var="commentURL">
					<c:param name="method" value="viewComments" />
					<c:param name="toolSessionID" value="${group.value}" />
			</c:url>
			<td width="20%"><html:link href="javascript:launchPopup('${commentURL}')"><fmt:message key="label.view.comments" /></html:link></td>
			</c:forEach>
		</c:if>
	</tr>
</table>

</table>
