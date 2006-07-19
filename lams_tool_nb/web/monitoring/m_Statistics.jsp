<%@ include file="/includes/taglibs.jsp"%>

<table>
	<tr><td colspan="2"><h2><fmt:message key="heading.totalLearnersInGroup" /></h2></td></tr>

<c:forEach var="group" items="${formBean.groupStatsMap}">
	<tr><td width="50%"><c:out value="${group.key}" /></td>
		<td><c:out value="${group.value}" /></td>
	</tr>
</c:forEach>
</table>

<table>
	<tr><td width="50%"><h2><fmt:message key="heading.totalLearners" /></h2></td>
		<td><c:out value="${formBean.totalLearners}" /></td>
	</tr>
</table>

</table>
