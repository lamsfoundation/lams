<%@ include file="/includes/taglibs.jsp"%>

<table>
	<tr><td colspan="2"><h4><fmt:message key="heading.totalLearnersInGroup" /></h4></td></tr>

<c:forEach var="group" items="${formBean.groupStatsMap}">
	<tr><td width="50%"><c:out value="${group.key}" /></td>
		<td><c:out value="${group.value}" /></td>
	</tr>
</c:forEach>
</table>

<table>
	<tr><td width="50%"><h4><fmt:message key="heading.totalLearners" /></h4></td>
		<td><c:out value="${formBean.totalLearners}" /></td>
	</tr>
</table>

</table>
